package com.ppsoln.ssrgdecoder;

import com.ppsoln.ssrgdecoder.dataType.RtcmLine;
import com.ppsoln.ssrgdecoder.dataType.SignalBiasData;
import com.ppsoln.ssrgdecoder.dataType.ClockCorrectionData;
import com.ppsoln.ssrgdecoder.dataType.OrbitCorrectionData;
import com.ppsoln.ssrgdecoder.dataType.Ssr;
import com.ppsoln.ssrgdecoder.dataType.IonosphereDelayData;
import com.ppsoln.ssrgdecoder.dataType.TroposphereDelayData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static com.ppsoln.ssrgdecoder.Util.readBits;
import static com.ppsoln.ssrgdecoder.Util.readUBits;

public class SsrgDecoder {
    private ArrayList<RtcmLine> rtcmLine;
    private ArrayList<OrbitCorrectionData> orbitCorrectionData;
    private int satoGs;
    private ArrayList<ClockCorrectionData> clockCorrectionData;
    private int satcGs;
    private ArrayList<SignalBiasData> signalBiasData;
    private int satbGs;
    private ArrayList<TroposphereDelayData> troposphereDelayData;
    private int tropGs;
    private ArrayList<IonosphereDelayData> ionosphereDelayData;
    private int stecGs;

    public SsrgDecoder() {
        orbitCorrectionData = new ArrayList<>();
        satoGs = 0;

        clockCorrectionData = new ArrayList<>();
        satcGs = 0;

        signalBiasData = new ArrayList<>();
        satbGs = 0;

        troposphereDelayData = new ArrayList<>();
        tropGs = 0;

        ionosphereDelayData = new ArrayList<>();
        stecGs = 0;
    }

    public Ssr ssrgParser() {
        Ssr ssrg = new Ssr();
        // int no = 0;
        // int subType = 0;
        int mNo = 0;

        for (RtcmLine line : rtcmLine) {
            if ( line.no == 4090) {
                // no = readUBits(line.buf, 0, 12);
                // subType = readUBits(line.buf, 12, 4);
                mNo = readUBits(line.buf, 16, 8);
            }

            // System.out.println("no : " + no + " | subType : " + subType + " | mNo : " + mNo);
            switch (mNo) {
                case 1:
                    parseSM001(line.buf);
                    break;
                case 2:
                    parseSM002(line.buf);
                    break;
                case 3:
                    parseSM003(line.buf);
                    break;
                case 4:
                    parseSM004(line.buf);
                    break;
                case 5:
                    parseSM005(line.buf);
                    break;
                case 6:
                    parseSM006(line.buf);
                    break;
            }
        }

        ssrg.orbitCorrectionData = orbitCorrectionData.toArray(new OrbitCorrectionData[0]);
        ssrg.clockCorrectionData = clockCorrectionData.toArray(new ClockCorrectionData[0]);
        ssrg.signalBiasData = signalBiasData.toArray(new SignalBiasData[0]);
        ssrg.troposphereDelayData = troposphereDelayData.toArray(new TroposphereDelayData[0]);
        ssrg.ionosphereDelayData = ionosphereDelayData.toArray(new IonosphereDelayData[0]);

        return ssrg;
    }


    // sato
    public void parseSM001(String buf) {
        int ver = readUBits(buf, 24, 3);
        int gs = readUBits(buf, 27,20);
        // int updateInterval = readUBits(buf, 47, 4);
        // int multipleMessageIndicator = readUBits(buf, 51, 1);
        // int reserved = readUBits(buf, 53, 4);
        int gnssIndicator = readUBits(buf, 57,4);
        // int satelliteReferenceDatum = readUBits(buf, 61, 1);
        int noSat = readUBits(buf, 62, 6);

        if (gs != satoGs) {
            satoGs = gs;
            orbitCorrectionData = new ArrayList<>();
        }

        int pos = 68;
        int size = (ver == 0) ? 8 : 11;
        for( int i = 0 ; i < noSat; i++) {
            int prn = _prnHeader(gnssIndicator) + readUBits(buf, pos, 6);
            int iod = readUBits(buf, pos + 6, size);
            double rad = readBits(buf, pos + 6 + size, 22) * 0.0001;
            double alt = readBits(buf, pos + 28 + size, 20) * 0.0004;
            double crt = readBits(buf, pos + 48 + size, 20) * 0.0004;
            orbitCorrectionData.add(new OrbitCorrectionData(gs, prn, iod, rad, alt, crt));
            pos += 68 + size;
        }
    }

    // satc
    public void parseSM002(String buf) {
        // int ver = readUBits(buf, 24, 3);
        int gs = readUBits(buf, 27, 20);
        // int updateInterval = readUBits(buf, 47, 4);
        // int multipleMessageIndicator = readUBits(buf, 51, 1);
        // int updateIntervalClass = readUBits(buf, 52, 1);
        // int reserved = readUBits(buf, 53, 4);
        int gnssIndicator = readUBits(buf, 57, 4);
        int noSat = readUBits(buf, 61, 6);

        if (gs != satcGs) {
            satcGs = gs;
            clockCorrectionData = new ArrayList<>();
        }

        int pos = 67;
        for (int i = 0 ; i < noSat ; i++) {
            int prn = _prnHeader(gnssIndicator) + readUBits(buf, pos, 6);
            double c0 = readBits(buf, pos + 6, 22) * 0.0001;
            double c1 = readBits(buf, pos + 28, 21) * 0.000001;
            double c2 = readBits(buf, pos + 49, 27) * 0.00000002;
            clockCorrectionData.add(new ClockCorrectionData(gs, prn, c0, c1, c2));
            pos += 76;
        }
    }

    // satb
    public void parseSM003(String buf) {
        int ver = readUBits(buf, 24, 3);
        int gs = readUBits(buf, 27, 20);
        // int updateInterval = readUBits(buf, 47, 4);
        // int multipleMessageIndicator = readUBits(buf, 51, 1);
        // int updateIntervalClass = readUBits(buf, 52, 1);
        // int reserved = readUBits(buf, 53, 4);
        int gnssIndicator = readUBits(buf, 57, 4);
        int noSat = readUBits(buf, 61, 6);

        if (gs != satbGs) {
            satbGs = gs;
            signalBiasData = new ArrayList<>();
        }

        int pos = 67;
        for (int i = 0 ; i < noSat ; i ++) {
            int prn = _prnHeader(gnssIndicator) + readUBits(buf, pos, 6);
            if (ver == 0) {
                double c1 = readBits(buf, pos + 6, 14) * 0.01;
                double p2 = readBits(buf, pos + 20, 14) * 0.01;
                double l1 = readBits(buf, pos + 34, 22) * 0.0001;
                double l2 = readBits(buf, pos + 56, 22) * 0.0001;

                signalBiasData.add(new SignalBiasData(gs, prn, 0, c1, l1));
                signalBiasData.add(new SignalBiasData(gs, prn, 11, p2, l2));
                pos += 78;
            } else {
                int noSigs = readUBits(buf, pos + 6, 4);
                pos += (ver == 1) ? 10 : 11;

                for (int j = 0; j < noSigs; j++) {
                    int id = readUBits(buf, pos, 5);
                    double c = readBits(buf, pos + 5, 14) * 0.01;
                    double p = readBits(buf, pos + 19, 22) * 0.0001;

                    signalBiasData.add(new SignalBiasData(gs, prn, id, c, p));
                    pos += (ver <= 2) ? 45 : 55;
                }
            }
        }
    }

    // trop
    public void parseSM004(String buf) {
        int ver = readUBits(buf, 24, 3);
        int gs = readUBits(buf, 27, 20);
        // int updateInterval = readUBits(buf, 47, 4);
        // int multipleMessageIndicator = readUBits(buf, 51, 1);
        // int updateIntervalClass = readUBits(buf, 52, 1);
        // int reserved = readUBits(buf, 53, 4);
        int noGP = readUBits(buf, 57, 6);

        if (gs != tropGs) {
            tropGs = gs;
            troposphereDelayData = new ArrayList<>();
        }

        int pos = 63;
        for (int i = 0; i < noGP; i++) {
            if (ver <= 1) {
                // int type = readUBits(buf, pos, 2);
                double lat = readBits(buf, pos + 2, 28) * 0.000001;
                double lon = readBits(buf, pos + 30, 29) * 0.000001;
                double hgt = readBits(buf, pos + 59, 24) * 0.001;
                double tr = readBits(buf, pos + 83, 16) * 0.0001;
                double tw = readBits(buf, pos + 99, 14) * 0.0001;
                troposphereDelayData.add(new TroposphereDelayData(gs, (int)lat, (int)lon, (int)hgt, tr, tw));
                pos += 113;
            } else {
                // int type = readUBits(buf, pos, 2);
                double lat = readBits(buf, pos + 2, 28) * 0.000001;
                double lon = readBits(buf, pos + 30, 29) * 0.000001;
                double hgt = readBits(buf, pos + 59, 24) * 0.001;
                double tr = readBits(buf, pos + 83, 18) * 0.0001;
                double tw = readBits(buf, pos + 101, 14) * 0.0001;
                troposphereDelayData.add(new TroposphereDelayData(gs, (int)lat, (int)lon, (int)hgt, tr, tw));
                pos += 115;
            }
        }
    }

    // stec
    public void parseSM005(String buf) {
        // int ver = readUBits(buf, 24, 3);
        int gs = readUBits(buf, 27, 20);
        // int updateInterval = readUBits(buf, 47, 4);
        // int multipleMessageIndicator = readUBits(buf, 51, 1);
        // int updateIntervalClass = readUBits(buf, 52, 1);
        // int reserved = readUBits(buf, 53, 4);
        int gnssIndicator = readUBits(buf, 57, 4);
        int noGP = readUBits(buf, 61, 6);

        if (gs != stecGs) {
            stecGs = gs;
            ionosphereDelayData = new ArrayList<>();
        }

        int pos = 67;
        for (int i = 0; i < noGP; i++) {
            // int type = readUBits(buf, pos, 2);
            double lat = readBits(buf, pos + 2, 28) * 0.000001;
            double lon = readBits(buf, pos + 30, 29) * 0.000001;
            double hgt = readBits(buf, pos + 59, 24) * 0.001;
            int noSat = readUBits(buf, pos + 83, 6);
            pos += 89;

            for (int j = 0; j < noSat; j++) {
                int prn = _prnHeader(gnssIndicator) + readUBits(buf, pos, 6);
                double stecValue = readBits(buf, pos + 6, 30) * 0.00001;
                pos += 36;

                ionosphereDelayData.add(new IonosphereDelayData(gs, (int)lat, (int)lon, (int)hgt, prn, stecValue));
            }
        }
    }

    // trop stec
    public void parseSM006(String buf) {
        int ver = readUBits(buf, 24, 3);
        int gs = readUBits(buf, 27, 20);
        // int updateInterval = readUBits(buf, 47, 4);
        // int multipleMessageIndicator = readUBits(buf, 51, 1);
        // int updateIntervalClass = readUBits(buf, 52, 1);
        // int reserved = readUBits(buf, 53, 4);
        int gnssIndicator = readUBits(buf, 57, 4);
        int noGP = readUBits(buf, 61, 6);

        if (gs != stecGs) {
            stecGs = gs;
            ionosphereDelayData = new ArrayList<>();
        }

        if (gs != tropGs) {
            tropGs = gs;
            troposphereDelayData = new ArrayList<>();
        }

        int  pos = 67;
        // int type = 0;
        double lat;
        double lon;
        double hgt;
        double tr;
        double tw;
        double noSat;
        int prn;
        double stecValue;
        for (int  i = 0; i < noGP; i++) {
            if (ver <= 1) {
                // type = readUBits(buf, pos, 2);
                lat = readBits(buf, pos + 2, 28) * 0.000001;
                lon = readBits(buf, pos + 30, 29) * 0.000001;
                hgt = readBits(buf, pos + 59, 24) * 0.001;
                tr = readBits(buf, pos + 83, 16) * 0.0001;
                tw = readBits(buf, pos + 99, 14) * 0.0001;
                noSat = readUBits(buf, pos + 113, 6);
                pos += 119;
            } else {
                // type = readUBits(buf, pos, 2);
                lat = readBits(buf, pos + 2, 28) * 0.000001;
                lon = readBits(buf, pos + 30, 29) * 0.000001;
                hgt = readBits(buf, pos + 59, 24) * 0.001;
                tr = readBits(buf, pos + 83, 18) * 0.0001;
                tw = readBits(buf, pos + 101, 14) * 0.0001;
                noSat = readUBits(buf, pos + 115, 6);
                pos += 121;
            }
            troposphereDelayData.add(new TroposphereDelayData(gs, (int)lat, (int)lon, (int)hgt, tr, tw));

            for (int j = 0; j < noSat; j++) {
                prn = _prnHeader(gnssIndicator) + readUBits(buf, pos, 6);
                stecValue = readBits(buf, pos + 6, 30) * 0.00001;
                pos += 36;
                ionosphereDelayData.add(new IonosphereDelayData(gs, (int)lat, (int)lon, (int)hgt, prn, stecValue));
            }
        }
    }

    // header
    private int _prnHeader(int gnssIndicator){
        switch (gnssIndicator){
            case 0:
                return 100;
            case 1:
                return 300;
            case 2:
                return 400;
            case 3:
                return 600;
            case 4:
                return 500;
            case 5:
                return 200;
        }
        return 0;
    }

    public boolean rtcmSplitter(InputStream inputSSRG) {
        rtcmLine = new ArrayList<>();
        try {
            if(inputSSRG == null) {
                return false;
            }
            int totalLength = inputSSRG.available();
            byte[] data = new byte[totalLength];
            int pos, length, crc, no, start, end;
            byte[] buf;

            if (inputSSRG.read(data) == -1) {
                return false;
            }

            pos = 0;
            while (pos < totalLength) {
                int preamble = 0;

                while(pos < totalLength && preamble != 0xD3) {
                    preamble = readByteToUInt(data, pos, 1);
                    pos++;
                }

                if (pos >= totalLength -1) {
                    break;
                }

                length = readByteToUInt(data, pos, 2) & 0x03ff;
                if (pos + 2 + length + 3 > totalLength) {
                    break;
                }

                pos = pos + 2;
                start = pos - 3;
                end = start + length + 3;
                buf = Arrays.copyOfRange(data, pos, pos+length);
                crc = readByteToUInt(data, end, 3);
                no = readMessageNumber(buf);
                pos = pos + length + 3;

                if( !Util.crc24(data, start, end, crc)){
                    // System.out.println("rtcm3 parity error: len=" + length);
                    break;
                }else {
                    rtcmLine.add(new RtcmLine(buf, crc, no));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int readByteToUInt(byte[] data, int pos, int end) {
        byte[] bytes = Arrays.copyOfRange(data, pos, pos + end);
        int res = 0;

        if (bytes.length == 1) {
            res |= bytes[0] & 0xff;
        }else if (bytes.length == 2) {
            res |= (bytes[0] & 0xff) << 8;
            res |= (bytes[1] & 0xff);
        }else if (bytes.length == 3) {
            res |= (bytes[0] & 0xff) << 16;
            res |= (bytes[1] & 0xff) << 8;
            res |= (bytes[2] & 0xff);
        }else if (bytes.length == 4) {
            res |= (bytes[0] & 0xff) << 24;
            res |= (bytes[1] & 0xff) << 16;
            res |= (bytes[2] & 0xff) << 8;
            res |= (bytes[3] & 0xff);
        }
        return res;
    }

    private int readMessageNumber (byte[] buf) {
        if(buf.length < 2) {
            return 0;
        }
        return (readByteToUInt(buf, 0, 2) >> 4);
    }
}
