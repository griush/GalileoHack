// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.location.custom_suplclient.supl;

import com.google.location.custom_suplclient.ephemeris.GnssEphemeris;
import com.google.location.custom_suplclient.iono.GnssIonoModel;
import com.google.location.custom_suplclient.utc.GnssUtcModel;

import java.util.List;

/** A container for GNSS satellite ephemeris and Ionospheric model parameters. */
public class SuplResponse {

  /* A list of ephemeris for GNSS satellites */
  public final List<GnssEphemeris> ephList;

  /* The parameters of the ionospheric model */
  public final List<GnssIonoModel> ionoModelList;

  /* The parameters of the utc clock model */
  public final List<GnssUtcModel> utcModelList;

  /* Constructor */
  public SuplResponse(List<GnssEphemeris> ephList, List<GnssIonoModel> ionoModelList, List<GnssUtcModel> utcModelList) {
    this.ephList = ephList;
    this.ionoModelList = ionoModelList;
    this.utcModelList = utcModelList;
  }
}
