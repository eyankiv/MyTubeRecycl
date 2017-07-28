package com.example.eyankiv.mytuberecycl;

public class YouTubeItem {
    private String mtitle;
    private String mLink;
    private String mThumb;

    public String getmThumb() {
        return mThumb;
    }

    public YouTubeItem(String mtitle, String mLink, String mThumb) {
        this.mtitle = mtitle;
        this.mLink = mLink;
        this.mThumb = mThumb;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getmLink() {
        return mLink;
    }

    @Override
    public String toString() {
        return "YouTubeItem{" +
                "mtitle='" + mtitle + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mThumb='" + mThumb+ '\''+
                '}';
    }
}

