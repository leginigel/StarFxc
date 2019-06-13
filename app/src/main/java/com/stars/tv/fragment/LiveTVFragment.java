package com.stars.tv.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Circle;
import com.stars.tv.R;
import com.stars.tv.bean.LiveTvBean;
import com.stars.tv.bean.LiveTvEpgBean;
import com.stars.tv.db.TvDao;
import com.stars.tv.presenter.ParseLiveTVEpgPresenter;
import com.stars.tv.presenter.LiveTvItemPresenter;
import com.stars.tv.presenter.LiveTvTitlePresenter;
import com.stars.tv.server.RxManager;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.NetUtil;
import com.stars.tv.utils.Utils;
import com.stars.tv.widget.media.IjkVideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class LiveTVFragment extends LiveTVBaseFragment {

    @BindView(R.id.live_tv_root_container)
    RelativeLayout rootLayout;

    @BindView(R.id.live_tv_channel_list_title)
    VerticalGridView titleVg;

    @BindView(R.id.live_tv_channel_list_content)
    VerticalGridView contentVg;

    @BindView(R.id.live_tv_player)
    FrameLayout playerLayout;

    @BindView(R.id.live_tv_player_video_view)
    IjkVideoView playerVideoView;

    @BindView(R.id.live_tv_player_hud_view)
    TableLayout playerHudView;

    @BindView(R.id.live_tv_player_loading)
    TextView playerLoadingView;

    @BindView(R.id.live_tv_info_banner_channel_num)
    TextView infoBannerNum;

    @BindView(R.id.live_tv_info_banner_channel_name)
    TextView infoBannerName;

    @BindView(R.id.live_tv_info_banner_epg_text)
    TextView infoBannerEpg;

    Unbinder unbinder;
    String mTvTitle;

    private int listPosition;
    private boolean isRemaining = false;
    private LiveTvBean curTvChannel;
    private String[] listType;

    private Context mContext;
    private List<LiveTvBean> channelList = new ArrayList<>();
    private List<LiveTvBean> allChannelList = new ArrayList<>();
    private List<LiveTvBean> cctvChannelList = new ArrayList<>();
    private List<LiveTvBean> satelliteChannelList = new ArrayList<>();
    private List<LiveTvBean> favChannelList = new ArrayList<>();
    private List<LiveTvBean> historyChannelList = new ArrayList<>();

    private Circle mCircleDrawable;

    private boolean isFullScreen = false;

    final int PLAY_CURRENT_CHANNEL = 0;
    private long PLAY_CURRENT_CHANNEL_TIMER = 1500; //ms
    private RxManager mRxManager = new RxManager();

    private String TAG = "LiveTVFragment";

    private int lastListPosition = -1; // curTvChannel last  title position
    private int chPosition; // curTvChannel channel position
    private View cCurView; // curTvChannel channel view
    private View cPreView; // curTvChannel channel last view

    public static LiveTVFragment getInstance(String titleMode) {
        return newInstance(titleMode);
    }

    public static LiveTVFragment newInstance(String titleName) {
        LiveTVFragment myFragment = new LiveTVFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName", titleName);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_CURRENT_CHANNEL:
                    playCurrentChannel(curTvChannel);
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        mContext = getContext();
        TvDao tvDao = new TvDao(mContext);
        mTvTitle = getArguments() != null ? getArguments().getString("titleName") : null;
        listType = getResources().getStringArray(R.array.channel_list);
        listType = Arrays.copyOf(listType, listType.length - 1);
        allChannelList = tvDao.queryChannelList();
        cctvChannelList = tvDao.queryCCTVChannelList();
        satelliteChannelList = tvDao.querySatelliteChannelList();
        favChannelList = tvDao.queryFavChannelList();
        historyChannelList = tvDao.queryHistoryChannelList();

        listPosition = (int) Utils.getSharedValue(mContext, "listPosition", 1);
        chPosition = (int) Utils.getSharedValue(mContext, "chPosition", 0);
        setChannelList(listPosition);
        if (channelList.size() == 0) {
            listPosition = 1;
            chPosition = 0;
            setChannelList(listPosition);
        }
        curTvChannel = channelList.get(chPosition);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_live_tv, container, false);
        unbinder = ButterKnife.bind(this, view);

        refreshTitleList();

        playerLayout.setOnFocusChangeListener((view1, hasFocus) -> {
            if (playerLayout != null) {
                playerLayout.findViewById(R.id.live_tv_player_board).setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
            }
        });
        initVideoPlayer();
        initLoading();
        return view;
    }

    private void refreshTitleList() {
        ArrayObjectAdapter titleArrayAdapter = new ArrayObjectAdapter(new LiveTvTitlePresenter());
        ItemBridgeAdapter titleBridgeAdapter = new ItemBridgeAdapter(titleArrayAdapter);
        titleArrayAdapter.addAll(0, Arrays.asList(listType));
        titleVg.setAdapter(titleBridgeAdapter);
        titleVg.setSelectedPosition(listPosition);
        titleVg.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                if (position != -1) {
                    child.itemView.setTag(position);
                    listPosition = position;
                    child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                        TextView title = view.findViewById(R.id.item_live_tv_category_title_text);
                        if (hasFocus) {
                            view.setBackgroundColor(getResources().getColor(R.color.color_focus));
                            title.setTextColor(getResources().getColor(R.color.color_all_white));
                        } else {
                            if (isRemaining) {
                                isRemaining = false;
                                title.setTextColor(getResources().getColor(R.color.color_focus));
                                view.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                            } else {
                                title.setTextColor(getResources().getColor(R.color.color_all_white));
                                view.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                            }
                        }
                    });
                    setChannelList(listPosition);
                    refreshChannelList();
                }
            }
        });
    }

    private void refreshChannelList() {
        ArrayObjectAdapter contentAdapter = new ArrayObjectAdapter(new LiveTvItemPresenter(1));
        ItemBridgeAdapter contentBridgeAdapter = new ItemBridgeAdapter(contentAdapter);
        contentAdapter.addAll(0, channelList);
        contentVg.setAdapter(contentBridgeAdapter);
        contentVg.setSelectedPosition(chPosition);
        contentVg.smoothScrollToPosition(chPosition);
        contentVg.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                if (position != -1) {
                    child.itemView.setTag(position);
                    cCurView = child.itemView;
                    chPosition = position;
                    child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                        TextView chNumber = view.findViewById(R.id.item_live_tv_channel_num);
                        TextView chName = view.findViewById(R.id.item_live_tv_channel_name);
                        if (hasFocus) {
                            chNumber.setTextColor(getResources().getColor(R.color.color_all_white));
                            chName.setTextColor(getResources().getColor(R.color.color_all_white));
                            view.setBackgroundColor(getResources().getColor(R.color.color_focus));
                            LiveTvBean focusTvBean = channelList.get((int) view.getTag());
                            if (curTvChannel != focusTvBean) {
                                curTvChannel = focusTvBean;
                                setTimer(PLAY_CURRENT_CHANNEL, PLAY_CURRENT_CHANNEL_TIMER);
                                showLoading();
                            }
                        } else {
                            if (isRemaining) {
                                chNumber.setTextColor(getResources().getColor(R.color.color_focus));
                                chName.setTextColor(getResources().getColor(R.color.color_focus));
                                view.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                                isRemaining = false;
                            } else {
                                chNumber.setTextColor(getResources().getColor(R.color.color_all_white));
                                chName.setTextColor(getResources().getColor(R.color.color_all_white));
                                view.setBackgroundColor(getResources().getColor(R.color.color_transparent));
                            }

                        }
                    });
                }
            }
        });
    }

    public void initVideoPlayer() {

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        playerVideoView.setMediaController(null);
        playerVideoView.setHudView(playerHudView);

        playerVideoView.setOnPreparedListener(iMediaPlayer -> hideLoading());

        playerVideoView.setOnInfoListener((iMediaPlayer, info, error) -> {
            if (info == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                showLoading();
            } else if (info == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                hideLoading();
            }
            return false;
        });

        playerVideoView.setOnErrorListener((iMediaPlayer, framework_err, impl_err) -> {
            showLoadingError(framework_err + "," + impl_err);
            return true;
        });
    }

    private void setChannelList(int position) {
        switch (position) {
            case 0:
                channelList = historyChannelList;
                break;
            case 1:
                channelList = allChannelList;
                break;
            case 2:
                channelList = cctvChannelList;
                break;
            case 3:
                channelList = satelliteChannelList;
                break;
            case 4:
                channelList = favChannelList;
                break;
            default:
                channelList = allChannelList;
                break;
        }
    }

    private void showFullScreen() {
        isFullScreen = true;
        if (NetUtil.isConnected()) {
            //TODO ENTER full screen
        } else {
            showLoadingError("0");
        }
    }

    private void setWavePosition() {
        Utils.setSharedValue(mContext, "listPosition", listPosition);
        Utils.setSharedValue(mContext, "chPosition", chPosition);
        if ((lastListPosition != -1) && titleVg.getChildAt(lastListPosition) != null) {
            showWaveAnimation(titleVg.getChildAt(lastListPosition).findViewById(R.id.item_live_tv_category_wave), false);
        }
        if (titleVg.getChildAt(listPosition) != null) {
            showWaveAnimation(titleVg.getChildAt(listPosition).findViewById(R.id.item_live_tv_category_wave), true);
        }
        lastListPosition = listPosition;

        int previous = contentVg.indexOfChild(cPreView);
        if (contentVg.getChildAt(previous) != null) {
            showWaveAnimation(contentVg.getChildAt(previous).findViewById(R.id.item_live_tv_channel_wave), false);
        }

        int current = contentVg.indexOfChild(cCurView);
        if (contentVg.getChildAt(current) != null) {
            showWaveAnimation(contentVg.getChildAt(current).findViewById(R.id.item_live_tv_channel_wave), true);
        }
        cPreView = cCurView;
    }

    private void playCurrentChannel(@NonNull LiveTvBean liveTvBean) {
        Log.v(TAG, "play url" + liveTvBean.getUrl().get(0));
        setWavePosition();
        if (NetUtil.isConnected()) {
            String mVideoPath = liveTvBean.getUrl().get(0);
            playerVideoView.setVideoURI(Uri.parse(mVideoPath));
            playerVideoView.start();
        } else {
            showLoadingError("0");
        }
        infoBannerNum.setText(String.valueOf(liveTvBean.getChannelNumber()));
        infoBannerName.setText(liveTvBean.getChannelName());
        infoBannerEpg.setText(getString(R.string.str_live_tv_epg_info_loading));
        parseTvMaoEpgData(curTvChannel);

    }

    private void initLoading() {
        if (NetUtil.isConnected()) {
            mCircleDrawable = new Circle();
            mCircleDrawable.setBounds(0, 0, 100, 100);
            mCircleDrawable.setColor(getResources().getColor(R.color.color_focus));
            playerLoadingView.setCompoundDrawables(null, null, mCircleDrawable, null);
        } else {
            showLoadingError("0");
        }
    }

    private void hideLoading() {
        if (playerLoadingView != null && playerLoadingView.getVisibility() == View.VISIBLE) {
            playerLoadingView.setVisibility(View.GONE);
        }
        if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
            mCircleDrawable.stop();
        }
    }

    private void showLoading() {
        if (NetUtil.isConnected()) {
            if (playerLoadingView != null) {
                playerLoadingView.setVisibility(View.VISIBLE);
                playerLoadingView.setText("");
            }
            mCircleDrawable.start();
        } else {
            showLoadingError("0");
        }
    }

    /**
     * @param errorCode 0 -> 网络错误  else ->error code
     */
    private void showLoadingError(String errorCode) {
        if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
            mCircleDrawable.stop();
        }
        if (playerVideoView != null) {
            playerVideoView.stopPlayback();
        }
        if (playerLoadingView != null) {
            playerLoadingView.setVisibility(View.VISIBLE);
            playerLoadingView.setTextColor(getResources().getColor(R.color.color_all_white));
            if (errorCode.equals("0")) {
                playerLoadingView.setText(getString(R.string.str_network_error));
            } else {
                playerLoadingView.setText(getString(R.string.str_code_error, errorCode));
            }
        }
    }

    private void showWaveAnimation(@NonNull ImageView view, boolean isShow) {
        if (isShow) {
            AnimationDrawable frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_wave);
            view.setBackground(frameAnim);
            view.setVisibility(View.VISIBLE);
            if (frameAnim != null) {
                frameAnim.start();
            }
        } else {
            AnimationDrawable anim = (AnimationDrawable) view.getBackground();
            if (anim != null && anim.isRunning()) {
                anim.stop();
            }
            view.setVisibility(View.GONE);
        }
    }

    private void setEpgInfo(String text) {
        if (text.equals("")) {
            infoBannerEpg.setText(getString(R.string.str_live_tv_epg_empty));
        } else {
            infoBannerEpg.setText(getString(R.string.str_live_tv_epg_data, text));
        }
    }

    private void setTimer(int what, long timerMillis) {
        if (mHandler.hasMessages(what)) {
            mHandler.removeMessages(what);
        }
        Message msg = mHandler.obtainMessage(what);
        mHandler.sendMessageDelayed(msg, timerMillis);
    }

    private void removeTimer(int what) {
        if (mHandler.hasMessages(what)) {
            mHandler.removeMessages(what);
        }
    }

    private void parseTvMaoEpgData(LiveTvBean tvBean) {
        ParseLiveTVEpgPresenter ps = new ParseLiveTVEpgPresenter();
        ps.requestTvMaoEpgData(tvBean, new CallBack<List<LiveTvEpgBean>>() {
            @Override
            public void success(List<LiveTvEpgBean> list) {
                if (!list.isEmpty()) {
                    setEpgInfo(list.get(0).getProgramName());
                } else {
                    setEpgInfo("");
                }
            }

            @Override
            public void error(String msg) {
                setEpgInfo("");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v(TAG, "isVisibleToUser:" + isVisibleToUser);

        if (isVisibleToUser) {
            setTimer(PLAY_CURRENT_CHANNEL, PLAY_CURRENT_CHANNEL_TIMER);
            showLoading();
        } else {
            mRxManager.clear();
            removeTimer(PLAY_CURRENT_CHANNEL);
            if (playerVideoView != null) {
                playerVideoView.stopPlayback();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG, "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView");
        mRxManager.clear();
        if (mCircleDrawable != null && mCircleDrawable.isRunning()) {
            mCircleDrawable.stop();
        }
        removeTimer(PLAY_CURRENT_CHANNEL);
        if (playerVideoView != null) {
            playerVideoView.stopPlayback();
        }
        IjkMediaPlayer.native_profileEnd();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    public boolean onKeyDown(KeyEvent event) {
        Log.v(TAG, "onKeyDown:" + event);
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_RIGHT: // 向右
                if (rootLayout != null && rootLayout.hasFocus()) {
                    if (rootLayout.getFocusedChild().getId() == R.id.live_tv_channel_list_title) {
                        isRemaining = true;
                        contentVg.requestFocus();
                        if (contentVg.getChildAt(0) != null) {
                            contentVg.getChildAt(0).requestFocus();
                        }
                        return true;
                    }
                    if (rootLayout.getFocusedChild().getId() == R.id.live_tv_channel_list_content) {
                        isRemaining = true;
                        playerLayout.requestFocus();
                        return true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (rootLayout != null && rootLayout.hasFocus()) {
                    if (rootLayout.getFocusedChild().getId() == R.id.live_tv_channel_list_content) {
                        removeTimer(PLAY_CURRENT_CHANNEL);
                        isRemaining = false;
                        titleVg.requestFocus();
                        return true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (rootLayout != null && rootLayout.hasFocus()) {
                    if (rootLayout.getFocusedChild().getId() == R.id.live_tv_player) {
                        showFullScreen();
                        return true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (rootLayout != null && rootLayout.hasFocus()) {
                    if (cPreView != null && contentVg.indexOfChild(cPreView) == contentVg.getChildCount() - 1) {
                        cPreView.findViewById(R.id.item_live_tv_channel_wave).setVisibility(View.GONE);
                        return false;
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (rootLayout != null) {
                if (!rootLayout.hasFocus()) {
                    if (channelList.size() != 0) {
                        contentVg.requestFocus();
                    } else {
                    titleVg.requestFocus();
                    }
                    return true;
                } else {
                    if (rootLayout.getFocusedChild().getId() == R.id.live_tv_channel_list_content) {
                        if (cPreView != null && contentVg.indexOfChild(cPreView) == 0) {
                            cPreView.findViewById(R.id.item_live_tv_channel_wave).setVisibility(View.GONE);
                            return false;
                        }
                    }
                }
                }
                break;
        }
        return false;
    }
}

