package id.linkaran.game_editor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;

import id.linkaran.game_editor.core.LevelInput;
import id.linkaran.game_editor.core.LevelScreen;
import id.linkaran.game_editor.core.LevelSetting;
import support.dealint.manager.GameManager;

public class Manager extends GameManager {

    public MenuScreen menuScreen;

    //state
    public String namaFile;
    public boolean baru = true;
    public String defaultName = "level";

    public boolean horizontal = true;
    public int panjang = 1000;
    public int lebar = 650;

    //level editor
    public LevelScreen levelScreen;
    public LevelSetting levelSetting;
    public LevelInput levelInput;

    public Manager() {
        super();
        VisUI.load();
        menuScreen = new MenuScreen(this, 1000, 600);
    }

    public int getWidth() {
        int w;
        if (horizontal) {
            w = panjang;
        } else {
            w = lebar;
        }
        return w;
    }

    public int getHeight() {
        int h;
        h = lebar;
        if (horizontal) {
        } else {
            h = panjang;
        }
        return h;
    }

    public void setScreen() {
        levelSetting = new LevelSetting();
        levelSetting.worldWidth = getWidth();
        levelSetting.worldHeight = getHeight();
        levelInput = new LevelInput();
        levelScreen = new LevelScreen(levelSetting, levelInput);
        levelSetting.setManager(this);
        levelInput.setManager(this);
        levelScreen.setManager(this);
        game.setScreen(levelScreen);
    }

    @Override
    public void loadAsset() {
        assetManager.load(GameAsset.BACKGROUND, Texture.class);
        assetManager.load(GameAsset.UISKIN, Skin.class);
        assetManager.finishLoading();
    }

    @Override
    public void loadSkin() {
        skin = assetManager.get(GameAsset.UISKIN);
    }

    public interface HasManager {
        void setManager(Manager manager);
    }
}