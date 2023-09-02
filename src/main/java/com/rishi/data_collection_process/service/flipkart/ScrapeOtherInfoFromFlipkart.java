package com.rishi.data_collection_process.service.flipkart;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ScrapeOtherInfoFromFlipkart {
	public String scrapeDescriptionFromUrl(String url) throws IOException {
		Document document = Jsoup.connect(url).get();

		Elements box = document.select("div._2418kt");

		StringBuilder descriptionBuilder = new StringBuilder();
		for (Element element : box.select("li._21Ahn-")) {
			descriptionBuilder.append(element.text()).append("\n");
		}
		return descriptionBuilder.toString();
	}

	public String scrapRating(String url) throws IOException {
		Document document = Jsoup.connect(url).get();

		String rating = document.select("div._3LWZlK._3uSWvT").text();
		return rating;
	}

	public String scrapReviews(String url) throws IOException {
		Document document = Jsoup.connect(url).get();

		Element reviewElement = document.selectFirst("span._2_R_DZ");
		String reviews = reviewElement != null ? reviewElement.text() : "";
		if (reviewElement != null) {
			String[] parts = reviews.split(" ");
			if (parts.length > 0) {
				reviews = parts[3];
			}
		}

		return reviews;
	}
}
