package com.ppsoln.ssrgdecoder.dataType;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class TroposphereDelayData {
    public int gs;
    public int latitude;
    public int longitude;
    public int height;
    public double ztd;
    public double zwd;

    public TroposphereDelayData(){
        this.gs = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.height = 0;
        this.ztd = 0;
        this.zwd =0;
    }

    public TroposphereDelayData(int _gs, int _latitude, int _longitude,
                                int _height, double _ztd, double _zwd) {
        this.gs = _gs;
        this.latitude = _latitude;
        this.longitude = _longitude;
        this.height = _height;
        this.ztd = _ztd;
        this.zwd = _zwd;
    }

    public TroposphereDelayData(JSONObject trop) {
        try {
            this.gs = trop.getInt("gs");
            this.latitude = trop.getInt("latitude");
            this.longitude = trop.getInt("longitude");
            this.height = trop.getInt("height");
            this.ztd = trop.getDouble("ztd");
            this.zwd = trop.getDouble("zwd");
        } catch(JSONException e){

        }
    }

    @NonNull
    @Override
    public String toString() {
        return gs + "\t" + latitude + "\t" + longitude + "\t" + height + "\t" + ztd + "\t" + zwd + "\t\n";
    }
}
