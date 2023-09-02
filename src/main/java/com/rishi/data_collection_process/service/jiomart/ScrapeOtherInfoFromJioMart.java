package com.rishi.data_collection_process.service.jiomart;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ScrapeOtherInfoFromJioMart {

	public String scrapeDescriptionFromUrl(String url) throws IOException {
		Document document = Jsoup.connect(url).get();

		Elements box = document.select("ul.product-key-features-list");

		StringBuilder descriptionBuilder = new StringBuilder();
		for (Element element : box.select("li")) {
			descriptionBuilder.append(element.text()).append("\n");
		}
		return descriptionBuilder.toString();
	}

	public String scrapeReviewFromUrl(String url) throws IOException {
		Document document = Jsoup.connect(url).get();

		Elements reviewDiv = document.select("span.review-count.jm-body-m-bold.jm-fc-primary-60.jm-pl-xs");
		if (reviewDiv != null) {
			String review = reviewDiv.text();
			return review;
		}

		return "0";
	}

	public String scrapeRatingFromUrl(String url) throws IOException {
		Document document = Jsoup.connect(url).get();

		Element ratingDiv = document.select("div.jm-rating-filled div[id]").last();
		if (ratingDiv != null) {
			String rating = ratingDiv.id();
			return rating;
		}

		return "0";
	}
}
