package com.ppsoln.ssrgdecoder.dataType;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class ClockCorrectionData {
    public int gs;
    public int prn;
    public double deltaClockC0;
    public double deltaClockC1;
    public double deltaClockC2;

    public ClockCorrectionData() {
        gs = 0;
        prn = 0;
        deltaClockC0 = 0;
        deltaClockC1 = 0;
        deltaClockC2 = 0;
    }

    public ClockCorrectionData(int _gs, int _prn, double _deltaClockC0, double _deltaClockC1, double _deltaClockC2){
        this.gs = _gs;
        this.prn = _prn;
        this.deltaClockC0 = _deltaClockC0;
        this.deltaClockC1 = _deltaClockC1;
        this.deltaClockC2 = _deltaClockC2;
    }

    public ClockCorrectionData(JSONObject satc){
        try {
            this.gs = satc.getInt("gs");
            this.prn = satc.getInt("prn");
            this.deltaClockC0 = satc.getDouble("deltaClockC0");
            this.deltaClockC1 = satc.getDouble("deltaClockC1");
            this.deltaClockC2 = satc.getDouble("deltaClockC2");
        }catch (JSONException e) {
            //
        }
    }

    @NonNull
    @Override
    public String toString() {
        return gs + "\t" + prn + "\t" + deltaClockC0 + "\t" + deltaClockC1 + "\t" + deltaClockC2 + "\t\n";
    }
}
