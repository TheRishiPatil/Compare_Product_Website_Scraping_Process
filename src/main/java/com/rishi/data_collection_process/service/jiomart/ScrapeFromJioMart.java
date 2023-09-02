package com.rishi.data_collection_process.service.jiomart;

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
public class ScrapeFromJioMart {
	@Autowired
	private ProductRepository repository;

	@Autowired
	private ScrapeOtherInfoFromJioMart infoFromJioMart;

	@Autowired
	private SetCategory setCategory;

	public void scrapeAndSave() {
		int i = 0;
		ClassLoader classLoader = getClass().getClassLoader();
		File input = new File(classLoader.getResource("jiomartWebsites/jiomart" + ".html").getFile());

		try {

			Document document = Jsoup.parse(input, "UTF-8");

			Elements box = document.select("div.ais-InfiniteHits");

			for (Element element : box.select("li.ais-InfiniteHits-item.jm-col-4.jm-mt-base")) {
				try {

					System.out.println("Jiomart " + i++);
					String productUrl = "https://www.jiomart.com"
							+ element.select("li.ais-InfiniteHits-item a.plp-card-wrapper").first().attr("href");
					String productName = element
							.select("li.ais-InfiniteHits-item a.plp-card-wrapper.plp_product_list.viewed").first()
							.attr("title");
					String productBrand = productName.split(" ")[0];

					String productPriceText = element.select("span.jm-heading-xxs").first().text().replaceAll("[^\\d.]",
							"");
					double productPriceFloat = Double.parseDouble(productPriceText);
					int productPrice = (int) productPriceFloat;

					String productRating = infoFromJioMart.scrapeRatingFromUrl(productUrl);
					String imgUrl = element.select("img.lazyautosizes.lazyloaded").first().attr("src");
					String reviews = infoFromJioMart.scrapeReviewFromUrl(productUrl);

					reviews = reviews.replaceAll("[()]", "");

					String description = infoFromJioMart.scrapeDescriptionFromUrl(productUrl);

					Set<String> categories = setCategory.categories(productBrand);

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
						product.setCompanyName("JioMart");
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
		} catch (IOException e) {
		}
	}
}
