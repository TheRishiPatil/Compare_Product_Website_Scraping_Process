package com.rishi.data_collection_process.service.flipkart;

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
public class ScrapeFromFlipkart {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ScrapeOtherInfoFromFlipkart descFromFlipkart;

	@Autowired
	private SetCategory setCategory;

	public void scrapeAndSave() {
		int i = 0;
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource("flipkartWebsites/flipkart.html").getFile());

		try {
			Document document = Jsoup.parse(input, "UTF-8");

			for (Element box : document.select("div._1YokD2._3Mn1Gg")) {

				for (Element element : box.select("div._4ddWXP, div._2kHMtA, div._1xHGtK._373qXS")) {

					try {

						String productUrl = "https://www.flipkart.com"
								+ element.select("a._1fQZEK, a.s1Q9rs, a.IRpwTa").first().attr("href");
						String productName = element.select("a.s1Q9rs, div._4rR01T, a.IRpwTa").text();
						String productBrand = productName.split(" ")[0];

						System.out.println("Flipkart " + i++ + " " + productName);

						String productPriceText = element.select("div._30jeq3, div._30jeq3._1_WHN1").text();
						int productPrice = Integer.parseInt(productPriceText.replaceAll("[^0-9]", ""));

						String productRating = element.select("div._3LWZlK").text();

						String imgUrl = element.select("img._396cs4, img._2r_T1I").attr("src");

						Element reviewElement = element.selectFirst("span._2_R_DZ");
						String reviews = reviewElement != null ? reviewElement.text() : "";
						if (reviewElement != null) {
							String[] parts = reviews.split(" ");
							if (parts.length > 0) {
								reviews = parts[3];
							}
						}

//						String productRating = descFromFlipkart.scrapRating(productUrl);
//						String reviews = descFromFlipkart.scrapReviews(productUrl);

						reviews = reviews.replaceAll("[()]", "");

						Set<String> categories = setCategory.categories(productBrand);

						String description = descFromFlipkart.scrapeDescriptionFromUrl(productUrl);

						ProductInfo existingProduct = repository.findByProductUrl(productUrl);
						if (existingProduct != null) {
							existingProduct.setName(productName);
							existingProduct.setBrandName(productBrand);
							existingProduct.setPrice(productPrice);
							existingProduct.setRating(productRating);
							existingProduct.setReview(reviews);
							existingProduct.setImgUrl(imgUrl);
							existingProduct.setDescription(description);
						} else {
							ProductInfo product = new ProductInfo();
							product.setProductUrl(productUrl);
							product.setName(productName);
							product.setBrandName(productBrand);
							product.setPrice(productPrice);
							product.setRating(productRating);
							product.setImgUrl(imgUrl);
							product.setReview(reviews);
							product.setCompanyName("Flipkart");
							product.setDescription(description);

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
