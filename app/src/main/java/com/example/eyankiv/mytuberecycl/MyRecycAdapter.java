package com.example.eyankiv.mytuberecycl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
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
    private final TubeItemClickListener mOnClicklistener;
    //ThumbListener thumbListener;
    String videoId;
    //YouTubePlayerFragment youTubePlayerFragment;

    public MyRecycAdapter(Activity activity,List<YouTubeItem> vodList, TubeItemClickListener listener) {
        this.vodList = vodList;
        this.activity = activity;
        mTubeItems = vodList.size();
        mOnClicklistener = listener;
        //TODO : vod list populate
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
    public void onBindViewHolder(final TubeVideoViewHolder holder, final int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);


        holder.tubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(videoId);
                thumbViewToLoaderMap.put(youTubeThumbnailView, youTubeThumbnailLoader);
                youTubeThumbnailView.setImageResource(R.drawable.loading_thumbnail);
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        videoId = vodList.get(position).getmLink().substring(32);
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Toast.makeText(activity.getParent(), "Error loading thumbnail: " + errorReason.toString(),Toast.LENGTH_SHORT);
                        youTubeThumbnailView.setImageResource(R.drawable.no_thumbnail);
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure: " + youTubeInitializationResult.toString());
            }
        });
        releaseLoaders();
    }


    @Override
    public int getItemCount() {
        return mTubeItems;
    }

    class TubeVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView listItemTubeView;
        YouTubeThumbnailView tubeThumbnailView;

        public TubeVideoViewHolder(View itemView) {
            super(itemView);

        }

        void bind(int listIndex) {
            listItemTubeView.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClicklistener.onVideoClicked(clickedPosition);


        }
    }

    public interface TubeItemClickListener{
        void onVideoClicked(int clickedVideoIndex);
    }
}
