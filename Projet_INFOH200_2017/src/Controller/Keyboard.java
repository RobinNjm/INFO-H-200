package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Game;

public class Keyboard implements KeyListener{
	private Game game;
	
	private static final int player1 = 0;
	private int selectedItem = 1;			//attribut permettant de savoir sur quel ojet de l'inventaire appliquer l'action
	
	public Keyboard(Game game){
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		
		switch (key){
			case KeyEvent.VK_D:						//D ----> mouvement du joueur vers la droite
				game.movePlayer(1, 0, player1);
				break;
			case KeyEvent.VK_Q:						//Q ----> mouvement du joueur vers la gauche
				game.movePlayer(-1, 0, player1);
				break;
			case KeyEvent.VK_S:						//S ----> mouvement du joueur vers le bas
				game.movePlayer(0, 1, player1);
				break;
			case KeyEvent.VK_Z:						//Z ----> mouvement du joueur vers le haut
				game.movePlayer(0, -1, player1);
				break;	
			case KeyEvent.VK_SPACE:					//SPACE ----> enclenchement d'une bombe
				game.dropBomb(player1);
				break;
			case KeyEvent.VK_RIGHT:					//FLECHE DROITE ----> attaque vers la droite
				game.attack(1, 0, player1);
				break;
			case KeyEvent.VK_LEFT:					//FLECHE GAUCHE ----> attaque vers la gauche
				game.attack(-1, 0, player1);
				break;
			case KeyEvent.VK_DOWN:					//FLECHE DU BAS ----> attaque vers le bas
				game.attack(0, 1, player1);
				break;
			case KeyEvent.VK_UP:					//FLECHE DU HAUT ----> attaque vers le haut
				game.attack(0, -1, player1);
				break;
			case KeyEvent.VK_CONTROL:				//CTRL ----> changement d'arme
				game.swapAttack();
				break;
			case KeyEvent.VK_ADD:					//+ DU PAVE NUMERIQUE ----> ajouter l'objet sur la case à l'inventaire
				game.pickItem(player1);
				break;
			case KeyEvent.VK_NUMPAD1:				//1 DU PAVE NUMERIQUE ----> sélectionner le premier objet de l'inventaire
				this.selectedItem = 1;
				break;
			case KeyEvent.VK_NUMPAD2:				//2 DU PAVE NUMERIQUE ----> sélectionner le deuxième objet de l'inventaire
				this.selectedItem = 2;
				break;
			case KeyEvent.VK_NUMPAD3:				//3 DU PAVE NUMERIQUE ----> sélectionner le troisième objet de l'inventaire
				this.selectedItem = 3;
				break;
			case KeyEvent.VK_NUMPAD4:				//4 DU PAVE NUMERIQUE ----> sélectionner le quatrième objet de l'inventaire
				this.selectedItem = 4;
				break;
			case KeyEvent.VK_NUMPAD5:				//5 DU PAVE NUMERIQUE ----> sélectionner le cinquième objet de l'inventaire
				this.selectedItem = 5;
				break;
			case KeyEvent.VK_SUBTRACT:				//- DU PAVE NUMERIQUE ----> supprimer l'objet sélectionné (1 par défaut)
				game.dropItem(player1, selectedItem - 1);
				this.selectedItem = 1;
				break;
			case KeyEvent.VK_ENTER:					//ENTER ----> utiliser l'objet sélectionné (1 par défaut)
			 	game.useItem(player1, selectedItem - 1);
			 	this.selectedItem = 1;
			 	break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}