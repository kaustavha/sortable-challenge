import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

import me.kaustav.sortable.*;

public class UnitTests {
	InputStream getRes(String name) {
		return ClassLoader.getSystemClassLoader().getResourceAsStream(name);
	}

	@Test 
	public void testInsertProduct() {
		Product prodObj = new Product();
		prodObj.product_name = "Sony_Cyber-shot_DSC-W310";
		prodObj.model = "DSC-W310";
		prodObj.family = "Cyber-shot";
		prodObj.manufacturer = "sony";
		prodObj.announced_date = "2010-01-06T19:00:00.000-05:00";
		ProductMap pm = new ProductMap();
		pm.insertProduct(prodObj);
		assertTrue(pm.products.containsKey("sony"));
		List<Product> prods = pm.products.get("sony");
		Product prod = prods.get(0);
		assertEquals(prod.product_name, "Sony_Cyber-shot_DSC-W310");
		assertEquals(prod.model, "DSC-W310");
		assertEquals(prod.family, "Cyber-shot");
		assertEquals(prod.announced_date, "2010-01-06T19:00:00.000-05:00");
		assertEquals(prod.listings.size(), 0);
	}

	@Test
	public void testJsonProductIntake() {
		ProductMap pm = new ProductMap();
		
		pm.populateProducts(pm.readStream(getRes("prods1.txt")));
		assertTrue(pm.products.containsKey("sony"));
		List<Product> prods = pm.products.get("sony");
		Product prod = prods.get(0);
		assertEquals(prod.product_name, "Sony_Cyber-shot_DSC-W310");
		assertEquals(prod.model, "DSC-W310");
		assertEquals(prod.family, "Cyber-shot");
		assertEquals(prod.announced_date, "2010-01-06T19:00:00.000-05:00");
		assertEquals(prod.listings.size(), 0);

		assertFalse(pm.products.containsKey("Panasonic"));
		pm.printListings();
	}

	@Test
	public void testInsertListing() {
		Listing listObj = new Listing();
		listObj.title = "DSC-W310 sony cyber shot for sale";
		listObj.manufacturer = "sony";

		Listing invalidListObj = new Listing();
		invalidListObj.title = "foobar foobar FDSC-W310sonysonycybercloth";
		invalidListObj.manufacturer = "sony";

		ProductMap pm = new ProductMap();
		pm.populateProducts(pm.readStream(getRes("prods1.txt")));
		pm.insertListing(listObj);
		pm.insertListing(invalidListObj);

		List<Product> prods = pm.products.get("sony");
		assertEquals(prods.size(), 1);
		Product prod = prods.get(0);
		assertEquals(prod.listings.size(), 1);
		assertEquals(prod.listings.get(0), listObj);
	}

	@Test
	public void testJsonListingIntake() {
		ProductMap pm = new ProductMap();
		pm.populateProducts(pm.readStream(getRes("prods2.txt")));
		pm.populateListings(pm.readStream(getRes("list1.txt")));

		List<Product> prods = pm.products.get("olympus");
		assertEquals(prods.size(), 1);
		Product prod = prods.get(0);
		assertEquals(prod.listings.size(), 2);
	}

	@Test
	public void testEdgeCases() {
		ProductMap pm = new ProductMap();
		pm.populateProducts(pm.readStream(getRes("prods2.txt")));
		pm.populateListings(pm.readStream(getRes("edgeList1.txt")));

		List<Product> prods = pm.products.get("olympus");
		Product prod = prods.get(0);
		assertEquals(prod.listings.size(), 3);
	}

}