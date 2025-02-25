package com.ppsoln.ssrgdecoder.dataType;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class IonosphereDelayData {
    public int gs;
    public int latitude;
    public int longitude;
    public int height;
    public int prn;
    public double stec;


    public IonosphereDelayData() {
        gs = 0;
        latitude = 0;
        longitude = 0;
        height = 0;
        prn = 0;
        stec = 0;
    }

    public IonosphereDelayData(int _gs, int _latitude, int _longitude,
                               int _height, int _prn, double _stec) {
        this.gs = _gs;
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.height = _height;
        this.prn = _prn;
        this.stec = _stec;
    }

    public IonosphereDelayData(JSONObject stec) {
        try {
            this.gs = stec.getInt("gs");
            this.latitude = stec.getInt("latitude");
            this.longitude = stec.getInt("longitude");
            this.height = stec.getInt("height");
            this.prn = stec.getInt("prn");
            this.stec = stec.getDouble("stec");
        } catch (JSONException e) {

        }
    }

    @NonNull
    @Override
    public String toString() {
        return gs + "\t" + latitude + "\t" + longitude + "\t" + height + "\t" + prn + "\t" + stec + "\t\n";
    }
}
