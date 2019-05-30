package com.stars.tv.youtube.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
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
import com.stars.tv.youtube.viewmodel.SearchViewModel;

public class SuggestListAdapter extends RecyclerView.Adapter<SuggestListAdapter.ViewHolder> {

    private Context mContext;
    private List<String> items;
    private int size = 10;
    private ViewGroup mLeftNav;

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
        if(items == null)
            viewHolder.textView.setText("CHECK "+ i);
        else{
            viewHolder.textView.setText(items.get(i));
            viewHolder.cardView.setOnClickListener(v ->{
                vm.searchRx(viewHolder.textView.getText().toString());
                vm.setIsLoading(true);
                resize(5);
                vm.setQueryString(viewHolder.textView.getText().toString(), true);
            });
        }

        viewHolder.cardView.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                else {
                    mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
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
            imageView = itemView.findViewById(R.id.img);
//            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_youtube_color_icon));
            textView = itemView.findViewById(R.id.suggest_text);
        }
    }

    public void refresh(List<String> list){
        this.items = new ArrayList<>();
        this.items.addAll(list);
        notifyDataSetChanged();
    }

    public void resize(int size){
        this.size = size;
        notifyDataSetChanged();
    }
}
