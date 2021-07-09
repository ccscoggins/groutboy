package groutboy;

/** class: GBTile
 *	author: Cameron Scoggins
 *	date: June 23, 2021
 *	purpose: Rewrite of original.  Provides implementation for single 8x8 pixel tile
 */

import java.util.StringReader;

import java.io.IOException;

public class GBTile{
	private int [] pixel;
	
	/**	default constructor
	 *	purpose: creates a new, blank tile
	 */
	public GBTile(){
		pixel = new int[64];
	}
	
	/**	constructor (String)
	 *	purpose: loads hexadecimal 2BPP data into the tile
	 */
	public GBTile(String hexString){
		StringReader hexInput = new StringReader(hexString);
		pixel = new int[64];
		short currByte;
		for(int i=0; i<16; i++){
			try{
				currByte = hexInput.read();
			} catch (IOException E){
				System.err.println("Exception occurred on reading tile from hex data.");
				E.printStackTrace();
			}
			for(int j=0; j<4; j++){
				pixel[i*4+j] = currByte & 0x03;
				currByte >> 2;
			}
		}
	}
	
	
	public boolean setPixel(int x, int y, int newValue){
		if(x < 0 || y < 0 ||
			x > 8 || y > 8 ||
			newValue < 0 || newValue > 3){
			System.err.println("Error: attempted to set pixel with invalid values: x=" + x + ", y=" + y + ", val=" + newValue);
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
	
	/**	method: getHexRepresentation
	 *	purpose: creates a 2bpp hex representation of the tile
	 *		for saving purposes
	 */
	public String getHexRepresentation(){
		short currByte = 0;
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<16; i++){
			for(int j=0; j<4; j++){
				currByte << 2;
				currByte |= pixel[i*4 + j];
			}
			buf.append(String.toHexString(currByte & 0xFF)); //and is failsafe in case numbers got weird.
			currByte = 0;
		}
		return buf.toString();
	}
	
}
