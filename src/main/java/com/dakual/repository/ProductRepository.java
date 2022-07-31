package com.dakual.repository;

import com.dakual.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    Page<Product> findByName(String name, Pageable pageable);
    @Query("{\"fuzzy\":{\"name\":{\"value\":\"?0\",\"fuzziness\":\"AUTO\",\"transpositions\":true}}}")
    List<Product> findByName(String name);
    Optional<Product> findById(String id);

    List<Product> findAll();
    void deleteById(String id);
}
