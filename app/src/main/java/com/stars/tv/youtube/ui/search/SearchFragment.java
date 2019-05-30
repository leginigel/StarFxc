package com.stars.tv.youtube.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.youtube.viewmodel.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 * ViewModel {@link SearchViewModel}
 */
public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private SearchViewModel mViewModel;
//    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SuggestListAdapter mSuggestListAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    private FragmentManager fm;
    private TextView searchBar;
    private CardView searchIcon, clearIcon, spaceIcon, backspaceIcon, shiftIcon;
    private View suggest, view;
    private FrameLayout mKeyboard, mRow;
    private AlphabetKeyborad mAlphabet;
    private NumberKeyboard mNumber;
    private SpinnerFragment mSpinner;
    private Keyboard mType;

    SearchRowFragment rowFragment;

    public enum Keyboard{
        Alphabet, Number
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);
        findView();

        mAlphabet = AlphabetKeyborad.newInstance();
        mNumber = NumberKeyboard.newInstance();
        mSpinner = SpinnerFragment.newInstance();
        rowFragment = SearchRowFragment.newInstance();
        if(savedInstanceState == null) {
            fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.keyboard, mAlphabet).commit();
            mType = Keyboard.Alphabet;

            fm.beginTransaction().replace(R.id.search_row, rowFragment).commit();
        }

        mSuggestListAdapter = new SuggestListAdapter();
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSuggestListAdapter.setHasStableIds(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(mSuggestListAdapter);

        spaceIcon.getChildAt(0).setOnClickListener(v -> {
            if(searchBar.getText() != "Search")
                mViewModel.setQueryString(" ", false);
        });
        clearIcon.getChildAt(0).setOnClickListener(v -> clearSearchBar());
        shiftIcon.getChildAt(0).setOnClickListener(v -> switchKeyboard());
        searchIcon.getChildAt(0).setOnClickListener(v -> querySearchResult(searchBar.getText().toString()));
        backspaceIcon.getChildAt(0).setOnClickListener(v -> deleteBarChar());

        setOnFocusListener();

        return view;
    }

    private void findView(){
        mKeyboard = view.findViewById(R.id.keyboard);
        mRow = view.findViewById(R.id.search_row);
        recyclerView = view.findViewById(R.id.rv_view);
        suggest = view.findViewById(R.id.rv_view);
        searchBar = view.findViewById(R.id.search_bar);
        searchIcon = view.findViewById(R.id.cardViewSearch);
        clearIcon = view.findViewById(R.id.cardViewClear);
        spaceIcon = view.findViewById(R.id.cardViewSpace);
        backspaceIcon = view.findViewById(R.id.cardViewBackspace);
        shiftIcon = view.findViewById(R.id.cardViewShift);
    }

    private void setOnFocusListener(){
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i = 4;i < viewGroup.getChildCount() - 1;i++){
            CardView cardView = (CardView) viewGroup.getChildAt(i);
            TextView textView = (TextView) cardView.getChildAt(0);
            textView.setOnFocusChangeListener((v, hasFocus)->{
                if(hasFocus){
                    cardView.setCardElevation(20);
                }
                else cardView.setCardElevation(0);
            });
        }
        backspaceIcon.getChildAt(0).setOnFocusChangeListener((v, hasFocus)->{
            if(hasFocus){
                ((CardView) v.getParent()).setCardElevation(20);
            }
            else ((CardView) v.getParent()).setCardElevation(0);
        });
    }

    private void switchKeyboard(){
        if(mType == Keyboard.Alphabet){
            fm.beginTransaction().replace(R.id.keyboard, mNumber).commit();
            mType = Keyboard.Number;
        }
        else{
            fm.beginTransaction().replace(R.id.keyboard, mAlphabet).commit();
            mType = Keyboard.Alphabet;
        }
    }

    private void querySearchResult(String query){
        Log.d(TAG,"querySearchResult");
        if(!query.equals("Search")) {
            mViewModel.searchRx(query);
            mViewModel.setIsLoading(true);
            mSuggestListAdapter.resize(5);
        }
    }

    private void searchGoogleSuggestion(String query){
        mViewModel.searchSuggestion(query);
    }

    private void deleteBarChar(){
        mViewModel.setQueryString("", true);
    }

    private void clearSearchBar(){
        mViewModel.setQueryString("clear", true);
        mSuggestListAdapter.resize(10);
        rowFragment.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        mViewModel.getQueryString().observe(getActivity(), (query)->{
            Log.d("getChar", query);
            if(query.equals("")){
                searchBar.setText("Search");
            }
            else
            searchBar.setText(query);
        });
        mViewModel.getSuggestions().observe(getActivity(), suggestions->{
//            Log.d("getSuggestion", suggestions.get(0));
            mSuggestListAdapter.refresh(suggestions);
        });
        mViewModel.getIsLoading().observe(getActivity(), isLoading->{
            if(isLoading){
                fm.beginTransaction().replace(R.id.search_row, mSpinner).commit();
            }
            else{
                fm.beginTransaction().replace(R.id.search_row, rowFragment).commit();
                mRow.requestFocus();
            }
        });
    }

}
