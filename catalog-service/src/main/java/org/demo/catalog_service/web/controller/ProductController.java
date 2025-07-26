package org.demo.catalog_service.web.controller;

import lombok.RequiredArgsConstructor;
import org.demo.catalog_service.domain.ProductService;
import org.demo.catalog_service.domain.records.PagedResult;
import org.demo.catalog_service.domain.records.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductService productService;

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        return productService.getProducts(pageNo);
    }
}
