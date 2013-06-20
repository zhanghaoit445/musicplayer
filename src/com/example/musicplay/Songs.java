package com.example.musicplay;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplay.adapter.MusicListAdapter;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

public class Songs extends Fragment {
    ActionSlideExpandableListView listview;
    Cursor c ;
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
     c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            new String[]{MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM_ID,} , null, null,null);
        // setListAdapter(new MusicListAdapter(getActivity(), c));
}
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
               View view=  inflater.inflate(R.layout.searchresult_pager0, container,false);
       view.findViewById(R.id.select_search_layout).setVisibility(View.GONE);
       listview=(ActionSlideExpandableListView) view.findViewById(R.id.list);
       listview.setAdapter(new MusicListAdapter(getActivity(), c));
       listview.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
           @Override
           public void onClick(View listView, View buttonview, int position) {/*
                                        MusicInfo musicInfo=  list.get(position);
               switch (buttonview.getId()) {
                   case R.id.buttonA://下载
                     Util.downland(downloadManager, musicInfo.addresString, musicInfo.nameString);
                       break;
                   case R.id.buttonB://播放
                       break;
                   case R.id.buttonC://加入队列
                       break;
                   default:
                       break;
               }
           */}
       }, R.id.music_buttonA, R.id.music_buttonB, R.id.music_buttonC);
               return view;
    }
}
