package com.ppsoln.ssrgdecoder.dataType;

public class RtcmLine {
    public String buf;
    public int crc;
    public int no;

    public RtcmLine(byte[] buf, int crc, int no){
        this.buf = byteArrayToBinaryString(buf);
        this.crc = crc;
        this.no = no;
    }

    private String byteArrayToBinaryString(byte[] b){
        StringBuilder sb=new StringBuilder();
        for (byte value : b) {
            sb.append(byteToBinaryString(value));
        }
        return sb.toString();
    }

    private String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }
}
