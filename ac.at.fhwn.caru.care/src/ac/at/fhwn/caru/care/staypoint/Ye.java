package ac.at.fhwn.caru.care.staypoint;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;

public class Ye implements IStayPointAlgorithm {
	
	private double distThreh;
	private Duration timeThreh;

	public Ye(double distThreh, Duration timeThreh) {
		this.distThreh = distThreh;
		this.timeThreh = timeThreh;
	}
	
	@Override
	public Collection<IStayPoint> detectStayPoints(Collection<ISpatioTemporalPoint> points) {
		ISpatioTemporalPoint[] tempPoints = points.toArray(new ISpatioTemporalPoint[0]);
		Collection<IStayPoint> stayPoints = new ArrayList<IStayPoint>();
		int i = 0;

		while (i < tempPoints.length) {
			int j = i + 1;
			
			while (j < tempPoints.length) {
				double dist = tempPoints[i].distanceFrom(tempPoints[j]);
				
				if (dist > this.distThreh) {
					Duration timeDiv = Duration.between(tempPoints[i].getTimeStamp(), tempPoints[j].getTimeStamp());
					
					if (timeDiv.getSeconds() > this.timeThreh.getSeconds()) {
						Collection<ISpatioTemporalPoint> temp = getPoints(tempPoints, i , j);
						IStayPoint sp = tempPoints[i].getStayPoint(temp);
						stayPoints.add(sp);
						
					}	
					
					break;
				}	
				
				j++;			
			}
			
			i = j;			
		}
		
		return stayPoints;
	}	
	
	private Collection<ISpatioTemporalPoint> getPoints(ISpatioTemporalPoint[] tempPoints, int start, int stop) {
		Collection<ISpatioTemporalPoint> result = new ArrayList<ISpatioTemporalPoint>();
		
		for (int i = start; i < tempPoints.length && i <= stop; i++) {
			result.add(tempPoints[i]);
		}
			
		return result;
	}
}
