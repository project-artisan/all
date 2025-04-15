package org.artisan.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@Component
@HttpExchange
public interface DiscordAlertManager {


    @PostExchange("/api/webhooks/{webHooksKey}")
    void send(@PathVariable String webHooksKey, @RequestBody Payload payload);

    record Payload(String content){

    }
}
