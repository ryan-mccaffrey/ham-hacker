package com.radix.hamhacker.models;

import java.util.Random;

/**
 *
 */
public class DummyPersonalData implements IPersonalDataModel {
  private static final String[] EMAILS = new String[]{"0@kermit.mit.edu", "1@kermit.mit.edu", "2@kermit.mit.edu",
      "3@kermit.mit.edu", "4@kermit.mit.edu", "5@kermit.mit.edu", "6@kermit.mit.edu",
      "7@kermit.mit.edu", "8@kermit.mit.edu", "9@kermit.mit.edu", "10@kermit.mit.edu",
      "11@kermit.mit.edu", "12@kermit.mit.edu", "13@kermit.mit.edu", "14@kermit.mit.edu",
      "15@kermit.mit.edu", "16@kermit.mit.edu", "17@kermit.mit.edu", "18@kermit.mit.edu",
      "19@kermit.mit.edu", "20@kermit.mit.edu", "21@kermit.mit.edu", "22@kermit.mit.edu",
      "23@kermit.mit.edu", "24@kermit.mit.edu", "25@kermit.mit.edu", "26@kermit.mit.edu",
      "27@kermit.mit.edu", "28@kermit.mit.edu", "29@kermit.mit.edu", "30@kermit.mit.edu",
      "31@kermit.mit.edu", "32@kermit.mit.edu", "33@kermit.mit.edu", "34@kermit.mit.edu"};

  private static final String[] LAST_NAMES = new String[]{"R.Contreras", "R,Contreras", "R. Contreras", "R, Contreras",
      "R Contreras", "Ri.Contreras", "Ri,Contreras", "Ri. Contreras", "Ri, Contreras",
      "Ri Contreras", "Ric.Contreras", "Ric,Contreras", "Ric. Contreras", "Ric, Contreras",
      "Ric Contreras", "Rich.Contreras", "Rich,Contreras", "Rich. Contreras",
      "Rich, Contreras", "Rich Contreras", "Richa.Contreras", "Richa,Contreras",
      "Richa. Contreras", "Richa, Contreras", "Richa Contreras", "Richar.Contreras",
      "Richar,Contreras", "Richar. Contreras", "Richar, Contreras", "Richar Contreras",
      "Richard.Contreras", "Richard,Contreras", "Richard. Contreras",
      "Richard, Contreras", "Richard Contreras"
  };

  static {
    {
      assert EMAILS.length == LAST_NAMES.length;
    }
  }

  private final int index;

  public DummyPersonalData() {
    int numEntries = EMAILS.length;
    index = new Random().nextInt(numEntries);
  }

  @Override
  public String getEmail() {
    return EMAILS[index];
  }

  @Override
  public String getLastName() {
    return LAST_NAMES[index];
  }

  @Override
  public String getFirstName() {
    return "Julian";
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
}
