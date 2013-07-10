package com.example.musicplay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SInger extends Fragment {
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    TextView xTextView=  new TextView(getActivity());
    xTextView.setId(3);
    xTextView.setText("艺术家");
    return xTextView;
}
}
