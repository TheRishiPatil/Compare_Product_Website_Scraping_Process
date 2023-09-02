package com.rishi.data_collection_process.service.croma;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rishi.data_collection_process.model.ProductCategory;
import com.rishi.data_collection_process.model.ProductInfo;
import com.rishi.data_collection_process.repository.ProductRepository;
import com.rishi.data_collection_process.service.SetCategory;

@Service
public class ScrapeFromCroma {
	@Autowired
	private ProductRepository repository;

	@Autowired
	private SetCategory setCategory;

	public void scrapeAndSave() {
		int i = 0;
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource("cromaWebsites/croma.html").getFile());

		try {
			Document document = Jsoup.parse(input, "UTF-8");

			Elements box = document.select("ul#product-list-back.product-list");

			for (Element element : box.select("li.product-item")) {

				try {

					System.out.println("Croma " + i++);
					String productUrl = "https://www.croma.com"
							+ element.select("h3.product-title.plp-prod-title a").first().attr("href");
					String productName = element.select("h3.product-title.plp-prod-title a").first().text();
					String productBrand = productName.split(" ")[0];

					String productPriceText = element.select("span[data-testid=new-price]").text();
					int productPrice = Integer.parseInt(productPriceText.replaceAll("[^0-9]", ""));

					Element ratingElement = element.select("span.cp-rating.plp-ratings a span").first();
					String productRating = ratingElement != null ? ratingElement.text() : "";

					String imgUrl = element.select("div.product-img.plp-card-thumbnail a img").attr("src");

					Element reviewElement = element.selectFirst("span.cp-rating.plp-ratings a span span");
					String reviews = reviewElement != null ? reviewElement.text() : "";
					if (reviewElement != null) {
						String[] parts = reviews.split(" ");
						if (parts.length > 0) {
							reviews = parts[3];
						}
					}

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
						existingProduct
								.setDescription("Please Visit " + productName + " on croma.com for More Information");
					} else {
						ProductInfo product = new ProductInfo();
						product.setProductUrl(productUrl);
						product.setName(productName);
						product.setBrandName(productBrand);
						product.setPrice(productPrice);
						product.setRating(productRating);
						product.setImgUrl(imgUrl);
						product.setReview(reviews);
						product.setCompanyName("Croma");
						product.setDescription("Please Visit " + productName + " on croma.com for More Information");

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
		} catch (IOException e) {
		}
	}
}
