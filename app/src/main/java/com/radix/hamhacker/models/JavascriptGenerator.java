package com.radix.hamhacker.models;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
public class JavascriptGenerator {
  private static final String TAG = JavascriptGenerator.class.toString();

  private final Context context;

  public JavascriptGenerator(Context context) {
    this.context = context;
  }

  public String get() {
    return "javascript:" + generateScriptBody(new DummyPersonalData());
  }

  private String generateScriptBody(IPersonalDataModel dataModel) {
    Log.d(TAG, "Using dataModel: " + dataModel.toString());
    String js = String.format(getJavascriptStringFromAssets(),
        dataModel.getFirstName(), dataModel.getLastName(), dataModel.getEmail(),
        dataModel.getMonthDob(), dataModel.getDayDob(), dataModel.getYearDob(),
        dataModel.getZip());
    return js;
  }

  private String getJavascriptStringFromAssets() {
    BufferedReader in = null;
    try {
      InputStream is = context.getAssets().open("hack_script.js");
      in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line);
      }

      return sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
      // Well shit
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return "console.log('js load error :(')";
  }
}
