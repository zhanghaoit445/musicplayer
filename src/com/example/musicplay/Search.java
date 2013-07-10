package com.example.musicplay;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Search extends Fragment implements OnClickListener, OnItemClickListener {
    EditText editText;
    ImageButton button;
    // com.tjerkw.slideexpandable.library.ActionSlideExpandableListView
    ActionSlideExpandableListView listView;
    LayoutInflater inflater;
    String searchNameString;
    Myadapter adapter;
    PagerBarHelper mPagerView;
    DisplayImageOptions options;
    ArrayList<String> urlArrayList;
    ImageLoader imageLoader;
    DownloadManager downloadManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchresult_pager0, null);
        view.setId(3);
        this.inflater = inflater;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .displayer(new RoundedBitmapDisplayer(60))
                .build();
        downloadManager=(DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
        button = (ImageButton) view.findViewById(R.id.btn_search1);
        editText = (EditText) view.findViewById(R.id.search_edit2);
        listView = (ActionSlideExpandableListView) view.findViewById(R.id.list);
        listView.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
            @Override
            public void onClick(View listView, View buttonview, int position) {
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
            }
        }, R.id.buttonA, R.id.buttonB, R.id.buttonC);
        listView.setOnItemClickListener(this);
        mPagerView = new PagerBarHelper(view.findViewById(R.id.FooterBar), getActivity());
        mPagerView.setNextOnClickListener(this);
        if (list != null) {
            adapter = new Myadapter();
            listView.setAdapter(adapter);
        }
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != null && list.size() > 0l) {
                    if (adapter != null) {
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
                searchNameString = editText.getText().toString();
                if (searchNameString == null)
                    Toast.makeText(getActivity(), R.string.query, 1).show();
                else {
                    try {
                        new Myhandle().execute(Util.urlString
                                + URLEncoder.encode(searchNameString, "utf-8"));
                        button.setEnabled(false);
                        final InputMethodManager imm = (InputMethodManager) getActivity()
                                .getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(),
                                0);
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    LinkedList<MusicInfo> list=new LinkedList<MusicInfo>();
    private class Myhandle extends AsyncTask<String, Void, LinkedList<MusicInfo>> {
        @Override
        protected LinkedList<MusicInfo> doInBackground(String... params) {
            return Util.getXiamiSRs(params[0]);
        }
        @Override
        protected void onPostExecute(LinkedList<MusicInfo> result) {
            super.onPostExecute(result);
            button.setEnabled(true);
            if (urlArrayList != null) {
                urlArrayList.addAll(getUrl(result));
            } else
                urlArrayList = getUrl(result);

            if (list == null && result == null) {// 第一次请求不成功

            } else if (list != null && result != null) {// 第二次请求成功
                list.addAll(result);
                System.out.println(list.size());
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter = new Myadapter();
                    adapter.notifyDataSetChanged();
                }
            } else if (result != null) {// 第一次请求成功
                list .addAll(result);
                adapter = new Myadapter();
                listView.setAdapter(adapter);
            }
            mPagerView.showFooter();
        }
    }

    class Myadapter extends BaseAdapter {
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        // private ImageLoadingListener animateFirstListener = new
        // AnimateFirstDisplayListener();
        private class ViewHolder {
            public TextView text, singer;
            public ImageView image;
            public ImageButton action;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.listiemt, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.music1);
                holder.singer = (TextView) view.findViewById(R.id.singer);
                holder.image = (ImageView) view.findViewById(R.id.listitem);
                holder.action = (ImageButton) view.findViewById(R.id.time_button);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.text.setText(list.get(position).nameString);
            holder.singer.setText(list.get(position).singerString);
            /*
             * holder.action.setOnClickListener(new OnClickListener() {
             * @Override public void onClick(View v) {
             * System.out.println("点击效果"+position); } });
             */
            /*
             * if (imageLoader==null) { imageLoader = ImageLoader.getInstance();
             * System.out.println("为空"); }else { System.out.println("不为空"); } if
             * (urlArrayList.isEmpty()) { System.out.println("urlArrayList为空");
             * }
             */
            imageLoader.displayImage(urlArrayList.get(position), holder.image, options,
                    animateFirstListener);
            return view;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
    }

    @Override
    public void onClick(View v) {
        try {
            Util.pageString++;
            new Myhandle().execute(Util.urlString
                    + URLEncoder.encode(searchNameString, "utf-8"));
            System.out.println(Util.pageString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        button.setEnabled(false);
        mPagerView.hideFooter();
    }

    /**
     * 拿出所有图片url
     * 
     * @return
     */
    private ArrayList<String> getUrl(LinkedList<MusicInfo> list) {
        if (list != null) {
            ArrayList<String> list2 = new ArrayList<String>();
            for (MusicInfo musicInfo : list) {
                list2.add(musicInfo.singerIMage);
            }
            return list2;
        } else
            return null;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    //displayedImages.add(imageUri);
                }
            }
        }
    }
}
