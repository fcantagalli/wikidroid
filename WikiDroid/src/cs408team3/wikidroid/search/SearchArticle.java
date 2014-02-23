package cs408team3.wikidroid.search;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.Toast;
import cs408team3.wikidroid.R;

public class SearchArticle extends AsyncTask<String, Integer, String> {

    private static final String TAG = "SearchArticle";

    private Context             context;
    private WikiDroidHttpClient httpClient;
    private WebView             webPage;

    public SearchArticle(Context context, WebView webPage) {
        this.context = context;
        this.httpClient = new WikiDroidHttpClient();
        this.webPage = webPage;
    }

    @Override
    protected String doInBackground(String... query) {
        String result = httpClient.searchGoogle(query[0]);

        publishProgress(50);

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            Toast.makeText(context, R.string.error_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        else if (result.equals("wrong url")) {
            Toast.makeText(context, R.string.error_internal, Toast.LENGTH_SHORT).show();
            return;
        }
        else if (result.equals("IOException")) {
            Toast.makeText(context, R.string.error_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<QueryContentHolder> resultList = httpClient.JSONToArray(result);

        Log.i(TAG, "List:  " + resultList);

        if (resultList == null) {
            Log.e(TAG, "Error when converting string to a list");
            Toast t = Toast.makeText(context, "Article not found", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.CENTER, 5, 5);
            t.show();
        }
        else {
            webPage.loadUrl(resultList.get(0).getLink());
        }

    }

}