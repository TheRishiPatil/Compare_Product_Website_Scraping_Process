package com.rishi.data_collection_process.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishi.data_collection_process.model.ProductInfo;

public interface ProductRepository extends JpaRepository<ProductInfo, Long> {

	ProductInfo findByProductUrl(String productUrl);
}
