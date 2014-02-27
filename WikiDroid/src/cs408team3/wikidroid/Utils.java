// Code borrowed from
// https://github.com/ManuelPeinado/GlassActionBar

package cs408team3.wikidroid;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class Utils {

    static final String LINKS = "com.example.WikiDroid.LINKS";
    private static final String TAG   = "wikiDroid";
    // public static Bitmap drawViewToBitmap(Bitmap dest, View view, int width,
    // int height, int downSampling, Drawable drawable) {
    public static Bitmap drawViewToBitmap(Bitmap dest, View view, int width, int height, int downSampling) {
        float scale = 1f / downSampling;
        int heightCopy = view.getHeight();
        view.layout(0, 0, width, height);
        int bmpWidth = (int) (width * scale);
        int bmpHeight = (int) (height * scale);
        if (dest == null || dest.getWidth() != bmpWidth || dest.getHeight() != bmpHeight) {
            dest = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        }
        Canvas c = new Canvas(dest);
        // drawable.setBounds(new Rect(0, 0, width, height));
        // drawable.draw(c);
        if (downSampling > 1) {
            c.scale(scale, scale);
        }
        view.draw(c);
        view.layout(0, 0, width, heightCopy);
        return dest;
    }

    /**
     * Trim Wikipedia title.
     *
     * For example, "Wikipedia - Wikipedia, the free encyclopedia" will be
     * Trimmed to "Wikipedia".
     *
     * @param title
     *            Original Wikipedia title.
     * @return Trimmed title. Or the original title if trimming failed.
     */
    public static String trimWikipediaTitle(String title) {
        if (title != null) {
            String[] splittedTitle = title.split(" - ");
            String nTitle;

            if (splittedTitle.length > 0) {
                nTitle = new String(splittedTitle[0]);
            } else {
                nTitle = new String(title);
            }

            return nTitle;
        } else {
            return null;
        }
    }

    /**
     * Test if there is available network to search on internet
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Method to test if the search term String is valid or not.
     * Test if its null, blank " " or "".
     *
     * @param term
     *            Search term String.
     * @param TAG
     *            Activity tag for debugging purpose.
     * @return
     */
    public static boolean verifySearchString(String term, String TAG) {
        String aux = term.replaceAll("[^\\w]", "");

        if(aux == null){
            System.err.println("term is null");
            return false;
        }
        if(aux.equals("")){
            System.err.println("term is empty");
            return false;
        }

        if(aux.equals("")){
            System.err.println("term is just blanket spaces");
            return false;
        }
        if(term.equals("@")){
            System.err.println("term is just @");
            return false;
        }
        if(term.equals("&")){
            System.err.println("term is just &");
            return false;
        }
        if(term.equals("\"\"")){
            System.err.println("term is just \"\"");
            return false;
        }
        return true;
    }

    public static void SaveLink(Context context, String name, String url) {
        SharedPreferences shared = context.getSharedPreferences(LINKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(name, url);
        editor.commit();
    }

    public static void DeleteLink(Context context, String name) {
        SharedPreferences shared = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(name);
        editor.commit();
    }

    public static String getSaveLink(Context context, String name) {
        String url = null;

        SharedPreferences sharedPref = context.getSharedPreferences(LINKS, Context.MODE_PRIVATE);
        url = sharedPref.getString(name, null);
        return url;
    }

    // This method save the file on the sd card, inside the folder /WikiDroid
    public static void saveArchive(WebView webpage, String fileName) {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/WikiDroid/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                webpage.saveWebArchive(dir.toString() + "/" + fileName + ".mht");
            }
            else {
                webpage.saveWebArchive(dir.toString() + "/" + fileName + ".xml");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : e.toString());
        }

    }

}
