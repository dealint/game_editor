package id.linkaran.game_editor.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;

import id.linkaran.game_editor.GameAsset;
import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.actor.Cursor;
import id.linkaran.game_editor.actor.SelectionTools;
import id.linkaran.game_editor.manager.DialogManager;
import id.linkaran.game_editor.manager.MenuManager;
import id.linkaran.game_editor.manager.WindowManager;
import support.dealint.actor.Layout;
import support.dealint.manager.GameScreen;
import support.dealint.manager.GameUtils;

public class LevelScreen extends GameScreen implements Manager.HasManager {

    Manager manager;
    LevelSetting setting;
    public DialogManager dialogManager;
    public MenuManager menuManager;
    public WindowManager windowManager;

    Image background;
    VisScrollPane scrollPane;
    public Cursor cursor;
    public Layout mainLayout;

    public LevelScreen(LevelSetting levelSetting, LevelInput levelInput) {
        super(levelSetting, null, levelInput);

        setting = levelSetting;
    }

    @Override
    public void setManager(Manager manager) {
        this.manager = manager;
        dialogManager = new DialogManager(manager);
        windowManager = new WindowManager(manager);
        menuManager = new MenuManager(manager);
    }

    @Override
    public void setInput() {
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        inputMultiplexer.addProcessor(guiStage);
        inputMultiplexer.addProcessor(input);
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void setStage() {
        background = new Image(GameUtils.getTexture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), GameAsset.COLOR_BACKGROUND_DARK));
        bgStage.addActor(background);

        mainLayout = new Layout(viewport, Color.LIGHT_GRAY);
        mainLayout.setSize(manager.getWidth(), manager.getHeight());

        setting.arrMap = new Array<Layout>();

        scrollPane = new VisScrollPane(mainLayout);
        scrollPane.setSize(worldWidth, worldHeight);
        scrollPane.setFlickScroll(false);
        scrollPane.setScrollPercentY(100);
        stage.addActor(scrollPane);
        stage.setScrollFocus(scrollPane);
    }

    @Override
    public void setGuiStage() {
        menuManager.setMenu();
        windowManager.setWindows();
        windowManager.resetWindow();

        setCursor();

        if (!manager.baru) {
            setting.loadDaftarNama();
            setting.load(true,"");
        } else {
            try {
                baru();
            } catch (Exception e) {
                System.out.println("daftar nama xml belum ada");
            }
        }
    }

    private void baru() {
        setting.arrBenda.clear();
        setting.loadDaftarNama();
    }

    private void setCursor() {
        cursor = new Cursor(manager, GameUtils.getTexture(100, 100, Color.BLUE));
        cursor.setSize(setting.bendaW, setting.bendaH);
        cursor.setVisible(false);
        stage.addActor(cursor);

        setting.selectionTools = new SelectionTools(viewport);
        setting.selectionTools.setVisible(false);
        stage.addActor(setting.selectionTools);
    }

    @Override
    public void updateStage(float w, float h, float size) {
        menuManager.updateLayoutInfo(w, h, size);

        windowManager.updateWindowObjek(w, h, size);
        windowManager.updateWindowProperties(w, h, size);
    }

    @Override
    public void dispose() {

    }
}