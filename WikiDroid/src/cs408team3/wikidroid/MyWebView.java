package cs408team3.wikidroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebView extends WebView {

	Context context;
	
	public MyWebView(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
	        this.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}

}

class MyWebViewClient extends WebViewClient {

	private Context context;

	public MyWebViewClient(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		if (Uri.parse(url).getHost().equals("www.??.wikipedia.com/*")) {
			// This is my web site, so do not override; let my WebView load
			// the page
			return false;
		}
		// Otherwise, the link is not for a page on my site, so launch
		// another Activity that handles URLs
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(intent);
		return true;
	}

}