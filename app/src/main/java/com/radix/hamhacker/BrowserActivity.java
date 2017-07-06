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
  private static final String URL_MAIN_PAGE = "https://lottery.broadwaydirect.com/show/hamilton/";
  private static final String ENTER_LOTTERY_URL_MATCH = "enter-lottery/?lottery=";
  private static final String POST_LOTTERY_SUCCESS_URL_MATCH = "enter-lottery/success";
  private static final String POST_LOTTERY_CONFIRM_EMAIL_URL_MATCH = "?action=validate";

  private WebView webView;
  private boolean inSession = false;
  private JavascriptGenerator mJavascriptGenerator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browser);

    mJavascriptGenerator = new JavascriptGenerator(this.getApplicationContext());
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
        webView.clearHistory();
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
        Log.d(TAG, "url: " + url);
        if (url.contains(ENTER_LOTTERY_URL_MATCH) && !inSession) {
          loadJs();
          inSession = true;
          webView.scrollBy(0, 1000);
        } else if (url.equals(URL_MAIN_PAGE)) {
          inSession = false;
          // Scroll a little to where the button is
          webView.scrollBy(0, 1000);
        } else if ((url.contains(POST_LOTTERY_SUCCESS_URL_MATCH) || url.contains(POST_LOTTERY_CONFIRM_EMAIL_URL_MATCH)) && inSession) {
          Log.d(TAG, "On success or email confirmation page. Going back");
          openLottery();
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
    webView.loadUrl(URL_MAIN_PAGE);
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
    String generatedJs = mJavascriptGenerator.get();
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

  @Override
  public void onBackPressed() {
    if (webView.canGoBack()) {
      webView.goBack();
    } else {
      super.onBackPressed();
    }
  }
}
