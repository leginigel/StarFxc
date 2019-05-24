package com.stars.tv.fragment;

//Alice@20190424

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stars.tv.R;

public class MediaInfoListFragment extends Fragment {
    private TextView mName,mScore;
    private Button mplot;
    private Button mFullScreen, mFavorite;
    private ViewGroup viewGroup;
    private ListView mMediaListView;
    private MediaAdapter mAdapter;
    private String medName, medScore, minfo1, minfo1Val, minfo2, minfo2Val, minfo3, minfo3Val,medplot;

    public static MediaInfoListFragment newInstance() {
        MediaInfoListFragment f = new MediaInfoListFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_media_info, container, false);

        initView();
        getMediaInfo();
        setMediaInfo();
        return viewGroup;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        mAdapter = new MediaAdapter(activity);
        mMediaListView.setAdapter(mAdapter);

        mAdapter.addItem(minfo1,minfo1Val);
        mAdapter.addItem(minfo2, minfo2Val);
        mAdapter.addItem(minfo3, minfo3Val);

    }


    final class MediaItem {
        String mNmae;
        String mValue;

        public MediaItem(String name, String value) {
            mNmae = name;
            mValue = value;
        }
    }

        final class MediaAdapter extends ArrayAdapter<MediaItem> {
            public MediaAdapter(Context context) {
                super(context, R.layout.info_list_item_2);
            }

            public void addItem(String name, String value) {
                add(new MediaItem(name, value));
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    view = inflater.inflate(R.layout.info_list_item_2, parent, false);
                }

                MediaAdapter.ViewHolder viewHolder = (MediaAdapter.ViewHolder) view.getTag();
                if (viewHolder == null) {
                    viewHolder = new MediaAdapter.ViewHolder();
                    viewHolder.mNameTextView = (TextView) view.findViewById(R.id.text1);
                    viewHolder.mValueTextView = (TextView) view.findViewById(R.id.text2);
                }

                MediaItem item = getItem(position);
                viewHolder.mNameTextView.setText(item.mNmae);
                viewHolder.mValueTextView.setText(item.mValue);

                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }

            final class ViewHolder {
                public TextView mNameTextView;
                public TextView mValueTextView;
            }
        }

    public void initView(){
        mName = (TextView) viewGroup.findViewById(R.id.name_txt);
        mScore= (TextView) viewGroup.findViewById(R.id.score_txt);
        mplot = (Button) viewGroup.findViewById(R.id.plot_txt);
        mplot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext(),R.style.dialog).setMessage(medplot)
                        .show();
            }
        });
        mFullScreen=(Button) viewGroup.findViewById(R.id.full_btn);
        mFavorite=(Button) viewGroup.findViewById(R.id.fav_btn);
        mMediaListView = (ListView) viewGroup.findViewById(R.id.mediainfo_list);
        mMediaListView.setFocusable(false);
    }
    public void getMediaInfo(){
        SharedPreferences videoinfoshare=getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        medName=videoinfoshare.getString("name","");
        medScore=videoinfoshare.getString("score","");

        minfo1="集数：";
        String latestOrder=videoinfoshare.getString("latestOrder","");
        String videoCount=videoinfoshare.getString("videoCount","");
        minfo1Val="更新至"+latestOrder+"集/共"+videoCount+"集";

        minfo2="导演：";
        minfo2Val=videoinfoshare.getString("directorname","");

        minfo3="主演：";
        minfo3Val=videoinfoshare.getString("secondInfo","");

        medplot=videoinfoshare.getString("description","");

    }

    public void setMediaInfo(){
        mName.setText(medName);
        mScore.setText(medScore);
        mplot.setText(medplot);
    }



}
