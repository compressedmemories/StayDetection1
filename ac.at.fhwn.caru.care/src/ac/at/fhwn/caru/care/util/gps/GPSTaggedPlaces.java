package ac.at.fhwn.caru.care.util.gps;

import java.time.Duration;
import java.time.LocalDateTime;

import ac.at.fhwn.caru.care.evaluation.ITaggedPlaces;
import ac.at.fhwn.caru.care.util.IPoint;

public class GPSTaggedPlaces extends GPSStayPoint implements ITaggedPlaces {
	
	private int id;

	public GPSTaggedPlaces(double longitude, double latitude, double altitude, LocalDateTime aTimeStamp, LocalDateTime bTimeStamp, int id) {
		super(longitude, latitude, altitude, aTimeStamp, bTimeStamp);
		this.id = id;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public IPoint getPoint() {
		return new GPSPoint(this.getX(), this.getY(), this.getZ());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GPSTaggedPlaces) {
			if (obj != null && this.getClass().equals(obj.getClass())) {
				GPSTaggedPlaces gps = (GPSTaggedPlaces) obj;
				if (gps.getX() == this.getX() && gps.getY() == this.getY() && gps.getZ() == this.getZ() && gps.getInTimeStamp() == this.getInTimeStamp() && gps.getOutTimeStamp() == this.getOutTimeStamp()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) ((int) (this.getX() + this.getY() + this.getZ()) * 1e10) +(int) Duration.between(this.getInTimeStamp(), this.getOutTimeStamp()).toMillis();
	}
}
