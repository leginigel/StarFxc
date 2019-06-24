package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.BaseGridView;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stars.tv.R;
import com.stars.tv.activity.VideoPreviewActivity;
import com.stars.tv.bean.IQiYiHotQueryBean;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.bean.IQiYiSearchBaseBean;
import com.stars.tv.bean.IQiYiSearchMovieBean;
import com.stars.tv.bean.IQiYiSearchSuggestBean;
import com.stars.tv.presenter.IQiYiParseSearchPresenter;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.view.MyVerticalGridView;
import com.stars.tv.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class SearchMoviceFragment extends BaseFragment {
    private static final String TAG = "SearchMoviceFragment";
    private static final int ITEM_NUM_ROW = 6; // 一行多少个row item.

    private static final int GRIDVIEW_LEFT_P = 40;
    private static final int GRIDVIEW_RIGHT_P = 50;
    private static final int GRIDVIEW_TOP_P = 10;
    private static final int GRIDVIEW_BOTTOM_P = 50;

    private static final int ITEM_TOP_PADDING = 15;
    private static final int ITEM_PADDING = 15;

    private int mPageNum = 1;
    public static final int PAGE_COUNT = 24;
    List<IQiYiMovieBean> mMoviceList = new ArrayList<>();
    List<Object> mWordList = new ArrayList<>();
    List<String> lists = new ArrayList<>();
    int mItemWidth = 0;
    int mItemHeight = 0;
    private boolean isAllKey = true; // true 全键盘 false T9
    private int firstPos = 0;
    private int sugPosition = 0;
    private int maxNum;
    private boolean isRightKey = false;
    private boolean isDownKey = false;
    private boolean isUpKey = false;
    private boolean isRemaining = false;
    private int typeSuggest;
    @BindView(R.id.all_key_vgridview)
    MyVerticalGridView allKeyVgridview; // 全键盘布局.
    @BindView(R.id.t9_key_vgridview)
    MyVerticalGridView t9KeyVgridview; // T9键盘.
    @BindView(R.id.search_vgridview)
    MyVerticalGridView searchVgridview;
    @BindView(R.id.suggest_vgridview)
    MyVerticalGridView suggestVgridview;
    @BindView(R.id.history_vgridview)
    MyVerticalGridView historyVgridview;
    Unbinder unbinder;

    @BindView(R.id.loading)
    ProgressBar loadingProgBar;
    @BindView(R.id.tip_tv)
    TextView tipTextView;
    @BindView(R.id.suggest_tv)
    TextView suggestTextView;
    @BindView(R.id.search_tv)
    TextView searchTextView;
    @BindView(R.id.remove_tv)
    TextView removeTextView;
    @BindView(R.id.history_tv)
    TextView historyTextView;
    @BindView(R.id.history_ll)
    LinearLayout historyLy;

    @BindView(R.id.search_show_tv)
    TextView searchShowTv;
    @BindView(R.id.clear_btn)
    Button clearBtn;
    @BindView(R.id.del_btn)
    Button delBtn;

    @BindView(R.id.key_select_vgridview)
    HorizontalGridView keySelectHgridview;

    /* T9键盘 */
    private Context mContext;
    private Button oneBtn;
    private Button twoBtn;
    private Button threeBtn;
    private Button fourBtn;
    private Button fiveBtn;
    private LinearLayout t9PopupFlyt;
    private PopupWindow window;
    String[] t9Keys;
    final int REFRESH_HOT_CONTENT = 0;
    final int REFRESH_SUGGEST_CONTENT = 1;
    final int REFRESH_MOIVE_CONTENT = 2;
    final int REFRESH_ERROR = -1;
    SearchMoviceFragment.SuggestItemAdapter mSuggestItemAdapter;
    SearchMoviceFragment.MoviceAdapter mMoviceAdapter;
    SearchMoviceFragment.HistoryItemAdapter mHistoryItemAdapter;
    String word;

    public static SearchMoviceFragment newInstance() {
        return new SearchMoviceFragment();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v("tttt", "msg.what:" + msg.what);
            switch (msg.what) {
                case REFRESH_HOT_CONTENT:
                    suggestTextView.setText("热门搜索");
                    mSuggestItemAdapter.notifyDataSetChanged();
                    break;
                case REFRESH_SUGGEST_CONTENT:
                    suggestTextView.setText("猜你想搜");
                    mSuggestItemAdapter.notifyDataSetChanged();
                    break;
                case REFRESH_MOIVE_CONTENT:
                    Log.v("tttt", "msg.what:" + msg.what);
                    mMoviceAdapter.notifyDataSetChanged();
                    if (mMoviceList.size() > 99 || mMoviceList.size() >= maxNum) {
                        searchVgridview.endRefreshingWithNoMoreData();
                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_movice, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = container.getContext();
//        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        initAllDatas();
        initSearchGridView();
        lists = getSearchHistory();
        Log.v(TAG, "lists" + lists.size());
        if (lists.size() > 0) {
            Log.v(TAG, "lists" + lists.toString());
            initHistoryGridView();
            historyLy.setVisibility(View.VISIBLE);
        }
        initSuggestGridView();
        initAllKeysGridView();
        initT9KeysGridView();
        initKeySelectGridView();
        return view;
    }

    private void initAllDatas() {
        parseIQiYiSearchHotQueryWord();
    }

    private void refreshRequest(int type, int position) {
        Log.v(TAG,"position:  "+position);
        Log.v(TAG,"type:  "+type);
        switch (type){
            case 0:
                if (lists!=null) {
                    Log.v(TAG,"position:  "+lists.get(position));
                    parseIQiYiSearchMovieList(lists.get(position), mPageNum, 20);
                }
                break;
            case 1:
                if (mWordList!=null) {
                    Log.v(TAG,"position:  "+((IQiYiSearchBaseBean)mWordList.get(position)).getQueryName());
                    parseIQiYiSearchMovieList(((IQiYiSearchBaseBean) mWordList.get(position)).getQueryName(), mPageNum, 20);
                }
                break;
        }

    }

    private void initHistoryGridView() {
        int topSpace = ViewUtils.getPercentHeightSize(4);
        historyVgridview.addItemDecoration(new SpaceItemDecoration(0, topSpace));
        historyVgridview.setPadding(15, 10, 0, 20);
        mHistoryItemAdapter = new HistoryItemAdapter();
        historyVgridview.setAdapter(mHistoryItemAdapter);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, (90+4) * lists.size()+50);
        historyVgridview.setLayoutParams(lp);
        historyVgridview.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                if(!historyVgridview.hasFocus()&&position==0){
                    Log.v(TAG,"historyVgridview position: "+position);
                    mMoviceList.clear();
                    sugPosition = position;
                    mPageNum = 1;
                    typeSuggest = 0;
                    refreshRequest(typeSuggest, sugPosition);
                }
                child.itemView.setTag(position);
                child.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        Log.v(TAG,"hasFocus"+hasFocus);
                        if (hasFocus) {
                            mPageNum = 1;
                            mMoviceList.clear();
                            sugPosition = (int) v.getTag();
                            Log.v(TAG,"historyVgridview:  "+typeSuggest);
                            typeSuggest = 0;
                            refreshRequest(typeSuggest, sugPosition);
                            if ((int) v.getTag() == lists.size() - 1) {
                                isDownKey = true;
                            }else if((int) v.getTag()==0){
                                isUpKey = true;
                            }
                            ((Button) child.itemView).setTextColor(getResources().getColor(R.color.color_white));
                        } else {
                            if (isRemaining) {
                                ((Button) child.itemView).setTextColor(getResources().getColor(R.color.title_select_color));
                                isRemaining = false;
                            } else {
                                ((Button) child.itemView).setTextColor(getResources().getColor(R.color.color_white));
                            }
                        }

                    }
                });

            }

        });
        historyVgridview.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                    isRemaining = true;
                } else if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if (isDownKey) {
                        suggestVgridview.requestFocusFromTouch();
                        return true;
                    }
                }else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    if (isUpKey) {
                        removeTextView.requestFocusFromTouch();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initSearchGridView() {
        searchVgridview.setPadding(GRIDVIEW_LEFT_P, GRIDVIEW_TOP_P, GRIDVIEW_RIGHT_P, GRIDVIEW_BOTTOM_P);
        searchVgridview.setNumColumns(2);
        int top = ViewUtils.getPercentHeightSize(ITEM_TOP_PADDING);
        int right = ViewUtils.getPercentWidthSize(ITEM_PADDING);
        searchVgridview.addItemDecoration(new SpaceItemDecoration(right, top));
        mMoviceAdapter = new MoviceAdapter();
        searchVgridview.setAdapter(mMoviceAdapter);
        searchVgridview.setOnLoadMoreListener(new MyVerticalGridView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPageNum += 1;
                refreshRequest(typeSuggest, sugPosition);
            }

            @Override
            public void onLoadEnd() {

            }
        });
    }

    private void initSuggestGridView() {
        int topSpace = ViewUtils.getPercentHeightSize(4);
        suggestVgridview.addItemDecoration(new SpaceItemDecoration(0, topSpace));
        suggestVgridview.setPadding(15, 10, 0, 20);
        mSuggestItemAdapter = new SuggestItemAdapter();
        suggestVgridview.setAdapter(mSuggestItemAdapter);
        suggestVgridview.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                if(!suggestVgridview.hasFocus()&&position==0&&lists.isEmpty()){
                    mMoviceList.clear();
                    sugPosition = position;
                    mPageNum = 1;
                    typeSuggest =1;
                    refreshRequest(typeSuggest, sugPosition);
                }
                child.itemView.setTag(position);
                Log.v(TAG,"suggestVgridview position: "+position);
                child.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        Log.v(TAG,"hasFocus"+hasFocus);
                        if(hasFocus) {
                            mPageNum = 1;
                            sugPosition = (int)v.getTag();
                            mMoviceList.clear();
                            typeSuggest =1;
                            refreshRequest(typeSuggest, sugPosition);
                            if ((int)v.getTag() == 0) {
                                isUpKey = true;
                            } else {
                                isUpKey = false;
                            }
                            ((Button) child.itemView).setTextColor(getResources().getColor(R.color.color_white));
                        } else {
                            if (isRemaining) {
                                ((Button) child.itemView).setTextColor(getResources().getColor(R.color.title_select_color));
                                isRemaining = false;
                            } else {
                                ((Button) child.itemView).setTextColor(getResources().getColor(R.color.color_white));
                            }
                        }

                    }
                });

            }

        });
        suggestVgridview.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                    isRemaining = true;
                } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    if (isUpKey) {

                        historyVgridview.requestFocusFromTouch();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void initAllKeysGridView() {
        int topSpace = ViewUtils.getPercentHeightSize(5);
        allKeyVgridview.addItemDecoration(new SpaceItemDecoration(2, topSpace));
        allKeyVgridview.setNumColumns(ITEM_NUM_ROW);
        allKeyVgridview.setAdapter(new KeyItemAdapter());
        allKeyVgridview.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                child.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            if (position == 5 || position == 11 || position == 17 || position == 23 || position == 29 || position == 35) {
                                isRightKey = true;
                            }else {
                                isRightKey = false;
                            }
                        }

                    }
                    });
            }
        });
        allKeyVgridview.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (isRightKey&&!lists.isEmpty()){
                        historyVgridview.requestFocusFromTouch();
                        return true;
                    }else if(isRightKey&&lists.isEmpty()){
                        suggestVgridview.requestFocusFromTouch();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initT9KeysGridView() {
        t9KeyVgridview.setNumColumns(3);
        int topSpace = ViewUtils.getPercentHeightSize(10);
        t9KeyVgridview.addItemDecoration(new SpaceItemDecoration(2, topSpace));
        t9KeyVgridview.setAdapter(new T9KeyItemAdapter());
        t9KeyVgridview.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                if (position == 2 || position == 5 || position == 8) {
                    isRightKey = true;
                } else {
                    isRightKey = false;
                }
            }
        });
        t9KeyVgridview.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (isRightKey) {
                        suggestVgridview.requestFocusFromTouch();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initKeySelectGridView() {
        int rightSpace = ViewUtils.getPercentHeightSize(5);
        keySelectHgridview.addItemDecoration(new SpaceItemDecoration(rightSpace, 0));
        keySelectHgridview.setAdapter(new KeySelectAdapter());
        keySelectHgridview.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                if (position == 0) {
                    isAllKey = true;
                } else {
                    isAllKey = false;
                }
                allKeyVgridview.setVisibility(isAllKey ? View.VISIBLE : View.GONE);
                t9KeyVgridview.setVisibility(isAllKey ? View.GONE : View.VISIBLE);
            }
        });
        keySelectHgridview.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    Button view = (Button) keySelectHgridview.getFocusedChild();
                    if (view != null) {
                        view.setTextColor(getResources().getColor(R.color.title_select_color));
                    }
                } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    Button view = (Button) keySelectHgridview.getFocusedChild();
                    if (!isAllKey) {
                        t9KeyVgridview.requestFocusFromTouch();
                        if (view != null) {
                            view.setTextColor(getResources().getColor(R.color.title_select_color));
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void hideT9Menu() {
        if (window != null && window.isShowing()) {
            window.dismiss();
            t9KeyVgridview.requestFocusFromTouch();
            t9KeyVgridview.setSelectedPosition(mSaveT9Postion);
        }
    }

    private void setSearchText(String text) {
        searchShowTv.setText(text);
        Log.v(TAG, "str" + text);
        if (!text.isEmpty()) {
            parseIQiYiSearchSuggestWord(searchShowTv.getText().toString(), mWordList.size());
        } else {
            parseIQiYiSearchHotQueryWord();
        }
    }

    private int mSaveT9Postion = -1;
    boolean isFirstLoad = true;

    /* 上部工具条点击事件 */
    @OnClick({R.id.clear_btn, R.id.del_btn, R.id.remove_tv})
    public void onTopBottomClicked(View view) {
        switch (view.getId()) {
            case R.id.clear_btn:
                searchShowTv.setText("");
                parseIQiYiSearchHotQueryWord();
                break;
            case R.id.del_btn:
                String str = searchShowTv.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    setSearchText(str.substring(0, str.length() - 1));
                    Log.v(TAG, "str0" + str.substring(0, str.length() - 1));
                }
                break;
                case R.id.remove_tv:
                    SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    historyLy.setVisibility(View.GONE);
        }
    }

    /* T9键盘按钮点击事件 */
    private class PopupClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.one_btn:
                    break;
                case R.id.two_btn:
                    break;
                case R.id.three_btn:
                    break;
                case R.id.four_btn:
                    break;
                case R.id.five_btn:
                    break;
            }
            setSearchText(searchShowTv.getText().toString() + ((Button) v).getText().toString());
            Log.v(TAG, "searchShowTv" + searchShowTv.getText().toString() + "button" + ((Button) v).getText().toString());
            hideT9Menu();
        }

    }

    @Override
    public boolean onKeyDown(KeyEvent event) {
        Log.v(TAG, "onKeyDown" + event);
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (window != null && window.isShowing()) {
                    hideT9Menu();
                    return true;
                } else if (searchVgridview != null && searchVgridview.hasFocus() && searchVgridview.getSelectedPosition() > 1) {
                    searchVgridview.setSelectedPosition(0);
                    searchVgridview.smoothScrollToPosition(0);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (suggestVgridview.hasFocus()||historyVgridview.hasFocus()) {
                    isRemaining = true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP_LEFT:
//                if (searchVgridview.hasFocus()) {
//                    suggestVgridview.setFocusable(true);
//                    suggestVgridview.setFocusableInTouchMode(true);
//                    suggestVgridview.requestFocusFromTouch();
//                    return true;
//                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(removeTextView.hasFocus()){
                    historyVgridview.requestFocusFromTouch();
                }
                break;
        }
        return false;
    }

    public void showPopup(View view, int position) {
        int width = 200;
        int height = 200;
        View contentView = View.inflate(mContext, R.layout.include_popup_key_layout, null);
        window = new PopupWindow(contentView, width, height, true);
        window.showAsDropDown(view, -(width - view.getWidth()) / 2, -((height + view.getHeight()) / 2), Gravity.CENTER);
        t9PopupFlyt = contentView.findViewById(R.id.t9_popup_flyt);
        oneBtn = contentView.findViewById(R.id.one_btn);
        twoBtn = contentView.findViewById(R.id.two_btn);
        threeBtn = contentView.findViewById(R.id.three_btn);
        fourBtn = contentView.findViewById(R.id.four_btn);
        fiveBtn = contentView.findViewById(R.id.five_btn);
        threeBtn.requestFocusFromTouch();
        initPopupUI(position);
        oneBtn.setOnClickListener(new PopupClickListener());
        twoBtn.setOnClickListener(new PopupClickListener());
        threeBtn.setOnClickListener(new PopupClickListener());
        fourBtn.setOnClickListener(new PopupClickListener());
        fiveBtn.setOnClickListener(new PopupClickListener());
    }

    private void initPopupUI(int position) {
        t9Keys = mContext.getResources().getStringArray(R.array.t9_keys);
        final String keyStr = t9Keys[position];
        final String[] keyArry = keyStr.split(",");
        try {
            fiveBtn.setVisibility(View.VISIBLE);
            oneBtn.setText(keyArry[0]);
            twoBtn.setText(keyArry[1].substring(0, 1));
            threeBtn.setText(keyArry[1].substring(2, 3));
            fourBtn.setText(keyArry[1].substring(4, 5));
            fiveBtn.setText(keyArry[1].substring(6));
        } catch (Exception e) {
            fiveBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获取爱奇艺搜索热词
     */
    private void parseIQiYiSearchHotQueryWord() {
        IQiYiParseSearchPresenter ps = new IQiYiParseSearchPresenter();
        ps.requestIQiYiSearchHotQueryWord(new CallBack<List<IQiYiHotQueryBean>>() {
            @Override
            public void success(List<IQiYiHotQueryBean> list) {
                for (IQiYiHotQueryBean bean : list) {
//                    Log.v(TAG, bean.toString());
                }
                mWordList.clear();
                mWordList.addAll(list);
                Log.v(TAG,"mWordList"+mWordList.size());
                mHandler.sendEmptyMessage(REFRESH_HOT_CONTENT);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取爱奇艺搜索关键字提示
     *
     * @param keyWord   搜索keyword
     * @param resultNum 返回结果数量
     */
    private void parseIQiYiSearchSuggestWord(String keyWord, int resultNum) {
        IQiYiParseSearchPresenter ps = new IQiYiParseSearchPresenter();
        ps.requestIQiYiSearchSuggestWord(keyWord, resultNum, new CallBack<List<IQiYiSearchSuggestBean>>() {
            @Override
            public void success(List<IQiYiSearchSuggestBean> list) {
                for (IQiYiSearchSuggestBean bean : list) {
//                    Log.v(TAG, bean.toString());
                }
                mWordList.clear();
                mWordList.addAll(list);
                Log.v(TAG,"mWordList"+mWordList.size());
                mHandler.sendEmptyMessage(REFRESH_SUGGEST_CONTENT);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    /**
     * 获取爱奇艺搜索结果
     *
     * @param keyWord  搜索词
     * @param pageNum  页码 便于翻页
     * @param pageSize 每页个数
     */
    private void parseIQiYiSearchMovieList(String keyWord, int pageNum, int pageSize) {
        IQiYiParseSearchPresenter ps = new IQiYiParseSearchPresenter();
        ps.requestIQiYiSearchMovieBeanList(keyWord, pageNum, pageSize, new CallBack<IQiYiSearchMovieBean>() {
            @Override
            public void success(IQiYiSearchMovieBean bean) {
                Log.v(TAG, "result_num =：" + bean.getResult_num());
                List<IQiYiMovieBean> list = bean.getMovieList();
                for (int i = 0; i < list.size(); i++) {
//                    Log.v(TAG, list.get(i).toString());
                }
//                mMoviceList.clear();
                word = keyWord;
                maxNum = bean.getMax_result_number();
                mMoviceList.addAll(list);
                Log.v(TAG, mMoviceList.toString());
                mHandler.sendEmptyMessage(REFRESH_MOIVE_CONTENT);
                searchVgridview.endMoreRefreshComplete();
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });
    }

    private final String PREFERENCE_NAME = "data";
    private final String SEARCH_HISTORY = "searchHistory";

    private void saveSearchHistory(String name) {
        SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, mContext.MODE_PRIVATE);
        if (TextUtils.isEmpty(name)) {
            return;
        }
        /* 获取之前保存的历史记录 */
        String oldHistory = sp.getString(SEARCH_HISTORY, "");
        String[] history = oldHistory.split(",");
        List<String> historyList = new ArrayList<String>(Arrays.asList(history));
        SharedPreferences.Editor editor = sp.edit();
        /* 移除之前重复添加的name */
        if (historyList.size() > 0) {
            for (int i = 0; i < historyList.size(); i++) {
                if (name.equals(historyList.get(i))) {
                    historyList.remove(i);
                    break;
                }
            }
            /* 将新的name添加到第0位，倒序 */
            historyList.add(0, name);
            /* 最多保存6条，删除最早搜索项 */
            if (historyList.size() > 6) {
                historyList.remove(historyList.size() - 1);
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < historyList.size(); i++) {
                sb.append(historyList.get(i) + ",");
            }
            /* 保存到sp */
            editor.putString(SEARCH_HISTORY, sb.toString());
            editor.commit();
        } else {
            /* 之前未添加过 */
            editor.putString(SEARCH_HISTORY, name + ",");
            editor.commit();
        }

    }

    /* 获取搜索历史 */
    private List<String> getSearchHistory() {
        SharedPreferences sp = mContext.getSharedPreferences(PREFERENCE_NAME, mContext.MODE_PRIVATE);
        String oldHistory = sp.getString(SEARCH_HISTORY, "");
        /* split后长度为1有一个空串对象 */
        String[] history = oldHistory.split(",");
        List<String> historyList = new ArrayList<String>(Arrays.asList(history));
        if (historyList.size() == 1 && historyList.get(0).equals("")) {
            historyList.clear();
        }
        return historyList;
    }

    /* Adapter */

    public class MoviceAdapter extends RecyclerView.Adapter<MoviceAdapter.ViewHolder> {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_videos_layout, null);
            // 保持影视比例.
            mItemWidth = (AutoSizeUtils.dp2px(Objects.requireNonNull(getContext()), AutoSizeConfig.getInstance().getDesignWidthInDp()) - GRIDVIEW_LEFT_P - GRIDVIEW_RIGHT_P - (ITEM_PADDING * ITEM_NUM_ROW)) / ITEM_NUM_ROW;
            mItemHeight = mItemWidth / 3 * 4;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mItemWidth, mItemHeight);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (null != mMoviceList) {
                final IQiYiMovieBean videoBean = mMoviceList.get(position);
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(videoBean.getImageUrl()).into(holder.bgIv);
                ((TextView) holder.nameTv).setAlpha(0.80f);
                ((TextView) holder.nameTv).setText(mMoviceList.get(position).getName());
                String latestOrder = videoBean.getLatestOrder();
                String videoCount = videoBean.getVideoCount();
                holder.infoTv.setVisibility(View.VISIBLE);
                if (latestOrder != null && videoCount != null) {
                    if (Objects.requireNonNull(videoCount).equals(Objects.requireNonNull(latestOrder))) {
                        holder.infoTv.setText(latestOrder + "集全");
                    } else {
                        holder.infoTv.setText("更新至" + latestOrder + "集");
                    }
                } else {
                    holder.infoTv.setText(videoBean.getScore());
                }
                if (videoBean.getPayMarkUrl() != null) {
                    holder.payIv.setVisibility(View.VISIBLE);
                    holder.payIv.setImageResource(R.drawable.vip_icon2);
                }
                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
//                        holder.boardView.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                        ViewUtils.scaleAnimator(view, hasFocus, 1.2f, 150);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (videoBean.getVideoInfoType().equals("1") && videoBean.getTvId() != null) {
                        saveSearchHistory(word);
                        Log.v(TAG, "word" + word);
                        Intent intent = new Intent(getContext(), VideoPreviewActivity.class);
                        intent.putExtra("videoBean", videoBean);
                        startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(getContext(), FullPlaybackActivity.class);
//                            intent.putExtra("videoBean", videoBean);
//                            startActivity(intent);
//                        }

                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return null != mMoviceList ? mMoviceList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView bgIv, payIv;
            TextView nameTv, infoTv;
            View boardView;

            public ViewHolder(View view) {
                super(view);
                bgIv = view.findViewById(R.id.bg_iv);
                payIv = view.findViewById(R.id.pay_iv);
                nameTv = view.findViewById(R.id.name_tv);
                boardView = view.findViewById(R.id.board_view);
                infoTv = view.findViewById(R.id.info_tv);
            }
        }

    }
    /**
     * 搜索历史 Adapter.
     */
    public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {
        public HistoryItemAdapter() {

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button view = new Button(parent.getContext());
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = 90;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, height);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setTextSize(12);
            view.setFocusableInTouchMode(true);
            view.setBackgroundResource(R.drawable.key_statue_bg_selector);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (lists != null) {
                ((Button) holder.itemView).setText(lists.get(position));
                ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.color_white));
                ((Button) holder.itemView).setGravity(Gravity.CENTER_VERTICAL);
                ((Button) holder.itemView).setPadding(15, 0, 0, 0);
                ((Button) holder.itemView).setSingleLine(true);
                ((Button) holder.itemView).setEllipsize(TextUtils.TruncateAt.END);
                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                    }
                });
                // 设置默认.
                if (firstPos == position || isRemaining) {
                    ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.title_select_color));
                }

            }

        }

        @Override
        public int getItemCount() {
            return null != lists ? lists.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }

        }
    }

    /**
     * 搜索提示 Adapter.
     */
    public class SuggestItemAdapter extends RecyclerView.Adapter<SuggestItemAdapter.ViewHolder> {

        public SuggestItemAdapter() {

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button view = new Button(parent.getContext());
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = 90;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, height);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setTextSize(12);
            view.setFocusableInTouchMode(true);
            view.setBackgroundResource(R.drawable.key_statue_bg_selector);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (mWordList != null) {
                if (mWordList.get(position) instanceof IQiYiHotQueryBean) {
                    IQiYiHotQueryBean suggestBean = (IQiYiHotQueryBean) mWordList.get(position);
                    ((Button) holder.itemView).setText(suggestBean.getQuery());
                } else if (mWordList.get(position) instanceof IQiYiSearchSuggestBean) {
                    IQiYiSearchSuggestBean suggestBean = (IQiYiSearchSuggestBean) mWordList.get(position);
                    ((Button) holder.itemView).setText(suggestBean.getName());
                }
                ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.color_white));
                ((Button) holder.itemView).setGravity(Gravity.CENTER_VERTICAL);
                ((Button) holder.itemView).setPadding(15, 0, 0, 0);
                ((Button) holder.itemView).setSingleLine(true);
                ((Button) holder.itemView).setEllipsize(TextUtils.TruncateAt.END);
                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                    }
                });
                // 设置默认.
                if (lists.isEmpty()) {
                    if (firstPos == position || isRemaining) {
                        ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.title_select_color));
                    }
                }

            }

        }


        @Override
        public int getItemCount() {
                return null != mWordList ? mWordList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }

        }
    }

    /**
     * 全键盘 Adapter.
     */
    public class KeyItemAdapter extends RecyclerView.Adapter<KeyItemAdapter.ViewHolder> {

        String[] allKeys;

        public KeyItemAdapter() {
            allKeys = getResources().getStringArray(R.array.all_keys);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button view = new Button(parent.getContext());
            int size = 80;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setTextSize(10);
            view.setFocusableInTouchMode(true);
            view.setBackgroundResource(R.drawable.key_statue_bg_selector);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            ((Button) holder.itemView).setText(allKeys[position]);
            ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.color_white));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSearchText(searchShowTv.getText() + allKeys[position]);
                    Log.v(TAG, "searchShowTv" + searchShowTv.getText() + "key" + allKeys[position]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return allKeys.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }

        }

    }

    /**
     * T9键盘 Adapter.
     * TODO: 这里的所有 Adapter 需要代码重构
     */
    public class T9KeyItemAdapter extends RecyclerView.Adapter<T9KeyItemAdapter.ViewHolder> {

//        String[] t9Keys;

        public T9KeyItemAdapter() {
            t9Keys = getResources().getStringArray(R.array.t9_keys);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_t9_key_layout, null);
            int size = 165;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
            view.setLayoutParams(lp);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final String keyStr = t9Keys[position];
            final String[] keyArry = keyStr.split(",");
            if (keyArry != null && keyArry.length >= 2) {
                holder.topTextView.setText(keyArry[0]);
                holder.bottomTextView.setText(keyArry[1].replace(".", ""));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Log.v("ttt","pos"+position);
                        showPopup(view, position);
                        mSaveT9Postion = t9KeyVgridview.getSelectedPosition();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return t9Keys.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView topTextView;
            TextView bottomTextView;

            public ViewHolder(View view) {
                super(view);
                topTextView = view.findViewById(R.id.top_tv);
                bottomTextView = view.findViewById(R.id.bottom_tv);
            }

        }

    }

    /**
     * 键盘选择 Adapter.
     */
    public class KeySelectAdapter extends RecyclerView.Adapter<KeySelectAdapter.ViewHolder> {

        private List<String> keySelectList = new ArrayList<String>() {
            {
                add("全键盘");
                add("T9键盘");
            }
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button view = new Button(parent.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(230, 100);
            view.setLayoutParams(lp);
            view.setFocusable(true);
            view.setTextSize(15);
            view.setFocusableInTouchMode(true);
            view.setBackgroundResource(R.drawable.key_statue_bg_selector);
            view.setTextColor(getResources().getColor(R.color.color_white));
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (null != keySelectList) {
                ((Button) holder.itemView).setText(keySelectList.get(position));
                ((Button) holder.itemView).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.color_white));
                        }
                    }
                });
            }
            // 设置默认.
            if (isFirstLoad) {
                if ((isAllKey && position == 0) || (!isAllKey && position == 1)) {
                    ((Button) holder.itemView).setTextColor(getResources().getColor(R.color.title_select_color));
                    isFirstLoad = false;
                }
            }
        }

        @Override
        public int getItemCount() {
            return null != keySelectList ? keySelectList.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }

        }

    }

}
