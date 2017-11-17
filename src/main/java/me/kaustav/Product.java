package me.kaustav.sortable;

import java.util.List;
import java.util.ArrayList;

public class Product {
	public String product_name;
	public String manufacturer;
	public String model;
	public String family;
	public String announced_date;
	public List<Listing> listings = new ArrayList<>();
}
