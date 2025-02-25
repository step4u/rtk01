package com.ppsoln.ssrgdecoder;

import com.ppsoln.ssrgdecoder.dataType.Ssr;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SsrgDecoderTest extends TestCase {
    SsrgDecoder ssrgDecoder = new SsrgDecoder();

    public void testReadByteToUInt() {
        byte[] data = new byte[] {-45, 0, 7, -1, -94};
        int pos, end;
        pos = 0;
        end = 1;
        assertEquals(211, ssrgDecoder.readByteToUInt(data, pos , end));

        pos = 1;
        end = 2;
        assertEquals(7, ssrgDecoder.readByteToUInt(data, pos , end));

        data = new byte[] {-45, -45, -45, -45, -45};
        pos = 0;
        end = 3;
        assertEquals(13882323, ssrgDecoder.readByteToUInt(data, pos , end));
    }

    public void testRtcmSplitter(){
        InputStream inputSSRG = null;
        try {
            inputSSRG = new FileInputStream("/Users/ichang-geon/Project/PPsoln/NGII202/ngii202/app/src/test/res/jenoba_ssrg_190228_g");
            assertEquals( true, ssrgDecoder.rtcmSplitter(inputSSRG));

            inputSSRG = null;
            assertEquals( false, ssrgDecoder.rtcmSplitter(inputSSRG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testSsrgParser() {
        InputStream inputSSRG = null;
        Ssr ssr;
        try {
            inputSSRG = new FileInputStream("/Users/ichang-geon/Project/PPsoln/NGII202/ngii202/app/src/test/res/jenoba_ssrg_190228_g");
            ssrgDecoder.rtcmSplitter(inputSSRG);
            ssr = ssrgDecoder.ssrgParser();
            assertNotNull(ssr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}