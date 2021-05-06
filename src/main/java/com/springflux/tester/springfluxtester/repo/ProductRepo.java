package com.springflux.tester.springfluxtester.repo;

import com.springflux.tester.springfluxtester.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepo extends ReactiveMongoRepository<Product,String> {
}
