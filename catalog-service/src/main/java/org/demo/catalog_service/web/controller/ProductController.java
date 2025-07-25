package org.demo.catalog_service.web.controller;

import lombok.RequiredArgsConstructor;
import org.demo.catalog_service.domain.ProductService;
import org.demo.catalog_service.domain.records.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    List<Product> getProducts(){
        return productService.getProducts(0);
    }
}
