package groutboy;
//Palette class rewrite 5/23/21

import java.awt.Color;
import java.util.ArrayList;

class PaletteSet{
	
	ArrayList<Palette> palettes = new ArrayList<Palette>();
	
	public PaletteSet(){
		palettes.add(Palette.defaultPal();
	}
	
	public Palette get(int index){
		if(index < palettes.size())
			return null;
		return palettes.get(index);
	}
	
}
