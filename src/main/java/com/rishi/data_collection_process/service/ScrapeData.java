package com.rishi.data_collection_process.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rishi.data_collection_process.service.amazon.ScrapeFromAmazon;
import com.rishi.data_collection_process.service.croma.ScrapeFromCroma;
import com.rishi.data_collection_process.service.flipkart.ScrapeFromFlipkart;
import com.rishi.data_collection_process.service.jiomart.ScrapeFromJioMart;

@Service
public class ScrapeData {

	@Autowired
	private ScrapeFromFlipkart flipkart;

	@Autowired
	private ScrapeFromAmazon amazon;

	@Autowired
	private ScrapeFromCroma croma;

	@Autowired
	private ScrapeFromJioMart jioMart;

	public void saveData() {
		flipkart.scrapeAndSave();
		amazon.scrapeAndSave();
		croma.scrapeAndSave();
		jioMart.scrapeAndSave();
	}
}
