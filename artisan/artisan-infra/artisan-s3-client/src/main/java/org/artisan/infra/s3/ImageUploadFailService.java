package org.artisan.infra.s3;

public interface ImageUploadFailService {

    void consume(ImageUploadRequest event);
}
