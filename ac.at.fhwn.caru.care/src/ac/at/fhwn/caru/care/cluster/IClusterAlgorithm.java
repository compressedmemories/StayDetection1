package ac.at.fhwn.caru.care.cluster;

import java.util.Collection;

import ac.at.fhwn.caru.care.util.ICluster;
import ac.at.fhwn.caru.care.util.IStayPoint;

public interface IClusterAlgorithm {
	public Collection<ICluster> buildClusters(Collection<IStayPoint> points);
}
