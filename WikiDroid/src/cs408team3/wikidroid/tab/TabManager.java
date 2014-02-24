package cs408team3.wikidroid.tab;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import cs408team3.wikidroid.R;
import cs408team3.wikidroid.Utils;

public class TabManager {

    private static final int   DEFAULT_TAB_LIMIT = 10;

    private final int          mTabLimit;

    private Context            mContext;
    private ArrayList<WebView> mTabs;
    private WebViewClient      mWebViewClient;
    private WebChromeClient    mWebChromeClient;

    public TabManager(Context context, int tabLimit, WebViewClient webViewClient, WebChromeClient webChromeClient) {
        mContext = context;
        mTabLimit = tabLimit;
        mTabs = new ArrayList<WebView>(tabLimit);
        mWebViewClient = webViewClient;
        mWebChromeClient = webChromeClient;
    }

    public TabManager(Context context, WebViewClient webViewClient, WebChromeClient webChromeClient) {
        this(context, DEFAULT_TAB_LIMIT, webViewClient, webChromeClient);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public boolean newTab() {
        WebView webView = new WebView(mContext);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(mWebViewClient);
        webView.setWebChromeClient(mWebChromeClient);

        boolean inserted = insertTab(webView);

        if (!inserted) {
            webView.destroy();
        }

        return inserted;
    }

    public boolean insertTab(WebView tab) {
        if (mTabs.size() >= mTabLimit) {
            return false;
        } else {
            return mTabs.add(tab);
        }
    }

    public WebView getTab(int index) {
        return mTabs.get(index);
    }

    public boolean removeTab(int index) {
        return mTabs.remove(index) != null;
    }

    public int size() {
        return mTabs.size();
    }

    public String getTitle(WebView webView) {
        String webViewTitle = Utils.trimWikipediaTitle(webView.getTitle());

        if (webViewTitle == null || webViewTitle.length() == 0) {
            webViewTitle = mContext.getString(R.string.default_tab_title);
        }

        return webViewTitle;
    }

    public String getTitle(int index) {
        WebView webView = getTab(index);

        return getTitle(webView);
    }

    public class ListAdapter extends BaseAdapter {

        private static final String TAG = "TabManager.ListAdapter";

        private LayoutInflater      mInflater;
        private int                 mResource;
        private int                 mFieldId;

        public ListAdapter(Context context, int resource, int textViewResourceId) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mResource = resource;
            mFieldId = textViewResourceId;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public WebView getItem(int position) {
            return mTabs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            TextView text;
            CheckBox favorite;
            if (convertView == null) {
                view = mInflater.inflate(mResource, parent, false);
                favorite = (CheckBox) view.findViewById(R.id.drawer_fav_link);
                final int pos = position;
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        CheckBox fav = (CheckBox) v;
                        if (fav.isChecked()) {

                            if (getItem(pos).getTitle().equals("") == false) {
                                Log.d("favLink", "is now checked");
                                Log.d("favLink", "oioi" + getItem(pos).getTitle() + "kkk");
                                // Utils.DeleteLink(v.getContext(),
                                // getItem(pos).getTitle());
                            }
                            else {
                                fav.setChecked(false);
                            }

                        }
                        else {

                            WebView w = getItem(pos);
                            if (w.getTitle().equals("") == false && w.getUrl() != null) {
                                Log.d("favLink", "is now unchecked");
                                Log.d("favLink", "oioi " + w.getTitle());
                                Log.d("favLink", w.getUrl());
                                // Utils.SaveLink(v.getContext(), w.getTitle(),
                                // w.getUrl());
                            }

                        }
                    }
                });
            } else {
                view = convertView;
            }

            try {
                text = (TextView) view.findViewById(mFieldId);
            } catch (ClassCastException e) {
                Log.e(TAG, "You must supply a resource ID for a TextView");
                throw new IllegalStateException(TAG + " requires the resource ID to be a TextView", e);
            }

            WebView webView = getItem(position);
            String webViewTitle = getTitle(webView);
            text.setText(webViewTitle);

            return view;
        }

    }

}
