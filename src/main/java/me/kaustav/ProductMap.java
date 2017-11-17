package me.kaustav.sortable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductMap {

	public Map<String, List<Product>> products = new HashMap<>();
	public BufferedReader productSource;
	public BufferedReader listingSource;

	public ProductMap(String products, String listings) {
		populateProducts(readFile(products));
		populateListings(readFile(listings));
	}

	public ProductMap(BufferedReader products, BufferedReader listings) {
		populateProducts(products);
		populateListings(listings);
	}

	public ProductMap() {}

	public void insertListing(Listing listing) {
		if (listing.title == null || listing.manufacturer == null) return;
		String listingManufac = listing.manufacturer.toLowerCase();
		listingManufac = listingManufac.split("\\s+")[0]; // strip country name from listings that have it in manufac name
		String listingTitle = listing.title.toLowerCase();

		if (products.containsKey(listingManufac)) {
			List prods = products.get(listingManufac);

			for (int i=0; i<prods.size(); i++) {
				Product prod = (Product)prods.get(i);
				String modelName = prod.model.toLowerCase();
				String modelNameConcat = modelName.replaceAll("\\s+", "");
				if (isMatch(listingTitle, modelName) || isMatch(listingTitle, modelNameConcat)) {
					if (prod.listings == null) prod.listings = new ArrayList<>();
					prod.listings.add(listing);
				}
			}
		}
	}

	boolean isMatch(String listingTitle, String val) {
		return listingTitle.matches(".*\\b"+val+"\\b.*");
	}

	public void insertProduct(Product prod) {
		if (prod.manufacturer == null) return;
		List prods;
		String manufac = prod.manufacturer.toLowerCase();
		if (products.containsKey(manufac)) {
			prods = products.get(manufac);
			prods.add(prod);
		} else {
			prods = new ArrayList();
			prods.add(prod);
		}
		products.put(manufac, prods);
	}

	public String listingsToJson() {
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		for (String key : products.keySet()) {

			for (Product prod : products.get(key)) {

				JsonElement jsonElement = gson.toJsonTree(prod.listings, new TypeToken<List<Listing>>() {}.getType());
				if (! jsonElement.isJsonArray()) jsonElement = new JsonArray();
				JsonArray jsonArr = jsonElement.getAsJsonArray();

				if (jsonArr.size()>0) {
					JsonObject jobject = new JsonObject();
					jobject.addProperty("product_name", prod.product_name);
					jobject.add("listings", jsonArr);

					sb.append(gson.toJson(jobject));
					sb.append('\n');
				}
			}
		}
		return sb.toString();
	}

	void writeOutput(String outStr) {
		try {
			FileWriter fw = new FileWriter("./output.txt", false);
			fw.write(outStr);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void printListings() {
		System.out.println(products.keySet());
		for (String key : products.keySet()) {
			for (Product prod : products.get(key)) {
				System.out.println(prod.product_name);
				if (prod.listings == null) continue;
				for (Listing listing : prod.listings) {
					System.out.println(listing.title);
				}
			}
		}
	}

	public void populateProducts(BufferedReader reader) {
		Gson gson = new Gson();
		try {
			String line = reader.readLine();
			while (line != null) {
				Product prodObj = new Product();
				JsonObject prod = gson.fromJson(line, JsonObject.class);
				prodObj = gson.fromJson(line,Product.class);
				JsonElement announced_date_json = prod.get("announced-date");
				if (announced_date_json != null) prodObj.announced_date = announced_date_json.getAsString();
				insertProduct(prodObj);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void populateListings(BufferedReader reader) {
		Gson gson = new Gson();
		try {
			String line = reader.readLine();
			while (line != null) {
				Listing listObj = gson.fromJson(line, Listing.class);
				insertListing(listObj);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedReader readFile(String name) {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new InputStreamReader(
					new FileInputStream("./" + name)
			));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bf;
	}

	public BufferedReader readStream(InputStream input) {
		return new BufferedReader(new InputStreamReader(input));
	}

	public BufferedReader readDefault(String name) {
		return readStream(ClassLoader.getSystemClassLoader().getResourceAsStream(name));
	}
}

