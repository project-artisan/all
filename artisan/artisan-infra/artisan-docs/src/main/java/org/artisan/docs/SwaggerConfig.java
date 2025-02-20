package org.artisan.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import javax.print.Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    @ConditionalOnMissingBean
    public DocumentSpec documentSpec(){
        return DocumentSpec.builder()
                .title("스웨거 설정")
                .description("DocumentSpec 정의하세요")
                .version("0.0.1")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI(@Autowired DocumentSpec documentSpec) {
        var openAPI = new OpenAPI();

        openAPI.setInfo(documentSpec.toInfo());

        if(documentSpec.enableJWT()) {
            var component = new Components().addSecuritySchemes(
                    "JWT",
                    new SecurityScheme().type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
            );

            openAPI.setComponents(component);
            openAPI.addSecurityItem(new SecurityRequirement().addList("JWT"));
        }

        return openAPI;
    }
}
