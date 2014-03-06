package cs408team3.wikidroid.tab;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cs408team3.wikidroid.R;
import cs408team3.wikidroid.Utils;

public class TabManager {

    private static final int   DEFAULT_TAB_LIMIT = 10;

    private final int          mTabLimit;

    private Context            mContext;
    private ArrayList<WebView> mTabs;
    private WebViewClient      mWebViewClient;
    private WebChromeClient    mWebChromeClient;

    private AtomicInteger      mForegroundTabIndex;

    public TabManager(Context context, int tabLimit, WebViewClient webViewClient, WebChromeClient webChromeClient) {
        mContext = context;
        mTabLimit = tabLimit;
        mTabs = new ArrayList<WebView>(tabLimit);
        mWebViewClient = webViewClient;
        mWebChromeClient = webChromeClient;

        mForegroundTabIndex = new AtomicInteger(Integer.MIN_VALUE);
    }

    public TabManager(Context context, WebViewClient webViewClient, WebChromeClient webChromeClient) {
        this(context, DEFAULT_TAB_LIMIT, webViewClient, webChromeClient);
    }

    public boolean newTab() {
        WebView webView = new WebView(mContext);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(false);
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

    /**
     * Get WebView object by index.
     *
     * @param index
     * @return
     */
    public WebView getTab(int index) {
        return mTabs.get(index);
    }

    /**
     * Get WebView object by index and set that WebView to foreground.
     *
     * @param index
     * @return
     */
    public WebView displayTab(int index) {
        setForeground(index);
        return getTab(index);
    }

    public boolean removeTab(int index) {
        if (mTabs.size() > 1) {
            return mTabs.remove(index) != null;
        } else {
            return false;
        }
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

    public ArrayList<String> getAllLinks() {
        if (mTabs.size() > 0) {
            ArrayList<String> links = new ArrayList<String>(mTabs.size());

            for (WebView view : mTabs) {
                String url = view.getUrl();
                if (url != null) {
                    links.add(url);
                }
            }

            return links;
        } else {
            return null;
        }
    }

    public void restoreAllLinks(ArrayList<String> links) {
        mTabs.clear();
        for (String url : links) {
            newTab();
            getTab(size() - 1).loadUrl(url);
        }
    }

    public synchronized boolean setForeground(int index) {
        if (index >= 0 && index < mTabs.size()) {
            mForegroundTabIndex.set(index);

            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isForeground(WebView view) {
        int viewIndex = mTabs.indexOf(view);

        return viewIndex == mForegroundTabIndex.get();
    }

}
