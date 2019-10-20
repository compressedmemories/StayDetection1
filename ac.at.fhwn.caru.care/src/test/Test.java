package test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.util.Pair;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import ac.at.fhwn.caru.care.cluster.DBSCAN;
import ac.at.fhwn.caru.care.dataparser.GPXParser;
import ac.at.fhwn.caru.care.evaluation.EvaluationResult;
import ac.at.fhwn.caru.care.evaluation.ITaggedPlaces;
import ac.at.fhwn.caru.care.evaluation.SRFEvaluation;
import ac.at.fhwn.caru.care.preprocessing.GPSPreprocessing;
import ac.at.fhwn.caru.care.staypoint.Ye;
import ac.at.fhwn.caru.care.util.ICluster;
import ac.at.fhwn.caru.care.util.ISpatioTemporalPoint;
import ac.at.fhwn.caru.care.util.IStayPoint;
import ac.at.fhwn.caru.care.util.gps.GPSTaggedPlaces;

public class Test {

	@SuppressWarnings({ "deprecation", "resource" })
	public static void main(String[] args) {	
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		Collection<Pair<EvaluationResult, TestInput>> results = new ArrayList<Pair<EvaluationResult, TestInput>>();
		Collection<TestInput> inputs = new ArrayList<TestInput>();
		
		/*
		inputs.add(new TestInput(100, 53, 25));
		inputs.add(new TestInput(100, 53, 50));
		inputs.add(new TestInput(100, 53, 75));
		inputs.add(new TestInput(100, 53, 100));
		inputs.add(new TestInput(100, 53, 125));
		inputs.add(new TestInput(100, 53, 150));
		inputs.add(new TestInput(100, 53, 175));
		inputs.add(new TestInput(100, 53, 200));
		inputs.add(new TestInput(100, 53, 225));
		inputs.add(new TestInput(100, 53, 250));
		inputs.add(new TestInput(100, 53, 275));
		inputs.add(new TestInput(100, 53, 300));
		
		inputs.add(new TestInput(300, 53, 25));
		inputs.add(new TestInput(300, 53, 50));
		inputs.add(new TestInput(300, 53, 75));
		inputs.add(new TestInput(300, 53, 100));
		inputs.add(new TestInput(300, 53, 125));
		inputs.add(new TestInput(300, 53, 150));
		inputs.add(new TestInput(300, 53, 175));
		inputs.add(new TestInput(300, 53, 200));
		inputs.add(new TestInput(300, 53, 225));
		inputs.add(new TestInput(300, 53, 250));
		inputs.add(new TestInput(300, 53, 275));
		inputs.add(new TestInput(300, 53, 300));
		
		inputs.add(new TestInput(500, 53, 25));
		inputs.add(new TestInput(500, 53, 50));
		inputs.add(new TestInput(500, 53, 75));
		inputs.add(new TestInput(500, 53, 100));
		inputs.add(new TestInput(500, 53, 125));
		inputs.add(new TestInput(500, 53, 150));
		inputs.add(new TestInput(500, 53, 175));
		inputs.add(new TestInput(500, 53, 200));
		inputs.add(new TestInput(500, 53, 225));
		inputs.add(new TestInput(500, 53, 250));
		inputs.add(new TestInput(500, 53, 275));
		inputs.add(new TestInput(500, 53, 300));
		
		inputs.add(new TestInput(700, 53, 25));
		inputs.add(new TestInput(700, 53, 50));
		inputs.add(new TestInput(700, 53, 75));
		inputs.add(new TestInput(700, 53, 100));
		inputs.add(new TestInput(700, 53, 125));
		inputs.add(new TestInput(700, 53, 150));
		inputs.add(new TestInput(700, 53, 175));
		inputs.add(new TestInput(700, 53, 200));
		inputs.add(new TestInput(700, 53, 225));
		inputs.add(new TestInput(700, 53, 250));
		inputs.add(new TestInput(700, 53, 275));
		inputs.add(new TestInput(700, 53, 300));
		*/
		
		inputs.add(new TestInput(900, 53, 25));
		inputs.add(new TestInput(900, 53, 50));
		inputs.add(new TestInput(900, 53, 75));
		inputs.add(new TestInput(900, 53, 100));
		inputs.add(new TestInput(900, 53, 125));
		inputs.add(new TestInput(900, 53, 150));
		inputs.add(new TestInput(900, 53, 175));
		inputs.add(new TestInput(900, 53, 200));
		inputs.add(new TestInput(900, 53, 225));
		inputs.add(new TestInput(900, 53, 250));
		inputs.add(new TestInput(900, 53, 275));
		inputs.add(new TestInput(900, 53, 300));
		inputs.add(new TestInput(900, 53, 325));
		inputs.add(new TestInput(900, 53, 350));
		
		/*
		inputs.add(new TestInput(1100, 53, 25));
		inputs.add(new TestInput(1100, 53, 50));
		inputs.add(new TestInput(1100, 53, 75));
		inputs.add(new TestInput(1100, 53, 100));
		inputs.add(new TestInput(1100, 53, 125));
		inputs.add(new TestInput(1100, 53, 150));
		inputs.add(new TestInput(1100, 53, 175));
		inputs.add(new TestInput(1100, 53, 200));
		inputs.add(new TestInput(1100, 53, 225));
		inputs.add(new TestInput(1100, 53, 250));
		inputs.add(new TestInput(1100, 53, 275));
		inputs.add(new TestInput(1100, 53, 300));
		
		inputs.add(new TestInput(1300, 53, 25));
		inputs.add(new TestInput(1300, 53, 50));
		inputs.add(new TestInput(1300, 53, 75));
		inputs.add(new TestInput(1300, 53, 100));
		inputs.add(new TestInput(1300, 53, 125));
		inputs.add(new TestInput(1300, 53, 150));
		inputs.add(new TestInput(1300, 53, 175));
		inputs.add(new TestInput(1300, 53, 200));
		inputs.add(new TestInput(1300, 53, 225));
		inputs.add(new TestInput(1300, 53, 250));
		inputs.add(new TestInput(1300, 53, 275));
		inputs.add(new TestInput(1300, 53, 300));
		*/
		
		Collection<Collection<ISpatioTemporalPoint>> data = null;
		Collection<ITaggedPlaces> places = new ArrayList<ITaggedPlaces>();
		String dirName = "C:\\Users\\Philipp\\OneDrive\\Ausbildung\\FH\\Masterarbeit\\GPS Daten\\";
		
		try {
			// Get Content
			File dir = new File(dirName);
			File[] files = dir.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".gpx") && !name.equals("140903") && !name.equals("140910");
			    }
			});
			
			GPXParser par = new GPXParser(files);
			data = par.getData();		
						
			CSVReader csvReader = new CSVReader(new FileReader(dirName + "tagged_places.csv"), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				GPSTaggedPlaces place = new GPSTaggedPlaces(Double.parseDouble(values[1]), Double.parseDouble(values[2]), 0, LocalDateTime.parse(values[3], formatter), LocalDateTime.parse(values[4], formatter), Integer.parseInt(values[0]));
				places.add(place);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		
		GPSPreprocessing gpsp = new GPSPreprocessing();
		data = gpsp.process(data);
		
		// Remove last 5 days
		Collection<Collection<ISpatioTemporalPoint>> tempdata = new ArrayList<Collection<ISpatioTemporalPoint>>();
		Collection<ITaggedPlaces> tempplaces = new ArrayList<ITaggedPlaces>();
		for (ITaggedPlaces itp : places) {
			if (itp.getInTimeStamp().isBefore(LocalDateTime.parse("2014-09-17 00:00", formatter))) {
				tempplaces.add(itp);
			}
		}
		for (Collection<ISpatioTemporalPoint> citp : data) {
			ISpatioTemporalPoint[] a  = citp.toArray(new ISpatioTemporalPoint[0]);
			if (a.length > 0 && a[0].getTimeStamp().isBefore(LocalDateTime.parse("2014-09-17 00:00", formatter))) {
				tempdata.add(citp);
			}
		}
		data = tempdata;
		places = tempplaces;
		// End of removing last 5 days
		
		for (TestInput ti : inputs) {
			Ye ye = new Ye(ti.getDistance(), Duration.ofSeconds(ti.getSeconds()));
			DBSCAN scan = new DBSCAN(ti.getRadius(), 0);
			SRFEvaluation eval = new SRFEvaluation(ti.getRadius(), Duration.ofSeconds(ti.getSeconds()));
			results.add(new Pair<EvaluationResult, TestInput>(eval.evaluate(data, ye, scan, places), ti));	
		}	
				
		try {
			FileWriter writer = new FileWriter("C:\\Users\\Philipp\\OneDrive\\Ausbildung\\FH\\Masterarbeit\\result.csv");
			writer.write("Seconds;Radius;Distance;RuntimeSec;DetPlaces;TaggedPlaces;placeTP;placeFN;placeFP;Recall;Precision;Fmeasure;Q_SA;Q_SU;Q_SAalt;stayTP;stayFN;stayFP;stRecall;stPrecision;stFmea;Q_TA;Q_TC;Days;Trackpoints;Staypoints;StaysInCluster\n");

			for (Pair<EvaluationResult, TestInput> p : results) {
				writer.write(p.getSecond().printLine() + ";" + p.getFirst().printLine(3) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		PrintToFile(results, data, "C:\\Users\\Philipp\\OneDrive\\Ausbildung\\FH\\Masterarbeit\\");
		System.out.println("Done");
	}
	
	private static void PrintToFile(Collection<Pair<EvaluationResult, TestInput>> results, Collection<Collection<ISpatioTemporalPoint>> data, String path) {
		EvaluationResult result = null;
		for (Pair<EvaluationResult, TestInput> p : results) {
			if (p.getSecond().getDistance() == 50) {
				result = p.getFirst();
			}
		}
		
		
		
		try {
			//PostgreSQLSaver saver = new PostgreSQLSaver("jdbc:postgresql://localhost:5432/postgres", "postgres", "12345qwert");
			//saver.storeResult(result);

			FileWriter writer = new FileWriter(path + "stay.csv");
			for (IStayPoint sp : result.getStaypoins()) {
				writer.write(sp.getX() + ";" + sp.getY() + ";" + sp.getInTimeStamp() + ";" + sp.getOutTimeStamp() + "\n");
			    
			}
			writer.close();
			
			writer = new FileWriter(path + "cluster.csv");
			for (ICluster c : result.getClusters()) {		
				writer.write(c.getCenter().getX() + ";" + c.getCenter().getY() + ";" + c.getInTimeStamp() + ";" + c.getOutTimeStamp() + ";" + getPolygon(c) + "\n");			    
			}
			writer.close();
			
			writer = new FileWriter(path + "data.csv");
			for (Collection<ISpatioTemporalPoint> cstp: data) {
				for (ISpatioTemporalPoint sp : cstp) {
					writer.write(sp.getX() + ";" + sp.getY() + ";" + sp.getTimeStamp() + "\n");
				}
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getPolygon(ICluster cluster) {
		String result = "POLYGON ((";
		
		for (IStayPoint isp : cluster.getStayPoints()) {
			result += isp.getX() + " " + isp.getY() + "," + cluster.getCenter().getX() + " " + cluster.getCenter().getY() + ",";
		}
		
		return result.substring(0, result.length() - 1) + "))";
	}
}
