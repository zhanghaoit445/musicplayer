
package com.example.musicplay;

import android.support.v4.app.Fragment;
import android.widget.TextView;

public final class TestFragment {
    public static Fragment newInstance(int position) {
        Fragment myfFragment = null;
        switch (position) {
            case 0:
                myfFragment=new Album();
                break;
            case 1:
                myfFragment=new Songs();
                break;
            case 2:
                myfFragment=new SInger();
                break;
            case 4:
                myfFragment=new Cach();
                break;
            case 3:
                myfFragment=new School();
                break;
            case 5:
                myfFragment=new Search();
                break;
            default:
                break;
        }
        if (myfFragment!=null) 
            return myfFragment;
        return myfFragment;
       
    }

    
/*     * private String mContent = "???";
     * @Override public void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState); if ((savedInstanceState != null) &&
     * savedInstanceState.containsKey(KEY_CONTENT)) { mContent =
     * savedInstanceState.getString(KEY_CONTENT); } 
     * }
     * 
     * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
     * container, Bundle savedInstanceState) { TextView text = new
     * TextView(getActivity()); text.setGravity(Gravity.CENTER);
     * text.setText(mContent); text.setTextSize(20 *
     * getResources().getDisplayMetrics().density); text.setPadding(20, 20, 20,
     * 20); LinearLayout layout = new LinearLayout(getActivity());
     * layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
     * LayoutParams.FILL_PARENT)); layout.setGravity(Gravity.CENTER);
     * layout.addView(text); return layout; }
     * @Override public void onSaveInstanceState(Bundle outState) {
     * super.onSaveInstanceState(outState); outState.putString(KEY_CONTENT,
     * mContent); }*/
     
}
