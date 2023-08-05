package com.suwon.faceAttendance.websocket;

public class Greeting {

    private byte[] imageBytes;

    public Greeting() {
    }

    public Greeting(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
