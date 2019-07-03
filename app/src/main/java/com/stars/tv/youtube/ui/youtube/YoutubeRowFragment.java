package com.stars.tv.youtube.ui.youtube;

import android.content.Context;
import android.content.res.Resources;
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
import android.support.v17.leanback.widget.ListRowView;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.stars.tv.R;
import com.stars.tv.youtube.YoutubeActivity;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.ui.YouTubeCardPresenter;
import com.stars.tv.youtube.ui.search.SearchCardPresenter;
import com.stars.tv.youtube.viewmodel.YoutubeViewModel;

/**
 * ViewModel {@link YoutubeViewModel}
 *
 */

public class YoutubeRowFragment extends RowsSupportFragment {

    private final static String TAG = YoutubeRowFragment.class.getSimpleName();
    private VerticalGridView mGridView;
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;
    private ListRowPresenter mListRowPresenter;
    private YouTubeCardPresenter mYouTubeCardPresenter;

    public static YoutubeRowFragment newInstance() {
        return new YoutubeRowFragment();
    }

    public static void highlightRowFocus(Context context, RowsSupportFragment rowsFragment){

        VerticalGridView verticalGridView = rowsFragment.getVerticalGridView();

        int selected_vertical = verticalGridView.getSelectedPosition();
        int row = (selected_vertical >= 1) ? 1 : 0;
        ViewGroup rowContainer = (ViewGroup) verticalGridView.getChildAt(row);  //row container

        ListRowView listRowView = (ListRowView) rowContainer.getChildAt(1);
        int selected_horizontal = listRowView.getGridView().getSelectedPosition();
        int numRow = listRowView.getGridView().getAdapter().getItemCount();
        int pos;
        if(selected_horizontal == (numRow - 1)){
            pos = 3;
        }
        else if(selected_horizontal >= 2){
            pos = 2;
        }
        else{
            pos = selected_horizontal;
        }
        ImageCardView imgCard = listRowView.getGridView().getChildAt(pos).findViewById(R.id.img_card_view);
        imgCard.setInfoAreaBackgroundColor(Color.WHITE);
        ((TextView) imgCard.findViewById(R.id.title_text))
                .setTextColor(context.getResources().getColor(R.color.background));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        Log.d(TAG, "onActivityCreated");
        mGridView = getVerticalGridView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initial(){
        mYouTubeCardPresenter = new YouTubeCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
        mListRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);

        setRows(null);
    }

    public final class YouTubeCardSelectedListener implements OnItemViewSelectedListener {

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
                    ((TextView) imgCard.findViewById(R.id.title_text)).setTextColor(Color.WHITE);
                }

                ArrayObjectAdapter rowsAdapter = getRowsAdapter();
                ListRow listRow = (ListRow) rowsAdapter.get(rowsAdapter.indexOf(row));
                ArrayObjectAdapter cardsAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                YouTubeCardPresenter cardPresenter = getCardPresenter();

                cardViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
                    if(event.getAction() == KeyEvent.ACTION_DOWN) {
                        cardPresenter.setDefaultFocus(v, keyCode);
                        if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            if(rowsAdapter.indexOf(row) == 0) {
                                if(cardPresenter instanceof SearchCardPresenter){
                                    imgCard.setInfoAreaBackgroundColor(getResources().getColor(R.color.background));
                                    ((TextView) imgCard.findViewById(R.id.title_text))
                                            .setTextColor(Color.WHITE);
                                }
                                else {
                                    cardPresenter.setPressBack(v);
                                    return true;
                                }
                            }
                        }
                        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                            if(cardsAdapter.indexOf(o) == 0) {
                                imgCard.setInfoAreaBackgroundColor(getResources().getColor(R.color.background));
                                ((TextView) imgCard.findViewById(R.id.title_text))
                                        .setTextColor(Color.WHITE);
                                if(cardPresenter instanceof SearchCardPresenter){

                                }
                                else {
                                    getActivity().findViewById(R.id.home_btn).requestFocus();
                                    return true;
                                }
                            }
                        }
                        if(keyCode == KeyEvent.KEYCODE_BACK) {
                            cardPresenter.setPressBack(v);
                            return true;
                        }
                    }
                    return false;
                });

                // Set the Selected Color
                imgCard = cardViewHolder.getImageCardView();
                imgCard.setInfoAreaBackgroundColor(Color.WHITE);
                ((TextView) imgCard.findViewById(R.id.title_text))
                        .setTextColor(getResources().getColor(R.color.background));
            }
        }

    }

    public final class YouTubeCardClickedListener implements OnItemViewClickedListener {
        private YouTubeVideo video = null;

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onItemClicked(Presenter.ViewHolder viewHolder, Object o,
                                  RowPresenter.ViewHolder viewHolder1, Row row) {
            if(o instanceof YouTubeVideo) {
                if(((YouTubeVideo) o).getId() != null){
                    video = (YouTubeVideo) o;
                    ((YoutubeActivity) getActivity()).playVideo(video);
                }
            }
        }
    }

    // All Needs to Override this Method
    public YoutubeFragment.TabCategory getTabCategory() {
        return null;
    }

    public ArrayObjectAdapter getRowsAdapter() {
        return mRowsAdapter;
    }

    public YouTubeCardPresenter getCardPresenter() {
        return mYouTubeCardPresenter;
    }
}
