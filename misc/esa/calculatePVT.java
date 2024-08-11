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