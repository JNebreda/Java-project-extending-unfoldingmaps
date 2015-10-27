import processing.core.PApplet;
import processing.core.PGraphics;


public class Slider{
	/** it creates a slider with a given range and provides a method to draw it. 
	 * It also provides two methods for the mouseDragged() and mousePressed event handlers
	 *  implemented in the main class: one to tell if the cursor is over the slider and 
	 *  another to set the selected value (year in this case) to the one under the thumb.
	 */
	
	private int posX;
	private int posY;
	private int length;
	private int intervalWidth;
	private int numIntervals;
	private int minValue;
	private int maxValue;
	private int selectedValue;
	private int selectedPos = 0;
	private Boolean isInSlider;
	
	public Slider(int min, int max){
		minValue = min;
		maxValue = max;
		selectedValue = min; //Stores the value that is selected in the slider
	}
	
	public void drawSlider(PApplet pg, int x, int y, int length){
		
		posX = x;
		posY = y;
		numIntervals = maxValue - minValue;
		intervalWidth = length / numIntervals;
		// since the interval width has to be an integer, the final length of the slider
		// is not exactly the one pass as a parameter, so that the ticks finish at the right place
		this.length = intervalWidth * numIntervals;
		
		// Save previous drawing style
		pg.pushStyle();
		
		// slider guide				
		pg.fill(100, 100, 100);
		pg.rect(x, y, this.length, 8);
		
		//ticks
		pg.fill(0);
		for (int i = 0; i <= numIntervals; i++){
			pg.line(posX + i * intervalWidth, posY, posX + i * intervalWidth, posY - 8);
		}
		
		//min and max values
		pg.text(minValue, posX - pg.textWidth(Integer.toString(minValue))/2, posY-10);
		pg.text(maxValue, posX + this.length - pg.textWidth(Integer.toString(minValue))/2, posY-10);
		
		//thumb
		pg.rect(posX + selectedPos * intervalWidth - 3, posY-3, 6, 13, 255);
		
		//selected value shown below the slider
		if((selectedValue != minValue) && (selectedValue != maxValue) && isInSlider){
			pg.text(selectedValue, posX + selectedPos * intervalWidth - pg.textWidth(Integer.toString(minValue))/2, posY+25);
		}
		
		// Restore previous drawing style
		pg.popStyle();		
	}
	
	public Boolean isInSlider(int x, int y){
		if (posX>0){
			isInSlider = ((x > posX) && (x < (posX + length)) && (y > posY - 50) && (y < (posY + 50)));	
		}
		else isInSlider = false;
		return isInSlider;
	}
	
	public void setSelectedValue(int x){		
		selectedPos = (x - posX + intervalWidth / 2) / intervalWidth;
		selectedValue = minValue + selectedPos;
	}

	public int getSelectedValue(){
		return selectedValue;
	}
	
	public int getLength(){
		return length;
	}
}
