package android.idigisign.com.pdfimagetabview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 04:58:23 PM
 * Version: V 1.0
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<MediaInfo> mListOfMedia;

    private boolean isZoom = false;

    public ScreenSlidePagerAdapter(FragmentManager fm, List<MediaInfo> listOfMedia,
                                   boolean isZoom) {
        super(fm);
        this.mListOfMedia = listOfMedia;
        this.isZoom = isZoom;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < mListOfMedia.size()) {
            MediaInfo mediaInfo = mListOfMedia.get(position);
            fragment = loadImageFragment(mediaInfo);
        }
        return fragment;
    }

    private Fragment loadImageFragment(MediaInfo mediaInfo) {
        ImageFragment fragment = new ImageFragment();
        fragment.setMediaInfo(mediaInfo);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.ZOOM, isZoom);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mListOfMedia.size();
    }
}

