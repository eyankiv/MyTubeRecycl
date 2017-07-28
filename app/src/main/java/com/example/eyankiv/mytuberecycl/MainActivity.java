package com.example.eyankiv.mytuberecycl;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    public static final String RAW_URL = "http://www.razor-tech.co.il/hiring/youtube-api.json";
    public static final int RECOVERY_DIALOG_REQUEST = 123;
    ListView vodListView;
    MyAdapter myAdapter;
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

        getVodRowData = new GetVodRawData(RAW_URL);
        vodListView = (ListView) findViewById(R.id.vodList);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                myAdapter = new MyAdapter(MainActivity.this, tubeVodItems);
                vodListView.setAdapter(myAdapter);
                vodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager().findFragmentById(R.id.tubeFragLbl);
                        tubeVideoId= tubeVodItems.get(position).getmLink().substring(32);
                        startActivity(YouTubeStandalonePlayer.createVideoIntent(MainActivity.this,myAdapter.DEVELOPER_KEY,tubeVideoId,0,true,true));

//                        youTubePlayerFragment.initialize(myAdapter.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
//                            @Override
//                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                                youTubePlayer.cueVideo(tubeVideoId);
//                            }
//
//                            @Override
//                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                                Toast.makeText(MainActivity.this, "Your video Failed due to : "+youTubeInitializationResult, Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    }
                });
            }
        };
        //we get the data before we execute.
        getVodRowData.setPostDataPull(r);
        getVodRowData.execute();
        tubeVodItems = getVodRowData.getmVodItems();



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