package ac.at.fhwn.caru.care.evaluation;

import java.util.Collection;

import ac.at.fhwn.caru.care.cluster.IClusterAlgorithm;
import ac.at.fhwn.caru.care.staypoint.IStayPointAlgorithm;
import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;

public interface IEvaluation {
	public EvaluationResult evaluate(Collection<Collection<ISpatioTemporalPoint>> data, IStayPointAlgorithm stayAlgs, IClusterAlgorithm clustAlg, Collection<ITaggedPlaces> places);
}