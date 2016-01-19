package android.idigisign.com.pdfimagetabview;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.idigisign.com.pdfimagetabview.loader.MediaLoader;
import android.idigisign.com.pdfimagetabview.model.Tab;
import android.idigisign.com.pdfimagetabview.widget.TabView;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 05:03:51 PM
 * Version: V 1.0
 */

/**
 * Explanation
 *
 *
 * There are two parts for this components:
 * 1. The ViewPager to show the Picture
 * 2. The Scrollview to show the thumbnails
 *
 * @author Xin Meng
 *
 */
public class ScrollGalleryView extends LinearLayout implements View.OnDragListener, View.OnLongClickListener{
    public final static String TAG = "ScrollGalleryView";
    //The current image number display, which is used for tab
    protected int pageNumber;
    //All the tabs on the ScrollGalleryView
    ArrayList<Tab> tabs = new ArrayList<>();

    //TabView on the top
    private TabView tabSignature;
    private TabView tabInitial;
    private TabView tabName;

    //Pdf image container
    RelativeLayout pdfImageLinearContainer;
    ImageView pdfImageView;

    private FragmentManager fragmentManager;
    private Context context;
    private Point displayProps;

    // Options
    private int thumbnailSize; // width and height in pixels
    private boolean zoomEnabled;
    private boolean thumbnailsHiddenEnabled;
    //

    // Views
    private LinearLayout thumbnailsContainer;
    private HorizontalScrollView horizontalScrollView;
    private final ViewPager.SimpleOnPageChangeListener viewPagerChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            scroll(thumbnailsContainer.getChildAt(position));
        }
    };
    //
    private ViewPager viewPager;
    private final OnClickListener thumbnailOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            scroll(v);
            viewPager.setCurrentItem((int) v.getTag(), true);
        }
    };

    //Load the image to the ViewPager
    private PagerAdapter pagerAdapter;
    private List<MediaInfo> mListOfMedia;

    public ScrollGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mListOfMedia = new ArrayList<>();

        setOrientation(VERTICAL);
        displayProps = getDisplaySize();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.scroll_gallery_view, this, true);

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.thumbnails_scroll_view);

        thumbnailsContainer = (LinearLayout) findViewById(R.id.thumbnails_container);
        thumbnailsContainer.setPadding(displayProps.x / 2, 0, displayProps.x / 2, 0);

        //Get all the tab button object on the top
        tabSignature = (TabView) findViewById(R.id.tab_signature);
        tabInitial = (TabView) findViewById(R.id.tab_initial);
        tabName = (TabView) findViewById(R.id.tab_name);

        tabSignature.setOnLongClickListener(this);
        tabInitial.setOnLongClickListener(this);
        tabName.setOnLongClickListener(this);

        findViewById(R.id.tabs_first_line).setOnDragListener(this);
        findViewById(R.id.tabs_second_line).setOnDragListener(this);

        pdfImageLinearContainer = (RelativeLayout) findViewById(R.id.image_container);
        pdfImageView = (ImageView) findViewById(R.id.backgroundImage);

        //pdfImageLinearContainer.setOnDragListener(this);
    }

    public ScrollGalleryView setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        initializeViewPager();
        return this;
    }

    public ScrollGalleryView addMedia(MediaInfo mediaInfo) {
        if (mediaInfo == null) {
            throw new NullPointerException("Infos may not be null!");
        }

        return addMedia(Collections.singletonList(mediaInfo));
    }

    public ScrollGalleryView addMedia(List<MediaInfo> infos) {
        if (infos == null) {
            throw new NullPointerException("Infos may not be null!");
        }

        for (MediaInfo info : infos) {
            mListOfMedia.add(info);

            final ImageView thumbnail = addThumbnail(getDefaultThumbnail());
            info.getLoader().loadThumbnail(getContext(), thumbnail, new MediaLoader.SuccessCallback() {
                @Override
                public void onSuccess() {
                    thumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            });

            pagerAdapter.notifyDataSetChanged();
        }
        return this;
    }


    private Bitmap getDefaultThumbnail() {
        return ((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.placeholder_image)).getBitmap();
    }

    /**
     * Set the current item displayed in the view pager.
     *
     * @param i a zero-based index
     * @return
     */
    public ScrollGalleryView setCurrentItem(int i) {
        viewPager.setCurrentItem(i, false);
        return this;
    }

    public ScrollGalleryView setThumbnailSize(int thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
        return this;
    }

    public ScrollGalleryView setZoom(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
        return this;
    }

    public ScrollGalleryView hideThumbnails(boolean thumbnailsHiddenEnabled) {
        this.thumbnailsHiddenEnabled = thumbnailsHiddenEnabled;
        horizontalScrollView.setVisibility(GONE);
        return this;
    }

    private Point getDisplaySize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(point);
        } else {
            point.set(display.getWidth(), display.getHeight());
        }
        return point;
    }

    private ImageView addThumbnail(Bitmap image) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp.setMargins(10, 10, 10, 10);
        Bitmap thumbnail = createThumbnail(image);

        ImageView thumbnailView = createThumbnailView(lp, thumbnail);
        thumbnailsContainer.addView(thumbnailView);
        return thumbnailView;
    }

    private ImageView createThumbnailView(LinearLayout.LayoutParams lp, Bitmap thumbnail) {
        ImageView thumbnailView = new ImageView(context);
        thumbnailView.setLayoutParams(lp);
        thumbnailView.setImageBitmap(thumbnail);
        thumbnailView.setTag(mListOfMedia.size() - 1);
        thumbnailView.setOnClickListener(thumbnailOnClickListener);
        thumbnailView.setScaleType(ImageView.ScaleType.CENTER);
        return thumbnailView;
    }

    private Bitmap createThumbnail(Bitmap image) {
        return ThumbnailUtils.extractThumbnail(image, thumbnailSize, thumbnailSize);
    }

    private void initializeViewPager() {
        viewPager = (HackyViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, mListOfMedia, zoomEnabled);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerChangeListener);
    }

    private void scroll(View thumbnail) {
        int thumbnailCoords[] = new int[2];
        thumbnail.getLocationOnScreen(thumbnailCoords);

        int thumbnailCenterX = thumbnailCoords[0] + thumbnailSize / 2;
        int thumbnailDelta = displayProps.x / 2 - thumbnailCenterX;

        horizontalScrollView.smoothScrollBy(-thumbnailDelta, 0);
    }

    private int calculateInSampleSize(int imgWidth, int imgHeight, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        while (imgWidth / inSampleSize > maxWidth || imgHeight / inSampleSize > maxHeight) {
            inSampleSize *= 2;
        }
        return inSampleSize;
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
                Log.i(TAG, "drop action");
                return true;

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
