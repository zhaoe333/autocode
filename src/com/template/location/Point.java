package com.template.location;

public class Point {
	private double lon;
	private double lat;
	private long loc_time;
	
	public Point() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Point(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
	}
	public Point(double lon, double lat,long loc_time) {
		super();
		this.lon = lon;
		this.lat = lat;
		this.loc_time = loc_time;
	}
	public Point(double[] latlon,long loc_time) {
		super();
		this.lon = latlon[1];
		this.lat = latlon[0];
		this.loc_time = loc_time;
	}
	/**
	 * 1 wgs to bd
	 * 2 gct to bd
	 * @param type
	 */
	public void build(String type){
		double[] latlon;
		switch (type){
			case "1":
				latlon = LocationUtils.transformFromWGSToGCJ(this.lat, this.lon);
				latlon = LocationUtils.transformFromGCJToBD09(latlon[0], latlon[1]);
				this.lat = latlon[0];
				this.lon = latlon[1];
				break;
			case "2":
				latlon = LocationUtils.transformFromGCJToBD09(lat, lon);
				this.lat = latlon[0];
				this.lon = latlon[1];
				break;
			default:
				break;
		}
		
	}
	
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public long getLoc_time() {
		return loc_time;
	}
	public void setLoc_time(long loc_time) {
		this.loc_time = loc_time;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		Point point = (Point)obj;
		return this.lat==point.lat&&this.lon==point.lon;
	}
}
