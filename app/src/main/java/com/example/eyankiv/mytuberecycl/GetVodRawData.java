package com.example.eyankiv.mytuberecycl;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yevgeni on 25/02/2017.
 */

public class GetVodRawData extends GetRawData {

    public static final String PLAYLIST_ARR_ITEM = "Playlists";


    private static final String LIST_ARR_VOD_ITEM = "ListItems";
    //VOD elements
    private static final String VOD_TITLE = "Title";
    private static final String VOD_LINK = "link";
    private static final String VOD_THUMB = "thumb";


    public static final String LOG_TAG = GetVodRawData.class.getSimpleName();

    private URL jsonLinkUrl;
    private List<YouTubeItem> mVodItems;
    private HashMap<String, List<YouTubeItem>> mVodList;
    private Handler handler = new Handler();
    private Runnable postDataPull;

    public void setPostDataPull(Runnable postDataPull) {
        this.postDataPull = postDataPull;
    }

    public GetVodRawData(String mRawUrl)  {
        super(null);
        mVodItems = new ArrayList<>();
        mVodList = new HashMap<>();
        try {
            jsonLinkUrl = new URL(mRawUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public List<YouTubeItem> getmVodItems() {
        return mVodItems;
    }

    public HashMap<String, List<YouTubeItem>> getmVodList() {
        return mVodList;
    }

    public void execute(){
        super.setmRawUrl(jsonLinkUrl.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG,"Link to Json is: "+jsonLinkUrl);
        downloadJsonData.execute(jsonLinkUrl.toString());
    }

    public void proccessRowData() {
        if (getmDownloadingStatus() != DownloadingStatus.OK) {
            Log.e(LOG_TAG, "error downloading raw file");
            return;
        }

        try {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray jsonVODplaylist = jsonData.getJSONArray(PLAYLIST_ARR_ITEM);
            for (int i = 0; i < jsonVODplaylist.length(); i++) {
                JSONArray jsonVODlistItems = jsonVODplaylist.getJSONObject(i).getJSONArray(LIST_ARR_VOD_ITEM);///****review
                for (int j = 0; j < jsonVODlistItems.length(); j++) {
                    JSONObject jsonTubeItem = jsonVODlistItems.getJSONObject(j);
                    String vodTitle = jsonTubeItem.getString(VOD_TITLE);
                    String vodLink = jsonTubeItem.getString(VOD_LINK);
                    String vodThumb = jsonTubeItem.getString(VOD_THUMB);
                    mVodItems.add(new YouTubeItem(vodTitle,vodLink,vodThumb));
                }
                mVodList.put(jsonVODlistItems.getString(i),mVodItems);
            }
            //here we make sure that we run the process after we get the data
            handler.post(postDataPull);


//            for (YouTubeItem singleVodItem: mVodItems) {
//                Log.v("Array of Vod items", singleVodItem.toString());
//            }

        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG,"Error processing Json data");
        }

    }

    public class DownloadJsonData extends DownloadRawData{
        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            proccessRowData();
        }

        @Override
        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }
    }
}

