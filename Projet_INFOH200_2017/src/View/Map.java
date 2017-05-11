package View;
import Model.GameObject;
import Model.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<GameObject> objects;

	protected int numberOfMonsters;
	private static final int sizeMap = 20;
	private int tileSize = 35*20/sizeMap;
	public int levelNumber = 0;
	
	private BufferedImage bomb ;
	private BufferedImage enemy;
	private BufferedImage player;
	private BufferedImage instantHeal;
	private BufferedImage healOverTime;
	private BufferedImage wall;
	private BufferedImage breakableBlock;
	private BufferedImage explosion;
	private BufferedImage teleporterItem;
	private BufferedImage invulnerabilityItem;
	
	public Map(){
		this.setFocusable(true);
		this.requestFocusInWindow();
		try {
			this.bomb = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/bomb.png")));
			this.enemy = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/enemy.png")));
			this.player = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/player.png")));
			this.instantHeal = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/LifePotion.png")));
			this.healOverTime = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/LifePotion2.png")));
			this.wall = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/Wall.png")));
			this.breakableBlock = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/breakableBlock.jpg")));
			this.explosion = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/explosion.png")));
			this.teleporterItem = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/chimichanga.png")));
			this.invulnerabilityItem = scaleImage(ImageIO.read(getClass().getResourceAsStream("/Images/invulnerabilityItem.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void paint(Graphics g) { 
		for(int i = 0; i<sizeMap; i++){				//largeur de map = sizeMap		
			for(int j = 0; j<sizeMap; j++){			//hauteur de map = sizeMap
				g.setColor(Color.LIGHT_GRAY);  		 // couleur de remplissage des cases
				g.fillRect(i*35*20/sizeMap, j*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
				g.setColor(Color.white);	   		 //couleur du tour des cases
				g.drawRect(i*35*20/sizeMap, j*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
			}
		}
		
		//inventaire
		for(int i = 1; i < 6; i++){
			g.setColor(Color.LIGHT_GRAY);  		 // couleur de remplissage des cases
			g.fillRect((sizeMap + 2)*35*20/sizeMap, i*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
			g.setColor(Color.white);	   		 //couleur du tour des cases
			g.drawRect((sizeMap + 2)*35*20/sizeMap, i*35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
		}
		
		//in use
		g.setColor(Color.LIGHT_GRAY);  		 // couleur de remplissage des cases
		g.fillRect((sizeMap + 7)*35*20/sizeMap, 35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
		g.setColor(Color.white);	   		 //couleur du tour des cases
		g.drawRect((sizeMap + 7)*35*20/sizeMap, 35*20/sizeMap, 35*20/sizeMap , 35*20/sizeMap ); 
		
		for(GameObject object : this.objects){
			int x = object.getPosX();
			int y = object.getPosY();
			int color = object.getColor();			
			
			if(color == 1){
				g.drawImage(player, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 2){
				g.drawImage(enemy, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 3){
				g.drawImage(instantHeal, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 4){
				g.drawImage(healOverTime, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 5){
				g.drawImage(teleporterItem, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 6){
				g.drawImage(bomb, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 7){
				g.drawImage(breakableBlock, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 8){
				g.drawImage(wall, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 9){
				g.drawImage(invulnerabilityItem, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}else if(color == 12){
				g.drawImage(explosion, x*35*20/sizeMap, y*35*20/sizeMap, null);
			}			
		}
		
		g.setColor(Color.white);			//couleur d'un rectangle d'affichage
		
		g.fillRect(700, tileSize, 2*tileSize, 5*tileSize);				//rectangle à gauche de l'inventaire
		g.fillRect(700 + 3*tileSize, tileSize, 4*tileSize, tileSize);	//rectangle entre l'inventaire et "in use"
		g.fillRect(700, 0, 800, tileSize);								//rectangle au-dessus de l'inventaire et de "in use"
		g.fillRect(700 + 3*tileSize, 2*tileSize, 800, 4*tileSize);		//rectangle sous "in use"
		g.fillRect(700 + 8*tileSize, tileSize, 800, tileSize);			//rectangle à droite de "in use"
		g.fillRect(700, 6*tileSize, 800, 800);							//rectangle derrière les compteurs de vie, monstre,...

		Font font = new Font("Courier", Font.BOLD, 25); 			//Police d'écriture
		g.setFont(font);
		
		
		Player player = ((Player) objects.get(0));
		
		g.setColor(Color.black);
		g.drawString("Inventory", 705, 20);
		g.drawString("In Use", 700 + 6*tileSize, 20);
		g.drawString("Level: " + levelNumber, 700 + 11*tileSize, 20);
		
		Font font2 = new Font("Courier", Font.BOLD, 30);
		g.setFont(font2);
		
		g.setColor(Color.red);
		g.drawString("Lifes: " + player.getLifes() , 710, 8*tileSize);   //écriture du nombre de vie restant
		
		g.setColor(Color.blue);
		g.drawString("Monsters: " + numberOfMonsters, 710, 10*tileSize);  //écriture du nombre de monstres restant
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawString("Bombs: " + player.getCountBomb(), 710, 12*tileSize);
	}
	
	private BufferedImage scaleImage(BufferedImage image){
		BufferedImage scaledImage = new BufferedImage(tileSize, tileSize, image.getType());
		Graphics2D graphics = scaledImage.createGraphics();
		graphics.drawImage(image, 0, 0, tileSize, tileSize, null);
		graphics.dispose();
		return scaledImage;
	}
	
	public synchronized void setObjects(ArrayList<GameObject> objects){
		this.objects = objects;
	}
	
	public void redraw(){
		this.repaint();
	}
	
	public static int getSizeMap(){
		return sizeMap;
	}
}

