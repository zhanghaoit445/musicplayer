package com.example.musicplay;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
public class Album extends Fragment {
  ListView  listiew;
  String [] albums;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            new String[]{MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME}, 
            null, 
            null,
            MediaStore.Audio.Media.ALBUM);
    c.moveToFirst();
    int num = c.getCount();
    HashSet set = new HashSet();
    for (int i = 0; i < num; i++){ //去除重复的专辑
        String szAlbum = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        set.add(szAlbum);
        c.moveToNext();
    }
    num = set.size();
    Iterator it = set.iterator();
    albums = new String[num];
    int i = 0;
    while(it.hasNext()){// 将专辑添加到String里面
        albums[i] = it.next().toString();
        i++;
    }
    String album="";
    for (int j=0;j<num; j++){
        if (j<num-1){
            album = album + "'" + albums[j] + "',"; 
        } else{
            album = album + "'" + albums[j] + "'";
        }
    }

    c.moveToFirst();
    HashMap<String, String> map = new HashMap<String, String>();
    int num1 = c.getCount();
    for (int j=0;j<num1;j++){
        map.put(c.getString(3), c.getString(2));
        c.moveToNext();
    }
    listiew = new ListView(getActivity());
    listiew.setId(1);
    listiew.setAdapter(new com.example.musicplay.adapter.AlbumListAdapter(getActivity(),albums,map)); //new AlbumListAdapter(this, albums,map)
  listiew.setCacheColorHint(R.color.transparent);
    //  listview.setOnItemClickListener(new AlbumsItemClickListener());
    
    return listiew;
    
}
}
