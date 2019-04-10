package id.linkaran.game_editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import id.linkaran.game_editor.utils.AlertDialog;
import id.linkaran.game_editor.utils.SimpleDialog;
import support.dealint.actor.Layout;
import support.dealint.manager.GameScreen;
import support.dealint.manager.GameUtils;

public class MenuScreen extends GameScreen {

    private Manager manager;
    private Image background;

    public VisTextButton btNew, btLoad, btHorizontal;
    public VisTextField tfNama;
    public VisLabel lbScreen;
    public Layout layoutMenu;

    public MenuScreen(Manager manager, float worldWidth, float worldHeight) {
        super(worldWidth, worldHeight);
        this.manager = manager;
    }

    @Override
    public void setInput() {
        super.setInput();
        inputMultiplexer.addProcessor(guiStage);
    }

    @Override
    public void setStage() {
        background = new Image(GameUtils.getTexture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), GameAsset.COLOR_BACKGROUND_DARK));
        bgStage.addActor(background);
    }

    @Override
    public void setGuiStage() {
        setLayoutMenu();
    }

    private void setLayoutMenu() {
        layoutMenu = new Layout(guiViewport, GameAsset.COLOR_BACKGROUND_LIGHT);
        layoutMenu.table.setFillParent(true);
        guiStage.addActor(layoutMenu);

        layoutMenu.table.setFillParent(true);
        guiStage.addActor(layoutMenu.table);
        layoutMenu.table.setFillParent(true);

        btHorizontal = new VisTextButton("horizontal");
        btHorizontal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (btHorizontal.getText().toString().equals("horizontal")) {
                    btHorizontal.setText("vertical");
                    manager.horizontal = false;
                } else {
                    btHorizontal.setText("horizontal");
                    manager.horizontal = true;
                }
            }
        });

        btNew = new VisTextButton("New");
        btNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tampilDialog(true);
            }
        });

        btLoad = new VisTextButton("Load");
        btLoad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tampilDialog(false);
            }
        });

        tfNama = new VisTextField(manager.defaultName);
        lbScreen = new VisLabel("");
    }

    private void lanjut() {
        manager.namaFile = tfNama.getText().trim();
        manager.setScreen();
    }

    @Override
    public void updateStage(float w, float h, float size) {
        layoutMenu.table.pad(size * 0.3f);
        layoutMenu.table.clearChildren();
        layoutMenu.table.add(lbScreen).width(w * 0.25f).colspan(2).padBottom(h * 0.03f).row();
        layoutMenu.table.add(tfNama).width(w * 0.25f).colspan(2).padBottom(h * 0.03f).row();

        layoutMenu.table.add(btHorizontal).width(w * 0.25f).colspan(2).padBottom(h * 0.05f).row();

        layoutMenu.table.add(btNew).width(w * 0.12f).padRight(w * 0.01f).padBottom(h * 0.03f);
        layoutMenu.table.add(btLoad).width(w * 0.12f).padBottom(h * 0.03f).row();
    }

    private void tampilDialog(final boolean baru) {
        final SimpleDialog simpleDialog;
        if (baru) {
            simpleDialog = new SimpleDialog("New?", "Ya", "Cancel", guiViewport,
                    0.5f, 0.2f);
        } else {
            simpleDialog = new SimpleDialog("Load?", "Ya", "Cancel", guiViewport,
                    0.5f, 0.2f);
        }

        simpleDialog.btPositive.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.baru = baru;
                if (baru) {
                    lanjut();
                } else {
                    if (fileAda(tfNama.getText().trim())) {
                        lanjut();
                    } else {
                        AlertDialog dialog = new AlertDialog("File tidak ada", "Oke",
                                guiStage, 0.4f, 0.5f);
                        dialog.tampil();
                    }
                }
                simpleDialog.remove();
                return true;
            }
        });

        simpleDialog.btNegative.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                simpleDialog.remove();
                return true;
            }
        });

        simpleDialog.show(guiStage);
    }

    public static boolean fileAda(String nama) {
        boolean ada = false;
        String path = Gdx.files.local("xml/dealint_editor/" + nama + ".xml").path();
        FileHandle file = Gdx.files.local(path);
        ada = file.exists();
        return ada;
    }

    @Override
    public void dispose() {

    }
}
