package com.example.eyankiv.mytuberecycl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yevgeni on 24/02/2017.
 */

public class MyAdapter extends ArrayAdapter<YouTubeItem> {

    public static final String DEVELOPER_KEY = "AIzaSyAcTTf9dmb1D-AqJ41Y9KcCEnWANrSr1Po";
    private static final String TAG = "initError";
    Activity activity;
    private final List<YouTubeItem> vodList;
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbViewToLoaderMap;
    //ThumbListener thumbListener;
    String videoId;
    YouTubePlayerFragment youTubePlayerFragment;


    public MyAdapter(Activity activity, List<YouTubeItem> vodList) {
        super(activity, R.layout.list_item, vodList);
        this.activity = activity;
        this.vodList = vodList;

        //thumbListener = new ThumbListener();
        thumbViewToLoaderMap = new HashMap<>();
    }

    static class ViewContainer {
        TextView vodTitle;
        YouTubeThumbnailView tubeThumbnailView;
        //YouTubeThumbnailLoader vodThumbLoader;
        ImageView vodThumb;
    }

    public void releaseLoaders() {
        for (YouTubeThumbnailLoader loader : thumbViewToLoaderMap.values()) {
            loader.release();
        }
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewContainer viewContainer = new ViewContainer();
        videoId = vodList.get(position).getmLink().substring(32);

        if (rowView == null) {
            final LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item, null);
            viewContainer.vodTitle = (TextView) rowView.findViewById(R.id.videoTitleLbl);

            //Youtube operation
            viewContainer.tubeThumbnailView = (YouTubeThumbnailView) rowView.findViewById(R.id.tubePlayerLbl);
            viewContainer.tubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView view, final YouTubeThumbnailLoader loader) {
                    loader.setVideo(videoId);
                    thumbViewToLoaderMap.put(view, loader);
                    view.setImageResource(R.drawable.loading_thumbnail);
                    loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            loader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            youTubeThumbnailView.setImageResource(R.drawable.no_thumbnail);
                        }
                    });

                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView view, YouTubeInitializationResult initializationResult) {
                    Log.d(TAG, "onInitializationFailure: " + initializationResult.getErrorDialog(activity, 123).toString());

                }


            });

            rowView.setTag(viewContainer);

//            if (viewContainer.vodThumbLoader == null){
//                viewContainer.vodThumbLoader = thumbViewToLoaderMap.get(viewContainer.tubeThumbnailView);
//            }
//            viewContainer.vodThumb = (ImageView) rowView.findViewById(R.id.youTubeThumbLbl);
//            rowView.setTag(viewContainer);
//        }else {
//            viewContainer = (ViewContainer) rowView.getTag();
//            if (viewContainer.vodThumbLoader == null){
//                viewContainer.vodThumbLoader = thumbViewToLoaderMap.get(viewContainer.tubeThumbnailView);//repeated
//                //viewContainer.videoId = vodList.get(position).getmLink().substring(32);//repeated
//                rowView.setTag(viewContainer.vodThumbLoader);//Not sure this is correct
//            }
//            /*else {
//                //String videoId = vodList.get(position).getmLink().substring(32);
//                viewContainer.vodThumbLoader.setVideo(vodList.get(position).getmLink().substring(32));
//                viewContainer.tubeThumbnailView.setImageResource(R.drawable.loading_thumbnail);
//            }*/
//        }
//        viewContainer.vodThumbLoader.setVideo(vodList.get(position).getmLink().substring(32));
//        viewContainer.tubeThumbnailView.setImageResource(R.drawable.loading_thumbnail);
//        viewContainer.vodTitle.setText(vodList.get(position).getMtitle());


            // TODO: 03/03/2017 setThumb image :
//        try {
//            Uri imageLink = new URI(vodList.get(position).getmThumb());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        viewContainer.vodThumb.setImageURI();

            //viewContainer.vodWebView.loadUrl(vodList.get(position).getmLink());//watch out for invalid url.

//        try {
//            URI thumbUri = new URI(vodList.get(position).getmThumb());
//            //viewContainer.vodThumb.setD;
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
        } else {
            viewContainer = (ViewContainer) rowView.getTag();
        }
        viewContainer.vodTitle.setText(vodList.get(position).getMtitle());
        //viewContainer.tubeThumbnailView.initialize(DEVELOPER_KEY,);

        return rowView;
    }


}

