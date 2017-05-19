package com.radix.hamhacker.models;

/**
 *
 */
public interface IPersonalDataModel {
  String getFirstName();
  String getLastName();

  String getMonthDob();
  String getDayDob();
  String getYearDob();

  String getEmail();
  String getZip();

  void incrementModel();
}
