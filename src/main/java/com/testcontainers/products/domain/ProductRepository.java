package com.testcontainers.products.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private static final AtomicLong ID = new AtomicLong(0L);
    private static final List<Product> PRODUCTS = new ArrayList<>();

    public List<Product> getAll() {
        return List.copyOf(PRODUCTS);
    }

    public Product create(Product product) {
        Product p = new Product(
            ID.incrementAndGet(),
            product.title(),
            product.description()
        );
        PRODUCTS.add(p);
        return p;
    }
}
