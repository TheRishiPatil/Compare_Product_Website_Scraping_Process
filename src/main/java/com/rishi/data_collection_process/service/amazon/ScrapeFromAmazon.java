package com.rishi.data_collection_process.service.amazon;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rishi.data_collection_process.model.ProductCategory;
import com.rishi.data_collection_process.model.ProductInfo;
import com.rishi.data_collection_process.repository.ProductRepository;
import com.rishi.data_collection_process.service.SetCategory;

@Service
public class ScrapeFromAmazon {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private SetCategory setCategory;

	public void scrapeAndSave() {

		int i = 0;

		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource("amazonWebsites/amazon.html").getFile());
		try {
			Document document = Jsoup.parse(input, "UTF-8");

			for (Element box : document.select("span.rush-component.s-latency-cf-section")) {

				for (Element element : box.select(
						"div.s-card-container.s-overflow-hidden.aok-relative.puis-include-content-margin.puis.puis-v1g4cn23aiw4pq21ytu1qia8qu3.s-latency-cf-section.s-card-border, div.s-card-container.s-overflow-hidden.aok-relative.puis-expand-height.puis-include-content-margin.puis.puis-v2jxz9i2qjba5p2azxbyo8sltz5.s-latency-cf-section.s-card-border, div.s-card-container.s-overflow-hidden.aok-relative.puis-include-content-margin.puis.puis-v2jxz9i2qjba5p2azxbyo8sltz5.s-latency-cf-section.s-card-border")) {

					try {

						System.out.println("Amazon " + i++);
						String productUrl = "https://www.amazon.in"
								+ element.select("a.a-link-normal.s-no-outline").first().attr("href");
						String productName = element.select(
								"span.a-size-medium.a-color-base.a-text-normal, span.a-size-base-plus.a-color-base.a-text-normal")
								.text();
						String productBrand = productName.split(" ")[0];

						String productPriceText = element.select("span.a-price-whole").text();
						int productPrice = 0;
						try {
							productPrice = Integer.parseInt(productPriceText.replaceAll("[^0-9]", ""));
						} catch (NumberFormatException e) {
							continue;
						}

						String productRating = element.select("span.a-icon-alt").text();
						String[] parts = productRating.split(" ");
						productRating = parts[0];

						String imgUrl = element.select("img.s-image").attr("src");

						Element reviewElement = element.selectFirst("span.a-size-base.s-underline-text");
						String reviews = reviewElement != null ? reviewElement.text() : "";

						reviews = reviews.replaceAll("[()]", "");

						Set<String> categories = setCategory.categories(productBrand);

						ProductInfo existingProduct = repository.findByProductUrl(productUrl);
						if (existingProduct != null) {
							existingProduct.setName(productName);
							existingProduct.setBrandName(productBrand);
							existingProduct.setPrice(productPrice);
							existingProduct.setRating(productRating);
							existingProduct.setReview(reviews);
							existingProduct.setImgUrl(imgUrl);
							existingProduct.setDescription(
									"Please Visit " + productName + " on amazon.com for More Information");
						} else {
							ProductInfo product = new ProductInfo();
							product.setProductUrl(productUrl);
							product.setName(productName);
							product.setBrandName(productBrand);
							product.setPrice(productPrice);
							product.setRating(productRating);
							product.setImgUrl(imgUrl);
							product.setReview(reviews);
							product.setCompanyName("Amazon");
							product.setDescription(
									"Please Visit " + productName + " on amazon.com for More Information");

							for (String category : categories) {
								ProductCategory productCategory = new ProductCategory();
								productCategory.setCategory(category);
								productCategory.setProduct(product);
								product.getCategories().add(productCategory);

							}

							repository.save(product);
						}
					} catch (Exception e) {

					}
				}
			}
		} catch (IOException e) {
		}
	}
}
