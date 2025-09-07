package org.demo.bookstore_webapp.client.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

/**
 * This is @HttpExchange method, which is similar to Feign clients.
 * The exchange methods, tell spring which service does the request needs to be redirected to.
 * Spring generates the dynamic proxies needed for these methods
 * The base url is configured in the {@link org.demo.bookstore_webapp.client.ClientsConfig}
 * */
public interface CatalogServiceClient {

    @GetExchange("/catalog/api/products")
    PagedResult<Product> getProducts(@RequestParam int page);

    @GetExchange("/catalog/api/products/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code);
}
