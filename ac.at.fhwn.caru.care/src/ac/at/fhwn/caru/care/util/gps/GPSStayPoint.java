package ac.at.fhwn.caru.care.util.gps;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import ac.at.fhwn.caru.care.util.IPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;

public class GPSStayPoint extends GPSPoint implements IStayPoint {

	private LocalDateTime inTimeStamp;
	private LocalDateTime outTimeStamp;
	
	public GPSStayPoint(double longitude, double latitude, double altitude, LocalDateTime aTimeStamp, LocalDateTime bTimeStamp) {
		super(longitude, latitude, altitude);
		this.setInOutTime(aTimeStamp, bTimeStamp);
	}
	
	private void setInOutTime( LocalDateTime aTimeStamp, LocalDateTime bTimeStamp) {
		if (aTimeStamp.isBefore(bTimeStamp)) {
			this.inTimeStamp = aTimeStamp;
			this.outTimeStamp = bTimeStamp;
		} else {
			this.inTimeStamp = bTimeStamp;
			this.outTimeStamp = aTimeStamp;
		}
	}

	@Override
	public LocalDateTime getInTimeStamp() {
		return this.inTimeStamp;
	}

	@Override
	public LocalDateTime getOutTimeStamp() {
		return this.outTimeStamp;
	}

	@Override
	public IStayPoint centroidOf(Collection<IStayPoint> points) {
		Collection<IPoint> temp = new ArrayList<IPoint>();
		LocalDateTime min = LocalDateTime.MAX;
		LocalDateTime max = LocalDateTime.MIN;
		
		for (IStayPoint sp : points) {
			temp.add(sp);
			if (sp.getInTimeStamp().isBefore(min)) {
				min = sp.getInTimeStamp();
			}
			
			if (sp.getInTimeStamp().isAfter(max)) {
				max = sp.getInTimeStamp();
			}
		}
		
		GPSPoint point = (GPSPoint) IPoint.centerOf(temp);
		return new GPSStayPoint(point.getX(), point.getY(), point.getY(), min, max);
	}

	@Override
	public double distanceFrom(IStayPoint point) {
		return super.distanceFrom(point);
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GPSStayPoint) {
			if (obj != null && this.getClass().equals(obj.getClass())) {
				GPSStayPoint gps = (GPSStayPoint) obj;
				if (gps.getX() == this.getX() && gps.getY() == this.getY() && gps.getZ() == this.getZ() && gps.getInTimeStamp().isEqual(this.getInTimeStamp()) && gps.getOutTimeStamp().isEqual(this.getOutTimeStamp())) {
					return true;
				}
			}
		}
		
		return false;
	}
}
