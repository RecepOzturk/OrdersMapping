import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;



public class Main extends PApplet{

	private static final long serialVersionUID = 1L;
	private UnfoldingMap map;
	public static String mbTilesString = "data/blankLight-1-3.mbtiles";

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	public static ArrayList<Order> Orders = new ArrayList<>();
	public static ArrayList<Seller> Sellers = new ArrayList<>();

	Seller kirmiziSeller = new Seller("kirmizi", 41.049792, 29.003031, 20, 30);
	Seller yesilSeller = new Seller("yesil", 41.069940, 29.019250, 35, 50);
	Seller maviSeller = new Seller("mavi", 41.049997, 29.026108, 20, 80);


	public void setup() {

		// Create Map
		size(900, 600, OPENGL);
		int zoomLevel = 13;
		map = new UnfoldingMap(this, 150, 50, 700, 500, new Microsoft.RoadProvider());
		MapUtils.createDefaultEventDispatcher(this, map);  // }
		map.zoomAndPanTo(zoomLevel, yesilSeller.getLoc());

		addOrders();
		Sellers.add(kirmiziSeller);
		Sellers.add(yesilSeller);
		Sellers.add(maviSeller);

		findOrders();
		arrangementMin();
		arrangementMax();
		
		printInformation();
	}

	public void draw() {
		List<Marker> orderMarkers = new ArrayList<Marker>();
		for (int i = 0; i < Orders.size(); i++) {
			SimplePointMarker orderMarker = new SimplePointMarker(Orders.get(i).getLoc());

			if (Orders.get(i).getSellerId() == 0) {
				orderMarker.setColor(color(255, 0, 0));
			}else if (Orders.get(i).getSellerId() == 1){
				orderMarker.setColor(color(0, 255, 0));
			}else if (Orders.get(i).getSellerId() == 2){
				orderMarker.setColor(color(0, 0, 255));
			}
			orderMarkers.add(orderMarker);
		}

		map.addMarkers(orderMarkers);

		SellerMarker sellerMarker = new SellerMarker(kirmiziSeller.getLoc(), kirmiziSeller);
		sellerMarker.setStrokeWeight(8);
		map.addMarkers(sellerMarker);
		
		sellerMarker = new SellerMarker(yesilSeller.getLoc(), yesilSeller);
		sellerMarker.setStrokeWeight(8);
		map.addMarkers(sellerMarker);

		sellerMarker = new SellerMarker(maviSeller.getLoc(), maviSeller);
		sellerMarker.setStrokeWeight(8);
		map.addMarkers(sellerMarker);  // }
		
		addKey();
		map.draw();
	}

	public void findOrders(){

		for (int i = 0; i < Orders.size(); i++) {
			Order o = Orders.get(i);
			Location orderLoc = o.getLoc();
			double minDistance = Double.MAX_VALUE;
			int sellerIndex = -1;

			if (o.getSellerId() == -1) {
				for (int j = 0; j < Sellers.size(); j++) {
					Seller s = Sellers.get(j);
					Location selLoc = s.getLoc();
					double difference = orderLoc.getDistance(selLoc);

					if (difference < minDistance) {
						minDistance = difference;
						sellerIndex = j;
					}
				}
				Orders.get(i).setSellerId(sellerIndex);
				Sellers.get(sellerIndex).addOrder(Orders.get(i));
			}
		}
	}

	public void arrangementMin(){

		for (int i = 0; i < Sellers.size(); i++) {
			Seller s = Sellers.get(i);
			while (s.getOrders().size() < s.getMin()) {
				double minDistance = Double.MAX_VALUE;
				int orderIndex = -1;
				int sellerIndex = -1;
				for (int j = 0; j < Orders.size(); j++) {
					Order o =  Orders.get(j);
					if (o.getSellerId() != i &&
							(Sellers.get(o.getSellerId()).getMin()) 
							<= Sellers.get(o.getSellerId()).getOrders().size()-1) {
						double difference = o.getLoc().getDistance(s.getLoc());
						if (difference < minDistance) {
							minDistance = difference;
							orderIndex = j;
							sellerIndex = o.getSellerId();
						}
					}
				}
				if (orderIndex != -1) {
					Orders.get(orderIndex).setSellerId(i);
					s.addOrder(Orders.get(orderIndex));
					Sellers.get(sellerIndex).getOrders().remove(Orders.get(orderIndex));
				}
			}
		}
	}

	public void arrangementMax(){
		
		for (int i = 0; i < Sellers.size(); i++) {
			Seller s = Sellers.get(i);
			
			while (s.getOrders().size() > s.getMax()) {
				
				double minDistance = Double.MAX_VALUE;
				int orderIndex = -1;
				int sellerIndex = -1;
				
				for (int j = 0; j < Sellers.size(); j++) {
					Seller s2 = Sellers.get(j);
					minDistance = Double.MAX_VALUE;
					orderIndex = -1;
					sellerIndex = -1;
					
					if (!s.getName().equals(s2.getName()) &&
							s2.getOrders().size() < s2.getMax()) {

						for (int k = 0; k < s.getOrders().size(); k++) {
							Order o = s.getOrder(k);
							double difference = s2.getLoc().getDistance(o.getLoc());
							if (difference < minDistance) {
								minDistance = difference;
								orderIndex = k;
								sellerIndex = j;
							}
						}
						if (orderIndex != -1) {
							s.getOrder(orderIndex).setSellerId(sellerIndex);
							s2.addOrder(s.getOrder(orderIndex));
							s.getOrders().remove(orderIndex);
						}
					}
				}
				
			}
		}
	}

	public void printInformation(){
		for (int i = 0; i < Sellers.size(); i++) {
			Seller s = Sellers.get(i);
			String orders = " ";
			for (int j = 0; j < s.getOrders().size(); j++) {
				orders += Integer.toString(s.getOrder(j).getId()) + " ";
			}
			System.out.println(s.getName() + " bayi için sipariþler : " + orders);
		}
	}
	
	public void addOrders(){
		Orders.add(new Order(100, 41.078260000000, 29.015770000000)); 
		Orders.add(new Order(101, 41.060780000000, 29.010830000000));
		Orders.add(new Order(102, 41.064160000000, 29.009970000000));
		Orders.add(new Order(103, 41.085290000000, 29.019790000000));
		Orders.add(new Order(104, 41.041840000000, 29.002990000000));
		Orders.add(new Order(105, 41.085500000000, 29.008710000000));
		Orders.add(new Order(106, 41.085040000000, 29.017570000000));
		Orders.add(new Order(107, 41.052210000000, 29.033230000000));
		Orders.add(new Order(108, 41.081170000000, 29.039800000000));
		Orders.add(new Order(109, 41.085960000000, 29.033490000000));
		Orders.add(new Order(110, 41.060720000000, 29.011340000000));
		Orders.add(new Order(111, 41.070020000000, 29.017650000000));
		Orders.add(new Order(112, 41.080520000000, 29.012240000000));
		Orders.add(new Order(113, 41.046150000000, 29.012040000000));
		Orders.add(new Order(114, 41.051280000000, 29.003100000000));
		Orders.add(new Order(115, 41.054340000000, 29.031720000000));
		Orders.add(new Order(116, 41.078260000000, 29.015770000000));
		Orders.add(new Order(117, 41.061770000000, 29.037880000000));
		Orders.add(new Order(118, 41.061480000000, 29.004810000000));
		Orders.add(new Order(119, 41.046830000000, 29.003130000000));
		Orders.add(new Order(120, 41.042900000000, 29.000040000000));
		Orders.add(new Order(121, 41.045500000000, 28.999780000000));
		Orders.add(new Order(122, 41.051280000000, 29.003100000000));
		Orders.add(new Order(123, 41.045500000000, 28.999780000000));
		Orders.add(new Order(124, 41.059380000000, 29.000360000000));
		Orders.add(new Order(125, 41.077080000000, 29.031190000000));
		Orders.add(new Order(126, 41.047130000000, 29.009110000000));
		Orders.add(new Order(127, 41.079980000000, 29.021330000000));
		Orders.add(new Order(128, 41.064160000000, 29.009970000000));
		Orders.add(new Order(129, 41.070920000000, 29.042000000000));
		Orders.add(new Order(130, 41.083220000000, 29.013630000000));
		Orders.add(new Order(131, 41.066040000000, 29.007370000000));
		Orders.add(new Order(132, 41.080850000000, 29.010360000000));
		Orders.add(new Order(133, 41.043780000000, 29.009960000000));
		Orders.add(new Order(134, 41.064160000000, 29.009970000000));
		Orders.add(new Order(135, 41.066000000000, 29.040690000000));
		Orders.add(new Order(136, 41.086880000000, 29.017910000000));
		Orders.add(new Order(137, 41.051750000000, 28.990540000000));
		Orders.add(new Order(138, 41.059280000000, 29.012050000000));
		Orders.add(new Order(139, 41.079220000000, 29.010440000000));
		Orders.add(new Order(140, 41.054610000000, 29.027400000000));
		Orders.add(new Order(141, 41.063070000000, 29.010960000000));
		Orders.add(new Order(142, 41.075950000000, 29.015770000000));
		Orders.add(new Order(143, 41.043780000000, 29.009960000000));
		Orders.add(new Order(144, 41.067660000000, 29.043050000000));
		Orders.add(new Order(145, 41.056190000000, 28.999960000000));
		Orders.add(new Order(146, 41.079190000000, 29.045160000000));
		Orders.add(new Order(147, 41.077080000000, 29.031190000000));
		Orders.add(new Order(148, 41.057970000000, 29.016160000000));
		Orders.add(new Order(149, 41.092640000000, 29.018040000000));
		Orders.add(new Order(150, 41.060780000000, 29.010830000000));
		Orders.add(new Order(151, 41.077360000000, 29.030970000000));
		Orders.add(new Order(152, 41.048580000000, 29.021250000000));
		Orders.add(new Order(153, 41.053072478048, 28.987209066283));
		Orders.add(new Order(154, 41.083100000000, 29.015150000000));
		Orders.add(new Order(155, 41.060780000000, 29.010830000000));
		Orders.add(new Order(156, 41.052430000000, 28.990450000000));
		Orders.add(new Order(157, 41.063710000000, 29.010420000000));
		Orders.add(new Order(158, 41.083290000000, 29.028520000000));
		Orders.add(new Order(159, 41.053670000000, 28.989390000000));
		Orders.add(new Order(160, 41.076840000000, 29.015430000000));
		Orders.add(new Order(161, 41.056950000000, 29.012310000000));
		Orders.add(new Order(162, 41.078880000000, 29.030180000000));
		Orders.add(new Order(163, 41.067730000000, 29.020290000000));
		Orders.add(new Order(164, 41.063070000000, 29.010960000000));
		Orders.add(new Order(165, 41.063070000000, 29.010960000000));
		Orders.add(new Order(166, 41.045950000000, 28.999860000000));
		Orders.add(new Order(167, 41.076840000000, 29.015430000000));
		Orders.add(new Order(168, 41.061600000000, 29.000460000000));
		Orders.add(new Order(169, 41.064380000000, 29.001360000000));
		Orders.add(new Order(170, 41.056890000000, 28.987130000000));
		Orders.add(new Order(171, 41.074350000000, 29.039250000000));
		Orders.add(new Order(172, 41.051820000000, 29.005970000000));
		Orders.add(new Order(173, 41.058420000000, 29.007200000000));
		Orders.add(new Order(174, 41.052080000000, 29.007210000000));
		Orders.add(new Order(175, 41.078910000000, 29.021220000000));
		Orders.add(new Order(176, 41.057180000000, 29.030470000000));
		Orders.add(new Order(177, 41.066040000000, 29.007370000000));
		Orders.add(new Order(178, 41.087650000000, 29.007350000000));
		Orders.add(new Order(179, 41.083680000000, 29.014990000000));
		Orders.add(new Order(180, 41.089100000000, 29.035370000000));
		Orders.add(new Order(181, 41.084760000000, 29.013510000000));
		Orders.add(new Order(182, 41.041840000000, 29.002990000000));
		Orders.add(new Order(183, 41.061950000000, 29.011850000000));
		Orders.add(new Order(184, 41.053340000000, 29.003820000000));
		Orders.add(new Order(185, 41.074380000000, 29.018530000000));
		Orders.add(new Order(186, 41.080440000000, 29.015540000000));
		Orders.add(new Order(187, 41.061130000000, 28.998080000000));
		Orders.add(new Order(188, 41.060780000000, 29.010830000000));
		Orders.add(new Order(189, 41.075650000000, 29.020470000000));
		Orders.add(new Order(190, 41.076840000000, 29.015430000000));
		Orders.add(new Order(191, 41.048590000000, 29.021570000000));
		Orders.add(new Order(192, 41.066040000000, 29.007370000000));
		Orders.add(new Order(193, 41.078476490079, 29.009396156746));
		Orders.add(new Order(194, 41.079370000000, 29.011590000000));
		Orders.add(new Order(195, 41.084650000000, 29.020610000000));
		Orders.add(new Order(196, 41.066040000000, 29.007370000000));
		Orders.add(new Order(197, 41.066440000000, 29.004270000000));
		Orders.add(new Order(198, 41.072770000000, 29.016660000000));
		Orders.add(new Order(199, 41.049030000000, 29.023410000000));

	}

	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		rect(25, 50, 80, 160);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Map Key", 40, 75);
		
		fill(color(255, 255, 0));
		triangle(35, 120, 40, 110, 45, 120);
		fill(color(255, 255, 0));
		ellipse(42, 160, 10, 10);
	
		
		fill(0, 0, 0);
		textSize(11);
		text("Seller", 60, 115);
		text("Order", 60, 160);
	}
	
	public void readData(){
		/*File excelFile = new File("siparis ve bayi koordinatlarÄ±.xlsx");

        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // we get first sheet
        XSSFSheet sheet = workbook.getSheetAt(0);

        // we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();

        Row row = rowIt.next();

        while(rowIt.hasNext()) {

          row = rowIt.next();


          // iterate on cells for the current row
          Iterator<Cell> cellIterator = row.cellIterator();

          while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            
            System.out.print(cell.toString() + ";");

          }

          //order.add(orders);

          //System.out.println(order);
          System.out.println();
        }

        workbook.close();
        fis.close();*/
	}
}



