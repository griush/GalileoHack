package esa.estec.galileo.galileopvt;

import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.cts.nano.Ephemeris;
import android.location.cts.nano.GalileoEphemeris;
import android.util.Log;
import android.util.Pair;

import com.google.location.lbs.gnss.gps.pseudorange.GpsMathOperations;
import com.google.location.lbs.gnss.gps.pseudorange.IonosphericModel;
import com.google.location.lbs.gnss.gps.pseudorange.SatellitePositionCalculator;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.location.GnssMeasurement.STATE_GAL_E1C_2ND_CODE_LOCK;
import static android.location.GnssMeasurement.STATE_TOW_DECODED;
import static android.location.GnssMeasurement.STATE_TOW_KNOWN;
import static android.location.GnssStatus.CONSTELLATION_GALILEO;
import static android.location.GnssStatus.CONSTELLATION_GPS;
import static com.google.location.lbs.gnss.gps.pseudorange.UserPositionVelocityWeightedLeastSquare.calculateGeometryMatrix;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.round;


public class PVTCalc {
    public boolean valid;
    public int numused;
    public double latitude, longitude, altitude, pdop, speedMPS, speedBearing, time;
    public String error="";

    public static final int METHOD_NONE            = 0;
    public static final int METHOD_GALILEO_ONLY    = 1;
    public static final int METHOD_GPS_ONLY        = 2;
    public static final int METHOD_GPS_AND_GALILEO = 3;

    public static final int IONO_NONE              = 1;
    public static final int IONO_KLOBUCHAR         = 2;
    public static final int IONO_NEQUICK           = 3;

    public static final double MAX_PDOP = 99.9;
    public static final int C_TO_N0_THRESHOLD_DB_HZ = 9;
    public static final int ELEVATION_THRESHOLD_DEG = 5;
    public static final double MAX_RANGE_RATE_STD = 0.1; // m/s

    public static final int MIN_NUM_SATS_ABOVE_THRESHOLD = 4;

    public static final int FREQ_L1 = 154;
    public static final int FREQ_L5 = 115;

    private class GalileoMeasurement{
        private int svid;
        private long freq;
        private GalileoEphemeris.GalEphemerisProto eph;
        private double pseudorange, prSigma, towRx, pseudorangeRate, prrSigma;
        private double weekNumber, CN0DbHz;

        private GalileoMeasurement(int svid, GalileoEphemeris.GalEphemerisProto currEph,
                                   double currTowRX, double pseudorange, double prSigma,
                                   double pseudorangeRate, double prrSigma, double weekNumber,
                                   double CN0DbHz, long freq) {
            this.svid = svid;
            this.freq = freq;
            this.eph = currEph;
            this.towRx = currTowRX;
            this.pseudorange = pseudorange;
            this.prSigma = prSigma;
            this.pseudorangeRate = pseudorangeRate;
            this.prrSigma = prrSigma;
            this.weekNumber = weekNumber;
            this.CN0DbHz = CN0DbHz;
        }
    }

    public class svNumber {
        public int GPS, GAL; // The coordinates of the point
        public svNumber(int gps, int gal) {
            this.GPS = gps; this.GAL = gal;
        }
        public svNumber() {
            this.GPS = 0; this.GAL = 0;
        }
        public void addGPS(){
            this.GPS = this.GPS+1;
        }
        public void addGAL(){
            this.GAL = this.GAL+1;
        }
    }

    public class outPVT {
        public int numSvUsed;
        public double PDOP;
        public outPVT () {
            this.numSvUsed = 0;
            this.PDOP = MAX_PDOP;
        }
        public outPVT (int numSv, double PDOP) {
            this.numSvUsed = numSv;
            this.PDOP = PDOP;
        }
    }

    private class Measurement{
        private int svid;
        private int constellation;
        private long freq;
        private GalileoEphemeris.GalEphemerisProto eph0;
        private Ephemeris.GpsEphemerisProto eph1;

        private double pseudorange, prSigma, towRx, pseudorangeRate, prrSigma;
        private double weekNumber, CN0DbHz;

        private Measurement(int svid, int constellation,
                            GalileoEphemeris.GalEphemerisProto eph0,
                            Ephemeris.GpsEphemerisProto eph1,
                            double currTowRX, double pseudorange, double prSigma,
                            double pseudorangeRate, double prrSigma, double weekNumber,
                            double CN0DbHz, long freq) {
            this.svid = svid;
            this.constellation = constellation;
            this.freq = freq;
            switch(constellation) {
                case 0: // Galileo
                    this.eph0 = eph0;
                    this.eph1 = eph0.getGpsEphemeris();
                    break;
                case 1: // GPS
                    this.eph0 = null;
                    this.eph1 = eph1;
                    break;
                default:
                    this.eph0 = eph0;
                    this.eph1 = eph1;
                    break;
            }
            this.towRx = currTowRX;
            this.pseudorange = pseudorange;
            this.prSigma = prSigma;
            this.pseudorangeRate = pseudorangeRate;
            this.prrSigma = prrSigma;
            this.weekNumber = weekNumber;
            this.CN0DbHz = CN0DbHz;
        }
        public GalileoMeasurement getGalileoMeasurement() {
            if (this.constellation == 0)
                return new GalileoMeasurement(this.svid, this.eph0, this.towRx,
                        this.pseudorange, this.prSigma, this.pseudorangeRate,
                        this.prrSigma, this.weekNumber, this.CN0DbHz, this.freq);
            else
                return null;
        }
    }
    public void calculatePVT(
            int pvtindex,
            boolean usegale1,
            boolean usegale5,
            boolean usegpsl1,
            boolean usegpsl5,
            int ionomodel,
            boolean tropomodel,
            boolean filtcn0,
            boolean filtelev,
            boolean useambig,
            boolean use3sats,
            boolean useinertial,
            boolean usekalman,
            GnssMeasurementsEvent event,
            Pair<Ephemeris.GpsNavMessageProto, GalileoEphemeris.GalNavMessageProto> validNavMsg,
            double latitude0,
            double longitude0,
            SatTable satTable,
            DataLogger rawLogger,
            DataLogger nmeaLogger) {

        GalileoEphemeris.GalNavMessageProto galNavMessageProto = validNavMsg.second;
        Ephemeris.GpsNavMessageProto gpsNavMessageProto = validNavMsg.first;


        svNumber usableSV = new svNumber(0,0);
        double timeUTC;
        List<Measurement> svMeasurements = new ArrayList<>();
        long mArrivalTimeSinceGpsEpochNs;
        int method;

        if(usegale1||usegale5) {
            if(usegpsl1||usegpsl5) method=METHOD_GPS_AND_GALILEO;
            else method=METHOD_GALILEO_ONLY;
        }
        else {
            if(usegpsl1||usegpsl5) method=METHOD_GPS_ONLY;
            else method=METHOD_NONE;
        }

        valid=false;

        GnssClock gnssClock = event.getClock();
        mArrivalTimeSinceGpsEpochNs = gnssClock.getTimeNanos() - gnssClock.getFullBiasNanos();

        // Compute the UTC time from GPS ToW by removing leap seconds
        int leapSec = 18; // number of leap seconds at the time of coding

        if (gpsNavMessageProto.utcModel != null)
            leapSec = gpsNavMessageProto.utcModel.leapSeconds;
        else
        if (galNavMessageProto.utcModel != null)
            leapSec = galNavMessageProto.utcModel.leapSeconds;

        double  tow = (mArrivalTimeSinceGpsEpochNs % 604800000000000L)*1e-9;
        timeUTC = tow - leapSec;
        // Check if the week number is changed
        if (timeUTC < 0.0)
            timeUTC = timeUTC + 3600*24*7;

        time = timeUTC;

        double[] positionVelocitySolutionECEF = GpsMathOperations.createAndFillArray(9, 0);

        // Check if there are enough signals above thresholds to use filters (TW)
        int numfiltcn0=0,numfiltelev=0;
        for (GnssMeasurement measurement : event.getMeasurements()) {
            long carrierFreq = getCarrierFreq(measurement);

            if(measurementIsSelected(measurement,usegale1,usegale5,usegpsl1,usegpsl5,carrierFreq)) {
                if(measurement.getCn0DbHz() >= C_TO_N0_THRESHOLD_DB_HZ) numfiltcn0++;
                if((satTable.findInList(measurement.getConstellationType(), measurement.getSvid())).elev >= ELEVATION_THRESHOLD_DEG) numfiltelev++;
            }
        }

        if(numfiltcn0 < MIN_NUM_SATS_ABOVE_THRESHOLD) filtcn0 = false;
        if(numfiltelev < MIN_NUM_SATS_ABOVE_THRESHOLD) filtelev = false;

        // Pre-process raw measurements
        for (GnssMeasurement measurement : event.getMeasurements()) {
            long carrierFreq = getCarrierFreq(measurement);

            if(measurementIsSelected(measurement,usegale1,usegale5,usegpsl1,usegpsl5,carrierFreq)) {
                try {
                    processMeas(measurement, svMeasurements, gnssClock, useambig, filtcn0, filtelev,
                            galNavMessageProto, gpsNavMessageProto, tow, usableSV, satTable, carrierFreq);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Compute PVT
        outPVT outMyPVT = new outPVT();

        Log.d ("_","__________________________________");
        Log.d ("Time", String.valueOf(timeUTC));
        Log.d ("PVT type", String.valueOf(method));

        try {
            WeightedLeastSquaresPVT(method, usableSV,
                svMeasurements, gpsNavMessageProto, galNavMessageProto,
                positionVelocitySolutionECEF,
                latitude0, longitude0, outMyPVT);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if ((outMyPVT.numSvUsed>0)&&(outMyPVT.PDOP < MAX_PDOP)) {
            double [] posEcef = new double[3];
            double [] velEcef = new double[3];
            posEcef[0] = positionVelocitySolutionECEF[0];
            posEcef[1] = positionVelocitySolutionECEF[1];
            posEcef[2] = positionVelocitySolutionECEF[2];
            velEcef[0] = positionVelocitySolutionECEF[5];
            velEcef[1] = positionVelocitySolutionECEF[6];
            velEcef[2] = positionVelocitySolutionECEF[7];
            GNSSLib tmpLib = new GNSSLib();
            GNSSLib.geoLLAandVel outLLA = tmpLib.new geoLLAandVel();
            tmpLib.pvECEF2NED(posEcef, velEcef, outLLA);

            speedMPS = Math.sqrt(
                    outLLA.velNorthMPS*outLLA.velNorthMPS +
                    outLLA.velEastMPS*outLLA.velEastMPS +
                    outLLA.velDownMPS*outLLA.velDownMPS);
            Log.d("LLA", String.valueOf(outLLA.latitudeRadians) + ", " +
                    String.valueOf(outLLA.longitudeRadians) +
                    ", "  + String.valueOf(outLLA.altitudeMeters));

            Log.d("Velocity NED", String.valueOf(outLLA.velNorthMPS) + ", " + String.valueOf(outLLA.velEastMPS) +
                    ", "  + String.valueOf(outLLA.velDownMPS));

            // TW
            speedBearing = (360.0f+Math.toDegrees(Math.atan2(outLLA.velEastMPS,outLLA.velNorthMPS)))%360.0f;

            // Check with respect to latitude and longitude of the reference
            if ((abs(Math.toDegrees(outLLA.latitudeRadians) - latitude0)>1) ||
                    (abs(Math.toDegrees(outLLA.longitudeRadians) - longitude0)>1)){
                numused = outMyPVT.numSvUsed;
                return;
            }
            switch(method) {
                case METHOD_GALILEO_ONLY :
                    for (Measurement svMeasurement : svMeasurements)
                        if (svMeasurement.constellation == 0)
                            satTable.usedInPvt[0][svMeasurement.svid] = true;
                    break;

                case METHOD_GPS_ONLY :
                    for (Measurement svMeasurement : svMeasurements)
                        if (svMeasurement.constellation == 1)
                            satTable.usedInPvt[1][svMeasurement.svid] = true;
                    break;

                case METHOD_GPS_AND_GALILEO :
                    for (Measurement svMeasurement : svMeasurements)
                        satTable.usedInPvt[svMeasurement.constellation][svMeasurement.svid] = true;
            }

            latitude = Math.toDegrees(outLLA.latitudeRadians);
            longitude = Math.toDegrees(outLLA.longitudeRadians);
            altitude = outLLA.altitudeMeters;
            numused = outMyPVT.numSvUsed;
            pdop = outMyPVT.PDOP;
            valid = true;

            if(rawLogger!=null) rawLogger.writePVTCalculationToFile(pvtindex==0?"Galileo":(pvtindex==1?"GPS":"Custom"),usableSV.GAL,usableSV.GPS,latitude,longitude,altitude,outLLA.velNorthMPS,outLLA.velEastMPS,outLLA.velDownMPS,pdop,timeUTC);
            if(nmeaLogger!=null) nmeaLogger.writeNMEAfix(latitude,longitude,altitude,usableSV.GAL,pdop);
        } else {
            numused = outMyPVT.numSvUsed;
        }

        Log.d ("Num SV", String.valueOf(numused));
    }


    private long getCarrierFreq(GnssMeasurement measurement) {
        long carrierFreq;

        if (measurement.hasCarrierFrequencyHz()) {
            carrierFreq = Math.round(measurement.getCarrierFrequencyHz() / 10.23e6);
        }
        else {
            carrierFreq = FREQ_L1;  // L1 default
        }

        return carrierFreq;
    }

    private boolean measurementIsSelected(GnssMeasurement measurement, boolean usegale1, boolean usegale5, boolean usegpsl1, boolean usegpsl5, long carrierFreq) {
        return ((measurement.getConstellationType()==CONSTELLATION_GALILEO)&&((usegale1&&(carrierFreq==FREQ_L1))||(usegale5&&(carrierFreq==FREQ_L5))))||
                ((measurement.getConstellationType()==CONSTELLATION_GPS)&&((usegpsl1&&(carrierFreq==FREQ_L1))||(usegpsl5&&(carrierFreq==FREQ_L5))));
    }

    private double calculatePredictedPseudorange(double[] satposECEF, double[] userposECEF) {
        return GpsMathOperations.vectorNorm(GpsMathOperations.subtractTwoVectors(satposECEF, userposECEF));
    }

    private double calculatePredictedPseudorangeRate(double[] satposECEF, double[] userposECEF,
                                                     double[] satvelECEF, double[] uservelECEF) {
        double [] rho = GpsMathOperations.subtractTwoVectors(satposECEF, userposECEF);
        double rhoNorm =  GpsMathOperations.vectorNorm(rho);
        double [] rhoDot = GpsMathOperations.subtractTwoVectors(satvelECEF, uservelECEF);
        double out = GpsMathOperations.dotProduct(rho, rhoDot);
        return out/rhoNorm;
    }

    public void processMeas(GnssMeasurement meas, List<Measurement> svMeasurements,
                            GnssClock gnssClock, boolean useambig, boolean filtcn0, boolean filtelev,
                            GalileoEphemeris.GalNavMessageProto galNavMessageProto,
                            Ephemeris.GpsNavMessageProto gpsNavMessageProto,
                            double tow, svNumber usableSV, SatTable satTable, long carrierFreq) {

        final double C_METRES_PER_SECOND = 299792458.0;
        double [] pseudorangeResult;
        double cn0;

        // Filter the measurements with low C/N0 (same threshold for GPS and Galileo)
        cn0 = meas.getCn0DbHz();
        if (filtcn0&&(cn0 < C_TO_N0_THRESHOLD_DB_HZ)) {
            return;
        }

        if(filtelev) {
            if ((satTable.findInList(meas.getConstellationType(), meas.getSvid())).elev < ELEVATION_THRESHOLD_DEG);
                return;
        }

        if (meas.getConstellationType()== CONSTELLATION_GALILEO) {
            pseudorangeResult = computeGalileoPseudorange(gnssClock, meas, useambig, null);
            if(pseudorangeResult[0]>0) {
                for(int i=0;i<(galNavMessageProto.ephemerids.length);i++) {
                    if (galNavMessageProto.ephemerids[i].prn == meas.getSvid()) {
                        // Verify the validity time of the ephemeris
                        int batchNum;
                        double ageOfToe; // hours
                        double ageOfData; // hours

                        ageOfToe = (tow - galNavMessageProto.ephemerids[i].toe) / 3600.0;
                        if (ageOfToe < -12 * 7)
                            ageOfToe = ageOfToe + 24 * 7;
                        batchNum = Math.floorDiv(galNavMessageProto.ephemerids[i].iodc, 127);
                        if (((galNavMessageProto.ephemerids[i].iodc % 127) == 0) &&
                                ((Math.round(galNavMessageProto.ephemerids[i].toe) % 76800) != 0))
                            batchNum = batchNum - 1;
                        ageOfData = ageOfToe + 3.0 * batchNum;

                        if ((ageOfToe < 4.0) && (ageOfToe > 0.0) && (ageOfData < 25.0)) {
                            svMeasurements.add(new Measurement(meas.getSvid(), 0,
                                    galNavMessageProto.ephemerids[i], null, pseudorangeResult[1],
                                    pseudorangeResult[0], meas.getReceivedSvTimeUncertaintyNanos()*C_METRES_PER_SECOND,
                                    meas.getPseudorangeRateMetersPerSecond(), meas.getPseudorangeRateUncertaintyMetersPerSecond(),
                                    pseudorangeResult[2], cn0, carrierFreq));
                            usableSV.addGAL();
                            break;
                        }
                    }
                }
            }
        }
        if (meas.getConstellationType()== CONSTELLATION_GPS)
        {
            pseudorangeResult = computeGpsPseudorange(gnssClock, meas, false, null);
            if(pseudorangeResult[0]>0) {
                for(int i=0;i<(gpsNavMessageProto.ephemerids.length);i++)
                    if((gpsNavMessageProto.ephemerids[i].prn)==meas.getSvid()) {
                        // Verify the validity time of the ephemeris
                        double ageOfToe; // hours
                        ageOfToe = (tow - gpsNavMessageProto.ephemerids[i].toe) / 3600.0;
                        if ((ageOfToe > -2.0) && (ageOfToe < 2.0)) {
                            svMeasurements.add(new Measurement(meas.getSvid(), 1,
                                    null, gpsNavMessageProto.ephemerids[i], pseudorangeResult[1],
                                    pseudorangeResult[0],  meas.getReceivedSvTimeUncertaintyNanos()*C_METRES_PER_SECOND,
                                    meas.getPseudorangeRateMetersPerSecond(), meas.getPseudorangeRateUncertaintyMetersPerSecond(),
                                    pseudorangeResult[2], cn0, carrierFreq));
                            usableSV.addGPS();
                            break;
                        }
                    }
            }
        }
        return;
    }


    //check long to double conversions
    public double[] computeGpsPseudorange(GnssClock clock, GnssMeasurement measurement, boolean useE1BC, DataLogger logger) {
        final int   SECONDS_PER_WEEK = 3600*24*7;
        final double C_METRES_PER_SECOND = 299792458.0;
        final long  NANOSECONDS_PER_SECOND = 1000000000,    NANOSECONDS_PER_E1C_2ND_CODE_PERIOD = 100000000;
        long        moduloPeriod, WeekNumberNanos, WeekNumber = 0;
        double      towRxSeconds=0.0, towTxSeconds=0.0, pseudoRange=0.0;
        long        towRxNanos, tRx;
        double      tTx, tRxSec, tTxSec;
        final int TOW_DECODED_MEASUREMENT_STATE_BIT = 3;

        if (((measurement.getState() & (1L << TOW_DECODED_MEASUREMENT_STATE_BIT)) != 0))
        {
            moduloPeriod = NANOSECONDS_PER_SECOND*SECONDS_PER_WEEK;

            long FullBiasNanos = clock.getFullBiasNanos();
            // double BiasNanos = clock.getBiasNanos(); removed because it is double and always 0

            WeekNumber = -clock.getFullBiasNanos()/(SECONDS_PER_WEEK*NANOSECONDS_PER_SECOND);
            //WeekNumber = (long) Math.floor(-clock.getFullBiasNanos()*1e-9/SECONDS_PER_WEEK);

            WeekNumberNanos = WeekNumber * NANOSECONDS_PER_SECOND * SECONDS_PER_WEEK;
            towRxNanos = clock.getTimeNanos() - FullBiasNanos - WeekNumberNanos;
            tRx = towRxNanos % moduloPeriod; // in ns
            towRxSeconds = towRxNanos*1e-9; // ToW at RX
            tTx = measurement.getReceivedSvTimeNanos() + measurement.getTimeOffsetNanos(); // in ns

            tRxSec = tRx*1e-9;
            tTxSec = tTx*1e-9;

            pseudoRange  = ((tRxSec - tTxSec) % (moduloPeriod*1e-9))*C_METRES_PER_SECOND; // in meters
            if (pseudoRange<0.0) // mod operation can return negative values
                pseudoRange = pseudoRange + (moduloPeriod*1e-9)*C_METRES_PER_SECOND;

            if(logger!=null)
                logger.writePseudoRangeCalculationToFile(measurement.getSvid(),
                        measurement.getState(),towRxNanos,tRxSec,tTxSec,pseudoRange,towTxSeconds);
        }

        double[] result = {pseudoRange, towRxSeconds, WeekNumber};
        return result;
    }

    //check long to double conversions
    public double[] computeGalileoPseudorange(GnssClock clock, GnssMeasurement measurement, boolean useE1BC, DataLogger logger) {
        final int   SECONDS_PER_WEEK = 3600*24*7;
        final double C_METRES_PER_SECOND = 299792458.0;
        final long  NANOSECONDS_PER_SECOND = 1000000000,    NANOSECONDS_PER_E1C_2ND_CODE_PERIOD = 100000000;
        long        moduloPeriod, WeekNumberNanos, WeekNumber = 0;
        double      towRxSeconds=0.0, towTxSeconds=0.0, pseudoRange=0.0;
        long        towRxNanos, tRx;
        double      tTx, tRxSec, tTxSec;

        boolean towAvail = ((measurement.getState()&STATE_TOW_DECODED)>0)||((measurement.getState()&STATE_TOW_KNOWN)>0);

        if(towAvail)
            moduloPeriod = NANOSECONDS_PER_SECOND*SECONDS_PER_WEEK;
        else //PC
            if ((measurement.getState()&STATE_GAL_E1C_2ND_CODE_LOCK)>0)
                moduloPeriod = NANOSECONDS_PER_E1C_2ND_CODE_PERIOD;
            else
                moduloPeriod = 4000000L;

        if(towAvail||
                ((measurement.getState()&STATE_GAL_E1C_2ND_CODE_LOCK)>0)||
                (((measurement.getState()&(1024+32)) == (1024+32))&&useE1BC)) {

            long FullBiasNanos = clock.getFullBiasNanos();
            // double BiasNanos = clock.getBiasNanos(); removed because it is double and always 0

            WeekNumber = -clock.getFullBiasNanos() / (SECONDS_PER_WEEK * NANOSECONDS_PER_SECOND);
            //WeekNumber = (long) Math.floor(-clock.getFullBiasNanos()*1e-9/SECONDS_PER_WEEK);

            WeekNumberNanos = WeekNumber * NANOSECONDS_PER_SECOND * SECONDS_PER_WEEK;
            towRxNanos = clock.getTimeNanos() - FullBiasNanos - WeekNumberNanos;

            if (towRxNanos > 604800000000000L) {
                towRxNanos = towRxNanos - 604800000000000L;
                WeekNumber = WeekNumber + 1;
            }

            tRx = towRxNanos % moduloPeriod; // in ns
            towRxSeconds = towRxNanos*1e-9; // ToW at RX
            tTx = measurement.getReceivedSvTimeNanos() + measurement.getTimeOffsetNanos(); // in ns

            tRxSec = tRx*1e-9;
            tTxSec = tTx*1e-9;

            pseudoRange  = ((tRxSec - tTxSec) % (moduloPeriod*1e-9))*C_METRES_PER_SECOND; // in meters
            if (pseudoRange<0.0) // mod operation can return negative values
                pseudoRange = pseudoRange + (moduloPeriod*1e-9)*C_METRES_PER_SECOND;

            if(logger!=null)
                logger.writePseudoRangeCalculationToFile(measurement.getSvid(), //SVID
                        measurement.getState(), // STATE
                        towRxNanos, // timeRxNanos
                        tRxSec, // timeRxSeconds
                        tTxSec, // timeTxSeconds
                        pseudoRange,
                        towTxSeconds);
        }

        double[] result = {pseudoRange, towRxSeconds, WeekNumber};
        return result;
    }

    public double solvePseudorangeAmbiguity(GalileoMeasurement currMeasurement, double modulo,
                                            double [] rxPos, double rxClockBias) {
        // PC correct ambiguous pseudoranges
        final double C_METRES_PER_SECOND = 299792458.0;
        int N = 20; // average value
        GNSSLib gnssLib = new GNSSLib();
        double tmpPseudoRange, TowAtTimeOfTransmission, svCb, tmpRes;
        double [] sp = new double[3];
        double [] sv = new double[3];

        tmpPseudoRange = currMeasurement.pseudorange + N*modulo*C_METRES_PER_SECOND;
        TowAtTimeOfTransmission = currMeasurement.towRx - tmpPseudoRange/C_METRES_PER_SECOND;
        svCb = gnssLib.satcb_e(currMeasurement.eph, TowAtTimeOfTransmission, true, currMeasurement.freq); // sec
        TowAtTimeOfTransmission = TowAtTimeOfTransmission - svCb;
        SatellitePositionCalculator.PositionAndVelocity satPosECEFMetersVelocityMPS = null;
        try {
            satPosECEFMetersVelocityMPS = SatellitePositionCalculator.
                    calculateSatellitePositionAndVelocityFromEphemeris(
                            currMeasurement.eph.getGpsEphemeris(),
                            TowAtTimeOfTransmission, (int) currMeasurement.eph.week,
                            rxPos[0], rxPos[1], rxPos[2], 0);
        } catch(Exception e) {
            e.printStackTrace();
        }

        sp[0] = satPosECEFMetersVelocityMPS.positionXMeters;
        sp[1] = satPosECEFMetersVelocityMPS.positionYMeters;
        sp[2] = satPosECEFMetersVelocityMPS.positionZMeters;
        sv[0] = satPosECEFMetersVelocityMPS.velocityXMetersPerSec;
        sv[1] = satPosECEFMetersVelocityMPS.velocityYMetersPerSec;
        sv[2] = satPosECEFMetersVelocityMPS.velocityZMetersPerSec;

        tmpRes = tmpPseudoRange - (calculatePredictedPseudorange(sp, rxPos)
                - svCb*C_METRES_PER_SECOND + rxClockBias);

        N = (int) round(tmpRes/(modulo*C_METRES_PER_SECOND));
        return (tmpPseudoRange - N*modulo*C_METRES_PER_SECOND);
    }

    public double[][] get2DSubArray(double[][] largeArray, int rowStartIndex, int rowEndIndex, int columnStartIndex,
                                    int columnEndIndex) {
        double[][] subArray = new double[rowEndIndex - rowStartIndex + 1][columnEndIndex - columnStartIndex + 1];
        int index = 0;
        for (int row = rowStartIndex; row <= rowEndIndex; row++) {
            subArray[index++] = Arrays.copyOfRange(largeArray[row], columnStartIndex, columnEndIndex+1);
        }
        return subArray;
    }

    public void WeightedLeastSquaresPVT(int method, svNumber usableSV,
                                       List<Measurement> svMeasurements,
                                       Ephemeris.GpsNavMessageProto gpsNavMessageProto,
                                       GalileoEphemeris.GalNavMessageProto galNavMessageProto,
                                       double [] positionVelocitySolutionECEF,
                                       double latitude0, double longitude0, outPVT outMyPVT) {

        final int MINIMUM_NUMBER_OF_USEFUL_SATELLITES = 4;
        final double C_METRES_PER_SECOND = 299792458.0;
        final int MAX_ITERATIONS = 10; /* max number of iteration for point pos */
        final double MAX_DELTA_METERS = 1e-4; // meter
        final double DOUBLE_ROUND_OFF_TOLERANCE = 0.0000000001;

        double PDOP = MAX_PDOP;
        double deltaDll = 0.5;
        double[] deltaPositionMeters;
        double[] deltaVelMeters;
        GNSSLib gnssLib = new GNSSLib();
        RealMatrix geometryMatrix;

        int numSat;
        int minSat = MINIMUM_NUMBER_OF_USEFUL_SATELLITES;
        switch(method) {
            case METHOD_GPS_AND_GALILEO:
                numSat = usableSV.GPS+usableSV.GAL;
                minSat = MINIMUM_NUMBER_OF_USEFUL_SATELLITES+1;
                break;

            case METHOD_GALILEO_ONLY:
                numSat = usableSV.GAL;
                break;

            case METHOD_GPS_ONLY:
                numSat = usableSV.GPS;
                break;

            default:
                numSat = 0;
        }

        outMyPVT.numSvUsed = numSat;

        if (numSat >= minSat) {

            // initialise the position
            double[] rxPos0Ecef = {0,0,0};
            if(latitude0!=999.9) {
                double[] rxPos0Lla = {latitude0 / 180.0 * Math.PI, longitude0 / 180.0 * Math.PI, 0.0};
                rxPos0Ecef = gnssLib.LLA2ECEF(rxPos0Lla);
            }
            positionVelocitySolutionECEF[0] = rxPos0Ecef[0];
            positionVelocitySolutionECEF[1] = rxPos0Ecef[1];
            positionVelocitySolutionECEF[2] = rxPos0Ecef[2];
            positionVelocitySolutionECEF[3] = 0.0;
            positionVelocitySolutionECEF[4] = 0.0;
            positionVelocitySolutionECEF[5] = 0.0;
            positionVelocitySolutionECEF[6] = 0.0;
            positionVelocitySolutionECEF[7] = 0.0;
            positionVelocitySolutionECEF[8] = 0.0;

            double[] residuals = new double[numSat];
            double[] residualsV = new double[numSat];
            double[] v = new double[numSat]; //normalised residuals
            //GGTO estimation
            double[][] geomMatrix = new double[numSat][5];
            //double[][] geomMatrix = new double[numSat][4];
            double maxresidual, maxDeltaPos;
            double[] xEcef = new double[3];
            double[] xDotEcef = new double[3];

            boolean doAtmoComputation = true;
            double[] atmoDelaySeconds = new double[numSat];
            double svCb, svCd, TowAtTimeOfTransmission;
            double[] sp = new double[3];
            double[] sv = new double[3];

            int iteration = 0;

            // Weight matrix for the weighted least square
            RealMatrix covarianceMatrixMetersSquare =
                    new Array2DRowRealMatrix(numSat, numSat);

            // Weight matrix for the weighted least square
            RealMatrix covarianceMatrixMPSSquare =
                    new Array2DRowRealMatrix(numSat, numSat);

            do {
                maxresidual = 0.0;
                xEcef[0] = positionVelocitySolutionECEF[0];
                xEcef[1] = positionVelocitySolutionECEF[1];
                xEcef[2] = positionVelocitySolutionECEF[2];

                xDotEcef[0] = positionVelocitySolutionECEF[5];
                xDotEcef[1] = positionVelocitySolutionECEF[6];
                xDotEcef[2] = positionVelocitySolutionECEF[7];

                int ii = 0;
                for (int i = 0; i < svMeasurements.size(); i++) {
                    double constRxClockBias = 0.0;
                    double constRxClockDrift = positionVelocitySolutionECEF[8];
                    int tmpConst;

                    // discard measurements in case of GPS or Galileo only solutions
                    tmpConst = svMeasurements.get(i).constellation;
                    if ((tmpConst == 1)&&(method == METHOD_GALILEO_ONLY))
                        continue;
                    if ((tmpConst == 0)&&(method == METHOD_GPS_ONLY))
                        continue;

                    // GGTO estimation
                    if (tmpConst == 0) // Galileo
                    {
                        constRxClockBias = positionVelocitySolutionECEF[4];
                        deltaDll = 0.25; //chip
                    }
                    if (tmpConst == 1) // GPS
                    {
                        constRxClockBias = positionVelocitySolutionECEF[3];
                        deltaDll = 0.5; //chip
                    }

                    if (svMeasurements.get(i).pseudorange <= 4e-3*C_METRES_PER_SECOND)
                    {
                        svMeasurements.get(i).pseudorange =
                                solvePseudorangeAmbiguity(
                                        svMeasurements.get(i).getGalileoMeasurement(), 4e-3,
                                        xEcef, constRxClockBias);
                    }

                    TowAtTimeOfTransmission = svMeasurements.get(i).towRx
                            - svMeasurements.get(i).pseudorange/C_METRES_PER_SECOND;

                    if (tmpConst == 0) {
                        svCb = gnssLib.satcb_e(svMeasurements.get(i).eph0, TowAtTimeOfTransmission,false, svMeasurements.get(i).freq);
                        svCd = 0.5*(gnssLib.satcb_e(svMeasurements.get(i).eph0, TowAtTimeOfTransmission+1,false, svMeasurements.get(i).freq) -
                                gnssLib.satcb_e(svMeasurements.get(i).eph0, TowAtTimeOfTransmission-1, false, svMeasurements.get(i).freq));
                    } else {
                            svCb = gnssLib.satcb_e_GPS(svMeasurements.get(i).eph1, TowAtTimeOfTransmission, false, svMeasurements.get(i).freq);
                            svCd = 0.5*(gnssLib.satcb_e_GPS(svMeasurements.get(i).eph1, TowAtTimeOfTransmission+1,false, svMeasurements.get(i).freq) -
                                gnssLib.satcb_e_GPS(svMeasurements.get(i).eph1, TowAtTimeOfTransmission-1, false, svMeasurements.get(i).freq));
                    }
                    TowAtTimeOfTransmission = TowAtTimeOfTransmission - svCb;
                    // calculate satellite position and velocity

                    SatellitePositionCalculator.PositionAndVelocity satPosECEFMetersVelocityMPS = null;
                    try {
                        satPosECEFMetersVelocityMPS = SatellitePositionCalculator.
                                calculateSatellitePositionAndVelocityFromEphemeris(
                                        svMeasurements.get(i).eph1,
                                        TowAtTimeOfTransmission, (int) svMeasurements.get(i).weekNumber,
                                        xEcef[0], xEcef[1], xEcef[2], svMeasurements.get(i).constellation);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    if (satPosECEFMetersVelocityMPS != null) {
                        // satellite position
                        sp[0] = satPosECEFMetersVelocityMPS.positionXMeters;
                        sp[1] = satPosECEFMetersVelocityMPS.positionYMeters;
                        sp[2] = satPosECEFMetersVelocityMPS.positionZMeters;
                        // satellite velocity
                        sv[0] = satPosECEFMetersVelocityMPS.velocityXMetersPerSec;
                        sv[1] = satPosECEFMetersVelocityMPS.velocityYMetersPerSec;
                        sv[2] = satPosECEFMetersVelocityMPS.velocityZMetersPerSec;
                    }

                    double linearCN0 = Math.pow(10.0, svMeasurements.get(i).CN0DbHz/10.0);

                    double codeSigma;

                    if (svMeasurements.get(i).freq == FREQ_L1)
                        codeSigma = C_METRES_PER_SECOND/1.023e6*Math.sqrt(1/(2.0*linearCN0));
                    else //L5/E5a
                        codeSigma = C_METRES_PER_SECOND/10.23e6*Math.sqrt(10.0/(2.0*linearCN0)*
                                (1.0+1.0/(linearCN0*1e-3)));
                    // Assuming uncorrelated pseudorange measurements, the covariance matrix will be diagonal as
                    // follows
                    covarianceMatrixMetersSquare.setEntry(ii, ii, codeSigma*codeSigma);

                    double rangeRateSigma = svMeasurements.get(i).prrSigma;

                    if (rangeRateSigma > MAX_RANGE_RATE_STD) {
                        rangeRateSigma = C_METRES_PER_SECOND/svMeasurements.get(i).freq/10.23e6/20e-3/2.0/Math.PI*
                                Math.sqrt(40/linearCN0);
                    }
                    covarianceMatrixMPSSquare.setEntry(ii, ii, rangeRateSigma*rangeRateSigma);

                    /* only with cn0
                    double rangeRateSigma = 0.0303*Math.sqrt(30/linearCN0);
                    covarianceMatrixMPSSquare.setEntry(ii, ii, rangeRateSigma*rangeRateSigma); */
                    double rho = calculatePredictedPseudorange(sp, xEcef);
                    // Compute the predicted range rate : dot(sp-xEcef, sv -xDotEcef)/norm(sp-xEcef)
                    double rhoDot = calculatePredictedPseudorangeRate(sp, xEcef, sv, xDotEcef);

                    // PC debug velocity
                    /*
                    Log.d("rhoDot", String.valueOf(rhoDot));
                    for (int kkk=0; kkk<3; kkk++) {
                        Log.d("sp", String.valueOf(sp[kkk]));
                        Log.d("xEcef", String.valueOf(xEcef[kkk]));
                        Log.d("sv", String.valueOf(sv[kkk]));
                        Log.d("xDotEcef", String.valueOf(xDotEcef[kkk]));
                    } */

                    residuals[ii] = svMeasurements.get(i).pseudorange -
                            (rho + constRxClockBias - svCb*C_METRES_PER_SECOND);

                    /*  residuals of velocity */
                    residualsV[ii] = svMeasurements.get(i).pseudorangeRate -
                    (rhoDot + constRxClockDrift - svCd*C_METRES_PER_SECOND);

                    // if available compute atmospheric corrections
                    if (doAtmoComputation)
                    {
                        // compute ionospheric delay with klobuchar algorithm
                        double ionoDelaySeconds = IonosphericModel.ionoKloboucharCorrectionSeconds(xEcef, sp,
                                svMeasurements.get(i).towRx, gpsNavMessageProto.iono.alpha,
                                gpsNavMessageProto.iono.beta, svMeasurements.get(i).freq*10.23e6);
                        // compute tropospheric delay
                        double T_amb=20;
                        double P_amb=101;
                        double P_vap=.86;
                        double tropoDelayMeters = gnssLib.CalculateTropoHopfield(T_amb,P_amb,P_vap,xEcef,sp);
                        //double tropoDelayMeters = TroposphericModelEgnos.calculateTropoCorrectionMeters()
                        atmoDelaySeconds[ii] = ionoDelaySeconds + tropoDelayMeters/C_METRES_PER_SECOND;
                    }
                    residuals[ii] = residuals[ii] - atmoDelaySeconds[ii]*C_METRES_PER_SECOND;
                    v[ii] = residuals[ii]/codeSigma; // normalisation for the Chi-square test
                    if(Math.abs(residuals[ii])>maxresidual)
                        maxresidual=Math.abs(residuals[ii]);

                    double[] r = {positionVelocitySolutionECEF[0] - sp[0],
                            positionVelocitySolutionECEF[1] - sp[1],
                            positionVelocitySolutionECEF[2] - sp[2]};
                    double norm = Math.sqrt(Math.pow(r[0], 2) + Math.pow(r[1], 2) + Math.pow(r[2], 2));

                    for (int j = 0; j < 3; j++)
                        geomMatrix[ii][j] = r[j]/norm;

                    if ((method == METHOD_GPS_ONLY)||(method == METHOD_GALILEO_ONLY))
                        geomMatrix[ii][3] = 1.;
                    else {
                        // GGTO estimation
                        if (tmpConst == 1) // GPS
                        {
                            geomMatrix[ii][3] = 1.;
                            geomMatrix[ii][4] = 0.;
                        }
                        if (tmpConst == 0) // Galileo
                        {
                            geomMatrix[ii][3] = 0.;
                            geomMatrix[ii][4] = 1.;
                        }
                    }
                    ii = ii + 1;
                }

                /* weighted least square estimation */
                if ((method == METHOD_GPS_ONLY)||(method == METHOD_GALILEO_ONLY))
                    geometryMatrix = new Array2DRowRealMatrix(get2DSubArray(geomMatrix,0,numSat-1,0, 3));
                else
                    geometryMatrix = new Array2DRowRealMatrix(geomMatrix);

                RealMatrix weightedGeometryMatrix;
                RealMatrix weightMatrixMetersMinus2 = null;
                // Apply weighted least square only if the covariance matrix is not singular (has a non-zero
                // determinant), otherwise apply ordinary least square. The reason is to ignore reported
                // signal to noise ratios by the receiver that can lead to such singularities

                LUDecomposition ludCovMatrixM2 = new LUDecomposition(covarianceMatrixMetersSquare);
                LUDecomposition ludCovMatrixM2V = new LUDecomposition(covarianceMatrixMPSSquare);
                double det = ludCovMatrixM2.getDeterminant();
                double detV = ludCovMatrixM2V.getDeterminant();

                RealMatrix Ht = geometryMatrix.transpose();
                RealMatrix HtH = Ht.multiply(geometryMatrix);

                try {
                    RealMatrix HtHInverse = new LUDecomposition(HtH).getSolver().getInverse();
                    RealMatrix HtHInverseHt = HtHInverse.multiply(Ht);

                    if (det <= DOUBLE_ROUND_OFF_TOLERANCE) {
                        // Do not weight the geometry matrix if covariance matrix is singular.
                        Log.d("Range Covariance Matrix det too low! ", String.valueOf(det));
                        deltaPositionMeters = GpsMathOperations.matrixByColVectMultiplication(HtHInverseHt.getData(), residuals);
                    } else {
                        //(HT R^-1 H)^-1 HT R^-1
                        weightMatrixMetersMinus2 = ludCovMatrixM2.getSolver().getInverse();
                        RealMatrix tempH = Ht.multiply(weightMatrixMetersMinus2).multiply(geometryMatrix);
                        RealMatrix hMatrix =  new LUDecomposition(tempH).getSolver().getInverse();
                        weightedGeometryMatrix = hMatrix.multiply(Ht).multiply(weightMatrixMetersMinus2);
                        deltaPositionMeters = GpsMathOperations.matrixByColVectMultiplication(
                                weightedGeometryMatrix.getData(), residuals);
                    }

                    Log.d("Range Rate Covariance Matrix det! ", String.valueOf(detV));
                    // same for velocity
                    /*
                    if (detV <= DOUBLE_ROUND_OFF_TOLERANCE) {
                        // Do not weight the geometry matrix if covariance matrix is singular.
                        Log.d("Covariance Matrix det too low! ", String.valueOf(detV));
                        deltaVelMeters = GpsMathOperations.matrixByColVectMultiplication(HtHInverseHt.getData(), residualsV);
                    } else {*/
                        //(HT R^-1 H)^-1 HT R^-1
                        weightMatrixMetersMinus2 = ludCovMatrixM2V.getSolver().getInverse();
                        RealMatrix tempH = Ht.multiply(weightMatrixMetersMinus2).multiply(geometryMatrix);
                        RealMatrix hMatrix =  new LUDecomposition(tempH).getSolver().getInverse();
                        weightedGeometryMatrix = hMatrix.multiply(Ht).multiply(weightMatrixMetersMinus2);
                        deltaVelMeters = GpsMathOperations.matrixByColVectMultiplication(
                                weightedGeometryMatrix.getData(), residualsV);
                    //}

                    PDOP = Math.sqrt(HtHInverse.getEntry(0,0) +
                            HtHInverse.getEntry(1,1) +
                            HtHInverse.getEntry(2,2)); // PDOP
                }
                catch (Exception e){
                    e.printStackTrace();
                    // GGTO ESTIMATION
                    deltaPositionMeters = new double[] {0.0, 0.0, 0.0, 0.0, 0.0};
                    deltaVelMeters =  new double[] {0.0, 0.0, 0.0, 0.0};
                    iteration = MAX_ITERATIONS;
                    PDOP = MAX_PDOP;
                }
                positionVelocitySolutionECEF[0] += deltaPositionMeters[0];
                positionVelocitySolutionECEF[1] += deltaPositionMeters[1];
                positionVelocitySolutionECEF[2] += deltaPositionMeters[2];

                if (method == METHOD_GPS_ONLY){
                    positionVelocitySolutionECEF[3] += deltaPositionMeters[3];
                    positionVelocitySolutionECEF[4] += 0;
                } else if (method == METHOD_GALILEO_ONLY) {
                    positionVelocitySolutionECEF[3] += 0;
                    positionVelocitySolutionECEF[4] += deltaPositionMeters[3];
                } else {
                    positionVelocitySolutionECEF[3] += deltaPositionMeters[3];
                    positionVelocitySolutionECEF[4] += deltaPositionMeters[4];
                }

                positionVelocitySolutionECEF[5] += deltaVelMeters[0];
                positionVelocitySolutionECEF[6] += deltaVelMeters[1];
                positionVelocitySolutionECEF[7] += deltaVelMeters[2];
                positionVelocitySolutionECEF[8] += deltaVelMeters[3];

                maxDeltaPos = Math.abs(deltaPositionMeters[0]) +
                        Math.abs(deltaPositionMeters[1]) +
                        Math.abs(deltaPositionMeters[2]);

                Log.d("idx#", String.valueOf(iteration));
                String tmp = "[";
                for (int kkk=0; kkk<residuals.length; kkk++) {
                    tmp = tmp + String.valueOf(residuals[kkk])+",";
                }
                tmp = tmp + "];";
                Log.d("PR res", tmp);

                tmp = "[";
                for (int kkk=0; kkk<residuals.length; kkk++) {
                    tmp = tmp + String.valueOf(residualsV[kkk])+",";
                }
                tmp = tmp + "];";
                Log.d("PR rate res",tmp);

                tmp = "[";
                for (int kkk=0; kkk<deltaPositionMeters.length; kkk++) {
                    tmp = tmp + String.valueOf(deltaPositionMeters[kkk])+",";
                }
                tmp = tmp + "];";
                Log.d("DeltaPos", tmp);

                tmp = "[";
                for (int kkk=0; kkk<deltaVelMeters.length; kkk++) {
                    tmp = tmp + String.valueOf(deltaVelMeters[kkk])+",";
                }
                tmp = tmp + "];";
                Log.d("DeltaVel", tmp);

                tmp = "[";
                for (int kkk=0; kkk<covarianceMatrixMPSSquare.getRowDimension(); kkk++) {
                    tmp = tmp + String.valueOf(covarianceMatrixMPSSquare.getEntry(kkk,kkk))+",";
                }
                tmp = tmp + "];";
                Log.d("Vel Cov Mat", tmp);

            } while((++iteration < MAX_ITERATIONS) &&
                    (maxDeltaPos > MAX_DELTA_METERS));
            if (PDOP < MAX_PDOP) {
                // Chi-square validation of residuals
                double chisqr[] = {      /* chi-sqr(n) (alpha=0.001) */
                        10.8, 13.8, 16.3, 18.5, 20.5, 22.5, 24.3, 26.1, 27.9, 29.6,
                        31.3, 32.9, 34.5, 36.1, 37.7, 39.3, 40.8, 42.3, 43.8, 45.3,
                        46.8, 48.3, 49.7, 51.2, 52.6, 54.1, 55.5, 56.9, 58.3, 59.7,
                        61.1, 62.5, 63.9, 65.2, 66.6, 68.0, 69.3, 70.7, 72.1, 73.4,
                        74.7, 76.0, 77.3, 78.6, 80.0, 81.3, 82.6, 84.0, 85.4, 86.7,
                        88.0, 89.3, 90.6, 91.9, 93.3, 94.7, 96.0, 97.4, 98.7, 100,
                        101, 102, 103, 104, 105, 107, 108, 109, 110, 112,
                        113, 114, 115, 116, 118, 119, 120, 122, 123, 125,
                        126, 127, 128, 129, 131, 132, 133, 134, 135, 137,
                        138, 139, 140, 142, 143, 144, 145, 147, 148, 149
                };
                double vv = 0.0;
                for (int ir=0;ir<numSat;ir++) {
                    vv = vv + v[ir]*v[ir];
                }
                if ((numSat>minSat)&&(vv>chisqr[numSat-minSat-1])) {
                    PDOP = MAX_PDOP;
                }
            }
        }

        outMyPVT.PDOP = PDOP;

        return;
    }


}