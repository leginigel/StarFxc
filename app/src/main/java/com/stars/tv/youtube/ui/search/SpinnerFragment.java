package com.stars.tv.youtube.ui.search;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.stars.tv.R;

public class SpinnerFragment extends Fragment {
    public static SpinnerFragment newInstance(){
        return new SpinnerFragment();
    }

    private static final String TAG = SpinnerFragment.class.getSimpleName();

    private static final int SPINNER_WIDTH = 200;
    private static final int SPINNER_HEIGHT = 200;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ProgressBar progressBar = new ProgressBar(container.getContext());
        if (container instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(SPINNER_WIDTH, SPINNER_HEIGHT, Gravity.CENTER);
            progressBar.setLayoutParams(layoutParams);
        }
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.btn_text), PorterDuff.Mode.SRC_IN);
        return progressBar;
    }
}
