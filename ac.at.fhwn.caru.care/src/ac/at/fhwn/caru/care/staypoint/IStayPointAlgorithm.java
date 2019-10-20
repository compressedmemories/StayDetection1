package ac.at.fhwn.caru.care.staypoint;

import java.util.Collection;

import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;

public interface IStayPointAlgorithm {
	public Collection<IStayPoint> detectStayPoints(Collection<ISpatioTemporalPoint> points);
}
