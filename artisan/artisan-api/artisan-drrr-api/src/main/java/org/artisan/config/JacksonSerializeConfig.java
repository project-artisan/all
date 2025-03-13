package org.artisan.config;

import static org.artisan.domain.file.FileType.URL;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.domain.file.ExternalURL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JacksonSerializeConfig {

    @Value("${cdn.url}")
    private String cdnUrl;
    @Value("${cdn.tech-blog-thumbnail}")
    private String path;

    @Bean
    public Module ModulecustomStringModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ExternalURL.class, new ImageSerializer(cdnUrl, path));
        return module;
    }

    @RequiredArgsConstructor
    static class ImageSerializer extends JsonSerializer<ExternalURL> {
        private final String cdnUrl;
        private final String path;

        @Override
        public void serialize(
                ExternalURL image,
                JsonGenerator jsonGenerator,
                SerializerProvider serializerProvider
        ) throws IOException {
            if(image.getId() == null){
                jsonGenerator.writeString("");
                return ;
            }

            if(image.getType() == URL) {
                jsonGenerator.writeString(image.getId());
                return ;
            }

            jsonGenerator.writeString(cdnUrl + path + image.getId());
        }
    }

}
