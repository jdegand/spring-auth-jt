package com.example.springauthjt.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.example.springauthjt.dto.Product;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService {

    List<Product> productList = null;

    @PostConstruct
    public void loadProductsFromDB() {
        productList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> Product.builder()
                        .productId(i)
                        .name("product " + i)
                        .quantity(new Random().nextInt(10))
                        .price(new Random().nextInt(5000)).build())
                .collect(Collectors.toList());
    }

    public List<Product> getProducts() {
        return productList;
    }

    public Product getProduct(int id) {
        return productList.stream()
                .filter(product -> product.getProductId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product " + id + " not found"));
    }

}
