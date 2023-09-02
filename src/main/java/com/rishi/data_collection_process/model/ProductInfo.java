package com.rishi.data_collection_process.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class ProductInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 2000)
	private String productUrl;
	private String name;
	private String brandName;
	private int price;
	private String rating;
	private String review;
	@Column(length = 2000)
	private String imgUrl;
	private String companyName;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Set<ProductCategory> categories = new HashSet<>();
	@Column(length = 2000)
	private String description;

	public ProductInfo() {

	}

	public ProductInfo(String productUrl, String name, String brandName, int price, String rating, String review,
			String imgUrl, String companyName, Set<ProductCategory> categories) {
		this.productUrl = productUrl;
		this.name = name;
		this.brandName = brandName;
		this.price = price;
		this.rating = rating;
		this.review = review;
		this.imgUrl = imgUrl;
		this.companyName = companyName;
		this.categories = categories;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public String getName() {
		return name;
	}

	public String getBrandName() {
		return brandName;
	}

	public int getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRating() {
		return rating;
	}

	public String getReview() {
		return review;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getCompanyName() {
		return companyName;
	}

	public Set<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<ProductCategory> categories) {
		this.categories = categories;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
