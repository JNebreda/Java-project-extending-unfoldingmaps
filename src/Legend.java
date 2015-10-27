import processing.core.PApplet;
import processing.core.PGraphics;

public class Legend {
	/**
	 * A class to create a legend for the correspondence between colors and statistical values.
	 * It reacts to the mouse, showing the value corresponding to the color below the cursor. 
	 */
	
	private float minValue;
	private float maxValue;
	private int fromColor;
	private int toColor;
	private int posX;
	private int posY;
	private int length;
	private PApplet pg;
	private int colorSelected;
	private String title;
	
	public Legend(String title, float min, float max, int from, int to){
		minValue = min;
		maxValue = max;
		fromColor = from;
		toColor = to;
		this.title = title;
	}
	
	public void drawLegend(PApplet pg, int posX, int posY, int length, int mouseX, int mouseY){
		this.posX = posX;
		this.posY = posY;
		this.length = length;
		this.pg = pg;
		
		// Save previous drawing style
		pg.pushStyle();
				
		//color filling
		for (int i = posX; i <= posX + length; i++) {
		      float inter = pg.map(i, posX, posX + length, 0, 1);
		      int c = pg.lerpColor(fromColor, toColor, inter);
		      pg.stroke(c);
		      pg.line(i, posY, i, posY + 25);
		    }
		
		//border
		pg.stroke(0);
		pg.rect(posX, posY, length, 25, 0);
		
		//title
		pg.fill(0);
		pg.textAlign(pg.CENTER);
		pg.text(title, posX + length / 2, posY-10);
				
		//min and max values
		pg.text(minValue, posX, posY-10);
		pg.text(maxValue, posX + length, posY-10);

		//corresponding value shown below the color box
		if(isInLegend(mouseX, mouseY)){
			
			float inter = pg.map((float) mouseX, (float)posX, (float)posX + length, minValue, maxValue);
			pg.fill(0);
			pg.text(inter, mouseX, posY + 40);
		}
		
		// Restore previous drawing style		
		pg.popStyle();

	}
	
	private Boolean isInLegend(int x, int y){
		if (posX>0){
			return ((x > posX) && (x < (posX + length)) && (y > posY) && (y < (posY + 25)));
		}
		else return false;
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public int getLength(){
		return length;
	}	
	
/*	public int getColorSelected(){
		return colorSelected;
	}
*/
}
