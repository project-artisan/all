package org.artisan.config;

import org.artisan.service.DiscordAlertManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class DiscordAlertConfig {


    @Bean("DiscordClient")
    RestClient discordClient(){
        return RestClient.builder()
                .baseUrl("https://discord.com")
                .build();
    }

    @Bean
    public DiscordAlertManager discordAlertManager() {
        RestClientAdapter adapter = RestClientAdapter.create(discordClient());

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(adapter)
                .build();

        return httpServiceProxyFactory.createClient(DiscordAlertManager.class);
    }
}
