package ac.at.fhwn.caru.care.util.gps;

import ac.at.fhwn.caru.care.util.IPoint;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

public class GPSPoint implements IPoint {
	
	private double longitude;
	private double latitude;
	private double altitude;

	public GPSPoint(double longitude, double latitude, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}	

	@Override
	public double distanceFrom(IPoint point) {	
		WayPoint wp1 = WayPoint.of(this.latitude, this.longitude, (long) this.altitude);
		WayPoint wp2 = WayPoint.of(point.getY(), point.getX(), (long)point.getZ());
		return Geoid.WGS84.distance(wp1, wp2).doubleValue();
	}

	@Override
	public double getX() {
		return this.longitude;
	}

	@Override
	public double getY() {
		return this.latitude;
	}

	@Override
	public double getZ() {
		return this.altitude;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GPSPoint) {
			if (obj != null && this.getClass().equals(obj.getClass())) {
				GPSPoint gps = (GPSPoint) obj;
				if (gps.getX() == this.getX() && gps.getY() == this.getY() && gps.getZ() == this.getZ()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) ((int) (this.getX() + this.getY() + this.getZ()) * 1e10);
	}
}
