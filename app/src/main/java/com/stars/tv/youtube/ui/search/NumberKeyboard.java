package com.stars.tv.youtube.ui.search;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.youtube.viewmodel.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 * ViewModel {@link SearchViewModel}
 */
public class NumberKeyboard extends Fragment {

    public static NumberKeyboard newInstance(){
        return new NumberKeyboard();
    }
    private SearchViewModel mViewModel;
    public static int OutId_Down, OutId_Left;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.number_keyboard, container, false);
        ViewGroup viewGroup = (ViewGroup) view;
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_home);
        RecyclerView recyclerView = fragment.getView().findViewById(R.id.rv_view);
        for (int i = 0;i < viewGroup.getChildCount();i++) {
            CardView cardView = (CardView) viewGroup.getChildAt(i);
            TextView textView = (TextView) cardView.getChildAt(0);
            textView.setOnFocusChangeListener((v, hasFocus)->{
                if(hasFocus){
                    cardView.setCardElevation(20);
                }
                else{
                    cardView.setCardElevation(0);
                }
            });
            textView.setOnClickListener(v -> mViewModel
                    .setQueryString((String) ((TextView) v).getText(), false));

            if(i == 0) {
                OutId_Left = textView.getId();
            }
            if(i > 20) {
                textView.setNextFocusDownId(R.id.keyboard_text_space);
            }
            if(i == 20 || i == 27)
                textView.setNextFocusRightId(textView.getId());

            int finalI = i;
            textView.setOnKeyListener((v, keyCode, event) -> {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        if(finalI % 7 == 0) {
                            OutId_Left = v.getId();
                            recyclerView.getChildAt(SuggestListAdapter.OutId).requestFocus();

                            return true;
                        }
                    }
                    else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if(finalI > 20) {
                            Log.d("Check", textView.getText().toString());
                            OutId_Down = v.getId();
                        }
                    }
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        SearchFragment.KeyboardFocusId = v.getId();
                        ((ViewGroup) getActivity().findViewById(R.id.left_nav)).getChildAt(1).requestFocus();
                        ((SearchFragment) fragment).setFocus(SearchFragment.FocusLocation.Keyboard);
                        return true;
                    }
                }
                return false;
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
    }
}
