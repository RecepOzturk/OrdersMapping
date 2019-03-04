import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

public class SellerMarker extends SimplePointMarker{
	public static int TRI_SIZE = 5;
	public int id = 0;
	public SellerMarker(Location location, Seller seller) {
		super(location);
		if(seller.getName().equals("kirmizi")){
			id = 0;
		}else if(seller.getName().equals("yesil")){
			id = 1;
		}else if(seller.getName().equals("mavi")){
			id = 2;
		}
	}
	
	public void draw(PGraphics pg, float x, float y) {
		// Save previous drawing style
		pg.pushStyle();
		
		switch (id) {
		case 0:
			pg.fill(255, 0, 0);
			break;
		case 1:
			pg.fill(0, 255, 0);
			break;
		case 2:
			pg.fill(0, 0, 255);
			break;
		default:
			pg.fill(255, 255, 255);
			break;
		}
		
		pg.triangle(x, y-TRI_SIZE, x-TRI_SIZE, y+TRI_SIZE, x+TRI_SIZE, y+TRI_SIZE);
		
		// Restore previous drawing style
		pg.popStyle();
	}
	
	
}
