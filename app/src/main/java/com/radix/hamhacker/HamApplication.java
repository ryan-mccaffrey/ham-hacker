package com.radix.hamhacker;

import android.app.Application;
import android.webkit.WebView;

public class HamApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    WebView.setWebContentsDebuggingEnabled(true);
  }
}
