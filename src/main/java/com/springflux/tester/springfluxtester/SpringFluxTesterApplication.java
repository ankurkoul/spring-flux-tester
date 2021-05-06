package com.springflux.tester.springfluxtester;

import com.springflux.tester.springfluxtester.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;
import com.springflux.tester.springfluxtester.repo.ProductRepo;

@SpringBootApplication
public class SpringFluxTesterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFluxTesterApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ReactiveMongoOperations ops, ProductRepo productRepo ){
		return vars->{
			Flux<Product> pList=Flux.just(
					new Product(null,"A1",10.0),
					new Product(null,"B2",20.02),
					new Product(null,"C3",30.05)
			).flatMap(productRepo::save);

			pList.thenMany(productRepo.findAll())
					.subscribe(System.out::println);

			//IF NON EMBEDED DB
			/*ops.collectionExists(Product.class)
					.flatMap(exist->exist? ops.dropCollection(Product.class)
							: Mono.just(exist))
					.thenMany(v->ops.createCollection(Product.class))
					.thenMany(pList)
					.thenMany(productRepo.findAll())
					.subscribe(System.out::println);*/

		};
	}
}
