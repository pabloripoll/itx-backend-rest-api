package api.com.model;

public record ProductDetail(
        String id,
        String name,
        Double price,
        Boolean availability
) {}
