package ac.at.fhwn.caru.care.util.gps;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import ac.at.fhwn.caru.care.util.ICluster;
import ac.at.fhwn.caru.care.util.IPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;

public class GPSCluster implements ICluster {
	
	private Collection<IStayPoint> clusterPoints;
	
	public GPSCluster(Collection<IStayPoint> clusterPoints) {
		this.clusterPoints = clusterPoints;
	}

	@Override
	public Collection<IStayPoint> getStayPoints() {
		return this.clusterPoints;
	}
	
	@Override
	public IPoint getCenter() {
		Collection<IPoint> points = new ArrayList<IPoint>();
		for (IStayPoint sp : this.clusterPoints) {
			points.add(new GPSPoint(sp.getX(), sp.getY(), sp.getZ()));
		}
		
		return IPoint.centerOf(points);
	}

	@Override
	public LocalDateTime getInTimeStamp() {
		LocalDateTime result = LocalDateTime.MAX;
		for (IStayPoint sp : this.clusterPoints) {
			if (sp.getInTimeStamp().isBefore(result)) {
				result = sp.getInTimeStamp();
			}
		}
		
		return result;
	}

	@Override
	public LocalDateTime getOutTimeStamp() {
		LocalDateTime result = LocalDateTime.MIN;
		for (IStayPoint sp : this.clusterPoints) {
			if (sp.getOutTimeStamp().isAfter(result)) {
				result = sp.getOutTimeStamp();
			}
		}
		
		return result;
	}

	@Override
	public IPoint getNearestStayPoint(IPoint p) {
		IPoint result = null;
		double dist = Double.MAX_VALUE;
		
		for (IStayPoint sp : this.clusterPoints) {
			double tempdist = sp.distanceFrom(p);
			
			if (tempdist < dist) {
				result = sp;
				dist = tempdist;
			}
		}
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GPSCluster) {
			if (obj != null && this.getClass().equals(obj.getClass())) {
				GPSCluster gps = (GPSCluster) obj;
				if (gps.getCenter().equals(this.getCenter()) && gps.getInTimeStamp().isEqual(this.getInTimeStamp()) && gps.getOutTimeStamp().isEqual(this.getOutTimeStamp())) {
					return true;
				}
			}
		}
		
		return false;
	}
}
