package View;
import Model.GameObject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window {
	private Map map = new Map();
	
	public Window(){	    
	    JFrame window = new JFrame("Best Game Ever");       //création d'une fenêtre contenant le jeu
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(0, 0, 1366, 768);                  // taille de la fenêtre
	    window.setResizable(false);							//interdit de changer la taille de la fenêtre
	    window.getContentPane().setBackground(Color.gray);	//couleur de fond
	    window.getContentPane().add(this.map);				//ajout de la map sur la fenêtre
	    window.setVisible(true);							//visibilité de la fenêtre
	    
	}

	
	synchronized public void setGameObjects(ArrayList<GameObject> objects){
		/* Sert à mettre la map à jour une fois que la liste d'objets a changé*/
		this.map.setObjects(objects);
		this.map.redraw();
	}
	
	public void setNumberOfMonsters(int num){
		map.numberOfMonsters = num;
	}
	
	public void update(){
		this.map.redraw();
	}
	
	public void setKeyListener(KeyListener keyboard){
	    this.map.addKeyListener(keyboard);
	}


	public void gameOver() {
		
		/*fonction lancée dans le cas où le joueur n'a plus de vies*/
		
		JFrame win = new JFrame("Game Over");				//nouvelle fenêtre appelée game over
	    win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    win.setBounds(15, 35, 530, 200);					//taille
	    win.setResizable(false);
	    win.getContentPane().setBackground(Color.red);		//couleur du fond
	    
	    Font font = new Font("TimesRoman", Font.BOLD, 100);	//police
	    
	    JLabel label = new JLabel("YOU LOSE", JLabel.CENTER);//création de ce qui va être affiché
	    label.setFont(font);

	    win.add(label);										//ajout du label à la fenêtre
	    
	    win.setVisible(true);
	}
}