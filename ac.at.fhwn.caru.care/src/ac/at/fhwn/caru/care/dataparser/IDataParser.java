package ac.at.fhwn.caru.care.dataparser;

import java.util.Collection;

import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;


public interface IDataParser {
	public Collection<Collection<ISpatioTemporalPoint>> getData() throws Exception;
}
