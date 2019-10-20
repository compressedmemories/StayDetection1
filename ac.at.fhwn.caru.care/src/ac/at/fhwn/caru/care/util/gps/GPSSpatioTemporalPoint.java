package ac.at.fhwn.caru.care.util.gps;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import ac.at.fhwn.caru.care.util.IPoint;
import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;

public class GPSSpatioTemporalPoint extends GPSPoint implements ISpatioTemporalPoint {

	private LocalDateTime timeStamp;
	
	public GPSSpatioTemporalPoint(double longitude, double latitude, double altitude, LocalDateTime timeStamp) {
		super(longitude, latitude, altitude);
		this.timeStamp = timeStamp;
	}

	@Override
	public LocalDateTime getTimeStamp() {
		return this.timeStamp;
	}

	@Override
	public IStayPoint getStayPoint(ISpatioTemporalPoint stp) {
		Collection<ISpatioTemporalPoint> cstp = new ArrayList<ISpatioTemporalPoint>();
		
		cstp.add(stp);
		cstp.add(this);
		return this.getStayPointFromList(cstp);
	}
	
	@Override
	public IStayPoint getStayPoint(Collection<ISpatioTemporalPoint> stps) {
		stps.add(this);
		
		return this.getStayPointFromList(stps);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GPSSpatioTemporalPoint) {
			if (obj != null && this.getClass().equals(obj.getClass())) {
				GPSSpatioTemporalPoint gps = (GPSSpatioTemporalPoint) obj;
				if (gps.getX() == this.getX() && gps.getY() == this.getY() && gps.getZ() == this.getZ() && gps.getTimeStamp().equals(this.getTimeStamp())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private IStayPoint getStayPointFromList(Collection<ISpatioTemporalPoint> stps) {
		Collection<IPoint> cstps = new ArrayList<IPoint>();
		
		for (ISpatioTemporalPoint stp : stps) {
			cstps.add(stp);
		}
		
		GPSPoint gpspoint = (GPSPoint) IPoint.centerOf(cstps);				
		LocalDateTime min = LocalDateTime.MAX;
		LocalDateTime max = LocalDateTime.MIN;
		
		for (ISpatioTemporalPoint itp : stps) {
			if (itp.getTimeStamp().isBefore(min)) {
				min = itp.getTimeStamp();
			}
			
			if (itp.getTimeStamp().isAfter(max)) {
				max = itp.getTimeStamp();
			}
		}
		
		return new GPSStayPoint(gpspoint.getX(), gpspoint.getY(), gpspoint.getZ(), min, max);
	}
}
