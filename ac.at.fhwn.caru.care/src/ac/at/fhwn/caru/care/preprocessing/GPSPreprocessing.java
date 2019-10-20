package ac.at.fhwn.caru.care.preprocessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;

public class GPSPreprocessing implements IPreprocessing {

	private Comparator<List<ISpatioTemporalPoint>> TimeListComparator = new Comparator<List<ISpatioTemporalPoint>>() {
		 public int compare(List<ISpatioTemporalPoint> l1, List<ISpatioTemporalPoint> l2) {
			 return l1.get(0).getTimeStamp().compareTo(l2.get(0).getTimeStamp());
		 }
	 };
	
	@Override
	public Collection<Collection<ISpatioTemporalPoint>> process(Collection<Collection<ISpatioTemporalPoint>> data) {
		List<List<ISpatioTemporalPoint>> result = new ArrayList<List<ISpatioTemporalPoint>>();
	
		for (Collection<ISpatioTemporalPoint> cistp : data) {
			List<ISpatioTemporalPoint> temp = (List<ISpatioTemporalPoint>) cistp;
			Collections.sort(temp, ISpatioTemporalPoint.TimeComparator);
			result.add(temp);
		}
		
		Collections.sort(result, this.TimeListComparator);
		Collection<Collection<ISpatioTemporalPoint>> result2 = new ArrayList<Collection<ISpatioTemporalPoint>>();
		result2.addAll(result);
		
		return result2;
	}
}
