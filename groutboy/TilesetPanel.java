package groutboy;

/** class: TilesetPanel
 *  author: Cameron Scoggins
 *	date: June 23, 2021
 *	purpose: Provides a Swing display panel for the tileset
 */

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.util.ArrayList;

class TilesetPanel extends JPanel implements DrawListener, DrawNotifier{
	private static final long serialVersionUID = 0x13;
	
	private ArrayList<DrawListener> listeners;
	private GBTileSet tileset;
	private Palette pal;
	private int activeTileIndex;
	private int hilightTileIndex;
	
	private int sizey, sizex;
	
	private class TileMouseHandler extends MouseAdapter{
		public void mousePressed(MouseEvent me){
			int posx = me.getX();
			int posy = me.getY();
			int target;
			//locate tile clicked, simple /10
			if(posx < 0 || posx >= sizex || posy < 0)
				return;
			else{
				target = (posx / 10) + (posy / 10 * 64);
				//System.out.println("Target = " + target);
				if(target <= tileset.size()){
					if (target == tileset.size()){
						tileset.expand();
					}
					activeTileIndex = target;
				}
			}
			
			repaint();
			notifyListeners();
		}
	}
	
	public void registerDrawListener(DrawListener dl){
		listeners.add(dl);
	}
	
	public void notifyListeners(){
		for(int i=0; i<listeners.size(); i++){
			listeners.get(i).notifyDraw("Tileset");
		}
	}
	
	public int getActiveTileIndex(){
		return activeTileIndex;
	}
	
	//Satisfies DrawListener interface
	public void notifyDraw(String source){
		repaint();
	}
	
	private void drawTile(Graphics2D g2d, int row, int col){
		GBTile tile = tileset.getTile(row*64 + col);
		for(int i=0; i<8; i++){
			for (int j=0; j<8; j++){
				g2d.setColor(pal.getColor(tile.getPixel(i, j)));
				g2d.fillRect(1 + (col * 10) + i, 1 + (row * 10) + j, 1, 1);
			}
		}
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		sizey = getHeight();
		sizex = getWidth();
		Graphics2D g2d = (Graphics2D) g;
		int size = tileset.size();
		//Stack 4x for demo purposes, may restack later
		for(int row = 0; row < 4; row++){
			if(size - (row * 64) > 0)
				for(int col = 0; col < 64; col++){
					if(size - (row * 64 + col) > 0)
						drawTile(g2d, row, col);
					else break;
				}
			else break;
		}
		g2d.setColor(Color.GREEN);
		g2d.drawRect((activeTileIndex % 64) * 10, (activeTileIndex / 64) * 10, 10, 10);
		if(hilightTileIndex > 0 && hilightTileIndex < size){
			g2d.setColor(Color.BLUE);
			g2d.drawRect((activeTileIndex % 64), (activeTileIndex / 64), 10, 10);
		}
	}
	
	public TilesetPanel(GBTileSet tileset){
		listeners = new ArrayList<DrawListener>();
		pal = Palette.defaultPal();
		activeTileIndex = 0;
		hilightTileIndex = -1;
		this.tileset = tileset;
		
		this.setPreferredSize(new Dimension(640, 40));
		this.addMouseListener(new TileMouseHandler());
		
	}
	
	public static void main(String[] args){
		GBTileSet tileset = new GBTileSet();
		tileset.expand();
		tileset.getTile(1).setAll(3);
		JFrame win = new JFrame("Tileset Panel Demo");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.add(new TilesetPanel(tileset));
		win.pack();
		win.setVisible(true);
	}
	
}
