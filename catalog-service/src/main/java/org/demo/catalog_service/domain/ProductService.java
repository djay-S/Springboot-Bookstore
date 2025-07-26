package org.demo.catalog_service.domain;

import lombok.AllArgsConstructor;
import org.demo.catalog_service.ApplicationProperties;
import org.demo.catalog_service.domain.records.PagedResult;
import org.demo.catalog_service.domain.records.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationProperties properties;

    public PagedResult<Product> getProducts(int pageNo) {
        pageNo = Math.max(0, pageNo - 1);

        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNo, properties.pageSize(), sort);
        Page<Product> productsPage = productRepository.findAll(pageable).map(ProductMapper::toProduct);

        return new PagedResult<>(
                productsPage.getContent(),
                productsPage.getTotalElements(),
                productsPage.getNumber() + 1,
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                productsPage.hasNext(),
                productsPage.hasPrevious());
    }
}
