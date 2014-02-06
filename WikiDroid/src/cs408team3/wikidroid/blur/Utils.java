// Code borrowed from
// https://github.com/ManuelPeinado/GlassActionBar

package cs408team3.wikidroid.blur;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class Utils {

    // public static Bitmap drawViewToBitmap(Bitmap dest, View view, int width, int height, int downSampling, Drawable drawable) {
    public static Bitmap drawViewToBitmap(Bitmap dest, View view, int width, int height, int downSampling) {
        float scale = 1f / downSampling;
        int heightCopy = view.getHeight();
        view.layout(0, 0, width, height);
        int bmpWidth = (int)(width * scale);
        int bmpHeight = (int)(height * scale);
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

}