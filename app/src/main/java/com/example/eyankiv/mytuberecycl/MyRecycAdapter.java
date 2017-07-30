package com.example.eyankiv.mytuberecycl;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eyankiv on 28-Jul-17.
 */

public class MyRecycAdapter extends RecyclerView.Adapter<MyRecycAdapter.TubeVideoViewHolder> {

    public static final String DEVELOPER_KEY = "AIzaSyAcTTf9dmb1D-AqJ41Y9KcCEnWANrSr1Po";
    private static final String initialErrorTAG = "initError";
    public static final String TAG = MyRecycAdapter.class.getSimpleName();
    private int mTubeItems;
    Activity activity;
    private final List<YouTubeItem> vodList;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbViewToLoaderMap;
    //ThumbListener thumbListener;
    String videoId;
    YouTubePlayerFragment youTubePlayerFragment;

    public MyRecycAdapter(Activity activity, List<YouTubeItem> vodList) {
        this.activity = activity;
        this.vodList = vodList;

        thumbViewToLoaderMap = new HashMap<>();
    }


    public void releaseLoaders(){
        for (YouTubeThumbnailLoader loader : thumbViewToLoaderMap.values()) {
            loader.release();
        }
    }
    @Override
    public TubeVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TubeVideoViewHolder viewHolder = new TubeVideoViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TubeVideoViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mTubeItems;
    }

    class TubeVideoViewHolder extends RecyclerView.ViewHolder{
        TextView listItemTubeView;
        YouTubeThumbnailView tubeThumbnailView;

        public TubeVideoViewHolder(View itemView) {
            super(itemView);
        }

        void bind(int listIndex) {
            listItemTubeView.setText(String.valueOf(listIndex));
        }
    }
}
