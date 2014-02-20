// Code borrowed from
// https://github.com/ManuelPeinado/GlassActionBar

package cs408team3.wikidroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

public class Utils {

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
        String[] splittedTitle = title.split(" - ");
        String nTitle;

        if (splittedTitle.length > 0) {
            nTitle = new String(splittedTitle[0]);
        } else {
            nTitle = new String(title);
        }

        return nTitle;
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
        if (term == null) {
            Log.w(TAG, "term is null");
            return false;
        }
        if (term.equals("")) {
            Log.w(TAG, "term is empty");
            return false;
        }
        // String aux = term.replaceAll(" ", "");
        if (term.equals("")) {
            Log.w(TAG, "term is just blanket spaces");
            return false;
        }
        if (term.equals("@")) {
            Log.w(TAG, "term is just @");
            return false;
        }
        if (term.equals("&")) {
            Log.w(TAG, "term is just &");
            return false;
        }
        if (term.equals("\"\"")) {
            Log.w(TAG, "term is just \"\"");
            return false;
        }
        return true;
    }

}
