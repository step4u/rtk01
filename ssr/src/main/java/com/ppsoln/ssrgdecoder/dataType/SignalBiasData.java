package com.ppsoln.ssrgdecoder.dataType;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class SignalBiasData {
    public int gs;
    public int prn;
    public int signalIndex;
    public double codeBias;
    public double phaseBias;

    public SignalBiasData() {
        this.gs = 0;
        this.prn = 0;
        this.signalIndex = 0;
        this.codeBias = 0;
        this.phaseBias = 0;
    }

    public SignalBiasData(int _gs, int _prn, int _signalIndex, double _codeBias, double _phaseBias) {
        this.gs = _gs;
        this.prn = _prn;
        this.signalIndex = _signalIndex;
        this.codeBias = _codeBias;
        this.phaseBias = _phaseBias;
    }

    public SignalBiasData(JSONObject satb) {
        try {
            this.gs = satb.getInt("gs");
            this.prn = satb.getInt("prn");
            this.signalIndex = satb.getInt("signalIndex");
            this.codeBias = satb.getDouble("codeBias");
            this.phaseBias = satb.getDouble("phaseBias");
        } catch(JSONException e) {
            //
        }
    }

    @NonNull
    @Override
    public String toString() {
        return gs + "\t" + prn + "\t" + signalIndex + "\t" + codeBias + "\t" + phaseBias + "\t\n";
    }
}
