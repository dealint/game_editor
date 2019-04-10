package support.dealint.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class GameManager {

    public AssetManager assetManager;
    public Game game;
    public Skin skin;

    public GameManager() {
        assetManager = new AssetManager();
    }

    public abstract void loadAsset();

    public abstract void loadSkin();
}