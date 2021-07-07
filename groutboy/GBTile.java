package groutboy;

/** class: GBTile
 *	author: Cameron Scoggins
 *	date: June 23, 2021
 *	purpose: Rewrite of original.  Provides implementation for single 8x8 pixel tile
 */


public class GBTile{
	private int [] pixel;
	
	public GBTile(){
		pixel = new int[64];
	}
	
	public boolean setPixel(int x, int y, int newValue){
		if(x < 0 || y < 0 ||
			x > 8 || y > 8 ||
			newValue < 0 || newValue > 3){
			System.err.println("Error: attempted to set invalid pixel with values: x=" + x + ", y=" + y + ", val=" + newValue);
			return false;
		}
		pixel[y * 8 + x] = newValue;
		return true;
	};
	
	public int getPixel(int x, int y){
		if(x < 0 || y < 0 ||x > 8 || y > 8){
			System.err.println("Error: attempted to retrieve pixel out of bounds.");
			return -1;
		}
		return pixel[y * 8 + x];
	}
	
	//demo purposes remove before flight
	public void setAll(int palIndex){
		for(int i=0; i<64; i++){
			pixel[i] = palIndex;
		}
	}
	
}
