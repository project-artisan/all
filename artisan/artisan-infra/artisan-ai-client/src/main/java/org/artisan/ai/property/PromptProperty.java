package org.artisan.ai.property;

import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.io.Resource;

@ConfigurationProperties("prompt")
@ConfigurationPropertiesBinding
public record PromptProperty(
        Resource backend,
        SystemPromptTemplate backendPromptTemplate
) {
    public PromptProperty {
        backendPromptTemplate = new SystemPromptTemplate(backend);
    }
}
