package android.idigisign.com.pdfimagetabview;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.idigisign.com.pdfimagetabview.loader.MediaLoader;
import android.idigisign.com.pdfimagetabview.model.Tab;
import android.idigisign.com.pdfimagetabview.widget.TabView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 04:58:40 PM
 * Version: V 1.0
 */

public class ImageFragment extends Fragment implements View.OnDragListener, View.OnLongClickListener{
    public final static String TAG = "ScrollGalleryView_ImageFragment";

    //Pdf image container
    RelativeLayout pdfImageLinearContainer;

    private MediaInfo mMediaInfo;

    private HackyViewPager viewPager;
    private ImageView backgroundImage;
    private PhotoViewAttacher photoViewAttacher;

    //All the tabs on the ScrollGalleryView
    ArrayList<Tab> tabs = new ArrayList<>();
    //All the tabs on the ScrollGalleryView
    ArrayList<TabView> tabViews = new ArrayList<>();
    public void setMediaInfo(MediaInfo mediaInfo) {
        mMediaInfo = mediaInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.image_fragment, container, false);
        backgroundImage = (ImageView) rootView.findViewById(R.id.backgroundImage);
        viewPager = (HackyViewPager) getActivity().findViewById(R.id.viewPager);

        pdfImageLinearContainer = (RelativeLayout) rootView.findViewById(R.id.image_container);
        pdfImageLinearContainer.setOnDragListener(this);

        //pdfImageLinearContainer.setOnDragListener(this);

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

        //We can not setOnTouch, because we need to Scale the image by using touch
//        int[] viewCoords = new int[2];
//        Log.i("Position2",
//                "Before Image location X: " + String.valueOf(viewCoords[0])
//                        + " Y: " + String.valueOf(viewCoords[1]));
//        backgroundImage.getLocationOnScreen(viewCoords);
//        Log.i("Position2",
//                "Image location X: " + String.valueOf(viewCoords[0])
//                        + " Y: " + String.valueOf(viewCoords[1]));
//        final int[][] imageXY = {viewCoords} ;
//
//        backgroundImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int touchX = (int) event.getX();
//                int touchY = (int) event.getY();
//
//                int imageX = touchX - imageXY[0][0]; // viewCoords[0] is the X coordinate
//                int imageY = touchY - imageXY[0][1]; // viewCoords[1] is the y coordinate
//
//                Log.i("Position2",
//                        "Position Touch on Image X: " + String.valueOf(imageX)
//                                + " Y: " + String.valueOf(imageY));
//                return false;
//            }
//        });
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

    @Override
    public boolean onDrag(View receivingLayoutView, DragEvent dragEvent) {

        View draggedImageView = (View) dragEvent.getLocalState();
        // Handles each of the expected events
        switch (dragEvent.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:
                Log.i(TAG, "drag action started");

                // Determines if this View can accept the dragged data
                if (dragEvent.getClipDescription()
                        .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    Log.i(TAG, "Can accept this data");

                    // returns true to indicate that the View can accept the dragged data.
                    return true;

                } else {
                    Log.i(TAG, "Can not accept this data");

                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.i(TAG, "drag action entered");
                //the drag point has entered the bounding box
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                Log.i(TAG, "drag action location" + dragEvent.getX());
                Log.i(TAG, "drag action location" + dragEvent.getY());
                /*triggered after ACTION_DRAG_ENTERED
                stops after ACTION_DRAG_EXITED*/
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.i(TAG, "drag action exited");
                //the drag shadow has left the bounding box
                return true;

            case DragEvent.ACTION_DROP:
            /* the listener receives this action type when drag shadow released
            over the target view the action only sent here if ACTION_DRAG_STARTED
            returned true return true if successfully handled the drop else false*/
                Log.i(TAG, "drop");
                ViewGroup draggedImageViewParentLayout
                        = (ViewGroup) draggedImageView.getParent();
                TabView imageView = new TabView(getContext());
                RelativeLayout.LayoutParams layoutParams =
                        new RelativeLayout.LayoutParams(
                                (int)getResources().getDimension(R.dimen.tab_width),
                                (int)getResources().getDimension(R.dimen.tab_height));
                int i = draggedImageView.getId();
                for (int index =0; index < tabViews.size();index++){
                    if (i == tabViews.get(index).getId()){
                        layoutParams.setMargins((int) dragEvent.getX(), (int) dragEvent.getY(), 0, 0);
                        pdfImageLinearContainer.updateViewLayout(tabViews.get(index), layoutParams);
                        break;
                    }
                }

                if (i == R.id.tab_signature) {
                    Log.i(TAG, "tab1");
                    //draggedImageViewParentLayout.removeView(draggedImageView);
                    //LinearLayout bottomLinearLayout = (LinearLayout) receivingLayoutView;
                    //dragEvent.getX()
                    //bottomLinearLayout.addView(draggedImageView);
                    imageView.setBackground(getResources().getDrawable(R.drawable.tab_design_signature));
                    //Log.i(TAG, "x: " + String.valueOf(dragEvent.getX()) + "  Y: " + String.valueOf(dragEvent.getY()));
                    layoutParams.setMargins((int) dragEvent.getX(), (int) dragEvent.getY(), 0, 0);
                    pdfImageLinearContainer.addView(imageView, layoutParams);

                    imageView.setOnLongClickListener(this);
                    imageView.setOnDragListener(this);
                    tabViews.add(imageView);
                    //draggedImageView.setVisibility(View.VISIBLE);
                    return true;
                } else if (i == R.id.tab_initial) {
                    Log.i(TAG, "tab2");
                    //draggedImageViewParentLayout.removeView(draggedImageView);
                    //LinearLayout bottomLinearLayout = (LinearLayout) receivingLayoutView;
                    //dragEvent.getX()
                    //bottomLinearLayout.addView(draggedImageView);
                    imageView.setBackground(getResources().getDrawable(R.drawable.tab_design_initial));
                    layoutParams.setMargins((int) dragEvent.getX(), (int) dragEvent.getY(), 0, 0);
                    //Log.i(TAG, "x: " + String.valueOf(dragEvent.getX()) + "  Y: " + String.valueOf(dragEvent.getY()));
                    pdfImageLinearContainer.addView(imageView, layoutParams);
                    //draggedImageView.setVisibility(View.VISIBLE);
                    return true;
                } else if(i == R.id.tab_name){
                    Log.i(TAG, "in default");
                    //draggedImageViewParentLayout.removeView(draggedImageView);
                    //LinearLayout bottomLinearLayout = (LinearLayout) receivingLayoutView;
                    //dragEvent.getX()
                    //bottomLinearLayout.addView(draggedImageView);
                    imageView.setBackground(getResources().getDrawable(R.drawable.tab_design_name));

                    layoutParams.setMargins((int) dragEvent.getX(), (int) dragEvent.getY(), 0, 0);
                    //Log.i(TAG, "x: " + String.valueOf(dragEvent.getX()) + "  Y: " + String.valueOf(dragEvent.getY()));
                    pdfImageLinearContainer.addView(imageView, layoutParams);
                    //draggedImageView.setVisibility(View.VISIBLE);
                    return true;
                }

            case DragEvent.ACTION_DRAG_ENDED:

                Log.i(TAG, "drag action ended");
                Log.i(TAG, "getResult: " + dragEvent.getResult());

//                if the drop was not successful, set the ball to visible
                if (!dragEvent.getResult()) {
                    Log.i(TAG, "setting visible");
                    draggedImageView.setVisibility(View.VISIBLE);
                }

                return true;
            // An unknown action type was received.
            default:
                Log.i(TAG, "Unknown action type received by OnDragListener.");
                break;
        }

        return false;
    }

    @Override
    public boolean onLongClick(View tabView) {
        //The tab on the top is touch and longClick
        //create clip data holding data of the type MIMETYPE_TEXT_PLAIN
        ClipData clipData = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(tabView);
        /*start the drag - contains the data to be dragged,
            metadata for this data and callback for drawing shadow*/
        tabView.startDrag(clipData, shadowBuilder, tabView, 0);
        //we're dragging the shadow so make the view invisible
        //imageView.setVisibility(View.INVISIBLE);
        return true;
    }
}

