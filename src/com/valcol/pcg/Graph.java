package com.valcol.pcg;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
 
public class Graph extends JPanel { 
	

	private Map map;
	
	public Graph(Map map2) {
		  
			this.map = map2;
	}
	
	public void paintComponent(Graphics g){
		
		Draw(g);
	 }         
	

	public void Draw(Graphics g){
	
		int [][] mapArray = map.getMap();
		
		int k = (int) (3);
		for(int x=0; x<mapArray .length; x++){
	        for(int y=0; y<mapArray [0].length; y++){ 
	        	
				if (mapArray [x][y] == 1)
					g.setColor(new Color(80,100,92));
				else if (mapArray [x][y] == 0)
					g.setColor(new Color(21,29,42));
				else if (mapArray [x][y] == 2)
					g.setColor(new Color(60,29,62));
				else if (mapArray [x][y] == 3)
					g.setColor(new Color(140,180,150));
				else if (mapArray [x][y] < 0)
					g.setColor(new Color(27,146,189));
				g.fillRect(x*k, y*k, k, k);
	
			}
		}
	}


}