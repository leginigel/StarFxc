package com.stars.tv.youtube.ui.youtube;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.app.RowsSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.stars.tv.R;
import com.stars.tv.youtube.YoutubeActivity;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.ui.PlayerControlsFragment;
import com.stars.tv.youtube.ui.YouTubeCardPresenter;
import com.stars.tv.youtube.viewmodel.YoutubeViewModel;

/**
 * ViewModel {@link YoutubeViewModel}
 *
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class YoutubeRowFragment extends RowsSupportFragment {

    private final static String TAG = YoutubeRowFragment.class.getSimpleName();
    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private Map<String, List<YouTubeVideo>> mRecommendedChannel;
    private Map<String, List<YouTubeVideo>> mLatestChannel;
    private Map<String, List<YouTubeVideo>> mMusicChannel;

    private ViewGroup mContainer;
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;
    private ListRowPresenter mListRowPresenter;
    private YouTubeCardPresenter mYouTubeCardPresenter;
    private View mVideoBox;
    private YouTubePlayer mPlayer;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private PlayerControlsFragment playerControlsFragment;

    public static YoutubeRowFragment newInstance() {
        return new YoutubeRowFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initial();

        setOnItemViewSelectedListener(new YouTubeCardSelectedListener());
        setOnItemViewClickedListener(new YouTubeCardClickedListener());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    public void setRows(Map<String, List<YouTubeVideo>> channelList){

        if(channelList == null){
            Log.d("Fragment Set Rows", "Channel NULL");
            mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);
            mRowsAdapter.clear();
            for (int i = 0; i < 3; i++) {
                mCardsAdapter.add(new YouTubeVideo(null, null,
                        null, 0, null, null));
            }
            for (int i = 0; i < 2; i++) {
                ListRow row = new ListRow(new HeaderItem("Row " + i), mCardsAdapter);
                mRowsAdapter.add(row);
            }
            setAdapter(mRowsAdapter);
        }
        else {
            Log.i("Fragment Set Rows", "Get Channels");
            mRowsAdapter.clear();
            channelList.forEach((channel,videos)->{
                mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);

                mCardsAdapter.addAll(0,videos);
                Log.d("Fragment Set Rows", "Get Channel : " + channel + videos.get(0).getTitle());
                HeaderItem headerItem = new HeaderItem(channel);

                ListRow row = new ListRow(headerItem, mCardsAdapter);
                mRowsAdapter.add(row);
            });
//            mRowsAdapter.notifyArrayItemRangeChanged(0, 10);
//            setAdapter(mRowsAdapter);
        }
    }

    public void initial(){
        mYouTubeCardPresenter = new YouTubeCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
        mListRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);

        setRows(null);

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.fragment_youtube_player);
        playerControlsFragment = (PlayerControlsFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.fragment_player_controls);
        mVideoBox = ((YoutubeActivity) getActivity()).getPlayerBox();
        mPlayer = ((YoutubeActivity) getActivity()).getYouTubePlayer();
    }

    public final class YouTubeCardSelectedListener implements OnItemViewSelectedListener{

        private ImageCardView imgCard = null;
//        private CustomCardView imgCard = null;

        @Override
        public void onItemSelected(Presenter.ViewHolder viewHolder, Object o,
                                   RowPresenter.ViewHolder viewHolder1, Row row) {
            YouTubeCardPresenter.CardViewHolder cardViewHolder = (YouTubeCardPresenter.CardViewHolder) viewHolder;
            if(o instanceof YouTubeVideo) {
                // Reset the ImageCardView Info Color
                if(imgCard != null){
                    imgCard.setInfoAreaBackgroundColor(getResources().getColor(R.color.background));
                    ((TextView) imgCard.findViewById(R.id.title_text))
                            .setTextColor(Color.WHITE);
                }

                // Set the Selected Color
                imgCard = cardViewHolder.getImageCardView();
                imgCard.setInfoAreaBackgroundColor(Color.WHITE);
                ((TextView) imgCard.findViewById(R.id.title_text))
                        .setTextColor(getResources().getColor(R.color.background));
            }
        }

    }

    public final class YouTubeCardClickedListener implements OnItemViewClickedListener {

        // TODO: 2019/5/21 The Video Player Should Change
        private YouTubeVideo video = null;

        @Override
        public void onItemClicked(Presenter.ViewHolder viewHolder, Object o,
                                  RowPresenter.ViewHolder viewHolder1, Row row) {
            if(o instanceof YouTubeVideo) {
                if(((YouTubeVideo) o).getId() != null){
                    video = (YouTubeVideo) o;
                    if (mVideoBox.getVisibility() != View.VISIBLE) {
                        playerControlsFragment.setVideo(video);
                        mVideoBox.setVisibility(View.VISIBLE);
                        mVideoBox.requestFocus();
                        mPlayer.loadVideo(video.getId());
                    }
                }
            }
        }
    }

    // All Needs to Override this Method
    public YoutubeFragment.TabCategory getTabCategory() {
        return null;
    }
}
