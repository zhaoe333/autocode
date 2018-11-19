package com.template.location;

import java.util.Arrays;

public class LocationUtils {
	private static double a = 6378245.0;
	private static double ee = 0.00669342162296594323;
	private static final double x_pi = 3000.0 * Math.PI / 180.0;

	public static double GPS2WGS84(double val) {
		int d = (int) Math.floor(val);
		double f = val - d;
		return d + f * 100 / 60;
	}

	public static double[] transformFromGCJToBD09(double latitude, double longitude) {
		double z = Math.sqrt(longitude * longitude + latitude * latitude) + 0.00002 * Math.sin(latitude * x_pi);
		double theta = Math.atan2(latitude, longitude) + 0.000003 * Math.cos(longitude * x_pi);
		double lon = z * Math.cos(theta) + 0.0065;
		double lat = z * Math.sin(theta) + 0.006;
		return new double[] { lat, lon };
	}

	public static double[] transformFromBD09ToGCJ(double latitude, double longitude) {
		double x = longitude - 0.0065, y = latitude - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double lon = z * Math.cos(theta);
		double lat = z * Math.sin(theta);
		return new double[] { lat, lon };
	}

	public static double[] transformFromWGSToGCJ(double latitude, double longitude) {

		// 如果在国外，则默认不进行转换
		if (outOfChina(latitude, longitude)) {
			return new double[] { latitude, longitude };
		}
		double dLat = transformLat(longitude - 105.0, latitude - 35.0);
		double dLon = transformLon(longitude - 105.0, latitude - 35.0);
		double radLat = latitude / 180.0 * Math.PI;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);

		return new double[] { latitude + dLat, longitude + dLon };
	}

	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(x > 0 ? x : -x);
		ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(x > 0 ? x : -x);
		ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
		return ret;
	}

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	// 经纬度转墨卡托
	// 经度(lon)，纬度(lat)
	public static double[] lonLat2Mercator(double lon, double lat) {
		double[] xy = new double[2];
		double x = lon * 20037508.342789 / 180;
		double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
		y = y * 20037508.34789 / 180;
		xy[0] = x;
		xy[1] = y;
		return xy;
	}

	// 墨卡托转经纬度

	public static double[] mercator2lonLat(double mercatorX, double mercatorY) {
		double[] xy = new double[2];
		double x = mercatorX / 20037508.34 * 180;
		double y = mercatorY / 20037508.34 * 180;
		y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
		xy[0] = x;
		xy[1] = y;
		return xy;
	}

	public static void main(String[] args) {
		double lon = 12515.2725225;
		double lat = 4352.2935506;
		lon = test(lon);
		lat = test(lat);
		System.out.println(lon+","+lat);
		lon = 12515.2725225;
		lat = 4352.2935506;
		lon = GPS2WGS84(lon);
		lat = GPS2WGS84(lat);
		System.out.println(lon+","+lat);
//		System.out.println(Arrays.toString(transformFromWGSToGCJ(lat, lon)));
//		double[] result = LocationUtils.transformFromGCJToBD09(39.89308773, 116.48772963);
//		System.out.println(Arrays.toString(LocationUtils.transformFromBD09ToGCJ(result[0], result[1])));
	}

	public static double test(double location) {
		int lon = ((int) location) / 100;
		return (location - lon * 100) / 60 + lon;
	}
}
