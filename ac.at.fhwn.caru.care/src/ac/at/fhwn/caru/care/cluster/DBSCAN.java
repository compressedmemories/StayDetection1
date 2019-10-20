package ac.at.fhwn.caru.care.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.stat.clustering.DBSCANClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;

import ac.at.fhwn.caru.care.util.ICluster;
import ac.at.fhwn.caru.care.util.IStayPoint;
import ac.at.fhwn.caru.care.util.gps.GPSCluster;

@SuppressWarnings("deprecation")
public class DBSCAN implements IClusterAlgorithm {
	
	private double distance;
	private int minpoints;
	
	public DBSCAN(double distance, int minpoints) {
		this.distance = distance;
		this.minpoints = minpoints;
	}

	@Override
	public Collection<ICluster> buildClusters(Collection<IStayPoint> points) {
		DBSCANClusterer<IStayPoint> dbscan = new DBSCANClusterer<IStayPoint>(this.distance, this.minpoints);
		Collection<ICluster> result = new ArrayList<ICluster>();
		
		List<Cluster<IStayPoint>> clusters = dbscan.cluster(points);		
		for (Cluster<IStayPoint> c : clusters) {
			GPSCluster gpsCluster = new GPSCluster(c.getPoints());
			result.add(gpsCluster);
		}
		
		return result;
	}
}
