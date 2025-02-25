package com.ppsoln.ssrgdecoder;

import com.ppsoln.ssrgdecoder.dataType.Ssr;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Base64.Encoder;

public class SSRClient implements Runnable {
    private Ssr ssr;

    private String serverURL;
    private int port;
    private String agent;
    private String authorization;
    private String mountPoint;

    private SsrgDecoder ssrgDecoder;

    public SSRClient(String url, int port, String id, String pwd, String mountPoint) {
        this.serverURL = url;
        this.port = port;
        this.agent = "NTRIP node-ssrg Client/0.1";
        this.authorization = "Basic " + toBase64(id + ":" + pwd);
        this.mountPoint = mountPoint;

        ssrgDecoder = new SsrgDecoder();
    }

    private String toBase64(String text){
        byte[] targetBytes = text.getBytes();

        Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(targetBytes);

        return new String(encodedBytes);
    }

    public Ssr getSSR(){
        return ssr;
    }

    @Override
    public void run(){
        try {
            Socket client = new Socket(serverURL, port);
            OutputStream output = client.getOutputStream();

            String msg = "GET " + this.mountPoint + " HTTP/1.1\r\nUser-Agent:" + agent + "\r\nAuthorization: " + authorization + "\n\n";
            output.write(msg.getBytes());

            while (true) {
                byte[] buffer = new byte[1024*1024];
                InputStream input = client.getInputStream();

                input.read(buffer);

                ssrgDecoder.rtcmSplitter(new ByteArrayInputStream(buffer));
                ssr = ssrgDecoder.ssrgParser();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}