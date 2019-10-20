package ac.at.fhwn.caru.care.evaluation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ac.at.fhwn.caru.care.cluster.IClusterAlgorithm;
import ac.at.fhwn.caru.care.staypoint.IStayPointAlgorithm;
import ac.at.fhwn.caru.care.util.ICluster;
import ac.at.fhwn.caru.care.util.IPoint;
import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;
import ac.at.fhwn.caru.care.util.gps.GPSCluster;

public class SRFEvaluation implements IEvaluation {
	
	private double radius;
	private Duration time;

	public SRFEvaluation(double radius, Duration time) {
		this.radius = radius;
		this.time = time;
	}
	
	@Override
	public EvaluationResult evaluate(Collection<Collection<ISpatioTemporalPoint>> data, IStayPointAlgorithm stayAlgs, IClusterAlgorithm clustAlg, Collection<ITaggedPlaces> places) {
		Collection<IStayPoint> staypoints = new ArrayList<IStayPoint>();
		Collection<ICluster> clusters = new ArrayList<ICluster>();
		
		// Part where the runtime is important.
		LocalDateTime stime = LocalDateTime.now();
		
		for (Collection<ISpatioTemporalPoint> cstp : data) {
			staypoints.addAll(stayAlgs.detectStayPoints(cstp));
		}
	
		clusters = clustAlg.buildClusters(staypoints);
		
		// End of part where the runtime is important.		
		Duration runtime = Duration.between(stime, LocalDateTime.now());
		
		Map<ITaggedPlaces, Collection<ICluster>> distanceTable = this.findMatchesByDistance(places, clusters);
		Collection<ICluster> clusterFP = this.findClusterFP(distanceTable, clusters);
		Collection<IPoint> clusterFN = this.findClusterFN(distanceTable, places);
		Map<IPoint, Collection<ICluster>> clusterTP = this.findClusterTP(distanceTable);
		
		Map<ITaggedPlaces, Collection<IStayPoint>> timeTable = this.findMatchesByTime(distanceTable);
		Collection<IStayPoint> staysInCluster = this.findStaysInCluster(clusters);
		Collection<IStayPoint> stayTP = this.findStayTP(clusterTP);
		Collection<IStayPoint> stayFP = this.findStayFP(staypoints, stayTP);
		Collection<IStayPoint> stayFN = this.findStayFN(places, timeTable);		
		
		return new EvaluationResult(runtime, this.countTaggedPlaces(places), clusterTP.size(), clusterFN.size(), clusterFP.size(), 
				stayTP.size(), stayFN.size(), stayFP.size(), clusters, staypoints, this.countDays(data), data.size(), 
				this.countTrackPoints(data), this.getQSA(distanceTable), this.getQSU(clusterTP, distanceTable.size()), this.getQTA(timeTable), this.getQTC(timeTable), staysInCluster.size(), this.getQTALT(distanceTable));
	}
	
	private Collection<IStayPoint> findStaysInCluster(Collection<ICluster> clusters) {
		Collection<IStayPoint> result = new ArrayList<IStayPoint>();
		
		for (ICluster c : clusters) {
			for (IStayPoint sp : c.getStayPoints()) {
				if (!result.contains(sp)) {
					result.add(sp);
				}
			}
		}
		
		return result;
	}
	
	private double getQTALT(Map<ITaggedPlaces, Collection<ICluster>> distanceTable) {
		Collection<Double> result = new ArrayList<Double>();
		
		for (ITaggedPlaces itp : distanceTable.keySet()) {
			Collection<Double> dt = new ArrayList<Double>();
			
			for (ICluster c : distanceTable.get(itp)) {
				dt.add(c.getNearestStayPoint(itp).distanceFrom(itp));
			}
			
			double dts = dt.stream().mapToDouble(Double::doubleValue).sum() / dt.size();
			if (dts <= this.radius) {
				result.add(Math.pow((double) this.radius - dts, 2) / Math.pow(this.radius, 2));
			}			
		}
		
		return result.stream().mapToDouble(Double::doubleValue).sum() / result.size();
	}
	
	private double getQTC(Map<ITaggedPlaces, Collection<IStayPoint>> timeTable) {
		if (timeTable.size() > 0) {
			Collection<Duration> result = new ArrayList<Duration>();
			long halftime = this.time.getSeconds() / 2;
			
			for (ITaggedPlaces itp : timeTable.keySet()) {
				ICluster cluster = new GPSCluster(timeTable.get(itp));
				//Duration tdel = Duration.ofSeconds((Duration.between(cluster.getInTimeStamp(), itp.getInTimeStamp()).abs().plus(Duration.between(cluster.getOutTimeStamp(), itp.getOutTimeStamp()).abs())).getSeconds() / 2);
				Duration tdel = Duration.ofSeconds((Duration.between(cluster.getInTimeStamp(), itp.getInTimeStamp()).abs().getSeconds() + Duration.between(cluster.getOutTimeStamp(), itp.getOutTimeStamp()).abs().getSeconds()) / 2);
				
				if (tdel.getSeconds() <= halftime) {
					result.add(tdel);
				}
			}
			
			return (double) result.size() / timeTable.size();			
		} else {
			return 0;
		}
	}
	
	private double getQTA(Map<ITaggedPlaces, Collection<IStayPoint>> timeTable) {
		Collection<Duration> result = new ArrayList<Duration>();
		long halftime = this.time.getSeconds() / 2;
		
		for (ITaggedPlaces itp : timeTable.keySet()) {
			ICluster cluster = new GPSCluster(timeTable.get(itp));
			//Duration tdel = Duration.ofSeconds(Duration.between(cluster.getInTimeStamp(), itp.getInTimeStamp()).abs().plus(Duration.between(cluster.getOutTimeStamp(), itp.getOutTimeStamp()).abs()).getSeconds() / 2);
			Duration tdel = Duration.ofSeconds((Duration.between(cluster.getInTimeStamp(), itp.getInTimeStamp()).abs().getSeconds() + Duration.between(cluster.getOutTimeStamp(), itp.getOutTimeStamp()).abs().getSeconds()) / 2);
			
			if (tdel.getSeconds() <= halftime) {
				result.add(tdel);
			}
		}
		
		if (result.size() == 0) {
			return 0;
		} else {
			long sum = result.stream().mapToLong(Duration::getSeconds).sum();		
			return 1 - ((double) sum / (halftime * result.size()));
		}		
	}
	
	private double getQSU(Map<IPoint, Collection<ICluster>> clustertp, int P) {
		if (P != 0) {
			Collection<ICluster> clu = new ArrayList<ICluster>();
			Collection<ICluster> Nmulti = new ArrayList<ICluster>();
			
			for (IPoint ip : clustertp.keySet()) {
				for (ICluster c : clustertp.get(ip)) {
					if (clu.contains(c)) {
						Nmulti.add(c);
					} else {
						clu.add(c);
					}
				}
			}
			
			return 1 - ((double)Nmulti.size() / P);
		}		
		
		return 0;
	}
	
	private double getQSA(Map<ITaggedPlaces, Collection<ICluster>> distanceTable) {
		Collection<Double> result = new ArrayList<Double>();
		
		for (ITaggedPlaces itp : distanceTable.keySet()) {
			Collection<Double> dt = new ArrayList<Double>();
			
			for (ICluster c : distanceTable.get(itp)) {
				dt.add(c.getNearestStayPoint(itp).distanceFrom(itp));
			}
			
			double dts = dt.stream().mapToDouble(Double::doubleValue).sum() / dt.size();
			if (dts <= this.radius / 2) {
				result.add((double) 1);
			} else {
				result.add(1 - ((double) dts / this.radius));
			}			
		}
		
		return result.stream().mapToDouble(Double::doubleValue).sum() / result.size();
	}
	
	private Collection<IStayPoint> findStayFN(Collection<ITaggedPlaces> places, Map<ITaggedPlaces, Collection<IStayPoint>> timeTable) {
		Collection<IStayPoint> result = new ArrayList<IStayPoint>();
		
		for (ITaggedPlaces itp : places) {
			if (!timeTable.containsKey(itp)) {
				result.add(itp);
			}
		}
		
		return result;
	}
	
	private Collection<IStayPoint> findStayFP(Collection<IStayPoint> staypoints, Collection<IStayPoint> stayTP) {
		Collection<IStayPoint> result = new ArrayList<IStayPoint>();
		
		for (IStayPoint sp : staypoints) {
			if (! stayTP.contains(sp)) {
				result.add(sp);
			}
		}
			
		return result;
	}
	
	private Collection<IStayPoint> findStayTP(Map<IPoint, Collection<ICluster>> clusterTP) {
		Collection<IStayPoint> result = new ArrayList<IStayPoint>();
		
		for (IPoint ip : clusterTP.keySet()) {
			for (ICluster isp : clusterTP.get(ip)) {
				result.addAll(isp.getStayPoints());
			}
		}
		
		return result;
	}
	
	private Map<IPoint, Collection<ICluster>> findClusterTP(Map<ITaggedPlaces, Collection<ICluster>> distanceTable) {
		Map<IPoint, Collection<ICluster>> result = new HashMap<IPoint, Collection<ICluster>>();
		
		for (ITaggedPlaces itp : distanceTable.keySet()) {
			if (!result.containsKey(itp.getPoint())) {
				result.put(itp.getPoint(), distanceTable.get(itp));
			}
		}		
		
		return result;
	}
	
	/*
	private Collection<ICluster> findClusterFP(Map<ITaggedPlaces, Collection<ICluster>> distanceTable, Collection<ICluster> clusters) {
		Collection<ICluster> result = new ArrayList<ICluster>();
		
		for (ICluster c : clusters) {
			boolean isIn = false;
			
			for (ITaggedPlaces itp : distanceTable.keySet()) {
				isIn |= distanceTable.get(itp).contains(c);
			}			
			
			if (!isIn) {
				result.add(c);
			}
		}		
		
		return result;
	}
	*/
	private Collection<ICluster> findClusterFP(Map<ITaggedPlaces, Collection<ICluster>> distanceTable, Collection<ICluster> clusters) {
		Collection<ICluster> nearestCluster = new ArrayList<ICluster>();
		Collection<ICluster> result = new ArrayList<ICluster>();
			
		for (ITaggedPlaces itp : distanceTable.keySet()) {
			ICluster nearest = null;
			for (ICluster c2 : distanceTable.get(itp)) {					
				if (nearest == null || itp.distanceFrom(c2.getNearestStayPoint(itp)) < itp.distanceFrom(nearest.getNearestStayPoint(itp))) {
					nearest = c2;
				}
			}
			nearestCluster.add(nearest);
		}	
		
		for (ICluster c : clusters) {	
			if (!nearestCluster.contains(c)) {
				result.add(c);
			}
		}				
			
		return result;
	}
	
	private Collection<IPoint> findClusterFN(Map<ITaggedPlaces, Collection<ICluster>> distanceTable, Collection<ITaggedPlaces> places) {
		Collection<IPoint> result = new ArrayList<IPoint>();
		for (ITaggedPlaces itp : places) {
			if (!distanceTable.containsKey(itp) && !result.contains(itp.getPoint())) {
				result.add(itp.getPoint());
			}
		}
		
		return result;	
	}
	
	private boolean timeBetween(LocalDateTime t, LocalDateTime t1, LocalDateTime t2) {
		if (t.isAfter(t1.minus(this.time)) && t.isBefore(t2.plus(this.time))) {
			return true;
		}
		
		return false;
	}
	
	private Map<ITaggedPlaces, Collection<IStayPoint>> findMatchesByTime(Map<ITaggedPlaces, Collection<ICluster>> distanceTable) {
		Map<ITaggedPlaces, Collection<IStayPoint>> result = new HashMap<ITaggedPlaces, Collection<IStayPoint>>();
		
		for (ITaggedPlaces itp : distanceTable.keySet()) {
			Collection<IStayPoint> timefits = new ArrayList<IStayPoint>();
			
			for (ICluster c : distanceTable.get(itp)) {
				for (IStayPoint st : c.getStayPoints()) {
					if (this.timeBetween(st.getInTimeStamp(), itp.getInTimeStamp(), itp.getOutTimeStamp()) && this.timeBetween(st.getOutTimeStamp(), itp.getInTimeStamp(), itp.getOutTimeStamp())) {
						timefits.add(st);
					}
				}
			}
			
			if (timefits.size() > 0) {
				result.put(itp, timefits);
			}
		}
	
		return result;
	}
	
	private Map<ITaggedPlaces, Collection<ICluster>> findMatchesByDistance(Collection<ITaggedPlaces> places, Collection<ICluster> clusters) {
		Map<ITaggedPlaces, Collection<ICluster>> result = new HashMap<ITaggedPlaces, Collection<ICluster>>();
		
		for (ITaggedPlaces itp : places) {			
			for (ICluster c : clusters) {			
				if (c.getNearestStayPoint(itp).distanceFrom(itp) <= this.radius) {
					if (!result.containsKey(itp)) {
						result.put(itp, new ArrayList<ICluster>());
					}
					
					result.get(itp).add(c);
				}
			}
		}
		
		return result;
	}	
	
	private int countTrackPoints(Collection<Collection<ISpatioTemporalPoint>> data) {
		int result = 0;
		
		for (Collection<ISpatioTemporalPoint> cistp : data) {
			result += cistp.size();
		}
		
		return result;
	}
	
	private int countDays(Collection<Collection<ISpatioTemporalPoint>> data) {
		Collection<LocalDate> result = new LinkedList<LocalDate>();
		
		for (Collection<ISpatioTemporalPoint> cistp : data) {
			for (ISpatioTemporalPoint istp: cistp) {
				LocalDate date = istp.getTimeStamp().toLocalDate();
				if (!result.contains(date)) {
					result.add(date);
				}
			}
		}
		
		return result.size();
	}
	
	private int countTaggedPlaces(Collection<ITaggedPlaces> places) {
		Collection<IPoint> result = new ArrayList<IPoint>();
		
		for (ITaggedPlaces tp : places) {
			if (!result.contains(tp.getPoint())) {
				result.add(tp.getPoint());
			}
		}
		
		return result.size();
	}	
}
