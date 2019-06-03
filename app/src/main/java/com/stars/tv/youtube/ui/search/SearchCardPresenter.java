package com.stars.tv.youtube.ui.search;

import android.view.KeyEvent;
import android.view.ViewGroup;

import com.stars.tv.R;
import com.stars.tv.youtube.ui.YouTubeCardPresenter;

public class SearchCardPresenter extends YouTubeCardPresenter {

    @Override
    public void setFocusNavigation(CardViewHolder cardViewHolder) {
        cardViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                    if(SuggestListAdapter.UpFromSuggestion)
                        v.setNextFocusUpId(R.id.rv_view);
                    else
                        v.setNextFocusUpId(R.id.keyboard_text_clear);
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                else {
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }
//                if(event.getAction() == KeyEvent.ACTION_DOWN){
//                    if(keyCode == KeyEvent.KEYCODE_BACK){
//                        mTopNav.requestFocus();
//                        return true;
//                    }
//                }
            }
            return false;
        });
    }
}
