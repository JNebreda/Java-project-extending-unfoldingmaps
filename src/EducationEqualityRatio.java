import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;

import java.util.HashSet;

import de.fhpotsdam.unfolding.marker.Marker;

public class EducationEqualityRatio extends PApplet{
/**
 * It shows the ratio of girls to boys enrolled at secondary level in public and private schools, calculated by dividing female 
 * gross enrollment ratio in secondary education by male gross enrollment ratio in secondary education, and multiplying by 100
 * The country shapes are loaded from a GeoJSON file via a data reader, and the data from a CSV file provided by the World Bank. 
 * The data value is encoded to transparency via a simplistic linear mapping.
 */
	
	UnfoldingMap map;
	List<Marker> countryMarkers;
	private int firstYear;
	private int lastYear;
	private HashSet<CountryYearStatistic> educationEqualityMap = new HashSet<CountryYearStatistic>();
	private Slider slider;
	private Legend legend;
	private int fromColor;
	private int toColor;
	private float minStat;
	private float maxStat;
	private String title;

	
	public void setup(){
		
		size(700, 700, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 0, 0, 700, 550, new MBTilesMapProvider(mbTilesString));
		}
		else {
			map = new UnfoldingMap(this, 0, 0, 700, 550, new Google.GoogleMapProvider());
		}		
		
		//Default event dispatcher to handle mouse and keyboard interactions
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// Load country polygons and adds them as markers
		List<Feature> countries;
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		
		// Load secondary school enrollment female to male ratios from a CSV file (provided by the World Bank)
		String fileName = "Data_Extract_From_Gender_Statistics_Data.csv";
		loadDataFromCSV(fileName);
		minStat = CountryYearStatistic.minStat;
		maxStat = CountryYearStatistic.maxStat;

		
		// Create a slider to change the year for which the statistic is shown
		slider = new Slider(firstYear, lastYear);
		
		//Create a legend to show the statistic value corresponding to the shade of the color 
		fromColor = color(140, 15, 60);
		toColor = color(250, 255, 190);
		//The value of title is assigned in the loadDataFromCSV helper function, from the data file,
		//but in this particular case I change it manually because the provided one wasn't clear enough
		title = "Ratio of female to male secondary enrollment (female % / male %)";
		legend = new Legend(title, minStat, maxStat, fromColor, toColor);
										
	}
	
	
	public void draw(){
		background(240,240,240);

		// Country markers are shaded according to the EducationEqualityRatio in the year selected in the slider
		int year = slider.getSelectedValue();
		shadeCountries(year, fromColor, toColor);
		
		// map, slider and legend are drawn
		map.draw();
		slider.drawSlider(this, 100, 670, 500);
		legend.drawLegend(this, 100, 595, slider.getLength(), mouseX, mouseY);
	}
	
	
// Helper method to load the data from a WorldBank CSV into a set of CountryYearStatistic	
	private HashSet<CountryYearStatistic> loadDataFromCSV(String fileName){
		String[] rows = loadStrings(fileName);
		
		int length = 0;
		for (int i = 0; i < rows.length; i++){
		// reads the information in each row of the file  
		// from the first row we extract the year in which the statistics start and the length of an standard row
		// from the following rows, if they have the standard length, we store them into a CountryYearStatistic object and add it to the set
		// because of splitting on just a comma, countries with a comma in their names have a longer length and are not added to the set	
		// we break the loop when we arrive to a 0 length row	
			
			String[] columns = rows[i].split(",");
			
			if(i == 0){
				firstYear = Integer.parseInt(columns[4].split(" ")[0]);
				length = columns.length;
				lastYear = Integer.parseInt(columns[length - 1].split(" ")[0]);
			}
			
			else if(columns.length == length){
				CountryYearStatistic data = new CountryYearStatistic(columns, firstYear);
				educationEqualityMap.add(data);
				if(i == 1){
					title = columns[0];  // In order to load the title just once
				}				
			}
			
			else if(columns.length == 0){
				break;			
			}
		}

		return educationEqualityMap;
	}
	
	//Helper method to color each country based on the Education Equality Ratio for the given year
	private void shadeCountries(int year, int from, int to){
		
		int colorLevel;
		
		for (Marker marker : countryMarkers) {
			
			marker.setColor(color(150,150,150));
			
			for (CountryYearStatistic countryEntry : educationEqualityMap){
				
				if(marker.getId().equals(countryEntry.getCountryID())){
					
					if (countryEntry.getYearStatMap().containsKey(year)){

						float educationEqualityRatio = countryEntry.getYearStatMap().get(year);
						
						if (educationEqualityRatio <= maxStat){
							float inter = map(educationEqualityRatio, minStat, maxStat, 0, 1);						
							colorLevel = lerpColor(from, to, inter);
						}
						else{
							colorLevel = to;
						}
						
						marker.setColor(colorLevel);
					}
				}
			}
		}
	}
					

	
	public void mouseDragged(){
		if(slider.isInSlider(mouseX, mouseY)){
			slider.setSelectedValue(mouseX);
		}
	}	
	
	public void mousePressed(){
		if(slider.isInSlider(mouseX, mouseY)){
			slider.setSelectedValue(mouseX);			
		}
	}
			

}
