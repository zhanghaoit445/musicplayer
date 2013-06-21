
package com.example.musicplay;

import android.provider.MediaStore;

public class SongBean implements MediaStore.Audio.AudioColumns {
    long song_id, album_id;

    public SongBean(long song_id, long album_id) {
        this.song_id = song_id;
        this.album_id = album_id;
    }
}
