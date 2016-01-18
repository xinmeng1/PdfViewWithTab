package android.idigisign.com.pdfimagetabview;

import android.idigisign.com.pdfimagetabview.loader.MediaLoader;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 04:52:41 PM
 * Version: V 1.0
 */

/**
 * Media Info contains the information required to load and display the media in the gallery.
 *
 * This class is used to contain the information of the image to show
 */
public class MediaInfo {

    private MediaLoader mLoader;

    /**
     * Static Function:
     *
     * input a MediaLoader, get a MediaInfo with this MediaLoader.
     *
     * @param mediaLoader MediaLoader, the loader for the image
     * @return MediaInfo
     */
    public static MediaInfo mediaLoader(MediaLoader mediaLoader) {
        return new MediaInfo().setLoader(mediaLoader);
    }

    /**
     *
     * @return MediaLoader
     */
    public MediaLoader getLoader() {
        return mLoader;
    }

    /**
     *
     * @param loader MediaLoader
     * @return MediaInfo
     */
    public MediaInfo setLoader(MediaLoader loader) {
        mLoader = loader;
        return this;
    }
}
