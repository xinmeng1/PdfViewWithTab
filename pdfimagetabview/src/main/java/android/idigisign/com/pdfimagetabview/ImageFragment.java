package android.idigisign.com.pdfimagetabview;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.idigisign.com.pdfimagetabview.loader.MediaLoader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 04:58:40 PM
 * Version: V 1.0
 */

public class ImageFragment extends Fragment {

    private MediaInfo mMediaInfo;

    private HackyViewPager viewPager;
    private ImageView backgroundImage;
    private PhotoViewAttacher photoViewAttacher;

    public void setMediaInfo(MediaInfo mediaInfo) {
        mMediaInfo = mediaInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.image_fragment, container, false);
        backgroundImage = (ImageView) rootView.findViewById(R.id.backgroundImage);
        viewPager = (HackyViewPager) getActivity().findViewById(R.id.viewPager);

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(Constants.IS_LOCKED, false);
            viewPager.setLocked(isLocked);
            backgroundImage.setImageBitmap((Bitmap) savedInstanceState.getParcelable(Constants.IMAGE));
            createViewAttacher(savedInstanceState);
        }

        loadImageToView();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        int[] viewCoords = new int[2];
        Log.i("Position2",
                "Before Image location X: " + String.valueOf(viewCoords[0])
                        + " Y: " + String.valueOf(viewCoords[1]));
        backgroundImage.getLocationOnScreen(viewCoords);
        Log.i("Position2",
                "Image location X: " + String.valueOf(viewCoords[0])
                        + " Y: " + String.valueOf(viewCoords[1]));
        final int[][] imageXY = {viewCoords} ;

        backgroundImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();

                int imageX = touchX - imageXY[0][0]; // viewCoords[0] is the X coordinate
                int imageY = touchY - imageXY[0][1]; // viewCoords[1] is the y coordinate

                Log.i("Position2",
                        "Position Touch on Image X: " + String.valueOf(imageX)
                                + " Y: " + String.valueOf(imageY));
                return false;
            }
        });
    }

    private void loadImageToView() {
        if (mMediaInfo != null) {
            mMediaInfo.getLoader().loadMedia(getActivity(), backgroundImage, new MediaLoader.SuccessCallback() {
                @Override
                public void onSuccess() {
                    createViewAttacher(getArguments());
                }
            });
        }
    }

    private void createViewAttacher(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(Constants.ZOOM)) {
            photoViewAttacher = new PhotoViewAttacher(backgroundImage);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(Constants.IS_LOCKED, viewPager.isLocked());
        }
        outState.putParcelable(Constants.IMAGE, ((BitmapDrawable) backgroundImage.getDrawable()).getBitmap());
        outState.putBoolean(Constants.ZOOM, photoViewAttacher != null);
        super.onSaveInstanceState(outState);
    }

    private boolean isViewPagerActive() {
        return viewPager != null;
    }
}

