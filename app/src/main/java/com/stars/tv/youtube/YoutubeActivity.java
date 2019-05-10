package com.stars.tv.youtube;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.stars.tv.R;
import com.stars.tv.youtube.ui.search.SearchFragment;
import com.stars.tv.youtube.ui.youtube.YoutubeFragment;

public class YoutubeActivity extends FragmentActivity {
    private static String TAG = YoutubeActivity.class.getSimpleName();
    private ImageView searchIcon, homeIcon, subIcon, folderIcon, settingIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        SearchFragment searchFragment = SearchFragment.newInstance();
        YoutubeFragment youtubeFragment = YoutubeFragment.newInstance();
//        YoutubeRowFragment youtubeRowFragment = YoutubeRowFragment.newInstance();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_home, youtubeFragment)
                    .commitNow();
        }

        searchIcon = findViewById(R.id.search_btn);
        homeIcon = findViewById(R.id.home_btn);
        subIcon = findViewById(R.id.subscribe_btn);
        folderIcon = findViewById(R.id.folder_btn);
        settingIcon = findViewById(R.id.setting_btn);

        setIconFocusListener(searchIcon);
        setIconFocusListener(homeIcon);
        setIconFocusListener(subIcon);
        setIconFocusListener(folderIcon);
        setIconFocusListener(settingIcon);

//        homeIcon.setOnFocusChangeListener((v, hasFocus) -> {
//            if(hasFocus) {
//                Log.d("Home", "Icon on Focus");
//                getSupportFragmentManager().beginTransaction().show(youtubeFragment).commitNow();
//            }
////            else
////                getSupportFragmentManager().beginTransaction().hide(youtubeRowFragment).commitNow();
//        });
//
//        searchIcon.setOnFocusChangeListener((v, hasFocus) -> {
//            if(hasFocus) {
//                Log.d("Search", "Icon on Focus");
//                getSupportFragmentManager().beginTransaction().show(searchFragment).commitNow();
//            }
////            else
////                getSupportFragmentManager().beginTransaction().hide(searchFragment).commitNow();
//        });
    }

    void setIconFocusListener(ImageView image){
        image.setOnFocusChangeListener((v, hasFocus)->{
            Drawable drawble = image.getDrawable();
            if(hasFocus) {
                Log.d(TAG, "Icon on Focus");
                drawble.setColorFilter(getResources().getColor(R.color.left_nav), PorterDuff.Mode.MULTIPLY);
                image.setImageDrawable(drawble);
            }
            else{
                drawble.setColorFilter(getResources().getColor(R.color.button), PorterDuff.Mode.SRC_IN);
                image.setImageDrawable(drawble);
            }
        });
    }
}
