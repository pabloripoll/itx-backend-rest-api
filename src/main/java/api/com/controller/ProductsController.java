package api.com.controller;

import api.com.model.ProductDetail;
import api.com.service.ProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductsController {

    private final ProductsService service;

    public ProductsController(ProductsService service) {
        this.service = service;
    }

    @GetMapping("/{productId}/similar")
    public Mono<ResponseEntity<List<ProductDetail>>> getProducts(
            @PathVariable String productId
    ) {
        return service.getProducts(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
