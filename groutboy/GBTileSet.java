package groutboy;

/** class: GBTileSet
 *  author: Cameron Scoggins
 *	date: June 23, 2021
 *	purpose: Rewrite of original.  Provides implementation for a set of tiles.
 */

import java.util.ArrayList;

public class GBTileSet{
	private ArrayList<GBTile> tilelist;
	
	public GBTileSet(){
		tilelist = new ArrayList<GBTile>();
		tilelist.add(new GBTile());
	}
	
	public GBTile getTile(int index){
		if(index < 0 || index >= tilelist.size()){
			System.err.println("Error: requested tile out of bounds");
			return tilelist.get(0);
		}
		else return tilelist.get(index);
	}
	
	public int size(){
		return tilelist.size();
	}
	
	public boolean hasRoom(){
		return tilelist.size() < 256;
	}
	
	public void expand(){
		tilelist.add(new GBTile());
	}
}
