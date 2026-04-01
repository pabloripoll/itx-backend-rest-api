package api.com.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "product.api")
public class ProductApiProperties {

    @NotNull
    private String baseUrl;
    private long timeoutMs = 3000;

    public String getBaseUrl() { return baseUrl; }

    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public long getTimeoutMs() { return timeoutMs; }

    public void setTimeoutMs(long timeoutMs) { this.timeoutMs = timeoutMs; }
}
