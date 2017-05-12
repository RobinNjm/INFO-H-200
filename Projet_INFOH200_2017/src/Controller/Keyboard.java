package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Game;

public class Keyboard implements KeyListener{
	private Game game;
	
	private static final int player1 = 0;
	private int selectedItem = 1;
	public Keyboard(Game game){
		this.game = game;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		
		switch (key){
			case KeyEvent.VK_D: 
				game.movePlayer(1, 0, player1);
				break;
			case KeyEvent.VK_Q:
				game.movePlayer(-1, 0, player1);
				break;
			case KeyEvent.VK_S:
				game.movePlayer(0, 1, player1);
				break;
			case KeyEvent.VK_Z:
				game.movePlayer(0, -1, player1);
				break;	
			case KeyEvent.VK_SPACE:
				game.dropBomb(player1);
				break;
			case KeyEvent.VK_P:
				game.unlimitedBombs(player1);
				break;
			case KeyEvent.VK_RIGHT:
				game.attack(1, 0, player1);
				break;
			case KeyEvent.VK_LEFT:
				game.attack(-1, 0, player1);
				break;
			case KeyEvent.VK_DOWN:
				game.attack(0, 1, player1);
				break;
			case KeyEvent.VK_UP:
				game.attack(0, -1, player1);
				break;
			case KeyEvent.VK_CONTROL:
				game.swapAttack();
				break;
			case KeyEvent.VK_ADD:
				game.pickItem(player1);
				break;
			case KeyEvent.VK_NUMPAD1:
				this.selectedItem = 1;
				break;
			case KeyEvent.VK_NUMPAD2:
				this.selectedItem = 2;
				break;
			case KeyEvent.VK_NUMPAD3:
				this.selectedItem = 3;
				break;
			case KeyEvent.VK_NUMPAD4:
				this.selectedItem = 4;
				break;
			case KeyEvent.VK_NUMPAD5:
				this.selectedItem = 5;
				break;
			case KeyEvent.VK_SUBTRACT:
				game.dropItem(player1, selectedItem - 1);
				this.selectedItem = 1;
				break;
			case KeyEvent.VK_ENTER:
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