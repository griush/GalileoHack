package com.gnsstracker.mainapp;

import android.app.Activity;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GALIProcesser {

    int pageCount = 0;
    private static class FetchResponse {
        public double lat;
        public double lon;
    }

    public GALIProcesser(Activity activity) {
        this.activity = activity;
    }

    public Activity activity;

    public class GALIData {
        public int svid = 0;
        public double t_oe = 0, M0 = 0, e = 0, sqa = 0;
        public double Omega0 = 0, i0 = 0, omega = 0, i_dot = 0;
        public double Omega_dot = 0, dn = 0, cuc = 0, cus = 0, crc = 0, crs = 0;
        public double cic = 0, cis = 0, t_oc = 0, af0 = 0, af1 = 0, af2 = 0;
    }

    private TextView ServerLocationDisplay;
    private ArrayList<GALIData> satellite_ephemeris = new ArrayList<>();

    public void onNewPage(byte[] page, int svid) throws MalformedURLException {
        boolean isE5bI = (page[0] & 0x80) == 0;
        if (!isE5bI) return; // E1-B not supported
        if ((page[0] & 0x40) != 0) return; // Alert page not supported

        System.out.println("New PAGE with NAV " + svid + " Count=" + pageCount++);

        byte[] data_i = new byte[16];
        for (int i = 0; i < 14; i++)
            data_i[i] = (byte) (((page[i] & 0x3F) << 2) | (page[i + 1] >>> 6));

        data_i[14] = (byte) (((page[14] & 0x0F) << 4) | (page[15] & 0xF0 >>> 4));
        data_i[15] = (byte) (((page[15] & 0x0F) << 4) | (page[16] & 0xF0 >>> 4));


        onNewData(data_i, svid);
    }

    private void onNewData(byte[] data, int svid) throws MalformedURLException {
        int type = data[0] >>> 2;
        System.out.println("DATA " + type);
        if (type > 4 || type < 1) return;

        boolean in_buffer = false;
        int entry_index = -1;
        GALIData gData = null;
        for (int i = 0; i < satellite_ephemeris.size(); i++) {
            if (svid == satellite_ephemeris.get(i).svid) {
                in_buffer = true;
                entry_index = i;
                gData = satellite_ephemeris.get(i);
                break;
            }
        }

        if (!in_buffer) gData = new GALIData();

        switch (type) {
            case 1:
                short t_oe_raw = (short) ((data[3] << 8) | data[4]);
                t_oe_raw >>>= 2;
                data = logicalRightShift(data);
                int M0_raw = data[4] << 24 | data[5] << 16 | data[6] << 8 | data[7];
                int e_raw = data[8] << 24 | data[9] << 16 | data[10] << 8 | data[11];
                int sqa_raw = data[12] << 24 | data[13] << 16 | data[14] << 8 | data[15];

                gData.t_oe = t_oe_raw * 60;
                gData.M0 = M0_raw * Math.pow(2, -31);
                gData.e = e_raw * Math.pow(2, -33);
                gData.sqa = sqa_raw * Math.pow(2, -19);
                break;
            case 2:
                int Omega0_raw = data[2] << 24 | data[3] << 16 | data[4] << 8 | data[5];
                int i0_raw = data[6] << 24 | data[7] << 16 | data[8] << 8 | data[9];
                int omega_raw = data[10] << 24 | data[11] << 16 | data[12] << 8 | data[13];
                short i_dot_raw = (short) ((data[14] << 8) | data[15]);
                i_dot_raw >>>= 2;

                gData.Omega0 = Omega0_raw * Math.pow(2, -31);
                gData.i0 = i0_raw * Math.pow(2, -31);
                gData.omega = omega_raw * Math.pow(2, -31);
                gData.i_dot = i_dot_raw * Math.pow(2, -31);
                break;
            case 3:
                int Omega_dot_raw = data[2] << 16 | data[3] << 8 | data[4];
                short dn_raw = (short) (data[5] << 8 | data[6]);
                short cuc_raw = (short) (data[7] << 8 | data[8]);
                short cus_raw = (short) (data[9] << 8 | data[10]);
                short crc_raw = (short) (data[11] << 8 | data[12]);
                short crs_raw = (short) (data[13] << 8 | data[14]);

                gData.Omega_dot = Omega_dot_raw * Math.pow(2, -43);
                gData.dn = dn_raw * Math.pow(2, -43);
                gData.cuc = cuc_raw * Math.pow(2, -29);
                gData.cus = cus_raw * Math.pow(2, -29);
                gData.crc = crc_raw * Math.pow(2, -5);
                gData.crs = crs_raw * Math.pow(2, -5);

                break;
            case 4:
                data = logicalRightShift(data);
                short cic_raw = (short) (data[3] << 8 | data[4]);
                short cis_raw = (short) (data[5] << 8 | data[6]);
                short t_oc_raw = (short) (data[7] << 8 | data[8]);
                t_oc_raw >>>= 2;

                // af0 b68..98 -> b70..100 => B8b6..B12b4
                // af1 b99..119 -> b101..121 => B12b5..B15b1
                // af2 b120..125 -> b122..127 => B15b2..B15b7

                int af0_raw = data[8] << 30 | data[9] << 22 | data[10] << 14 | data[11] << 6 | data[12] >>> 2;
                af0_raw >>>= 1;
                int af1_raw = (data[12] & 0x03) << 18 | data[13] << 10 | data[14] << 2 | (data[15] >>> 6);
                byte af2_raw = (byte) (data[15] & 0x3F);

                gData.cic = cic_raw * Math.pow(2, -29);
                gData.cis = cis_raw * Math.pow(2, -29);
                gData.t_oc = t_oc_raw * 60;
                gData.af0 = af0_raw * Math.pow(2, -34);
                gData.af1 = af1_raw * Math.pow(2, -46);
                gData.af2 = af2_raw * Math.pow(2, -59);
                break;
        }

        if (in_buffer) satellite_ephemeris.set(entry_index, gData);
        else satellite_ephemeris.add(gData);

        try {
            URL url = new URL(ServerHostname.ENDPOINT_LOCATION);

            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            // on below line setting method as post.
            client.setRequestMethod("POST");

            // on below line setting content type and accept type.
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");

            // on below line setting client.
            client.setDoOutput(true);

            String json;
            try {
                json = new ObjectMapper().writeValueAsString(satellite_ephemeris.toArray());
                System.out.println(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // on below line we are creating an output stream and posting the data.
            try (OutputStream os = client.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String JsonContent = "";

            // on below line creating and initializing buffer reader.
            try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(), "utf-8"))) {

                // on below line creating a string builder.
                StringBuilder response = new StringBuilder();

                // on below line creating a variable for response line.
                String responseLine = null;

                // on below line writing the response
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonContent = response.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                GALIProcesser.FetchResponse dataResponse = objectMapper.readValue(JsonContent, GALIProcesser.FetchResponse.class);

                activity.runOnUiThread(() -> {
                    // TODO: Add this controls on the Nerd Data page

                    //ServerLocationDisplay = activity.findViewById(R.id.CurrentLocationDisplay);
                    //ServerLocationDisplay.setText("Lat: " + dataResponse.lat + "\nLon: " + dataResponse.lon);
                });


            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private static byte[] logicalRightShift(byte[] byteArray) {
        byte[] shiftedArray = new byte[byteArray.length];

        for (int i = 0; i < byteArray.length; i++) {
            byte shiftedByte = (byte) (byteArray[i] >>> 2); // Perform logical right shift by 2 bits
            if (i < byteArray.length - 1) {
                byte lastTwoBits = (byte) (byteArray[i] & 0b00000011); // Extract last two bits
                shiftedArray[i + 1] |= lastTwoBits; // Set last two bits in the next byte
            }
            shiftedArray[i] = shiftedByte;
        }
        return shiftedArray;
    }
}

