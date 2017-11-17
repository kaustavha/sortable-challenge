package me.kaustav.sortable;

public class Main {
    public static void main(String[] args) {
		ProductMap pm;
    	// CLI args: products listings
    	if (args.length == 2) {
    		String prodsPath = args[0];
    		String listingsPath = args[1];
			pm = new ProductMap(prodsPath, listingsPath);
		} else {
			pm = new ProductMap();

			pm.populateProducts(pm.readStream(
				ClassLoader.getSystemClassLoader().getResourceAsStream("products.txt")
			));

			pm.populateListings(pm.readStream(
				ClassLoader.getSystemClassLoader().getResourceAsStream("listings.txt")
			));
		}
		pm.writeOutput(pm.listingsToJson());
    }
}
