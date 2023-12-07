package com.testcontainers.products.domain;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

  private final JdbcClient jdbcClient;

  public ProductRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  public List<Product> getAll() {
    return jdbcClient.sql("SELECT * FROM products").query(Product.class).list();
  }

  public Product create(Product product) {
    String sql =
      "INSERT INTO products(title, description) VALUES (:title,:description) RETURNING id";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcClient
      .sql(sql)
      .param("title", product.title())
      .param("description", product.description())
      .update(keyHolder);
    Long id = keyHolder.getKeyAs(Long.class);
    return new Product(id, product.title(), product.description());
  }
}
