package ac.at.fhwn.caru.care.util;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;

public interface ISpatioTemporalPoint extends IPoint {	
    public LocalDateTime getTimeStamp();
    public IStayPoint getStayPoint(ISpatioTemporalPoint stp);
    public IStayPoint getStayPoint(Collection<ISpatioTemporalPoint> stps);
    
	 public static Comparator<ISpatioTemporalPoint> TimeComparator = new Comparator<ISpatioTemporalPoint>() {
		 public int compare(ISpatioTemporalPoint p1, ISpatioTemporalPoint p2) {
			 return p1.getTimeStamp().compareTo(p2.getTimeStamp());
		 }
	 };
}
