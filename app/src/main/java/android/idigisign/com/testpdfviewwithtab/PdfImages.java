package android.idigisign.com.testpdfviewwithtab;

import android.idigisign.com.pdfimagetabview.model.Tab;

import java.util.ArrayList;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 06:03:01 PM
 * Version: V 1.0
 */

public class PdfImages {
    Integer pageNum;
    ArrayList<String> urls;

    public ArrayList<Tab> getTabs() {
        return tabs;
    }

    public void setTabs(ArrayList<Tab> tabs) {
        this.tabs = tabs;
    }

    ArrayList<Tab> tabs;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }
}
