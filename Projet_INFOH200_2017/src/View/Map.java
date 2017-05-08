package View;
import Model.GameObject;
import Model.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Map extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<GameObject> objects;

	protected int numberOfMonsters;
	private static final int sizeMap = 30;
	
	public Map(){
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	public synchronized void paint(Graphics g) { 
		for(int i = 0; i<sizeMap + 10; i++){		//largeur de map = 20		
			for(int j = 0; j<sizeMap; j++){			//hauteur de map = 20
				int x = i;
				int y = j;
				g.setColor(Color.LIGHT_GRAY);  		 // couleur de remplissage des cases
				g.fillRect(x*35*20/sizeMap, y*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
				g.setColor(Color.white);	   		 //couleur du tour des cases
				g.drawRect(x*35*20/sizeMap, y*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
			}
		}
		
		for(GameObject object : this.objects){
			int x = object.getPosX();
			int y = object.getPosY();
			int color = object.getColor();			
			
			if(color == 0){					//définition de la couleur de remplissage de l'objet
				g.setColor(Color.PINK);
			}else if(color == 1){
				g.setColor(Color.YELLOW);
			}else if(color == 2){
				g.setColor(Color.ORANGE);
			}else if(color == 3){
				g.setColor(Color.CYAN);
			}else if(color == 4){
				g.setColor(Color.MAGENTA);
			}else if(color == 5){
				g.setColor(Color.BLUE);
			}else if(color == 6){
				g.setColor(Color.RED);
			}else if(color == 7){
				g.setColor(Color.GRAY);
			}else if(color == 8){
				g.setColor(Color.BLACK);
			}else if(color == 9){
				g.setColor(Color.WHITE);
			}else if(color == 10){
				g.setColor(Color.GREEN);
			}else if(color == 11){
				g.setColor(Color.YELLOW);
			}
			

			g.fillRect(x*35*20/sizeMap, y*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap );
			g.setColor(Color.black);			//couleur du cadre de l'objet
			g.drawRect(x*35*20/sizeMap, y*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
		}
		
		g.setColor(Color.white);			//couleur d'un rectangle d'affichage
		g.fillRect(770, 0, 93, 800);		//rectangle entre l'inventaire et "in use"
		g.fillRect(700, 0, 800, 23);		//rectangle au-dessus de l'inventaire
		g.fillRect(700, 0, 46, 800);		//rectangle à gauche de l'inventaire
		g.fillRect(700, 140, 100, 800);		//rectangle sous l'inventaire
		g.fillRect(887, 0, 700, 800);		//rectangle à droite de "in use"
		g.fillRect(860, 47, 100, 800);		//rectangle sous le "in use"
		g.fillRect(0, 700, 800, 100);		//rectangle sous le plateau de jeu
		Font font = new Font("Courier", Font.BOLD, 20); 	//Police d'écriture
		g.setFont(font);
		
		
		Player player = ((Player) objects.get(0));
		
		g.setColor(Color.black);
		g.drawString("Inventory", 705, 20);
		g.drawString("In Use", 840, 20);
		
		Font font2 = new Font("Courier", Font.BOLD, 30);
		g.setFont(font2);
		
		g.setColor(Color.red);
		g.drawString("Lifes: " + player.getLifes() , 705, 200);   //écriture du nombre de vie restant
		
		g.setColor(Color.blue);
		g.drawString("Monsters: " + numberOfMonsters, 705, 300);  //écriture du nombre de monstres restant
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawString("Bombs: " + player.getCountBomb(), 705, 400);
	}
	
	public void setObjects(ArrayList<GameObject> objects){
		this.objects = objects;
	}
	
	public void redraw(){
		this.repaint();
	}
	
	public static int getSizeMap(){
		return sizeMap;
	}
}

