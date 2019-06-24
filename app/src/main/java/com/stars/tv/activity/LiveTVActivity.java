package com.stars.tv.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Circle;
import com.stars.tv.R;
import com.stars.tv.bean.LiveTvBean;
import com.stars.tv.bean.LiveTvEpgBean;
import com.stars.tv.db.TvDao;
import com.stars.tv.presenter.LiveTvItemPresenter;
import com.stars.tv.presenter.ParseLiveTVEpgPresenter;
import com.stars.tv.utils.CallBack;
import com.stars.tv.utils.NetUtil;
import com.stars.tv.utils.Utils;
import com.stars.tv.utils.ViewUtils;
import com.stars.tv.view.SpaceItemDecoration;
import com.stars.tv.widget.media.IjkVideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class LiveTVActivity extends BaseActivity {

    @BindView(R.id.live_tv_channel_list)
    RelativeLayout chListLayout;

    @BindView(R.id.live_tv_channel_list_title_text)
    TextView titleText;

    @BindView(R.id.live_tv_channel_list_content)
    VerticalGridView chListContent;

    @BindView(R.id.live_tv_info_banner)
    RelativeLayout infoBannerLayout;

    @BindView(R.id.live_tv_info_banner_channel_logo)
    ImageView infoBannerLogo;

    @BindView(R.id.live_tv_info_banner_channel_num)
    TextView infoBannerNumber;

    @BindView(R.id.live_tv_info_banner_channel_name)
    TextView infoBannerName;

    @BindView(R.id.live_tv_info_banner_epg_current)
    TextView infoBannerEpgCurrent;

    @BindView(R.id.live_tv_info_banner_epg_next)
    TextView infoBannerEpgNext;

    @BindView(R.id.live_tv_player)
    FrameLayout playerLayout;

    @BindView(R.id.live_tv_player_video_view)
    IjkVideoView playerVideoView;

    @BindView(R.id.live_tv_player_hud_view)
    TableLayout playerHudView;

    @BindView(R.id.live_tv_player_loading)
    TextView playerLoadingView;

    @BindView(R.id.live_tv_channel_list_tips)
    TextView tipsView;

    Unbinder unbinder;

    Context mContext;

    final int HIDE_CHANNEL_INFO_BANNER = 0;
    final int HIDE_CHANNEL_LIST = 1;
    final int SET_CHANNEL_TO_HISTORY = 2;
    final int PLAY_CURRENT_CHANNEL = 3;
    long PLAY_CURRENT_CHANNEL_TIMER = 1500; //ms
    long TIMEOUT_TIMER = 6000; //ms
    long ADD_TO_HISTORY_TIMER = 10000; // 播放超过十秒加入到历史记录

    private int chPosition;
    private int listPosition;
    private String[] listType;

    private boolean isChListShow = false;
    private boolean isInfoBannerShow = false;

    private TvDao tvDao;
    private List<LiveTvBean> channelList = new ArrayList<>();
    private List<LiveTvBean> allChannelList = new ArrayList<>();
    private List<LiveTvBean> cctvChannelList = new ArrayList<>();
    private List<LiveTvBean> satelliteChannelList = new ArrayList<>();
    private List<LiveTvBean> favChannelList = new ArrayList<>();
    private List<LiveTvBean> historyChannelList = new ArrayList<>();

    private LiveTvBean curTvChannel;
    private View curItem;
    private boolean shortPress = false;

    private Circle mCircleDrawable;

    private String TAG = "LiveTVActivity";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_CHANNEL_INFO_BANNER:
                    hideInfoBanner();
                    break;
                case HIDE_CHANNEL_LIST:
                    hideChannelList();
                    playCurTvChannel(curTvChannel);
                    break;
                case SET_CHANNEL_TO_HISTORY:
                    setHistory(curTvChannel.getChannelNumber());
                    break;
                case PLAY_CURRENT_CHANNEL:
                    playCurTvChannel(curTvChannel);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tv);
        mContext = this;
        unbinder = ButterKnife.bind(this);
        listType = getResources().getStringArray(R.array.channel_list);
        Intent intent = getIntent();
        curTvChannel = (LiveTvBean) intent.getSerializableExtra("curTvChannel");
        listPosition = (int) Utils.getSharedValue(mContext, "listPosition", 1);
        chPosition = (int) Utils.getSharedValue(mContext, "chPosition", 0);
        tvDao = new TvDao(this);
        refreshChannelList();
        if (curTvChannel == null) {
            curTvChannel = allChannelList.get(0);
        }

        initVideoPlayer();
        initLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurTvChannel(curTvChannel);
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

    private void refreshChannelList() {
        allChannelList = tvDao.queryChannelList();
        cctvChannelList = tvDao.queryCCTVChannelList();
        satelliteChannelList = tvDao.querySatelliteChannelList();
        favChannelList = tvDao.queryFavChannelList();
        historyChannelList = tvDao.queryHistoryChannelList();
        setChannelList(listPosition);
    }

    private void showChannelList() {
        isChListShow = true;
        if (isInfoBannerShow) {
            hideInfoBanner();
        }
        setChannelList(listPosition);
        chListLayout.setVisibility(View.VISIBLE);
        chListLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.in_from_left));
        initChannelListAdapter();
        setTimer(HIDE_CHANNEL_LIST, TIMEOUT_TIMER);
    }

    private void hideChannelList() {
        if (isChListShow) {
            removeTimer(HIDE_CHANNEL_LIST);
            if (chListLayout != null) {
                chListLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.out_to_left));
                chListLayout.setVisibility(View.GONE);
            }
            isChListShow = false;
        }
    }

    private void initChannelListAdapter() {
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new LiveTvItemPresenter());
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(arrayObjectAdapter);
        arrayObjectAdapter.addAll(0, channelList);
        chListContent.setAdapter(itemBridgeAdapter);
        chListContent.addItemDecoration(new SpaceItemDecoration(mContext, 0, 1, getResources().getColor(R.color.color_all_white)));

        if (chPosition < channelList.size()) {
            chListContent.setSelectedPosition(chPosition);
            chListContent.smoothScrollToPosition(chPosition);
        }
        chListContent.requestFocus();
        chListContent.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
            @Override
            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
                super.onChildViewHolderSelected(parent, child, position, subposition);
                if (position != -1) {
                    child.itemView.setTag(position);
                    chPosition = position;
                    curItem = child.itemView;
                    curTvChannel = channelList.get(chPosition);
                    child.itemView.setOnFocusChangeListener((view, hasFocus) -> {
                        ViewUtils.scaleAnimator(view, hasFocus, 1.1f, 150);
                        TextView channelName = view.findViewById(R.id.item_live_tv_channel_name);
                        TextView channelNumber = view.findViewById(R.id.item_live_tv_channel_num);
                        TextView channelEpg = view.findViewById(R.id.item_live_tv_channel_epg_text);
                        int color = hasFocus ? getResources().getColor(R.color.color_focus) : getResources().getColor(R.color.color_all_white);
                        channelName.setTextColor(color);
                        channelNumber.setTextColor(color);
                        channelEpg.setTextColor(color);
                        channelEpg.setSelected(hasFocus);
                    });
                }
            }
        });
    }

    private void showInfoBanner() {
        if (isChListShow) {
            return;
        }
        infoBannerName.setText(curTvChannel.getChannelName());
        infoBannerNumber.setText(String.valueOf(curTvChannel.getChannelNumber()));
        isInfoBannerShow = true;
        infoBannerEpgCurrent.setSelected(true);
        infoBannerEpgCurrent.setText(getString(R.string.str_live_tv_epg_info_loading));
        infoBannerEpgNext.setSelected(true);
        infoBannerEpgNext.setText(getString(R.string.str_live_tv_epg_info_loading));
        parseTvMaoEpgData(curTvChannel);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_logo_tv).error(R.drawable.ic_logo_tv);
        Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(curTvChannel.getLogo()).into(infoBannerLogo);
        infoBannerLayout.setVisibility(View.VISIBLE);
        infoBannerLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_bottom));
        setTimer(HIDE_CHANNEL_INFO_BANNER, TIMEOUT_TIMER);
    }

    private void parseTvMaoEpgData(LiveTvBean tvBean) {
        ParseLiveTVEpgPresenter ps = new ParseLiveTVEpgPresenter();
        ps.requestTvMaoEpgData(tvBean, new CallBack<List<LiveTvEpgBean>>() {
            @Override
            public void success(List<LiveTvEpgBean> list) {
                setEpgInfo(list);
            }

            @Override
            public void error(String msg) {
                setEpgInfo(null);
            }
        });
    }

    private void setEpgInfo(List<LiveTvEpgBean> list) {
        if (isInfoBannerShow) {
            if (list != null && list.size() >= 2) {
                infoBannerEpgCurrent.setText(list.get(0).getProgramName());
                infoBannerEpgNext.setText(list.get(1).getProgramName());
            } else if (list != null && list.size() == 1) {
                infoBannerEpgCurrent.setText(list.get(0).getProgramName());
                infoBannerEpgNext.setText(getString(R.string.str_live_tv_epg_empty));
            } else {
                infoBannerEpgCurrent.setText(getString(R.string.str_live_tv_epg_empty));
                infoBannerEpgNext.setText(getString(R.string.str_live_tv_epg_empty));
            }
        }
    }

    private void hideInfoBanner() {
        if (isInfoBannerShow) {
            removeTimer(HIDE_CHANNEL_INFO_BANNER);
            if (infoBannerLayout != null) {
                infoBannerLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_to_bottom));
                infoBannerLayout.setVisibility(View.GONE);
            }
            isInfoBannerShow = false;
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

    private void setCurTvChannel(@NonNull LiveTvBean liveTvBean) {
        curTvChannel = liveTvBean;
        setTimer(PLAY_CURRENT_CHANNEL, PLAY_CURRENT_CHANNEL_TIMER);
        showLoading();
    }

    private void playCurTvChannel(@NonNull LiveTvBean liveTvBean) {
        Log.v(TAG, "listPosition " + listPosition);
        Log.v(TAG, "chPosition " + chPosition);
        Utils.setSharedValue(mContext, "listPosition", listPosition);
        Utils.setSharedValue(mContext, "chPosition", chPosition);

        if (NetUtil.isConnected()) {
            String mVideoPath = liveTvBean.getUrl().get(0);
            playerVideoView.setVideoURI(Uri.parse(mVideoPath));
            playerVideoView.start();
            setTimer(SET_CHANNEL_TO_HISTORY, ADD_TO_HISTORY_TIMER);
            showInfoBanner();
        } else {
            showLoadingError("0");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v(TAG, "onKeyDown " + event);
        if (isChListShow) {
            removeTimer(HIDE_CHANNEL_LIST);
        }
        if (isInfoBannerShow) {
            removeTimer(HIDE_CHANNEL_INFO_BANNER);
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            event.startTracking();
            shortPress = event.getRepeatCount() == 0;
            return true;
        } else {
            onDispatchKeyEvent(event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.v(TAG, "onKeyUp " + event);
        if (isChListShow) {
            setTimer(HIDE_CHANNEL_LIST, TIMEOUT_TIMER);
        }
        if (isInfoBannerShow) {
            setTimer(HIDE_CHANNEL_INFO_BANNER, TIMEOUT_TIMER);
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (shortPress) {
                if (isChListShow) {
                    setCurTvChannel(channelList.get(chPosition));
                    hideChannelList();
                } else {
                    showChannelList();
                }
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.v(TAG, "onKeyLongPress " + event);
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
            boolean isFav = channelList.get(chPosition).getIsFav();
            isFav = !isFav;
            curItem.findViewById(R.id.item_live_tv_channel_favourite).setVisibility(isFav ? View.VISIBLE : View.GONE);
            setFav(isFav);
            return true;
        }
        return false;
    }

    public void onDispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_ENTER: // 键盘ENTER
                if (isChListShow) {
                    setCurTvChannel(channelList.get(chPosition));
                    hideChannelList();
                } else {
                    showChannelList();
                }
                break;
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_M: // Menu Key  删除history
                if (isChListShow && listPosition == 0) {
                    tvDao.clearAllHistory();
                    refreshChannelList();
                    initChannelListAdapter();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP: // 向上
                if (!isChListShow) {
                    channelUp();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN: // 向下
                if (!isChListShow) {
                    channelDown();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT: // 向左
                listPosition--;
                if (listPosition < 0)
                    listPosition = 4;
                setChannelList(listPosition);
                chPosition = 0;
                initChannelListAdapter();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT: // 向右
                listPosition++;
                if (listPosition > 4)
                    listPosition = 0;
                setChannelList(listPosition);
                chPosition = 0;
                initChannelListAdapter();
                break;
        }
    }

    private void onBack() {
        if (isChListShow) {
            hideChannelList();
        } else {
            onBackPressed();
        }
    }


    private void setFav(boolean fav) {
        tvDao.setFav(channelList.get(chPosition).getChannelNumber(), fav);
        refreshChannelList();
        initChannelListAdapter();
    }

    private void setHistory(int channelNumber) {
        tvDao.setHistory(channelNumber, true);
        refreshChannelList();
    }

    private void channelDown() {
        int curNum = curTvChannel.getChannelNumber();
        curNum--;
        if (curNum <= 0) {
            curNum = allChannelList.get(allChannelList.size() - 1).getChannelNumber();
        }
        setCurTvChannel(tvDao.queryChannelWithChannelNumber(curNum));
        listPosition = 1;
        chPosition = curNum - 1;
    }

    private void channelUp() {
        int curNum = curTvChannel.getChannelNumber();
        curNum++;
        if (curNum >= allChannelList.size()) {
            curNum = allChannelList.get(0).getChannelNumber();
        }
        setCurTvChannel(tvDao.queryChannelWithChannelNumber(curNum));
        listPosition = 1;
        chPosition = curNum - 1;
    }

    private void setChannelList(int position) {
        String tips = getString(R.string.str_live_tv_channel_list_tips_default);
        switch (position) {
            case 0:
                channelList = historyChannelList;
                tips = getString(R.string.str_live_tv_channel_list_tips_history);
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
                tips = getString(R.string.str_live_tv_channel_list_tips_favorite);
                break;
            default:
                channelList = allChannelList;
                break;
        }
        if (isChListShow) {
            titleText.setText(listType[listPosition]);
            tipsView.setText(tips);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
        if (playerVideoView != null) {
            playerVideoView.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        unbinder.unbind();
    }
}
