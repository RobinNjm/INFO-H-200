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
	    JFrame window = new JFrame("Best Game Ever");       //cr�ation d'une fen�tre contenant le jeu
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(0, 0, 1366, 768);                  // taille de la fen�tre
	    window.setResizable(false);							//interdit de changer la taille de la fen�tre
	    window.getContentPane().setBackground(Color.gray);	//couleur de fond
	    window.getContentPane().add(this.map);				//ajout de la map sur la fen�tre
	    window.setVisible(true);							//visibilit� de la fen�tre
	    
	}

	
	synchronized public void setGameObjects(ArrayList<GameObject> objects){
		/* Sert � mettre la map � jour une fois que la liste d'objets a chang�*/
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
		
		/*fonction lanc�e dans le cas o� le joueur n'a plus de vies*/
		
		JFrame win = new JFrame("Game Over");				//nouvelle fen�tre appel�e game over
	    win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    win.setBounds(15, 35, 530, 200);					//taille
	    win.setResizable(false);
	    win.getContentPane().setBackground(Color.red);		//couleur du fond
	    
	    Font font = new Font("TimesRoman", Font.BOLD, 100);	//police
	    
	    JLabel label = new JLabel("YOU LOSE", JLabel.CENTER);//cr�ation de ce qui va �tre affich�
	    label.setFont(font);

	    win.add(label);										//ajout du label � la fen�tre
	    
	    win.setVisible(true);
	}
}