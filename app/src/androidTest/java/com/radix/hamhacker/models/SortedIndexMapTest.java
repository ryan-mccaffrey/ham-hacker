package com.radix.hamhacker.models;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SortedIndexMapTest {

  @Before
  public void setUp() throws Exception {
    SortedIndexMap sortedIndexMap = new SortedIndexMap(InstrumentationRegistry.getContext(), 0);
    sortedIndexMap.initializeMapping();
  }

  @Test
  public void testSortEntriesByValue() {
    Map<String, Integer> map = new HashMap<>();
    map.put("young", 1);
    map.put("really_old", 3);
    map.put("old", 2);

    List<Map.Entry<String, Integer>> sortedEntries = SortedIndexMap.getEntriesSortedByValues(map);
    assertEquals("young", sortedEntries.get(0).getKey());
    assertEquals("old", sortedEntries.get(1).getKey());
    assertEquals("really_old", sortedEntries.get(2).getKey());
  }

  @Test
  public void testGetYoungestEntry() {
    int numIndices = 3;

    SortedIndexMap sortedIndexMap = new SortedIndexMap(InstrumentationRegistry.getContext(), numIndices);
    int youngest = sortedIndexMap.getYoungestIndex();

    // that youngest index should be the only non-zero value in the map
    Map<Integer, Long> indexMapping = sortedIndexMap.indexMapping;
    for (int key : indexMapping.keySet()) {
      long value = indexMapping.get(key);
      if (key == youngest) {
        assertTrue(value > 0);
      } else {
        assertEquals(key, value);
      }
    }
  }

  @Test
  public void testMapPersists() {
    int numIndices = 3;

    SortedIndexMap sortedIndexMap = new SortedIndexMap(InstrumentationRegistry.getContext(), numIndices);
    int youngest = sortedIndexMap.getYoungestIndex();

    // recreate the map
    SortedIndexMap sortedIndexMapFromStorage = new SortedIndexMap(InstrumentationRegistry.getContext(), numIndices);

    // that youngest index should be the only non-zero value in the map
    Map<Integer, Long> indexMapping = sortedIndexMapFromStorage.indexMapping;
    for (int key : indexMapping.keySet()) {
      long value = indexMapping.get(key);
      if (key == youngest) {
        assertTrue(value > 0);
      } else {
        assertEquals(key, value);
      }
    }
  }

  @Test
  public void testSequentialReads() {
    int numIndices = 16;
    SortedIndexMap sortedIndexMap = new SortedIndexMap(InstrumentationRegistry.getContext(), numIndices);

    for (int i = 0; i < numIndices; i++) {
      assertEquals("0 is first, then 1, then 2, etc.", i, sortedIndexMap.getYoungestIndex());
    }
  }
}
