package com.dakual.service;

import com.dakual.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product save(Product book);
    void delete(Product book);
    Page<Product> findByName(String name, PageRequest pageRequest);
    List<Product> findByName(String name);
    Optional<Product> findbyId(String id);
    List<Product> findAll();
    void deleteById(String id);
}
