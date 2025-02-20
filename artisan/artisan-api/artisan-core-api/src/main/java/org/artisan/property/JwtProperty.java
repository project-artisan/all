package org.artisan.property;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;

@ConfigurationProperties("jwt")
@ConfigurationPropertiesBinding
public record JwtProperty(
        Resource publicKey,
        Resource privateKey,
        RSAPublicKey rsaPublicKey,
        RSAPrivateKey rsaPrivateKey
) {

    public JwtProperty {
        try {
            rsaPublicKey = RsaKeyConverters.x509().convert(publicKey.getInputStream());
            rsaPrivateKey = RsaKeyConverters.pkcs8().convert(privateKey.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

