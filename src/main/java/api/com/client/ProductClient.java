package api.com.client;

import api.com.config.ProductApiProperties;
import api.com.model.ProductDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Component
public class ProductClient {

    private static final Logger log = LoggerFactory.getLogger(ProductClient.class);

    private final WebClient webClient;
    private final Duration timeout;

    public ProductClient(WebClient.Builder builder, ProductApiProperties props) {
        this.webClient = builder
                .baseUrl(Objects.requireNonNull(props.getBaseUrl(), "product.api.base-url must be set"))
                .build();
        this.timeout = Duration.ofMillis(props.getTimeoutMs());
    }

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
