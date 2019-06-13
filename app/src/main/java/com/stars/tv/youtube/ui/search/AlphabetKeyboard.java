package com.stars.tv.youtube.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
public class AlphabetKeyboard extends Fragment {

    public static AlphabetKeyboard newInstance(){
        return new AlphabetKeyboard();
    }
    public static final String TAG = AlphabetKeyboard.class.getSimpleName();
    private SearchViewModel mViewModel;
    public static int OutId_Down, OutId_Left;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.alphabet_keyboard, container, false);
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
                    .setQueryString(((String) ((TextView) v).getText()).toLowerCase(), false));
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
//                            recyclerView.requestFocus();
//                            recyclerView.restoreDefaultFocus();
//                            recyclerView.getLayoutManager().findViewByPosition(0).requestFocus();
                            recyclerView.getChildAt(SuggestListAdapter.OutId).requestFocus();
                            return true;
                        }
                    }
                    else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if(finalI > 20) {
//                            Log.d("Check", textView.getText().toString());
                            OutId_Down = v.getId();
                        }
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
