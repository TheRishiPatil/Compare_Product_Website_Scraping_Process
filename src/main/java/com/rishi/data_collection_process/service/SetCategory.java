package com.rishi.data_collection_process.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class SetCategory {

	public Set<String> categories(String brand) {
		Set<String> categories = new HashSet<>();
		categories.add("gaming pc cases");
		categories.add("gaming pc cabinets");
		categories.add("gaming pc cabinet");

		categories.add(brand);
		categories.add(brand + " gaming pc case");
		categories.add(brand + " gaming pc cabinets");
		return categories;
	}
}
