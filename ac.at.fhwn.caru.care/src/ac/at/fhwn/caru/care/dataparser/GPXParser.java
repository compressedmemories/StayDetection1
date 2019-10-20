package ac.at.fhwn.caru.care.dataparser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;
import ac.at.fhwn.caru.care.util.gps.GPSSpatioTemporalPoint;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Point;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;

public class GPXParser implements IDataParser {
	
	private File[] files;
	
	public GPXParser(File[] files) {
		this.files = files;
	}

	@Override
	public Collection<Collection<ISpatioTemporalPoint>> getData() throws Exception {		
		Collection<Collection<ISpatioTemporalPoint>> results = new ArrayList<Collection<ISpatioTemporalPoint>>();
		for (File f : files) {		
			// Reading GPX 1.1 file.
			GPX gpx11 = GPX.reader(GPX.Version.V11).read(f);
			for (Track t : gpx11.getTracks()) {
				results.add(this.getPointsFromTrack(t));
			}
		}
		
		return results;
	}
	
	private Collection<ISpatioTemporalPoint> getPointsFromTrack(Track track) {
		Collection<ISpatioTemporalPoint> points = new ArrayList<ISpatioTemporalPoint>();
		
		for (TrackSegment ts : track.getSegments()) {
			for(Point p : ts.getPoints()) {
				GPSSpatioTemporalPoint gpsstp = new GPSSpatioTemporalPoint(p.getLongitude().doubleValue(), p.getLatitude().doubleValue(), 
						p.getElevation().get().doubleValue(), p.getTime().get().toLocalDateTime());
				points.add(gpsstp);				
			}
		}
		
		return points;		
	}
}
