package builders;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import building.Building;
import building.Entertainment;
import clock.Schedule;
import log.LoggerUtility;


/**
 * 
 * @author matthieu - Quentin
 *
 */
public class EntertainmentBuilder {

	private static final String[] ENTERTAINMENTINFOMAPPING = {"NAME", "OPENING_TIME", "CLOSING_TIME", "DURATION", "EMOTION", "MONEY", "FAMILY"};
	private static final String ENTERTAINMENTINFOPATH = System.getProperty("user.dir") + "/res/entertainment.csv";
	private int currentIndiceInEntertainment;
	
	private static Logger logger = LoggerUtility.getLogger(EntertainmentBuilder.class);
	
	private Entertainment enter;
	
	public EntertainmentBuilder(){
	}
	
	
	public void  creatEntertainment(){
		enter = new Entertainment();
		enter.setReward(new double[3]);
		initEntertainmentInfo(enter);
		enter.setMaxUser(Building.density);
		logger.info("Entertainment creation for: " + enter.getName());
	}
	/**
	 * This methode search in entertainment.csv to initialiaze enter
	 * @param work
	 */
	public void initEntertainmentInfo(Entertainment enter){
		try{
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(ENTERTAINMENTINFOMAPPING);
			
			FileReader fileReader = new FileReader(ENTERTAINMENTINFOPATH);
			CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
			
			List<CSVRecord> csvRecords = csvFileParser.getRecords();
			
			//research in file
			for(int i=0; i<=currentIndiceInEntertainment; i++){
				CSVRecord record = csvRecords.get(i);
				if(i==currentIndiceInEntertainment){
					enter.setName(record.get("NAME"));
					enter.setReward(Double.valueOf(record.get("EMOTION")), 0);
					enter.setReward(Double.valueOf(record.get("MONEY")), 1);
					enter.setReward(Double.valueOf(record.get("FAMILY")), 2);
					//enter.setAverageUsageTime(record.get("DURATION"));
					//Schedule averageUsageTime = new Schedule(0,0);
					//averageUsageTime.convertString(record.get("DURATION"));
					enter.setAverageUsageTime(new Schedule(record.get("DURATION")));
					//enter.setTimeTable(record.get("OPENING_TIME") + "/" + record.get("CLOSING_TIME"));
					enter.setOpeningTime(new Schedule(record.get("OPENING_TIME")));
					enter.setClosingTime(new Schedule(record.get("CLOSING_TIME")));
				}
			}
			
			currentIndiceInEntertainment ++;
			
			csvFileParser.close();
			fileReader.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	public Entertainment getEnter() {
		return enter;
	}


	public void setEnter(Entertainment enter) {
		this.enter = enter;
	}
	
	
}
