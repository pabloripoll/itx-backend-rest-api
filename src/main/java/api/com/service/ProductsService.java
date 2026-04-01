package api.com.service;

import api.com.client.ProductClient;
import api.com.model.ProductDetail;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductsService {

    private final ProductClient productClient;

    public ProductsService(ProductClient productClient) {
        this.productClient = productClient;
    }

    /**
     * Returns the list of similar ProductDetail for a given productId.
     * - Returns empty Mono (→ 404) if similarIds call fails or returns nothing.
     * - Fetches all product details in parallel.
     * - Skips products that are not found, errored, or timed out.
     */
    public Mono<List<ProductDetail>> getProducts(String productId) {
        return productClient.getIds(productId)
                .flatMap(ids -> {
                    if (ids.isEmpty()) {
                        return Mono.empty(); // triggers 404
                    }
                    return Flux.fromIterable(ids)
                            .flatMap(id -> productClient.getProductDetail(id)) // parallel
                            .collectList();
                });
    }
}
