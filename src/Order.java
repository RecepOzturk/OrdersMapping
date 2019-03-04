import de.fhpotsdam.unfolding.geo.Location;

public class Order {
	private int id;
	private double lat;
	private double lon;
	private int sellerId;
	private Location loc;
	
	public Order(int id, double lat, double lon) {
		super();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.loc = new Location(lat, lon);
		this.sellerId = -1;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public void setLat(int lan) {
		this.lat = lan;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}
	
	
}
