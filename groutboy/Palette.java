package groutboy;

import java.awt.Color;
import java.util.Formatter;

public class Palette{
		private Color palColors[];
		
		public Palette(){
			palColors = new Color[4];
		}
		
		public Color getColor(int index){
			if(index < 0 || index > 3)
				return null;
			else return palColors[index];
		}
		
		public boolean setColor(int index, Color c){
			if(index < 0 || index > 3)
				return false;
			else palColors[index] = c;
			return true;
		}
		
		public String toString(){
			StringBuilder builder = new StringBuilder(48);
			Formatter f = new Formatter();
			builder.append("P,");
			for(int i=0; i<4; i++){
				builder.append(f.format("0x%06X,", palColors[i].getRGB() & 0x00FFFFFF));
			}
			builder.setCharAt(builder.length()-1, '\n');
			return builder.toString();
		}
		
		//Inverts palette.  Useful for working with sprites, which only have 3 available colors (essentially swaps black and white for transparent)
		public void invert(){
			Color swap = palColors[0];
			palColors[0] = palColors[3];
			palColors[3] = swap;
			swap = palColors[1];
			palColors[1] = palColors[2];
			palColors[2] = swap;
		}
		
		public static Palette defaultPal(){
			Palette ret = new Palette();
			ret.setColor(0, new Color(0x10, 0x10, 0x10));
			ret.setColor(1, new Color(0x80, 0x80, 0x80));
			ret.setColor(2, new Color(0xA0, 0xA0, 0xA0));
			ret.setColor(3, new Color(0xFF, 0xFF, 0xFF));
			return ret;
		}
	}
