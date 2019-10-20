package ac.at.fhwn.caru.care.preprocessing;

import java.util.Collection;

import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;

public interface IPreprocessing {
	public Collection<Collection<ISpatioTemporalPoint>> process(Collection<Collection<ISpatioTemporalPoint>> data);
}
