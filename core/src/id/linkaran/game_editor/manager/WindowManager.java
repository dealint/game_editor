package id.linkaran.game_editor.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.core.LevelScreen;
import id.linkaran.game_editor.core.LevelSetting;
import support.dealint.actor.Layout;
import support.dealint.manager.GameUtils;

public class WindowManager {

    Manager manager;
    LevelSetting setting;
    LevelScreen screen;

    public Layout layoutWindowProperties, layoutWindowObjek;
    VisWindow windowObjek;
    VisWindow windowProperties;

    public VisSelectBox sbObjek;
    VisTextButton btObjek, btSalin;
    VisTextButton btUbahObjek;
    Array<VisTextButton> arrTools;



    //layout properties
    VisTextField tfWidth, tfHeight;

    public WindowManager(Manager manager) {
        this.manager = manager;
        setting = manager.levelSetting;
        screen = manager.levelScreen;
    }

    public void setWindows() {
        setWindowObjek();
        setWindowProperties();
    }

    /**
     * 0 : tambah objeck
     * 1 : salin
     * 2 : pilih objek
     * @param state
     */
    public void objekState(int state) {

        if (state == 0) {
            screen.cursor.setVisible(true);
            screen.cursor.setSize(setting.bendaW, setting.bendaH);
            screen.cursor.texture = new Texture(setting.arrImgObjek.get(sbObjek.getSelectedIndex()));
            setting.state = LevelSetting.State.OBJEK;
            setting.jenisObjek = sbObjek.getSelectedIndex();
        } else if (state == 1) {
            if (setting.bendaDipilih != null) {
                screen.cursor.setVisible(true);
                screen.cursor.setSize(setting.bendaDipilih.getWidth(), setting.bendaDipilih.getHeight());
                screen.cursor.texture = setting.bendaDipilih.texture;
                setting.state = LevelSetting.State.SALIN;
            }
        } else {
            screen.cursor.setVisible(false);
            setting.state = LevelSetting.State.PILIH;
        }
    }

    private void setWindowObjek() {
        windowObjek = new VisWindow("Objek", true);
        windowObjek.setResizable(true);
        screen.guiStage.addActor(windowObjek);

        layoutWindowObjek = new Layout(screen.viewport, null);
        layoutWindowObjek.table.setFillParent(true);
        layoutWindowObjek.table.left();
        layoutWindowObjek.table.bottom();
        windowObjek.addActor(layoutWindowObjek);

        sbObjek = new VisSelectBox();
        sbObjek.setItems(setting.arrNamaObjek);

        arrTools = new Array<VisTextButton>();

        btObjek = new VisTextButton("+");
        btObjek.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                objekState(0);
            }
        });

        btSalin = new VisTextButton("Salin");
        btSalin.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                objekState(1);
            }
        });

        arrTools.add(btObjek);

        btUbahObjek = new VisTextButton("Ubah");
        btUbahObjek.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.dialogManager.tampilUbahBenda(sbObjek);
            }
        });
    }

    private void setWindowProperties() {
        windowProperties = new VisWindow("Properties", true);
        windowProperties.setResizable(true);
        screen.guiStage.addActor(windowProperties);

        layoutWindowProperties = new Layout(screen.guiViewport, null);
        layoutWindowProperties.table.setFillParent(true);
        layoutWindowProperties.table.left();
        layoutWindowProperties.table.bottom();
        windowProperties.addActor(layoutWindowProperties);


        tfWidth = new VisTextField("50");
        tfHeight = new VisTextField("50");

        tfWidth.setTextFieldListener(new VisTextField.TextFieldListener() {
            @Override
            public void keyTyped(VisTextField textField, char c) {
                if (GameUtils.isNumeric(tfWidth.getText())) {
                    setting.bendaW = Float.parseFloat(tfWidth.getText());
                    if (setting.bendaDipilih != null) {
                        setting.bendaDipilih.setWidth(Float.parseFloat(tfWidth.getText()));
                    }
                }
            }
        });

        tfHeight.setTextFieldListener(new VisTextField.TextFieldListener() {
            @Override
            public void keyTyped(VisTextField textField, char c) {
                if (GameUtils.isNumeric(tfHeight.getText())) {
                    setting.bendaH = Float.parseFloat(tfHeight.getText());
                    if (setting.bendaDipilih != null) {
                        setting.bendaDipilih.setHeight(Float.parseFloat(tfHeight.getText()));
                    }
                }
            }
        });
    }


    public void updateWindowObjek(float w, float h, float size) {
        resetWindow();

        layoutWindowObjek.table.padLeft(10);
        layoutWindowObjek.table.clearChildren();
        layoutWindowObjek.table.add(sbObjek).width(windowObjek.getWidth() * 0.6f)
                .padBottom(10).padRight(5);
        layoutWindowObjek.table.add(btObjek).width(windowObjek.getWidth() * 0.2f)
                .padBottom(10).row();
        layoutWindowObjek.table.add(btUbahObjek).width(windowObjek.getWidth() * 0.9f)
                .padBottom(10).colspan(2).row();

        layoutWindowObjek.table.add(btSalin).width(windowObjek.getWidth() * 0.9f)
                .padBottom(10).colspan(2);
    }

    public void updateWindowProperties(float w, float h, float size) {
        resetWindow();

        layoutWindowProperties.table.clearChildren();
        layoutWindowProperties.table.add(tfWidth).width(layoutWindowProperties.getWidth()*0.4f).padBottom(10).padRight(10);
        layoutWindowProperties.table.add(tfHeight).width(layoutWindowProperties.getWidth()*0.4f).padBottom(10).row();
    }

    public void resetWindow() {
        windowObjek.setSize(Gdx.graphics.getWidth()*0.12f, Gdx.graphics.getHeight()*0.2f);
        windowProperties.setSize(Gdx.graphics.getWidth()*0.12f, Gdx.graphics.getHeight()*0.3f);
        windowObjek.setPosition(0, Gdx.graphics.getHeight()-windowObjek.getHeight()-screen.menuManager.namafile.getHeight());
        windowProperties.setPosition(0, windowObjek.getY()-windowProperties.getHeight());
    }
}
