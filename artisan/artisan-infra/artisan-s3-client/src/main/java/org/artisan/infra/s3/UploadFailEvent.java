package org.artisan.infra.s3;

import org.springframework.context.ApplicationEvent;

public class UploadFailEvent extends ApplicationEvent {

    public UploadFailEvent(Object source) {
        super(source);
    }
}
