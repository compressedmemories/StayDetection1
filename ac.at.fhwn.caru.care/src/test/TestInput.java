package test;

public class TestInput {
	private long seconds = 900;
	private double radius = 53;
	private double distance = 200;
	
	public TestInput(long seconds, double radius, double distance) {
		this.distance = distance;
		this.radius = radius;
		this.seconds = seconds;
	}
	
	public long getSeconds() {
		return this.seconds;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public double getDistance() {
		return this.distance;
	}
	
	public String printLine() {
		return this.seconds + ";" + this.radius + ";" + this.distance;
	}
}
