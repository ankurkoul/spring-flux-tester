package com.springflux.tester.springfluxtester.controller;

import com.springflux.tester.springfluxtester.model.Product;
import com.springflux.tester.springfluxtester.model.ProductEvent;
import com.springflux.tester.springfluxtester.repo.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/products")

public class ProductController {
    private ProductRepo productRepo;

    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> getProduct(@PathVariable String id) {
        return productRepo.findById(id)
                .map(p-> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product) {
        return productRepo.save(product);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable String id,
                                                       @RequestBody Product product) {
        return productRepo.findById(id)
                .flatMap(existingProduct->{
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    return productRepo.save(existingProduct);
                }).map(updatedProduct->ResponseEntity.ok(updatedProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id,
                                                    @RequestBody Product product) {
        return productRepo.findById(id)
                .flatMap(existingProduct->{
                    return productRepo.delete(existingProduct)
                            .then(Mono.just(ResponseEntity.ok().<Void>build()));
                }) .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAllProduct() {
        return productRepo.deleteAll();
    }

    @GetMapping(value = "/events",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getEvent() {
      return  Flux.interval(Duration.ofSeconds(1)).map(val-> new ProductEvent(val,"EVENT_HAPPENS"));
       // return productRepo.findAll();
    }
}
