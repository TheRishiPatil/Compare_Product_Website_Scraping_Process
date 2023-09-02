package com.rishi.data_collection_process.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.data_collection_process.service.ScrapeData;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ScrapeData productService;

	@GetMapping("/scrape")
	public String scrapeAndSaveProducts() {
		productService.saveData();
		return "Scraping and saving products completed!";
	}

}