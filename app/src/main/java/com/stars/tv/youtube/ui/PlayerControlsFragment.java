package com.stars.tv.youtube.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stars.tv.R;
import com.stars.tv.youtube.data.YouTubeVideo;
import com.stars.tv.youtube.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerControlsFragment extends Fragment {

    public static PlayerControlsFragment newInstance() {
        return new PlayerControlsFragment();
    }

    ImageButton playButton;
    TextView titleText, channelText, countText, publishText, timeText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_controls, container, false);
        playButton = view.findViewById(R.id.play_button);
        timeText = view.findViewById(R.id.play_time);
        titleText = view.findViewById(R.id.play_title);
        channelText = view.findViewById(R.id.play_channel);
        countText = view.findViewById(R.id.play_count);
        publishText = view.findViewById(R.id.play_publish);
        return view;
    }

    public void showTime(){
        playButton.requestFocus();
    }

    public void setVideo(YouTubeVideo video){
        titleText.setText(Html.fromHtml(video.getTitle()));
        timeText.setText(Utils.DurationConverter(video.getDuration()));
        channelText.setText(video.getChannel() + " ‧ ");
        countText.setText(Utils.CountConverter(video.getNumber_views()) + " views ‧ ");
        publishText.setText(Utils.TimeConverter(video.getTime()) + " ago");
    }
}
