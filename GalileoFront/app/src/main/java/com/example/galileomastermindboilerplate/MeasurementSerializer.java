package com.example.galileomastermindboilerplate;

import android.location.GnssMeasurement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MeasurementSerializer {
  public static String serialize(GnssMeasurement measurement) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(measurement);
  }
}
