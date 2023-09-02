package com.rishi.data_collection_process.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductInfo product;

	private String category;

	public ProductCategory() {
		super();
	}

	public ProductCategory(Long id, ProductInfo product, String category) {
		super();
		this.id = id;
		this.product = product;
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public String getCategory() {
		return category;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
