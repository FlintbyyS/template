package com.game.utility;

public class NoSuchPlayerException extends  RuntimeException {

    public NoSuchPlayerException(String message) {
        super(message);
    }
}