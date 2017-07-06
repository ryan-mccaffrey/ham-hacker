package com.radix.hamhacker.models;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DummyPersonalData implements IPersonalDataModel {
  private int index;
  private final List<String> mAllNames;
  private final SortedIndexMap mSortedIndexMap;
  private static final String NAME_DELIMITER = "|";

  public DummyPersonalData(Context context) {
    mAllNames = getLastNamesFromAssets(context);
    int numEntries = mAllNames.size();

    // Get the least recently used index
    mSortedIndexMap = new SortedIndexMap(context, numEntries);
  }

  @Override
  public void incrementModel() {
    index = mSortedIndexMap.getOldestIndex();
  }

  @Override
  public String getEmail() {
    return index + "@kermit.mit.edu";
  }

  @Override
  public String getLastName() {
    final String name = mAllNames.get(index);
    int delimiterPosition = name.indexOf(NAME_DELIMITER);

    return name.substring(delimiterPosition + 1, name.length()).trim();
  }

  @Override
  public String getFirstName() {
    final String name = mAllNames.get(index);
    int delimiterPosition = name.indexOf(NAME_DELIMITER);

    return name.substring(0, delimiterPosition).trim();
  }

  @Override
  public String getMonthDob() {
    return "05";
  }

  @Override
  public String getDayDob() {
    return "02";
  }

  @Override
  public String getYearDob() {
    return "1994";
  }

  @Override
  public String getZip() {
    return "10001";
  }

  @Override
  public String toString() {
    return "DummyPersonalData{" +
        "index= " + index +
        ", getEmail= " + getEmail() +
        ", getLastName= " + getLastName() +
        '}';
  }

  /**
   * There's a LOT of last names. Load them all
   *
   * @param context
   * @return
   */
  private static List<String> getLastNamesFromAssets(Context context) {
    List<String> names = new ArrayList<>();

    BufferedReader in = null;
    try {
      InputStream is = context.getAssets().open("last_names.txt");
      in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

      String line;
      while ((line = in.readLine()) != null) {
        names.add(line);
      }

      return names;
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

    names.add("descriptive error name");
    return names;
  }
}
