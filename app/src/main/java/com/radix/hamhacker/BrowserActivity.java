package com.radix.hamhacker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.radix.hamhacker.models.JavascriptGenerator;

public class BrowserActivity extends AppCompatActivity {
  private static final String TAG = BrowserActivity.class.toString();

  private WebView webView;
  private boolean inSession = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browser);

    this.webView = (WebView) findViewById(R.id.webviewBrowser);

    findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        webView.goBack();
      }
    });
    findViewById(R.id.buttonForward).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        webView.goForward();
      }
    });
    findViewById(R.id.buttonReload).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openLottery();
      }
    });

    // set the client
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        webView.loadUrl(request.getUrl().toString());
        return true;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (url.contains("enter-lottery") && !inSession) {
          loadJs();
          inSession =  true;
        } else {
          Log.d(TAG, "Url isn't the lottery page, not loading js. Url: " + url);
        }
      }
    });

    // and the chrome
    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public boolean onConsoleMessage(ConsoleMessage cm) {
        Log.d(TAG, "CONSOLE LOG: " + cm.message() + " -- From line "
            + cm.lineNumber() + " of "
            + cm.sourceId());
        return true;
      }
    });

    // set the settings
    setWebSettings(webView.getSettings());

    openLottery();
  }

  /**
   * Opens the main lottery URL
   */
  private void openLottery() {
    inSession = false;
    // Clear all the data
    webView.clearCache(true);
    clearAllCookieData();

    // load the url
    webView.loadUrl("https://lottery.broadwaydirect.com/show/hamilton/");

    // Scroll a little to where the button is
    webView.scrollBy(0, 1000);
  }

  public static void clearAllCookieData() {
    CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
      @Override
      public void onReceiveValue(Boolean aBoolean) {
        CookieManager.getInstance().flush();
      }
    });
  }

  private void loadJs() {
    String generatedJs = new JavascriptGenerator(this.getApplicationContext()).get();
    Log.v(TAG, "Using generated js:\n" + generatedJs);
    webView.loadUrl(generatedJs);
  }

  private void setWebSettings(WebSettings webSettings) {
    webSettings.setJavaScriptEnabled(true);
    webSettings.setAllowContentAccess(true);
    webSettings.setAllowFileAccess(true);
    webSettings.setAllowUniversalAccessFromFileURLs(true);
    webSettings.setAllowFileAccessFromFileURLs(true);
  }
}
