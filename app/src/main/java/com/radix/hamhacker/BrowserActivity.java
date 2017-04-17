package com.radix.hamhacker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends AppCompatActivity {
  private static final String TAG = BrowserActivity.class.toString();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browser);
    boolean isOnline = getIntent().getBooleanExtra(MainActivity.IS_ONLINE_EXTRA, true);

    final WebView webView = (WebView) findViewById(R.id.webviewBrowser);
    setWebSettings(webView.getSettings());
    if (isOnline) {
      loadWebviewOnline(webView);
    } else {
//      loadWebviewOffline(webView);
    }

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        webView.loadUrl(request.getUrl().toString());
        return true;
      }
    });
  }

  private void setWebSettings(WebSettings webSettings) {
    webSettings.setJavaScriptEnabled(true);
    webSettings.setAllowContentAccess(true);
    webSettings.setAllowFileAccess(true);
    webSettings.setAllowUniversalAccessFromFileURLs(true);
    webSettings.setAllowFileAccessFromFileURLs(true);
  }

  private void loadWebviewOnline(WebView webView) {
    webView.loadUrl("https://lottery.broadwaydirect.com/show/hamilton/");
  }

  private void loadWebviewOffline(WebView webView) {
    webView.loadUrl("file:///android_asset/saved_frame.html");
  }
}
