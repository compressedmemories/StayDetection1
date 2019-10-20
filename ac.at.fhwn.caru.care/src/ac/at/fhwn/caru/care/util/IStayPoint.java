package ac.at.fhwn.caru.care.util;

import java.time.LocalDateTime;

import org.apache.commons.math3.stat.clustering.Clusterable;


@SuppressWarnings("deprecation")
public interface IStayPoint extends IPoint, Clusterable<IStayPoint> {
    public LocalDateTime getInTimeStamp();
    public LocalDateTime getOutTimeStamp();
}
