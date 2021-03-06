package com.radix.hamhacker.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Maintains a mapping of indices by last time accessed. Writes back all changes to disk immediately.
 */
public class SortedIndexMap {
  private static final String TAG = SortedIndexMap.class.toString();
  private static final String INDEX_PREFS_FILE_NAME = "indices";

  private final int numIndices;
  @VisibleForTesting
  final Map<Integer, Long> indexMapping;
  private final SharedPreferences prefsMapping;
  private final Context mContext;

  public SortedIndexMap(Context context, int numIndices) {
    this.prefsMapping = context.getSharedPreferences(INDEX_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    this.numIndices = numIndices;

    // it's either 0 (uninitialized) or for another num of indices. Rebuild
    if (prefsMapping.getAll().size() != numIndices) {
      initializeMapping();
    }

    indexMapping = getMappingFromStorage();
    mContext = context.getApplicationContext();
  }

  private int getNumEntriesYoungerThanOneDay() {
    long currentTime = getTimestampValue();
    int numEntries = 0;
    for (long timestamp : indexMapping.values()) {
      // get the diff
      long timeDifference = currentTime - timestamp;
      long numHours = TimeUnit.HOURS.convert(timeDifference, TimeUnit.MILLISECONDS);

      if (numHours > 12) {
        numEntries++;
      }
    }

    Log.d(TAG, "There are " + numEntries + " older than 12 hours");
    return numEntries;
  }

  /**
   * Gets the oldest index from storage. Also sets the age of the index to now.
   */
  public int getOldestIndex() {
    // Sort the map
    List<Map.Entry<Integer, Long>> sortedEntries = getEntriesSortedByValues(indexMapping);
    int oldestIndex = sortedEntries.get(0).getKey();

    // Reset the age of the entry
    writeAgeToEntryInMap(oldestIndex, getTimestampValue());

    final int numEntriesYoungerThanOneDay = getNumEntriesYoungerThanOneDay();
    int entriesDoneToday = numIndices - numEntriesYoungerThanOneDay;
    showToast(String.format(Locale.ENGLISH, "There are %d submissions left after this one!", numEntriesYoungerThanOneDay));
    showToast(String.format(Locale.ENGLISH, "%d submissions done today!", entriesDoneToday));
    return oldestIndex;
  }

  private void writeAgeToEntryInMap(int index, long age) {
    // update the mapping
    indexMapping.put(index, age);

    // update storage
    SharedPreferences.Editor editor = prefsMapping.edit();
    editor.putLong(Integer.toString(index), age);
    editor.apply();
  }

  private Long getTimestampValue() {
    return System.currentTimeMillis();
  }

  /**
   * Iterate over the keys and return the indices by their timestamps
   */
  private Map<Integer, Long> getMappingFromStorage() {
    Map<Integer, Long> result = new HashMap<>();

    for (String key : prefsMapping.getAll().keySet()) {
      int keyValue = Integer.valueOf(key);
      long mappingValue = prefsMapping.getLong(key, 0L);
      result.put(keyValue, mappingValue);
    }

    assert result.size() == numIndices;
    return result;
  }

  @VisibleForTesting
  void initializeMapping() {
    Log.d(TAG, "Initialized mapping to storage");
    SharedPreferences.Editor editor = prefsMapping.edit();
    editor.clear();
    for (int i = 0; i < numIndices; i++) {
      // Write the index as the value. This way, 0 is first, then 1, etc.
      editor.putLong(Integer.toString(i), i);
    }

    editor.apply();
  }

  /**
   * Sorts the entries of the map into a list in ascending order.
   */
  @VisibleForTesting
  static <K, V extends Comparable<? super V>> List<Map.Entry<K,V>> getEntriesSortedByValues(Map<K,V> map) {
    List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

    Collections.sort(sortedEntries,
        new Comparator<Map.Entry<K, V>>() {
          @Override
          public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
            return entry1.getValue().compareTo(entry2.getValue());
          }
        });
    return sortedEntries;
  }

  private void showToast(String msg) {
    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
  }
}
