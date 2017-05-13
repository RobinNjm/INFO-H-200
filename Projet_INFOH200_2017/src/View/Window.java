package View;
import Model.GameObject;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window {
	public Map map = new Map();
	private BufferedImage losingImage;
	
	public Window(){	    
	    JFrame window = new JFrame("Best Game Ever");       //cr�ation d'une fen�tre contenant le jeu
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(0, 0, 1366, 768);                  // taille de la fen�tre
	    window.setResizable(false);							//interdit de changer la taille de la fen�tre
	    window.getContentPane().setBackground(Color.gray);	//couleur de fond
	    window.getContentPane().add(this.map);				//ajout de la map sur la fen�tre
	    window.setVisible(true);							//visibilit� de la fen�tre
	    try {
			losingImage = ImageIO.read(getClass().getResourceAsStream("/Images/losingDeadpool.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	public void setNumberOfMonsters(int num){
		map.numberOfMonsters = num;
	}
	
	public void update(ArrayList<GameObject> objects){
		this.map.setObjects(objects);
		this.map.redraw();
	}
	
	public void setKeyListener(KeyListener keyboard){
	    this.map.addKeyListener(keyboard);
	}

	public void gameOver() {
		
		/*fonction lanc�e dans le cas o� le joueur n'a plus de vies*/
		
		JFrame win = new JFrame("Game Over");				//nouvelle fen�tre appel�e game over
	    win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    win.setBounds(100, 0, 500, 773);					//taille
	    win.setResizable(false);
	    
	    win.getContentPane().add(new JLabel(new ImageIcon(losingImage)));
	    
	    win.setVisible(true);
	}
}