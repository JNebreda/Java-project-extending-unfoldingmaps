import java.util.HashMap;

/**
 * A class to store a country's information: country name and ID, and hashmap of year-statistic pairs
 * @author Jenifer
 *
 */
public class CountryYearStatistic {
	
	private String country;
	private String countryID;
	private HashMap<Integer, Float> YearStatMap = new HashMap<Integer, Float>();
	public static float minStat = 0;
	public static float maxStat = 0;
	
	//Constructor
	public CountryYearStatistic(String[] columns, int firstYear){
		country = columns[2];
		countryID = columns[3];
		int year = firstYear;
		
		for (int i = 4; i < columns.length; i++){
			if (!columns[i].equals("..")){
				float stat = Float.parseFloat(columns[i]);	
				YearStatMap.put(year, stat);
				if (stat < minStat) minStat = stat;
				//if (stat > maxStat) maxStat = stat;
				maxStat = 1.1f; //In this particular case, only a couple of countries have values above 1.1
								//if we go up to the maxStat, we lose contrast between the more meaningful values
								//the ideal would be to distribute the colors in a more elaborate way
			}
			year++;
		}
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getCountryID(){
		return countryID;
	}

	public HashMap<Integer, Float> getYearStatMap(){
		return YearStatMap;
	}
	
}
