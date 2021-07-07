package groutboy;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.awt.geom.Rectangle2D;

class DrawPanel extends JPanel implements DrawNotifier{
	private ArrayList<DrawListener> listeners;
	double pixelSize, yoffset, xoffset;
	private int sizey, sizex;
	private int highlightIndex;
	private int paletteIndex;
	private GBTile activeTile;
	private Palette activePal;
	
	private class DrawMouseHandler extends MouseAdapter{
		private int oldy, oldx, posy, posx;
		private double xpix, ypix;
		public void mousePressed(MouseEvent me){
			oldx = me.getX();
			oldy = me.getY();
			int target = pixIndexForPosition(oldx, oldy);
			if(target < 0)
				return;
			else{
				activeTile.setPixel(target % 8, target / 8,	paletteIndex);
			}
			notifyListeners();
			
			repaint();
		}
		
		public void mouseMoved(MouseEvent me){
			posx = me.getX();
			posy = me.getY();
			highlightIndex = pixIndexForPosition(posx, posy);
			repaint();
			oldx = posx;
			oldy = posy;
		}
		
		public void mouseDragged(MouseEvent me){
			posx = me.getX();
			posy = me.getY();
			int distx = posx - oldx;
			int disty = posy - oldy;
			highlightIndex = pixIndexForPosition(posx, posy);
			for(int i=0; i<16; i++){
				int target = pixIndexForPosition(oldx + (i * distx / 16), oldy + (i * disty / 16));
				if(target < 0)
					break;
				else{
					activeTile.setPixel(target % 8, target / 8,	paletteIndex);
				}
			}			
			repaint();
			notifyListeners();
			oldx = posx;
			oldy = posy;
		}
	
	}
	
	private int pixIndexForPosition(int x, int y){
		if(x < xoffset || y < yoffset ||
			x >= sizex + xoffset ||
			y >= sizey + yoffset){
			return -1;
		}
		int ret = (int)((((int)(y - yoffset) / (int)pixelSize) * 8) +
						((int)(x - xoffset) / (int)pixelSize));
		if(ret >= 64)
			return -1;
		else
			return ret;
	}
	
	public void setTile(GBTile newTile){
		activeTile = newTile;
	}
	
	public void setPal(Palette newPal){
		activePal = newPal;
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		sizey = getHeight();
		sizex = getWidth();
		
		//set pixelsize to the lesser of width and height over 8
		pixelSize = (sizex < sizey ? sizex : sizey) / 8.0;
		//set offsets in case window changed
		yoffset = (sizey - (8 * pixelSize)) / 2;
		yoffset = (sizex - (8 * pixelSize)) / 2;
		
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				g2d.setColor(activePal.getColor(activeTile.getPixel(i, j)));
				g2d.fill(new Rectangle2D.Double(xoffset + pixelSize * i,
					 yoffset + pixelSize * j,
					 pixelSize,
					 pixelSize));
			}
		}
		
		if(highlightIndex >= 0 && highlightIndex < 64){
			g2d.setColor(Color.GREEN);
			int y = highlightIndex / 8;
			int x = highlightIndex % 8;
			g2d.draw(new Rectangle2D.Double(
				xoffset + pixelSize * x,
				yoffset + pixelSize * y,
				pixelSize,
				pixelSize));
		}
		
	}
	
	public void registerDrawListener(DrawListener dl){
		listeners.add(dl);
	}
	
	public void notifyListeners(){
		for(int i=0; i<listeners.size(); i++){
			listeners.get(i).notifyDraw("Draw");
		}
	}
	
	public void setPalette(Palette pal, int palid){
		activePal = pal;
		paletteIndex = palid;
	}
	
	public DrawPanel(GBTile tile, Palette pal){
		
		listeners = new ArrayList<DrawListener>();
		this.activeTile = tile;
		this.activePal = pal;
		paletteIndex = 0;
		
		this.setPreferredSize(new Dimension(640, 640));
		this.addMouseListener(new DrawMouseHandler());
		this.addMouseMotionListener(new DrawMouseHandler());
	}
	
	public static void main(String[] args){
		GBTile tile = new GBTile();
		Palette p = Palette.defaultPal();
		
		JFrame win = new JFrame("Draw Panel Demo");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.add(new DrawPanel(tile, p));
		win.pack();
		win.setVisible(true);
	}
	
}
