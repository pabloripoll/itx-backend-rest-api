package api.com.client;

import api.com.model.ProductDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class ProductClient {

    private static final Logger log = LoggerFactory.getLogger(ProductClient.class);

    private final WebClient webClient;
    private final Duration timeout;

    public ProductClient(
            WebClient.Builder builder,
            @Value("${product.api.base-url}") String baseUrl,
            @Value("${product.api.timeout-ms}") long timeoutMs
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.timeout = Duration.ofMillis(timeoutMs);
    }

    /**
     * Returns the list of similar product IDs for a given product.
     * Emits empty if the product is not found (404).
     */
    public Mono<List<String>> getIds(String productId) {
        return webClient.get()
                .uri("/product/{id}/similarids", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(String.class)
                .collectList()
                .timeout(timeout)
                .doOnError(e -> log.warn("Error fetching similarids for {}: {}", productId, e.getMessage()))
                .onErrorResume(e -> Mono.just(List.of()));
    }

    /**
     * Returns the product detail for a given ID.
     * Returns empty Mono if not found (404), errored (5xx), or timed out.
     */
    public Mono<ProductDetail> getProductDetail(String productId) {
        return webClient.get()
                .uri("/product/{id}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> Mono.empty())
                .bodyToMono(ProductDetail.class)
                .timeout(timeout)
                .doOnError(e -> log.warn("Error fetching product {}: {}", productId, e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }
}
