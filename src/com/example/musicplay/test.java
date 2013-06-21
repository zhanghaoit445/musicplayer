package com.example.musicplay;

import android.database.Cursor;
import android.provider.MediaStore;
import android.test.AndroidTestCase;
public class test extends AndroidTestCase{
    //MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    public void  name() {
        Cursor c = getContext().getContentResolver().query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI ,
                new String[]{
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME,
}, 
                null, 
                null,
                null);
        if (c==null) {
            System.out.println("空空空空空空空空空空空");
        }
        if (c.getCount()<=0) {
            System.out.println("0空空空空空空空空空空空");
        }
       while (c.moveToNext()) {
         //  c.getColumnIndex(MediaStore.Audio.Media.ALBUM)
        System.out.println(c.getString(2));
        System.out.println(c.getString(0));
        System.out.println(c.getString(1));
    }//https://github.com/zhanghaoit445/player.git
    }
}
