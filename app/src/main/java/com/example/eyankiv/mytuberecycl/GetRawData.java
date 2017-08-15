package com.example.eyankiv.mytuberecycl;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by yevgeni on 24/02/2017.
 */


enum DownloadingStatus {
    IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK
}

public class GetRawData {

    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadingStatus mDownloadingStatus;

    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
        this.mDownloadingStatus = DownloadingStatus.IDLE;
    }
    /*public GetRawData(File jsonFileName){
        File jsonTxtFile = jsonFileName;
        if (jsonFileName.length() == 0) {
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    String jsonTxt = new String(Files.readAllBytes(Paths.get("TubeListJson.json")));
                    mData = jsonTxt;
                }else {
                    System.out.println("file supports java 7 or higher");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/



    public void setmRawUrl(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }

    public void Reset(){
        this.mDownloadingStatus = DownloadingStatus.IDLE;
        this.mRawUrl = null;
        this.mData = null;
    }

    public String getmData() {
        return mData;
    }

    public DownloadingStatus getmDownloadingStatus() {
        return mDownloadingStatus;
    }

    public void execute(){
        this.mDownloadingStatus = DownloadingStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawUrl);
    }

    public class DownloadRawData extends AsyncTask<String,Void,String>{

        @Override
        protected void onPostExecute(String webData) {
            mData = webData;
            //to display for self
            // Log.v(LOG_TAG, "Data received: "+mData);
            if(mData == null){
                if(mRawUrl == null)
                    mDownloadingStatus = DownloadingStatus.NOT_INITIALIZED;
                mDownloadingStatus = DownloadingStatus.FAILED_OR_EMPTY;
            }else {
                mDownloadingStatus = DownloadingStatus.OK;
            }
        }


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            StringBuilder stringBuilder = new StringBuilder();

            if(params == null)
                return null;
            URL url;
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                byte[] buffer = new byte[1024];
                int actuallyRead;
                while((actuallyRead = inputStream.read(buffer)) != -1){
                    stringBuilder.append(new String(buffer,0,actuallyRead));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG,"error");
            }finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
                if(inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return stringBuilder.toString();
        }
    }
}
