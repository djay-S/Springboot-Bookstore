package org.demo.catalog_service.domain;

import lombok.RequiredArgsConstructor;
import org.demo.catalog_service.domain.records.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts(int pageNo) {
        return productRepository.findAll()
                .stream().map(ProductMapper::toProduct)
                .toList();
    }
}
