package ac.at.fhwn.caru.care.evaluation;

import java.time.Duration;
import java.util.Collection;

import ac.at.fhwn.caru.care.util.ICluster;
import ac.at.fhwn.caru.care.util.IStayPoint;

public class EvaluationResult {
	private Duration runtime;
	private int taggedPlcaes;
	private int days;
	private int tracks;
	private int trackPoints;
	private int clustertp;
	private int clusterfn;
	private int clusterfp;
	private int staytp;
	private int stayfn;
	private int stayfp;
	private int staysInCluster;
	private double qsa;
	private double qsu;
	private double qta;
	private double qtc;	
	private double qtalt;
	private Collection<IStayPoint> staypoints;
	private Collection<ICluster> clusters;
	
	public EvaluationResult(Duration runtime, int taggedPlcaes, int clustertp, int clusterfn, int clusterfp, int staytp, int stayfn, int stayfp, 
			Collection<ICluster> clusters, Collection<IStayPoint> staypoints, int days, int tracks, int trackPoints, double qsa, double qsu, double qta, double qtc, int staysInCluster, double qtalt) {
		this.runtime = runtime;
		this.taggedPlcaes = taggedPlcaes;
		this.clusters = clusters;
		this.staypoints = staypoints;
		this.days = days;
		this.tracks = tracks;
		this.trackPoints = trackPoints;
		this.clustertp = clustertp;
		this.clusterfn = clusterfn;
		this.clusterfp = clusterfp;
		this.stayfn = stayfn;
		this.stayfp = stayfp;
		this.staytp = staytp;
		this.qsa = qsa;
		this.qsu = qsu;
		this.qta = qta;
		this.qtc = qtc;
		this.staysInCluster = staysInCluster;
		this.qtalt = qtalt;
	}	
	
	private double toDecimals(double val, int decimal) {
		int fact = (int) Math.pow(10, decimal);
		return (double) Math.round(val * fact) / fact;
	}
	
	public double getQTALT() {
		return this.qtalt;
	}
	
	public int getStaysInCluster() {
		return this.staysInCluster;
	}
	
	
	public double getQTC() {
		return this.qtc;
	}
	
	public double getQTA() {
		return this.qta;
	}
	
	public double getQSU() {
		return this.qsu;
	}
	
	public double getQSA() {
		return this.qsa;
	}
	
	public double getPrecision() {
		return (double) this.clustertp / (this.clustertp + this.clusterfp);
	}
	
	public double getFmeasure() {
		return (2 * this.getRecall() * (double) this.getPrecision()) / (this.getPrecision() + this.getRecall());
	}
	
	public double getRecall( ) {
		return (double) this.clustertp / (this.clustertp + this.clusterfn);
	}
	
	public double getStayRecall( ) {
		return (double) this.staytp / (this.staytp + this.stayfn);
	}
	
	public double getStayPrecision() {
		return (double) this.staytp / (this.staytp + this.stayfp);
	}
	
	public double getStayFmea() {
		return (double) (this.getStayPrecision() + this.getStayRecall()) / 2;
	}
	
	public int getClusterFN() {
		return this.clusterfn;
	}
	
	public int getClusterFP() {
		return this.clusterfp;
	}
	
	public int getStayTP() {
		return this.staytp;
	}
	
	public int gettStayFN() {
		return this.stayfn;
	}
	
	public int gettStayFP() {
		return this.stayfp;
	}
	
	public int getClusterTP() {
		return this.clustertp;
	}
	
	public Collection<ICluster> getClusters() {
		return this.clusters;
	}
	
	public Collection<IStayPoint> getStaypoins() {
		return this.staypoints;
	}
	
	public int getDays() {
		return this.days;
	}
	
	public int getTrackPoints() {
		return this.trackPoints;
	}
	
	public int getTaggedPlcaes() {
		return this.taggedPlcaes;
	}
	
	public Duration getRuntime() {
		return this.runtime;
	}
	
	public int getTracks() {
		return this.tracks;
	}
	
	public String printLine(int decimals) {
		return this.runtime.getSeconds() + ";" + this.clusters.size() + ";" + this.taggedPlcaes + ";" + this.clustertp + ";" +
				this.clusterfn + ";" + this.clusterfp + ";" + this.toDecimals(this.getRecall(), decimals) + ";" + this.toDecimals(this.getPrecision(), decimals) + ";" + this.toDecimals(this.getFmeasure(), decimals) + ";" +
				this.toDecimals(this.qsa, decimals) + ";" + this.toDecimals(this.qsu, decimals) + ";" + this.toDecimals(this.qtalt, decimals) + ";" + this.staytp + ";" + this.stayfn + ";" + this.stayfp + ";" + this.toDecimals(this.getStayRecall(), decimals) + ";" +
				this.toDecimals(this.getStayPrecision(), decimals) + ";" + this.toDecimals(this.getStayFmea(), decimals) + ";" + this.toDecimals(this.qta, decimals) + ";" + this.toDecimals(this.qtc, decimals) + ";"  + 
				this.days + ";" + this.trackPoints + ";" + this.staypoints.size() + ";" + this.staysInCluster;
	}
}
