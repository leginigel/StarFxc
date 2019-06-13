package com.stars.tv.youtube.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.stars.tv.R;
import com.stars.tv.youtube.ui.youtube.YoutubeRowFragment;
import com.stars.tv.youtube.viewmodel.SearchViewModel;

public class SuggestListAdapter extends RecyclerView.Adapter<SuggestListAdapter.ViewHolder> {

    private String [] default_suggestion = {
            "golden state warriors vs toronto raptors",
            "kadenang ginto",
            "never really over katy perry",
            "press press cardi b",
            "us china trade war",
            "avenger end game",
            "league of legends msi",
            "gay marriage",
            "ford v ferrari trailer",
            "james arthur falling like the stars"
    };
    private Context mContext;
    private List<String> items;
    private int size = 10;
    private ViewGroup mLeftNav;
    private SearchFragment mSearchFragment;

    public static int OutId = 0;
    public static boolean UpFromSuggestion = true;

    public SuggestListAdapter(SearchFragment searchFragment) {
        this.mSearchFragment  =searchFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        mLeftNav = ((FragmentActivity) mContext).findViewById(R.id.left_nav);
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.suggest_viewholder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        Log.d("onBindViewHolder", "" + i);
        SearchViewModel vm = ViewModelProviders.of((FragmentActivity) mContext).get(SearchViewModel.class);
        if(items == null){
//            viewHolder.textView.setText("CHECK "+ i);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_whatshot_24dp));
            viewHolder.textView.setText(default_suggestion[i]);
        }
        else{
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.textView.setText(items.get(i));
            viewHolder.cardView.setOnClickListener(v ->{
                mSearchFragment.getRow().setVisibility(View.VISIBLE);
                vm.searchRx(viewHolder.textView.getText().toString());
                vm.setIsLoading(true);
                resize(5);
                vm.setQueryString(viewHolder.textView.getText().toString(), true);
            });
        }

        setOnKeyListener(viewHolder.cardView, i);
    }

    private void setOnKeyListener(CardView cardView, int i){
        cardView.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    View searchRow = mSearchFragment.getView().findViewById(R.id.search_row);
                    if(searchRow.getVisibility() == View.VISIBLE) {
                        Fragment frag = mSearchFragment.getFragmentManager().findFragmentById(R.id.search_row);
                        if(frag instanceof SearchRowFragment)
                            YoutubeRowFragment.highlightRowFocus(mContext, (SearchRowFragment) frag);
                    }
                    v.setNextFocusDownId(R.id.search_row);
                    UpFromSuggestion = true;
                }
                else {
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                    v.setNextFocusRightId(mSearchFragment.getRecyclerViewNextFocusRightId());
                    OutId = i;
                }
                else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    OutId = i;
                    mSearchFragment.setFocus(SearchFragment.FocusLocation.Suggestion);
                }
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.suggest_card);
            cardView.setNextFocusLeftId(R.id.search_btn);
            cardView.setOnFocusChangeListener((v, focus)->{
                if(focus) cardView.setCardElevation(20);
                else cardView.setCardElevation(0);
            });
            imageView = itemView.findViewById(R.id.suggest_img);
            textView = itemView.findViewById(R.id.suggest_text);
        }
    }

    public void refresh(List<String> list){
        if(items != null)
            this.items.clear();
        if(list != null) {
            this.items = new ArrayList<>();
            this.items.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        this.items = null;
    }

    public void resize(int size){
        this.size = size;
        notifyDataSetChanged();
    }
}
