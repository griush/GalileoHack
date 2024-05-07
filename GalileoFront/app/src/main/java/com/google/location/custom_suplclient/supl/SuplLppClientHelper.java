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

import com.google.location.custom_suplclient.asn1.supl2.lpp.*;
import com.google.location.custom_suplclient.asn1.supl2.lpp_ver12.A_GNSS_ProvideAssistanceData;
import com.google.location.custom_suplclient.asn1.supl2.lpp_ver12.GNSS_NavModelSatelliteElement;
import com.google.location.custom_suplclient.asn1.supl2.lpp_ver12.GNSS_NavigationModel;
import com.google.location.custom_suplclient.asn1.supl2.lpp_ver12.LPP_Message;
import com.google.location.custom_suplclient.asn1.supl2.supl_pos.SUPLPOS;
import com.google.location.custom_suplclient.asn1.supl2.ulp_version_2_parameter_extensions.Ver2_PosPayLoad_extension;
import com.google.location.custom_suplclient.asn1.supl2.ulp_version_2_parameter_extensions.Ver2_PosPayLoad_extension.lPPPayloadType.lPPPayloadTypeComponentType;
import com.google.location.custom_suplclient.ephemeris.GalEphemeris;
import com.google.location.custom_suplclient.ephemeris.GloEphemeris;
import com.google.location.custom_suplclient.ephemeris.GnssEphemeris;
import com.google.location.custom_suplclient.ephemeris.GpsEphemeris;
import com.google.location.custom_suplclient.ephemeris.KeplerianModel;
import com.google.location.custom_suplclient.iono.GnssIonoModel;
import com.google.location.custom_suplclient.iono.KlobucharIono;
import com.google.location.custom_suplclient.iono.NeQuickIono;
import com.google.location.custom_suplclient.utc.GnssUtcModel;
import com.google.location.custom_suplclient.utc.UtcModelSet1;
import com.google.location.custom_suplclient.utc.UtcModelSet2;
import com.google.location.custom_suplclient.utc.UtcModelSet3;
import com.google.location.custom_suplclient.utc.UtcModelSet4;
import com.google.location.custom_suplclient.utc.UtcModelSet5;
import com.google.location.custom_suplclient.supl.SuplConstants.GnssConstants;
import com.google.location.custom_suplclient.supl.SuplConstants.LppConstants;
import com.google.location.custom_suplclient.supl.SuplConstants.ScaleFactors;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * A helper that contains methods to convert SUPL LPP messages to {@link GnssIonoModel} and
 * instances of {@link GnssEphemeris}.
 */
class SuplLppClientHelper {

  /**
   * Builds an instance of {@link GnssUtcModel} containing UTC model parameters.
   */
  static GnssUtcModel buildGnssUtcModel(GNSS_UTC_Model utc) {

    if(utc.isUtcModel1() == true){
      return buildUtcModelSet1(utc.getUtcModel1());
    }
    else if(utc.isUtcModel2() == true){
      return buildUtcModelSet2(utc.getUtcModel2());
    }
    else if(utc.isUtcModel3() == true){
      return buildUtcModelSet3(utc.getUtcModel3());
    }
    else if(utc.isUtcModel4() == true){
      return buildUtcModelSet4(utc.getUtcModel4());
    }
    else if(utc.isExtensionUtcModel5_r12() == true){
      return buildUtcModelSet5(utc.getExtensionUtcModel5_r12());
    }
    else{
      return null;
    }
  }

  /**
   * Builds an instance of {@link GnssUtcModel} containing UTC model set 1 parameters
   * extracted from {@link UtcModelSet1}.
   */
  static GnssUtcModel buildUtcModelSet1(UTC_ModelSet1 utc) {
    UtcModelSet1.Builder utcSetBuilder = UtcModelSet1.newBuilder();

    utcSetBuilder.setUtcA1(utc.getGnss_Utc_A1().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_A1 );
    utcSetBuilder.setUtcA0(utc.getGnss_Utc_A0().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_A0 );
    utcSetBuilder.setUtcWn(utc.getGnss_Utc_WNt().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_WNT );
    utcSetBuilder.setUtcWnLsf(utc.getGnss_Utc_WNlsf().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_WNLSF);
    utcSetBuilder.setUtcTot(utc.getGnss_Utc_Tot().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_TOT );
    utcSetBuilder.setUtcDeltaTls(utc.getGnss_Utc_DeltaTls().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_DELTA_TLS);
    utcSetBuilder.setUtcDeltaTlsf(utc.getGnss_Utc_DeltaTlsf().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_DELTA_TLSF);
    utcSetBuilder.setUtcDn(utc.getGnss_Utc_DN().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_DN);

    return utcSetBuilder.build();
  }

  /**
   * Builds an instance of {@link GnssUtcModel} containing UTC model set 2 parameters
   * extracted from {@link UtcModelSet2}.
   */
  static GnssUtcModel buildUtcModelSet2(UTC_ModelSet2 utc) {
    UtcModelSet2.Builder utcSetBuilder = UtcModelSet2.newBuilder();

    utcSetBuilder.setUtcA0(utc.getUtcA0().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_A0 );
    utcSetBuilder.setUtcA1(utc.getUtcA1().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_A1 );
    utcSetBuilder.setUtcA2(utc.getUtcA2().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_A2 );
    utcSetBuilder.setUtcWnot(utc.getUtcWNot().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_WNOT );
    utcSetBuilder.setUtcDeltaTls(utc.getUtcDeltaTls().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_DELTA_TLS);
    utcSetBuilder.setUtcWnLsf(utc.getUtcWNlsf().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_WNLSF);
    utcSetBuilder.setUtcTot(utc.getUtcTot().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_TOT );
    utcSetBuilder.setUtcDeltaTls(utc.getUtcDeltaTls().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_DELTA_TLS);
    utcSetBuilder.setUtcDeltaTlsf(utc.getUtcDeltaTlsf().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET2_DELTA_TLSF);
    utcSetBuilder.setUtcDn(Integer.parseInt(utc.getUtcDN().toString()) * ScaleFactors.UTC_MODEL_SET2_DN);

    return utcSetBuilder.build();
  }

  /**
   * Builds an instance of {@link GnssUtcModel} containing UTC model set 3 parameters
   * extracted from {@link UtcModelSet3}.
   */
  static GnssUtcModel buildUtcModelSet3(UTC_ModelSet3 utc) {
    UtcModelSet3.Builder utcSetBuilder = UtcModelSet3.newBuilder();

    utcSetBuilder.setNA(utc.getNA().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET3_NA );
    utcSetBuilder.setTauC(utc.getTauC().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET3_TAUC );
    utcSetBuilder.setB1((utc.getB1() == null) ? 0.0 : utc.getB1().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET3_B1 );
    utcSetBuilder.setB2((utc.getB2() == null) ? 0.0 : utc.getB2().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET3_B2 );
    utcSetBuilder.setKp((utc.getKp() == null) ? 0.0 : Integer.parseInt(utc.getKp().toString()) * ScaleFactors.UTC_MODEL_SET3_KP);

    return utcSetBuilder.build();
  }

  /**
   * Builds an instance of {@link GnssUtcModel} containing UTC model set 4 parameters
   * extracted from {@link UtcModelSet4}.
   */
  static GnssUtcModel buildUtcModelSet4(UTC_ModelSet4 utc) {
    UtcModelSet4.Builder utcSetBuilder = UtcModelSet4.newBuilder();

    utcSetBuilder.setUtcA1wnt(utc.getUtcA0wnt().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_A0_WNT );
    utcSetBuilder.setUtcA0wnt(utc.getUtcA1wnt().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_A1_WNT );
    utcSetBuilder.setUtcWnt(utc.getUtcWNt().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_WNT );
    utcSetBuilder.setUtcDeltaTls(utc.getUtcDeltaTls().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_DELTA_TLS);
    utcSetBuilder.setUtcWnLsf(utc.getUtcWNlsf().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_WNLSF);
    utcSetBuilder.setUtcTot(utc.getUtcTot().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_TOT );
    utcSetBuilder.setUtcDeltaTls(utc.getUtcDeltaTls().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_DELTA_TLS);
    utcSetBuilder.setUtcDeltaTlsf(utc.getUtcDeltaTlsf().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_DELTA_TLSF);
    utcSetBuilder.setUtcDn(utc.getUtcDN().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET4_DN);

    return utcSetBuilder.build();
  }

  /**
   * Builds an instance of {@link GnssUtcModel} containing UTC model set 5 parameters
   * extracted from {@link UtcModelSet5}.
   */
  static GnssUtcModel buildUtcModelSet5(UTC_ModelSet5_r12 utc) {
    UtcModelSet5.Builder utcSetBuilder = UtcModelSet5.newBuilder();

    utcSetBuilder.setUtcA1(utc.getUtcA1_r12().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET5_A1 );
    utcSetBuilder.setUtcA0(utc.getUtcA0_r12().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET1_A0 );
    utcSetBuilder.setUtcWnLsf(utc.getUtcWNlsf_r12().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET5_WNLSF);
    utcSetBuilder.setUtcDeltaTls(utc.getUtcDeltaTls_r12().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET5_DELTA_TLS);
    utcSetBuilder.setUtcDeltaTlsf(utc.getUtcDeltaTlsf_r12().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET5_DELTA_TLSF);
    utcSetBuilder.setUtcDn(utc.getUtcDN_r12().getInteger().byteValue() * ScaleFactors.UTC_MODEL_SET5_DN);

    return utcSetBuilder.build();
  }

  /**
   * Builds an instance of {@link GnssIonoModel} containing Klobuchar model parameters
   * extracted from {@link KlobucharModelParameter}.
   */
  static GnssIonoModel buildGnssIonoModel(KlobucharModelParameter iono) {
    KlobucharIono.Builder ionoBuilder = KlobucharIono.newBuilder();
    byte[] dataId = iono.getDataID().getValue().toByteArray();
    ionoBuilder.setDataId(dataId);

    double[] alpha = new double[4];
    alpha[0] = iono.getAlfa0().getInteger().byteValue() * ScaleFactors.IONO_ALFA_0;
    alpha[1] = iono.getAlfa1().getInteger().byteValue() * ScaleFactors.IONO_ALFA_1;
    alpha[2] = iono.getAlfa2().getInteger().byteValue() * ScaleFactors.IONO_ALFA_2;
    alpha[3] = iono.getAlfa3().getInteger().byteValue() * ScaleFactors.IONO_ALFA_3;
    for (int i = 0; i < alpha.length; ++i) {
      ionoBuilder.addAlpha(alpha[i], i);
    }

    double[] beta = new double[4];
    beta[0] = iono.getBeta0().getInteger().byteValue() * ScaleFactors.IONO_BETA_0;
    beta[1] = iono.getBeta1().getInteger().byteValue() * ScaleFactors.IONO_BETA_1;
    beta[2] = iono.getBeta2().getInteger().byteValue() * ScaleFactors.IONO_BETA_2;
    beta[3] = iono.getBeta3().getInteger().byteValue() * ScaleFactors.IONO_BETA_3;
    for (int i = 0; i < beta.length; ++i) {
      ionoBuilder.addBeta(beta[i], i);
    }
    return ionoBuilder.build();
  }

  /**
   * Builds an instance of {@link GnssIonoModel} containing NeQuick model parameters
   * extracted from {@link NeQuickModelParameter}.
   */
  static GnssIonoModel buildGnssIonoModel(NeQuickModelParameterV12 iono) {
    NeQuickIono.Builder ionoBuilder = NeQuickIono.newBuilder();
    double[] ai = new double[3];
    ai[0] = iono.getAi0().getInteger().byteValue() * ScaleFactors.GAL_IONO_AIO;
    ai[1] = iono.getAi1().getInteger().byteValue() * ScaleFactors.GAL_IONO_AI1;
    ai[2] = iono.getAi2().getInteger().byteValue() * ScaleFactors.GAL_IONO_AI2;
    for (int i = 0; i < ai.length; ++i) {
      ionoBuilder.addAi(ai[i], i);
    }

    double[] ionoStormFlag = new double[5];
    ionoStormFlag[0] = (iono.getIonoStormFlag1() == null) ? -1.0 :iono.getIonoStormFlag1().getInteger().byteValue();
    ionoStormFlag[1] = (iono.getIonoStormFlag2() == null) ? -1.0 :iono.getIonoStormFlag2().getInteger().byteValue();
    ionoStormFlag[2] = (iono.getIonoStormFlag3() == null) ? -1.0 :iono.getIonoStormFlag3().getInteger().byteValue();
    ionoStormFlag[3] = (iono.getIonoStormFlag4() == null) ? -1.0 :iono.getIonoStormFlag4().getInteger().byteValue();
    ionoStormFlag[4] = (iono.getIonoStormFlag5() == null) ? -1.0 :iono.getIonoStormFlag5().getInteger().byteValue();
    for (int i = 0; i < ionoStormFlag.length; ++i) {
      ionoBuilder.addIonoStormFlag(ionoStormFlag[i], i);
    }
    return ionoBuilder.build();
  }

  /**
   * Obtains the reference of {@link A_GNSS_ProvideAssistanceData} from {@link SUPLPOS}.
   */
  static A_GNSS_ProvideAssistanceData getAssistanceDataFromSuplPos(SUPLPOS message) {
    Ver2_PosPayLoad_extension.lPPPayloadType lppPayload =
        message.getPosPayLoad().getExtensionVer2_PosPayLoad_extension().getLPPPayload();
    Iterator<lPPPayloadTypeComponentType> lppMessages = lppPayload.getValues().iterator();
    LPP_Message assistanceDataMessage = LPP_Message.fromPerUnaligned(lppMessages.next().getValue());

    return assistanceDataMessage
        .getLpp_MessageBody()
        .getC1()
        .getProvideAssistanceData()
        .getCriticalExtensions()
        .getC1()
        .getProvideAssistanceData_r9()
        .getA_gnss_ProvideAssistanceData();
  }

  /**
   * Builds a list of {@link GnssEphemeris} instances for GLONASS satellites with information from
   * {@link GNSS_NavigationModel}.
   */
  static List<GnssEphemeris> generateGloEphList(
      GNSS_NavigationModel navModel,
      DateTime moscowDate,
      Map<Integer, Integer> svidToFreqNumMap) {
    List<GnssEphemeris> ephList = new ArrayList<>();
    for (GNSS_NavModelSatelliteElement element : navModel.getGnss_SatelliteList().getValues()) {
      GloEphemeris gloEph = buildGloEphemeris(element, moscowDate, svidToFreqNumMap);
      if (gloEph.health == 0) {
        ephList.add(gloEph);
      }
    }
    return ephList;
  }

  /**
   * Builds a list of {@link GnssEphemeris} instances for GPS satellites with information from
   * {@link GNSS_NavigationModel}.
   */
  static List<GnssEphemeris> generateGpsEphList(GNSS_NavigationModel navModel, int gpsWeek) {
    List<GnssEphemeris> ephList = new ArrayList<>();
    for (GNSS_NavModelSatelliteElement element : navModel.getGnss_SatelliteList().getValues()) {
      int svid = element.getSvID().getSatellite_id().getInteger().intValue() + 1;
      GpsEphemeris gpsEph = buildGpsEphemeris(svid, element, gpsWeek);
      if (gpsEph.health == 0) {
        ephList.add(gpsEph);
      }
    }
    return ephList;
  }

  /**
   * Builds a list of {@link GnssEphemeris} instances for Galileo satellites with information from
   * {@link GNSS_NavigationModel}.
   */
  static List<GnssEphemeris> generateGalEphList(GNSS_NavigationModel navModel, int galWeek) {
    List<GnssEphemeris> ephList = new ArrayList<>();
    for (GNSS_NavModelSatelliteElement element : navModel.getGnss_SatelliteList().getValues()) {
      KeplerianModel keplerModel = getGalKeplerianModel(element);
      int svid = element.getSvID().getSatellite_id().getInteger().intValue() + 1;
      for (StandardClockModelElementV12 clock :
          element.getGnss_ClockModel().getStandardClockModelList().getValues()) {
        int svHealth = convertBitSetToInt(element.getSvHealth().getValue());
        if (svHealth == 0) {
          ephList.add(
              GalEphemeris.newBuilder()
                  .setSvid(svid)
                  .setTocS(clock.getStanClockToc().getLong() * ScaleFactors.GAL_CLK_TOC)
                  .setAf0S(clock.getStanClockAF0().getLong() * ScaleFactors.GAL_CLK_AF0)
                  .setAf1SecPerSec(clock.getStanClockAF1().getLong() * ScaleFactors.GAL_CLK_AF1)
                  .setAf2SecPerSec2(clock.getStanClockAF2().getLong() * ScaleFactors.GAL_CLK_AF2)
                  .setTgdS(clock.getStanClockTgd().getLong() * ScaleFactors.GAL_CLK_TGD)
                  .setIsINav(clock.getStanModelID().getLong() == 0)
                  .setKeplerianModel(keplerModel)
                  .setWeek(galWeek)
                  .setIode(convertBitSetToInt(element.getIod().getValue()))
                  .setHealth(svHealth)
                  .build());
        }
      }
    }
    return ephList;
  }

  /**
   * Gets the {@link DateTime} from a {@link GNSS_SystemTime} instance based on the GNSS ID
   * associated with the {@link GNSS_SystemTime}.
   */
  static DateTime getReferenceGnssTime(GNSS_SystemTime systemTime) {
    DateTime result = null;
    switch (systemTime.getGnss_TimeID().getGnss_id().enumValue()) {
      case gps:
        result = new DateTime(
            systemTime.getGnss_DayNumber().getInteger().intValue() * TimeConstants.MILLIS_PER_DAY
                + systemTime.getGnss_TimeOfDay().getInteger().intValue()
                * TimeConstants.MILLIS_PER_SECOND, DateTimeZone.UTC);
        break;
      default:
        throw new UnsupportedOperationException(
            "Unsupported LPP GNSS constellation: "
                + systemTime.getGnss_TimeID().getGnss_id().enumValue());
    }
    return result;
  }

  /**
   * Creates a map with svid of GLONASS satellites as keys and carrier frequency numbers ([-7, 13])
   * as values from information in {@link GNSS_ID_GLONASS}.
   */
  static Map<Integer, Integer> getGloSvidToFreqNumMap(GNSS_ID_GLONASS gloId) {
    Map<Integer, Integer> svidToFreqNumMap = new HashMap<>();
    for (GNSS_ID_GLONASS_SatElement element : gloId.getValues()) {
      int svid = element.getSvID().getSatellite_id().getInteger().intValue() + 1;
      int freqNum = element.getChannelNumber().getInteger().intValue();
      svidToFreqNumMap.put(svid, freqNum);
    }
    return svidToFreqNumMap;
  }

  /**
   * Converts the value in {@link BitSet} to an integer, where in the input {@code bits}, LSBs have
   * greater indices than MSBs.
   */
  public static int convertBitSetToInt(BitSet bits) {
    int sum = 0;
    for (int i = 0; i < bits.length(); i++) {
      sum = (sum << 1) + (bits.get(i) ? 1 : 0);
    }
    return sum;
  }

  /**
   * Builds a {@link GloEphemeris} instance for the GLONASS satellite from the {@link
   * GNSS_NavModelSatelliteElement} instance containing ephemeris parameters, the reference date
   * {@code DateTime} in Moscow time, and a map with svid as keys and frequency number as values.
   */
  private static GloEphemeris buildGloEphemeris(
      GNSS_NavModelSatelliteElement element,
      DateTime moscowDate,
      Map<Integer, Integer> svidToFreqNumMap) {
    GLONASS_ClockModel clock = element.getGnss_ClockModel().getGlonass_ClockModel();
    NavModel_GLONASS_ECEF orbit = element.getGnss_OrbitModel().getGlonass_ECEF();

    // Gets the time of ephemeris, and converts it to UTC time zone.
    // Time of day (minutes) in UTC(SU)
    int iodMin =
        convertBitSetToInt(element.getIod().getValue()) * LppConstants.LPP_GLO_IOD_SCALE_FACTOR;
    DateTime moscowTimeOfEph =
        new DateTime(
            moscowDate.getYear(),
            moscowDate.getMonthOfYear(),
            moscowDate.getDayOfMonth(),
            iodMin / 60,
            iodMin % 60,
            DateTimeZone.UTC);
    DateTime utcTimeOfEph = moscowTimeOfEph.minusHours(TimeConstants.MOSCOW_UTC_TIME_OFFSET_HOURS);

    int svid = element.getSvID().getSatellite_id().getInteger().intValue() + 1;
    int carrierFreqNum = svidToFreqNumMap.get(svid);

    return GloEphemeris.newBuilder()
        .setFreqNum(carrierFreqNum)
        .setSvid(svid)
        .setUtcTime(utcTimeOfEph)
        .setHealth(element.getSvHealth().getValue().get(1) ? 1 : 0)
        .setBiasS(clock.getGloTau().getInteger().intValue() * ScaleFactors.GLO_CLK_TAU)
        .setRelFreqBias(clock.getGloGamma().getInteger().intValue() * ScaleFactors.GLO_CLK_GAMMA)
        .setAgeDays(orbit.getGloEn().getInteger().intValue())
        .setXSatPosM(
            orbit.getGloX().getInteger().intValue()
                * ScaleFactors.GLO_ORB_POS_KM
                * GnssConstants.METERS_PER_KM)
        .setYSatPosM(
            orbit.getGloY().getInteger().intValue()
                * ScaleFactors.GLO_ORB_POS_KM
                * GnssConstants.METERS_PER_KM)
        .setZSatPosM(
            orbit.getGloZ().getInteger().intValue()
                * ScaleFactors.GLO_ORB_POS_KM
                * GnssConstants.METERS_PER_KM)
        .setXSatVelMps(
            orbit.getGloXdot().getInteger().intValue()
                * ScaleFactors.GLO_ORB_VEL_KMPS
                * GnssConstants.METERS_PER_KM)
        .setYSatVelMps(
            orbit.getGloYdot().getInteger().intValue()
                * ScaleFactors.GLO_ORB_VEL_KMPS
                * GnssConstants.METERS_PER_KM)
        .setZSatVelMps(
            orbit.getGloZdot().getInteger().intValue()
                * ScaleFactors.GLO_ORB_VEL_KMPS
                * GnssConstants.METERS_PER_KM)
        .setXMoonSunAccMps2(
            orbit.getGloXdotdot().getInteger().intValue()
                * ScaleFactors.GLO_ORB_ACCELERATION_KMPS2
                * GnssConstants.METERS_PER_KM)
        .setYMoonSunAccMps2(
            orbit.getGloYdotdot().getInteger().intValue()
                * ScaleFactors.GLO_ORB_ACCELERATION_KMPS2
                * GnssConstants.METERS_PER_KM)
        .setZMoonSunAccMps2(
            orbit.getGloZdotdot().getInteger().intValue()
                * ScaleFactors.GLO_ORB_ACCELERATION_KMPS2
                * GnssConstants.METERS_PER_KM)
        .build();
  }

  /**
   * Builds a {@link GpsEphemeris} instance for the GPS satellite with id {@code svid} containing
   * the ephemeris parameters from {@link GNSS_NavModelSatelliteElement}.
   */
  private static GpsEphemeris buildGpsEphemeris(
      int svid, GNSS_NavModelSatelliteElement element, int gpsWeek) {
    NavModelNAV_KeplerianSet orbit = element.getGnss_OrbitModel().getNav_KeplerianSet();
    KeplerianModel keplerModel = getGpsKeplerianModel(element);
    NAV_ClockModel clock = element.getGnss_ClockModel().getNav_ClockModel();
    int iod = convertBitSetToInt(element.getIod().getValue());
    return GpsEphemeris.newBuilder()
        .setSvid(svid)
        .setWeek(gpsWeek)
        .setHealth(convertBitSetToInt(element.getSvHealth().getValue()))
        .setTgdS(clock.getNavTgd().getInteger().byteValue() * ScaleFactors.GPS_NAV_TGD)
        .setAf0S(clock.getNavaf0().getInteger().intValue() * ScaleFactors.GPS_NAV_AF0)
        .setAf1SecPerSec(clock.getNavaf1().getInteger().shortValue() * ScaleFactors.GPS_NAV_AF1)
        .setAf2SecPerSec2(clock.getNavaf2().getInteger().byteValue() * ScaleFactors.GPS_NAV_AF2)
        .setTocS(clock.getNavToc().getInteger().intValue() * ScaleFactors.GPS_NAV_TOC)
        .setIodc(iod)
        .setIode(iod)
        .setL2PDataFlag(orbit.getAddNAVparam().getEphemL2Pflag().getInteger().intValue() != 0)
        .setCodeL2(orbit.getAddNAVparam().getEphemCodeOnL2().getInteger().intValue())
        .setKeplerianModel(keplerModel)
        .build();
  }

  private static KeplerianModel getGpsKeplerianModel(GNSS_NavModelSatelliteElement element) {
    NavModelNAV_KeplerianSet orbit = element.getGnss_OrbitModel().getNav_KeplerianSet();
    return KeplerianModel.newBuilder()
        .setToeS(orbit.getNavToe().getInteger().intValue() * ScaleFactors.GPS_NAV_TOE)
        .setCic(orbit.getNavCic().getInteger().shortValue() * ScaleFactors.GPS_NAV_CIC)
        .setCis(orbit.getNavCis().getInteger().shortValue() * ScaleFactors.GPS_NAV_CIS)
        .setCrc(orbit.getNavCrc().getInteger().shortValue() * ScaleFactors.GPS_NAV_CRC)
        .setCrs(orbit.getNavCrs().getInteger().shortValue() * ScaleFactors.GPS_NAV_CRS)
        .setCuc(orbit.getNavCuc().getInteger().shortValue() * ScaleFactors.GPS_NAV_CUC)
        .setCus(orbit.getNavCus().getInteger().shortValue() * ScaleFactors.GPS_NAV_CUS)
        .setDeltaN(orbit.getNavDeltaN().getInteger().shortValue() * ScaleFactors.GPS_NAV_DELTA_N)
        .setM0(orbit.getNavM0().getInteger().intValue() * ScaleFactors.GPS_NAV_M0)
        .setOmega(orbit.getNavOmega().getInteger().intValue() * ScaleFactors.GPS_NAV_W)
        .setOmega0(orbit.getNavOmegaA0().getInteger().intValue() * ScaleFactors.GPS_NAV_OMEGA0)
        .setOmegaDot(
            orbit.getNavOmegaADot().getInteger().intValue() * ScaleFactors.GPS_NAV_OMEGA_A_DOT)
        .setI0(orbit.getNavI0().getInteger().intValue() * ScaleFactors.GPS_NAV_I0)
        .setIDot(orbit.getNavIDot().getInteger().intValue() * ScaleFactors.GPS_NAV_I_DOT)
        .setEccentricity(orbit.getNavE().getInteger().longValue() * ScaleFactors.GPS_NAV_E)
        .setSqrtA(
            orbit.getNavAPowerHalf().getInteger().longValue() * ScaleFactors.GPS_NAV_A_POWER_HALF)
        .build();
  }

  private static KeplerianModel getGalKeplerianModel(GNSS_NavModelSatelliteElement element) {
    NavModelKeplerianSet orbit = element.getGnss_OrbitModel().getKeplerianSet();
    return KeplerianModel.newBuilder()
        .setToeS(orbit.getKeplerToe().getInteger().intValue() * ScaleFactors.GAL_NAV_TOE)
        .setCic(orbit.getKeplerCic().getInteger().shortValue() * ScaleFactors.GAL_NAV_CIC)
        .setCis(orbit.getKeplerCis().getInteger().shortValue() * ScaleFactors.GAL_NAV_CIS)
        .setCrc(orbit.getKeplerCrc().getInteger().shortValue() * ScaleFactors.GAL_NAV_CRC)
        .setCrs(orbit.getKeplerCrs().getInteger().shortValue() * ScaleFactors.GAL_NAV_CRS)
        .setCuc(orbit.getKeplerCuc().getInteger().shortValue() * ScaleFactors.GAL_NAV_CUC)
        .setCus(orbit.getKeplerCus().getInteger().shortValue() * ScaleFactors.GAL_NAV_CUS)
        .setDeltaN(orbit.getKeplerDeltaN().getInteger().shortValue() * ScaleFactors.GAL_NAV_DELTA_N)
        .setM0(orbit.getKeplerM0().getInteger().intValue() * ScaleFactors.GAL_NAV_M0)
        .setOmega(orbit.getKeplerW().getInteger().intValue() * ScaleFactors.GAL_NAV_W)
        .setOmega0(orbit.getKeplerOmega0().getInteger().intValue() * ScaleFactors.GAL_NAV_OMEGA0)
        .setOmegaDot(
            orbit.getKeplerOmegaDot().getInteger().intValue() * ScaleFactors.GAL_NAV_OMEGA_DOT)
        .setI0(orbit.getKeplerI0().getInteger().intValue() * ScaleFactors.GAL_NAV_I0)
        .setIDot(orbit.getKeplerIDot().getInteger().intValue() * ScaleFactors.GAL_NAV_I_DOT)
        .setEccentricity(orbit.getKeplerE().getInteger().longValue() * ScaleFactors.GAL_NAV_E)
        .setSqrtA(
            orbit.getKeplerAPowerHalf().getInteger().longValue() * ScaleFactors.GAL_NAV_SQRT_A)
        .build();
  }
}
