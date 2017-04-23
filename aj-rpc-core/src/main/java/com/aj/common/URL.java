package com.aj.common;

/**
 * Created by chaiaj on 2017/4/8.
 */
public class URL {
    private String host;
    private int port;

    public URL(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "URL{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
