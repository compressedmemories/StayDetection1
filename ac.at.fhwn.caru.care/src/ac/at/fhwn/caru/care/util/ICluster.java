package ac.at.fhwn.caru.care.util;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ICluster {
	public Collection<IStayPoint> getStayPoints();
	public IPoint getCenter();
	public IPoint getNearestStayPoint(IPoint p);
	public LocalDateTime getInTimeStamp();
    public LocalDateTime getOutTimeStamp();
}
