package cs408team3.wikidroid.languages;

import java.util.ArrayList;

import android.os.AsyncTask;

public class UrlList extends AsyncTask<Void, Void, String[]> {

    private Languages languages;

    public UrlList(Languages languages) {
        this.languages = languages;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        ArrayList<String> urlList = languages.getLanguageURLs();

        return urlList.toArray(new String[urlList.size()]);
    }


    @Override
    protected void onPostExecute(String[] result) {
        // languageURLs = result;
    }

}
