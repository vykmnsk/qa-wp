package com.tabcorp.qa.common;

public class FrameworkError extends RuntimeException {

    public FrameworkError(String msg) {
        super(msg);
    }

    public FrameworkError(Exception e) {
        super(e);
    }

}
