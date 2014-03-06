/**
 * Author: Felipe Tozato Cantagalli
 *
 * I've Created this activity because i need to test some functionalities, like
 * to load a saved link and a saved webPage
 * put first i would like to discuss how we gonna do that before putting on the
 * tabs, you know.
 * So, don't worry about this activity now, it's just for my tests. I will not
 * call it on our main activity.
 */

package cs408team3.wikidroid.listArticles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hyperionics.war_test.WebArchiveReader;

import cs408team3.wikidroid.R;
import cs408team3.wikidroid.Utils;

public class LoadSavedStuffs extends Activity {

    private WebView       webpage;
    private WebViewClient mWebViewClient;
    private final String  TAG = "LoadStuffs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_saved_stuffs);


        webpage = (WebView) findViewById(R.id.webView1);
        webpage.getSettings().setJavaScriptEnabled(true);
        webpage.getSettings().setBuiltInZoomControls(true);
        webpage.getSettings().setDisplayZoomControls(false);

        mWebViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Page " + url + " loaded");

                getActionBar().setTitle(Utils.trimWikipediaTitle(view.getTitle()));

            }
        };
        // set webView client to update action bar.
        webpage.setWebViewClient(mWebViewClient);

        // this part is to load a saved link, so just call load on the webview
        String link = getIntent().getStringExtra("url");
        if (link != null) {
            Log.i("loadstuffs", "link not nulll: " + link);
            webpage.loadUrl(link);
        }
        // this part is to load a article when saved on the
        String filename = getIntent().getStringExtra("filename");
        if (filename != null) {
            Log.i("oii1", "filename not null" + filename);
            loadSavedWebPage(webpage, filename);
        }

        // if (webpage.getTitle() != null) {
        this.getActionBar().setTitle(Utils.trimWikipediaTitle(webpage.getTitle()));
        // }

    }

    private void loadSavedWebPage(WebView webView, String fileName) {
        File sdCard = Environment.getExternalStorageDirectory();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            File dir = new File(sdCard.getAbsolutePath() + "/WikiDroid/" + fileName);
            webView.loadUrl("file:///" + dir.toString());
        }
        else { // This part is for code below KITKAT, i didn't tested it yet.
            File dir = new File(sdCard.getAbsolutePath() + "/WikiDroid/" + fileName + ".xml");
            try {
                // read the saved file.
                FileInputStream is = new FileInputStream(dir);
                WebArchiveReader wr = new WebArchiveReader() {

                    @Override
                    public void onFinished(WebView v) {
                        // we are notified here when the page is fully loaded.

                    }
                };

                if (wr.readWebArchive(is)) {
                    wr.loadToWebView(webView);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.load_saved_stuffs, menu);
        return true;
    }

}
