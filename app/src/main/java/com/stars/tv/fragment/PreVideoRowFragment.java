package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stars.tv.R;
import com.stars.tv.activity.VideoPreview;
import com.stars.tv.bean.IQiYiMovieBean;
import com.stars.tv.presenter.IQiYiParseStarRecommendPresenter;
import com.stars.tv.sample.MyPresenterSelector;
import com.stars.tv.sample.PreVideoItemPresenter;
import com.stars.tv.utils.CallBack;
import com.stars.tv.view.MyVerticalGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PreVideoRowFragment extends Fragment {

    private static final int GRID_VIEW_LEFT_PX = 80;
    private static final int GRID_VIEW_RIGHT_PX = 50;
    private static final int GRID_VIEW_TOP_PX = 30;
    private static final int GRID_VIEW_BOTTOM_PX = 50;

    List<IQiYiMovieBean> mVideoList = new ArrayList<>();
    List<IQiYiMovieBean> mCharactorList = new ArrayList<>();

    private String tvId;

    private VideoPreview videoPreview;
    final int REFRESH_RecommendList = 0;

    @BindView(R.id.video_content_v_grid)
    MyVerticalGridView videoGrid;

    Unbinder unbinder;

    ArrayObjectAdapter mRowsAdapter;
    ItemBridgeAdapter mItemBridgeAdapter;
    int mSubPosition;
    ItemBridgeAdapter.ViewHolder mSelectedViewHolder;
    String mcharactor;

    public PreVideoRowFragment() {
    }

    public static PreVideoRowFragment getInstance(String charactor) {
        return newInstance(charactor);
    }

    public static PreVideoRowFragment newInstance(String charactor) {
        PreVideoRowFragment myFragment = new PreVideoRowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("charactor", charactor);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcharactor = getArguments() != null ? getArguments().getString("charactor") : null;
        parseJSONWithJSONObject(mcharactor);

        refreshRequest();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewo_sample, container, false);
        unbinder = ButterKnife.bind(this, view);
        // 初始化影视垂直布局.
        videoGrid.setPadding(GRID_VIEW_LEFT_PX, GRID_VIEW_TOP_PX, GRID_VIEW_RIGHT_PX, GRID_VIEW_BOTTOM_PX);
        // 处理item放大挡住其他Item.
        videoGrid.setClipChildren(false);
        videoGrid.setClipToPadding(false);
        // 设置间隔.
        videoGrid.setPadding(30, 30, 30, 30);
        // 设置垂直item的间隔.
        videoGrid.setVerticalSpacing(50);
        // 设置缓存.
        videoGrid.getRecycledViewPool().setMaxRecycledViews(0, 10);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        videoPreview = (VideoPreview) context;
        videoPreview.setHandler(mHandler);


        Log.v("tttonmHandleroutput", mHandler.toString());
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.v("ttt msg", "" + msg.what);
            switch (msg.what) {
                case REFRESH_RecommendList:
                    refreshRecomendList();
                    mItemBridgeAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };


    public void refreshRecomendList() {
        List<IQiYiMovieBean> list = mVideoList;
        final MyPresenterSelector myPresenterSelector = new MyPresenterSelector();
        mRowsAdapter = new ArrayObjectAdapter(myPresenterSelector);
        PreVideoItemPresenter videoItemPresenter = new PreVideoItemPresenter();

        ArrayObjectAdapter listRowAdapter1 = new ArrayObjectAdapter(videoItemPresenter);
        for (int i = 0; i < mCharactorList.size(); i++) {
            listRowAdapter1.add(mCharactorList.get(i));
        }
        HeaderItem header1 = new HeaderItem(0, "演员列表");
        ListRow listRow1 = new ListRow(header1, listRowAdapter1);
        mRowsAdapter.add(listRow1);

        ArrayObjectAdapter listRowAdapter2 = new ArrayObjectAdapter(videoItemPresenter);
        for (int j = 0; j < mVideoList.size(); j++) {
            listRowAdapter2.add(mVideoList.get(j));
        }

        HeaderItem header2 = new HeaderItem(0, "推荐列表");
        ListRow listRow2 = new ListRow(header2, listRowAdapter2);
        mRowsAdapter.add(listRow2);
        Log.v("tttlistRow2", listRow2.toString());

        mItemBridgeAdapter = new ItemBridgeAdapter(mRowsAdapter);
        videoGrid.setAdapter(mItemBridgeAdapter);
        videoGrid.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder viewHolder, int position, int subposition) {
                super.onChildViewHolderSelected(parent, viewHolder, position, subposition);
                //TODO 设置为focus行为
                if (position == 0) {
                    Log.v("tttvideoGrid", "position00");

                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    ItemBridgeAdapter.AdapterListener mBridgeAdapterListener = new ItemBridgeAdapter.AdapterListener() {
        @Override
        public void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
            setRowSelected(viewHolder, false);
        }

        @Override
        public void onDetachedFromWindow(ItemBridgeAdapter.ViewHolder vh) {
            if (mSelectedViewHolder == vh) {
                setRowSelected(mSelectedViewHolder, false);
                mSelectedViewHolder = null;
            }
        }

        public void onUnbind(ItemBridgeAdapter.ViewHolder vh) {
            setRowSelected(vh, false);
        }
    };


    /**
     * 测试改变选中ROW的颜色.
     */
    @TargetApi(Build.VERSION_CODES.M)
    void setRowSelected(ItemBridgeAdapter.ViewHolder vh, boolean selected) {
        vh.itemView.setBackground(new ColorDrawable(selected ? getResources().getColor(R.color.color_focus) : getResources().getColor(R.color.color_transparent)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void refreshRequest() {
        SharedPreferences videoinfoshare = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        tvId = videoinfoshare.getString("tvId", "");

        parseIQiYiStarRecommendList(mCharactorList.get(0).getAlbumId(), "6", tvId, true);
    }

    /**
     * 解析json
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            Log.v("tttparseJSON", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String image_url = jsonObject.getString("image_url");
                String name = jsonObject.getString("name");
                String id = jsonObject.getString("id");

                IQiYiMovieBean videoBean = new IQiYiMovieBean();
                videoBean.setImageUrl(image_url);
                videoBean.setName(name);
                videoBean.setAlbumId(id);     //this is starid
                mCharactorList.add(videoBean);
                Log.v("tttmCharactorList", mCharactorList.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取明星推荐视频
     *
     * @param starId     明星id 205633105
     * @param size       需要推荐的个数  20
     * @param tvId       此处无意义，但是必须要有，可以填入当前视频的tvId   875112600
     * @param withCookie 此处根据需要填入，true/false
     */
    private void parseIQiYiStarRecommendList(String starId, String size, String tvId, boolean withCookie) {
        IQiYiParseStarRecommendPresenter ps = new IQiYiParseStarRecommendPresenter();
        ps.requestIQiYiStarRecommendList(starId, size, tvId, withCookie, new CallBack<List<IQiYiMovieBean>>() {
            @Override
            public void success(List<IQiYiMovieBean> list) {
                mVideoList.addAll(list);
                for (IQiYiMovieBean bean : list) {
                    Log.v("PreVideoRowFragment", bean.toString());
                }
                mHandler.sendEmptyMessage(REFRESH_RecommendList);
            }

            @Override
            public void error(String msg) {
                //TODO 获取失败
            }
        });

    }
}
