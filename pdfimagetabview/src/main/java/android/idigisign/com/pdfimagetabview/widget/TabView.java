package android.idigisign.com.pdfimagetabview.widget;

import android.content.Context;
import android.idigisign.com.pdfimagetabview.model.Tab;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 05:05:13 PM
 * Version: V 1.0
 */

public class TabView extends ImageView {

    //Contain the information to draw the Tab on the pdf image
    protected Tab tab;


    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public TabView(Context context) {
        super(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
