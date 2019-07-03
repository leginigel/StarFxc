package com.stars.tv.youtube.ui.search;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.ImageCardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.youtube.ui.YouTubeCardPresenter;

public class SearchCardPresenter extends YouTubeCardPresenter {

    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        return super.onCreateViewHolder(viewGroup);
    }

    @Override
    protected void setFocusNavigation(CardViewHolder cardViewHolder) {
        cardViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                setDefaultFocus(v, keyCode);
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    setPressBack(v);
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void setDefaultFocus(View v, int keyCode) {
        if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
            if(SuggestListAdapter.UpFromSuggestion)
                v.setNextFocusUpId(R.id.rv_view);
            else
                v.setNextFocusUpId(R.id.keyboard_text_clear);
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            v.setNextFocusLeftId(R.id.search_btn);
            ((SearchFragment) mFragment).setFocus(SearchFragment.FocusLocation.SearchRow);
            mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        }
    }

    @Override
    public void setPressBack(View v) {
        ((SearchFragment) mFragment).setFocus(SearchFragment.FocusLocation.SearchRow);
        mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mLeftNav.getChildAt(1).requestFocus();
        ImageCardView imgCard = v.findViewById(R.id.img_card_view);
        setCardUnfocused(imgCard);
    }
}
