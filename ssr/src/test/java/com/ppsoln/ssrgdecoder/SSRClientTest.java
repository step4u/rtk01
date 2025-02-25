package com.ppsoln.ssrgdecoder;

import com.ppsoln.ssrgdecoder.dataType.Ssr;

import junit.framework.TestCase;

import static java.lang.Thread.sleep;

public class SSRClientTest extends TestCase {

    public void testSSRClient(){
        final SSRClient ssrClient = new SSRClient("fkp.ngii.go.kr", 2201, "dudrnr6847", "ngii", "/SSRG");
        final Thread tcpClient = new Thread(ssrClient);
        Ssr ssr;

        tcpClient.start();
        try {
            sleep(5000);
            ssr = ssrClient.getSSR();
            assertNotNull(ssr);

            sleep(5000);
            ssr = ssrClient.getSSR();
            assertNotNull(ssr);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tcpClient.interrupt();
    }
}