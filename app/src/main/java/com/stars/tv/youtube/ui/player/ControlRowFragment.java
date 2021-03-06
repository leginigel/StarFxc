package com.stars.tv.youtube.ui.player;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ListRowView;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.ui.YouTubeCardPresenter;
import com.stars.tv.youtube.ui.youtube.YoutubeRowFragment;
import com.stars.tv.youtube.viewmodel.YoutubeViewModel;

public class ControlRowFragment extends YoutubeRowFragment {

    public static ControlRowFragment newInstance(String videoId) {
        ControlRowFragment controlRowFragment = new ControlRowFragment();
        Bundle args = new Bundle();
        args.putString("ID", videoId);
        controlRowFragment.setArguments(args);
        return controlRowFragment;
    }

    private YoutubeViewModel mViewModel;
    private ControlCardPresenter mCardPresenter;
    private ListRowPresenter mListRowPresenter;
    private ArrayObjectAdapter mRowsAdapter, mCardsAdapter;
    private ListRow mRow;

    private void setRows(List<YouTubeVideo> videos) {
        mCardsAdapter = new ArrayObjectAdapter(mCardPresenter);
        mCardsAdapter.addAll(0, videos);
        mRow = new ListRow(new HeaderItem("Suggestion "), mCardsAdapter);
        mRowsAdapter.add(mRow);
    }

    @Override
    public void initial() {

        PlayerControlsFragment playerControlsFragment =
                (PlayerControlsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if(playerControlsFragment.getPlayerStateChangeListener()
                .getPlayerState() == PlayerControlsFragment.PlayerState.VIDEO_ENDED)
            setExpand(true);
        else
            setExpand(false);
        mCardPresenter = new ControlCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
//        mListRowPresenter.setSelectEffectEnabled(true);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);
        setAdapter(mRowsAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemViewSelectedListener(new ControlRowCardSelectedListener());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        setRows(mViewModel.getRelatedVideos());
    }

    public final class ControlRowCardSelectedListener implements OnItemViewSelectedListener {

        private ImageCardView imgCard = null;
//        private CustomCardView imgCard = null;

        @Override
        public void onItemSelected(Presenter.ViewHolder viewHolder, Object o,
                                   RowPresenter.ViewHolder viewHolder1, Row row) {
            YouTubeCardPresenter.CardViewHolder cardViewHolder = (YouTubeCardPresenter.CardViewHolder) viewHolder;
            if(o instanceof YouTubeVideo) {
                YouTubeCardPresenter cardPresenter = getCardPresenter();
                // Reset the ImageCardView Info Color
                if(imgCard != null){
                    cardPresenter.setCardUnfocused(imgCard);
                    imgCard.setInfoAreaBackgroundColor(Color.TRANSPARENT);
                }

                // Set the Selected Color
                imgCard = cardViewHolder.getImageCardView();
                cardPresenter.setCardFocused(imgCard);
            }
        }

    }

    @Override
    public YouTubeCardPresenter getCardPresenter() {
        return mCardPresenter;
    }

    public void setRowAlpha(float alpha) {
        ViewGroup rowContainer = (ViewGroup) getVerticalGridView().getChildAt(0);
        ListRowView listRowView = (ListRowView) rowContainer.getChildAt(1);
        listRowView.setAlpha(alpha);
    }
}
