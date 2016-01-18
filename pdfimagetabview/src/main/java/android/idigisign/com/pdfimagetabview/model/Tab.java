package android.idigisign.com.pdfimagetabview.model;

/**
 * Created by Android Studio.
 * User: xin
 * Date: 18/01/2016 0018
 * Time: 05:06:40 PM
 * Version: V 1.0
 */

public class Tab {

    protected String id = null;
    protected Integer xpos = null;
    protected Integer ypos = null;
    protected Integer width = null;
    protected Integer high = null;
    protected String type = null;
    protected Boolean signed = false;
    protected Integer pageNumber = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getXpos() {
        return xpos;
    }

    public void setXpos(Integer xpos) {
        this.xpos = xpos;
    }

    public Integer getYpos() {
        return ypos;
    }

    public void setYpos(Integer ypos) {
        this.ypos = ypos;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

}
