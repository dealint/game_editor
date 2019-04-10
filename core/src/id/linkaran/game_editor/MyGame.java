package id.linkaran.game_editor;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
	Manager manager;

	public MyGame() {

	}

	@Override
	public void create() {
		manager = new Manager();
		manager.game = this;
		setScreen(manager.menuScreen);
	}
}

