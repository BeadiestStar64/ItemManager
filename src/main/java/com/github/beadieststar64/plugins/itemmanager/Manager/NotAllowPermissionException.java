package com.github.beadieststar64.plugins.itemmanager.Manager;

import java.io.Serial;

public class NotAllowPermissionException extends RuntimeException {

    //Declaration to avoid warnings
    @Serial
    private static final long serialVersionUID = 1L;

    //Construct
    public NotAllowPermissionException(NotAllowPermissionException e) {
        super(e);
    }
}
