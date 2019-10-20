package ac.at.fhwn.caru.care.util;

import java.util.Collection;

import ac.at.fhwn.caru.care.util.gps.GPSPoint;

public interface IPoint {
	public double getX();
    public double getY();
    public double getZ();
      
    public double distanceFrom(IPoint point);
    
    public static IPoint centerOf(Collection<IPoint> points) {
		IPoint[] tempPoints = points.toArray(new IPoint[0]);
		double  xSum = 0, ySum = 0, zSum = 0;
		
		for (IPoint i : tempPoints) {
			xSum += i.getX();
			ySum += i.getY();
			zSum += i.getZ();
		}
		
		return new GPSPoint(xSum / tempPoints.length, ySum / tempPoints.length, zSum / tempPoints.length);
	}
}
