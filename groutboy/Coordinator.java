package groutboy;

import javax.swing.JFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class Coordinator extends JFrame implements DrawListener{
	private static final long serialVersionUID = 0x14;
	
	private PalettePanel palpane;
	private DrawPanel drawpane;
	private TilesetPanel tilespane;
	
	private GBTileSet tileset;
	private Palette pal;
	private int palIndex;
	
	public void notifyDraw(String source){
		//System.out.println("Coordinator notified");
		if(source.equals("Palette")){
			//System.out.println("Palette Notifier");
			pal = palpane.getActivePal();
			palIndex = palpane.getPalIndex();
			drawpane.setPalette(pal, palIndex);
			drawpane.repaint();
		}
		else if(source.equals("Tileset")){
			//System.out.println("Tileset Notifier");
			drawpane.setTile(tileset.getTile(tilespane.getActiveTileIndex()));
			drawpane.repaint();
		}
	}
	
	public Coordinator(){
		super("GroutBoy NEO");
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		tileset = new GBTileSet();
		
		palpane = new PalettePanel();
		pal = palpane.getActivePal();
		palIndex = palpane.getPalIndex();
		palpane.registerDrawListener(this);
		
		tilespane = new TilesetPanel(tileset);
		tilespane.registerDrawListener(this);
		
		drawpane = new DrawPanel(tileset.getTile(0), pal);
		
		drawpane.registerDrawListener(tilespane);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1; 
		gbc.gridheight = 4;
		this.add(palpane, gbc);
		
		gbc.gridy = 0;
		gbc.gridx = 1;
		gbc.gridwidth = 10;
		gbc.gridheight = 10;
		this.add(drawpane, gbc);
		
		gbc.gridy = 11;
		gbc.gridheight = 1;
		this.add(tilespane, gbc);
		
		this.pack();
	}
	
	public static void main(String[] args){
		Coordinator win = new Coordinator();
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
	}
}
