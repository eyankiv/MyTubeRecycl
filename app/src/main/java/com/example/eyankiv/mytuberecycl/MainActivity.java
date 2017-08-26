package com.example.eyankiv.mytuberecycl;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnFullscreenListener{

    private MyRecycAdapter mRecycAdapter;
    private RecyclerView mVodRecycList;
    public static final int TUBE_LIST_ITEMS = 200;
    Toast mToast;
    public static final int RECOVERY_DIALOG_REQUEST = 123;

    public static final String RAW_URL = "http://www.razor-tech.co.il/hiring/youtube-api.json";
//        ListView vodListView;
//       MyAdapter myAdapter;
    HashMap<String, String> vodItemList = new HashMap<>();
    List<YouTubeItem> tubeVodItems;
    //GetRawData getRawData;
    GetVodRawData getVodRowData;
    String tubeVideoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final YouTubeInitializationResult result = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if(result != YouTubeInitializationResult.SUCCESS){
            result.getErrorDialog(this,0).show();
        }


        //getRawData = new GetRawData(RAW_URL);
        //getRawData.execute();

        getVodRowData = new GetVodRawData(RAW_URL,getAssets());
        getVodRowData.proccessRowData();
        //vodListView = (ListView) findViewById(R.id.vodList);
        mVodRecycList = (RecyclerView)findViewById(R.id.vodList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mVodRecycList.setLayoutManager(layoutManager);
        //mVodRecycList.setHasFixedSize(true);
        tubeVodItems = getVodRowData.getmVodItems();

        mRecycAdapter = new MyRecycAdapter(this, tubeVodItems, new MyRecycAdapter.TubeItemClickListener() {
            @Override
            public void onVideoClicked(int clickedVideoIndex) {
                tubeVideoId = tubeVodItems.get(clickedVideoIndex).getmLink().substring(32);
                startActivity(YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, MyRecycAdapter.DEVELOPER_KEY, tubeVideoId, 0, true, true));
            }
        });
        mVodRecycList.setAdapter(mRecycAdapter);


        checkYoutubeApi();
        //Log.v("Vod Items from Json",tubeVodItems.toString());
    }

    private void checkYoutubeApi(){
        YouTubeInitializationResult errorReason = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if(errorReason.isUserRecoverableError()){
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        }else if (errorReason != YouTubeInitializationResult.SUCCESS){
            Toast.makeText(this, "There was an error initializing YoutubePlayer "+errorReason.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFullscreen(boolean b) {

    }
}