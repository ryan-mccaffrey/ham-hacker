package com.radix.hamhacker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {
  private Context applicationContext;
  public static final String IS_ONLINE_EXTRA = "is_online?";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    applicationContext = this.getApplicationContext();

    findViewById(R.id.fabOnline).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startBrowserActivity(true);
      }
    });
    findViewById(R.id.fabOffline).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startBrowserActivity(false);
      }
    });
  }

  /**
   * Starts the browser with the actual hamilton URL or nah
   */
  private void startBrowserActivity(boolean isOnline) {
    Intent intent = new Intent(applicationContext, BrowserActivity.class);
    intent.putExtra(IS_ONLINE_EXTRA, isOnline);
    startActivity(intent);
  }
}
