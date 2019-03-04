import java.util.ArrayList;

import de.fhpotsdam.unfolding.geo.Location;

public class Seller {
	
	private String name;
	private ArrayList<Order> orders;
	private int min;
	private int max;
	private Location loc;
	private double totalCost;
	
	public Seller(String name, double lat, double lon, int min, int max) {
		super();
		this.name = name;
		this.loc = new Location(lat, lon);
		this.orders = new ArrayList<Order>(1);
		this.min = min;
		this.max = max;
		this.totalCost = 0;
	}
	
	public void addOrder(Order o){
		this.orders.add(o);
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}
	public Order getOrder(int i) {
		return orders.get(i);
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	
	
}
