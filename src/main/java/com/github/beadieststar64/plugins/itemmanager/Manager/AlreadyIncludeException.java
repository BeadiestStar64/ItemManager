package com.github.beadieststar64.plugins.itemmanager.Manager;

import java.io.Serial;

public class AlreadyIncludeException extends RuntimeException {

    //Declaration to avoid warnings
    @Serial
    private static final long serialVersionUID = 1L;

    //Construct
    public AlreadyIncludeException() {
        super();
    }
}
