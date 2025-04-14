package org.artisan.listener;

import lombok.RequiredArgsConstructor;
import org.artisan.service.DiscordAlertManager;
import org.artisan.service.DiscordAlertManager.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@Profile("prod")
@RequiredArgsConstructor
public class SystemEventListener {

    private final DiscordAlertManager discordAlertManager;

    @Value("${discord.web-hook-key}")
    private String key;


    @EventListener(ApplicationReadyEvent.class)
    public void open(){
        discordAlertManager.send(key, new Payload("open"));
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosed() {
        discordAlertManager.send(key, new Payload("close"));
    }
}
