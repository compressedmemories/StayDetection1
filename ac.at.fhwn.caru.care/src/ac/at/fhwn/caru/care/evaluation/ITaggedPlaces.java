package ac.at.fhwn.caru.care.evaluation;

import ac.at.fhwn.caru.care.util.IPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;

public interface ITaggedPlaces extends IStayPoint {
	public int getId();
	public IPoint getPoint();
}
