package com.ppsoln.ssrgdecoder.dataType;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class OrbitCorrectionData {
    public int gs;
    public int prn;
    public int iod;
    public double radial;
    public double alongTrack;
    public double crossTrack;

    public OrbitCorrectionData(){
        gs = 0;
        prn = 0;
        iod = 0;
        radial = 0.0;
        alongTrack = 0.0;
        crossTrack = 0.0;
    }

    public OrbitCorrectionData(int _gs, int _prn, int _iod,
                               double _radial, double _alongTrack, double _crossTrack) {
        this.gs  = _gs;
        this.prn = _prn;
        this.iod = _iod;
        this.radial = _radial;
        this.alongTrack = _alongTrack;
        this.crossTrack = _crossTrack;
    }

    public OrbitCorrectionData(JSONObject _Stec){
        try {
            this.gs = _Stec.getInt("gs");
            this.prn = _Stec.getInt("prn");
            this.iod = _Stec.getInt("iod");
            this.radial = _Stec.getDouble("radial");
            this.alongTrack = _Stec.getDouble("alongTrack");
            this.crossTrack = _Stec.getDouble("crossTrack");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return gs + "\t" + prn + "\t" + iod + "\t" + radial + "\t" + alongTrack + "\t" + crossTrack + "\t\n";
    }
}
