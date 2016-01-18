package android.idigisign.com.testpdfviewwithtab;

import android.idigisign.com.pdfimagetabview.MediaInfo;
import android.idigisign.com.pdfimagetabview.ScrollGalleryView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Let thinking about the input & output of the pdf image tab view.
 *
 * According to the API, we can get the PDF images and the tabs attached to the document.
 *
 *
 * Input: PDF images URL list & Tabs List. (If not support the tabs edit, need not tabs list)
 *
 * Workload:
 *  1. show the PDF images and the Tabs on the images
 *  2. UI to control user add tabs
 *
 * Output: the new Tabs list
 *
 *
 */

public class MainActivity extends AppCompatActivity {

    private ScrollGalleryView scrollGalleryView;
    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "http://i-digi-sign.appspot.com/rest/document/ag1zfmktZGlnaS1zaWduciYLEgRVc2VyGICAgMDYzp4KDAsSCERvY3VtZW50GICAgICw2IIKDA/page/0?scaling=100&access_token=81c8d3c68ae2c1a0315cf76e024b8032_ag1zfmktZGlnaS1zaWduchMLEgZQZXJzb24YgICAgKbvjgoM",
            "http://i-digi-sign.appspot.com/rest/document/ag1zfmktZGlnaS1zaWduciYLEgRVc2VyGICAgMDYzp4KDAsSCERvY3VtZW50GICAgICw2IIKDA/page/1?scaling=100&access_token=81c8d3c68ae2c1a0315cf76e024b8032_ag1zfmktZGlnaS1zaWduchMLEgZQZXJzb24YgICAgKbvjgoM",
            "http://i-digi-sign.appspot.com/rest/document/ag1zfmktZGlnaS1zaWduciYLEgRVc2VyGICAgMDYzp4KDAsSCERvY3VtZW50GICAgICw2IIKDA/page/2?scaling=100&access_token=81c8d3c68ae2c1a0315cf76e024b8032_ag1zfmktZGlnaS1zaWduchMLEgZQZXJzb24YgICAgKbvjgoM",
            "http://i-digi-sign.appspot.com/rest/document/ag1zfmktZGlnaS1zaWduciYLEgRVc2VyGICAgMDYzp4KDAsSCERvY3VtZW50GICAgICw2IIKDA/page/3?scaling=100&access_token=81c8d3c68ae2c1a0315cf76e024b8032_ag1zfmktZGlnaS1zaWduchMLEgZQZXJzb24YgICAgKbvjgoM"
    ));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Now prepare the input and output
        PdfImages pdfImages = new PdfImages();
        pdfImages.setPageNum(4);
        pdfImages.setUrls(images);

        List<MediaInfo> infos = new ArrayList<>(pdfImages.getUrls().size());
        for (String url : pdfImages.getUrls()) {
            infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));
        }

        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
