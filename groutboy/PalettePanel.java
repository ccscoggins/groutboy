package groutboy;
// PalettePanel rewrite May 23 2021

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;

import javax.swing.JFrame;

public class PalettePanel extends JPanel implements DrawNotifier{
	private ArrayList<DrawListener> listeners;
	private ArrayList<Palette> pal;
	private JButton forward;
	private JButton back;
	private JTextField selectionNumber;
	boolean colorMode = false;
	boolean spriteMode = false;
	
	int palSelector; //0-15, index of palette in structure
	int palInnerIndex; //0-3, index of active color in palette
	
	private class DrawPanel extends JPanel{
		private double sizey, sizex;
		private class PaletteMouseControl extends MouseAdapter{
			public void mousePressed(MouseEvent me){
				int posx = me.getX();
				int posy = me.getY();
				//System.out.println("Click! X = " + posx + "; Y = " + posy);
				if(posx < 0 || posx > sizex || posy < 0)
					return;
				for(int i=1; i<=4; i++){
					if(posy <= (i * (sizey / 4))){
						setPalIndex(i - 1);
						break;
					}
				}
				repaint();
				notifyListeners();
			}
			//public void mouseDragged(MouseEvent me){}
			//public void mouseMoved(MouseEvent me){}
			//public void mouseExited(MouseEvent me){}
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			sizey = getHeight();
			sizex = getWidth();
			
			for(int i=0; i<4; i++){
				g2d.setColor(
					PalettePanel.this.pal.
						get(palSelector).getColor(i)
				);
				//System.out.printf("Drawing box %d with color %X\n", i, (g2d.getColor().getRGB() & 0x00FFFFFF));
				g2d.fill(new Rectangle2D.Double(1,
					1 + (i * (sizey / 4)),
					sizex - 2,
					(sizey/4) - 2));
			}
			
			if(!(palInnerIndex < 0 || palInnerIndex > 3)){
				g2d.setColor(new Color(0x0000FF));
				g2d.setStroke(new BasicStroke(2));
				g2d.draw(new Rectangle2D.Double(
					1,
					(palInnerIndex * (sizey / 4)) + 1,
					sizex - 2,
					(sizey / 4) - 1));
			}
		}
		
		public DrawPanel(){
			PaletteMouseControl pmc = new PaletteMouseControl();
			this.addMouseListener(pmc);
			//this.addMouseMotionListener(pmc);
			this.setPreferredSize( new Dimension(64, 256) );
			repaint();
		}
	}
	
	public void setPalIndex(int newId){
		if(newId < 0 || newId > 3)
			return;
		palInnerIndex = newId;
	}
	
	public void registerDrawListener(DrawListener dl){
		listeners.add(dl);
	}
	
	public void notifyListeners(){
		for(int i=0; i<listeners.size(); i++){
			listeners.get(i).notifyDraw("Palette");
		}
	}
	
	public int getPalIndex(){
		return palInnerIndex;
	}
	
	public Palette getActivePal(){
		return(pal.get(palSelector));
	}
	
	public PalettePanel(){
		super(new GridBagLayout());
		listeners = new ArrayList<DrawListener>();
		GridBagConstraints gbc = new GridBagConstraints();
		
		back = new JButton("<");
		forward = new JButton(">");
		selectionNumber = new JTextField();
		pal = new ArrayList<Palette>();
		pal.add(Palette.defaultPal());
		
		palSelector = 0;
		palInnerIndex = 0;
		selectionNumber.setText(String.valueOf(palSelector));
		
		PalettePanel.DrawPanel dp = new PalettePanel.DrawPanel();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(back, gbc);
		
		gbc.gridx = 1;
		this.add(selectionNumber, gbc);
		
		gbc.gridx = 2;
		this.add(forward, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 3;
		gbc.gridheight = 6;
		this.add(dp, gbc);
		
		
	}
	
	//Test method for palette control
	public static void main(String[] args){
		JFrame window = new JFrame("");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(new PalettePanel());
		window.pack();
		window.setVisible(true);
	}
}
