package id.linkaran.game_editor.manager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;

import id.linkaran.game_editor.utils.AlertDialog;
import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.core.LevelScreen;
import id.linkaran.game_editor.core.LevelSetting;
import support.dealint.actor.Layout;

public class MenuManager {

    Manager manager;
    LevelScreen screen;
    WindowManager windowManager;
    LevelSetting setting;

    MenuBar menuBar;
    Layout layoutMenu;
    VisLabel namafile;
    VisTextButton btSave, btKembali;
    VisTextArea taInfo, taJumlahBagian;

    public MenuManager(Manager manager) {
        this.manager = manager;
        setting = manager.levelSetting;
        screen = manager.levelScreen;
        windowManager = screen.windowManager;
    }

    public void updateLayoutInfo(float w, float h, float size) {
        layoutMenu.setSize(w, h * 0.05f);
        layoutMenu.setPosition(0, h - layoutMenu.getHeight());
        layoutMenu.table.top();

        layoutMenu.table.clearChildren();
        layoutMenu.table.add(namafile).width(w*0.05f).padLeft(10);
        layoutMenu.table.add(menuBar.getTable()).expandX().fillX();
    }

    public void setMenu() {
        menuBar = new MenuBar();

        tambahFileMenu();
        tambahViewMenu();
        tambahEditMenu();

        layoutMenu = new Layout(screen.guiViewport, null);
        layoutMenu.table.setFillParent(true);
        screen.guiStage.addActor(layoutMenu);

        taInfo = new VisTextArea();
        taJumlahBagian = new VisTextArea();
        namafile = new VisLabel(manager.namaFile);
    }

    public void tambahFileMenu() {
        Menu fileMenu = new Menu("File");
        MenuItem itemSimpan = new MenuItem("Simpan");
        itemSimpan.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.dialogManager.tampilPastikanSimpan(namafile.getText().toString());
            }
        });
        fileMenu.addItem(itemSimpan);

        MenuItem itemSimpanBaru = new MenuItem("Simpan baru");
        itemSimpanBaru.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (namafile.getText().equals("")) {
                    AlertDialog dialog = new AlertDialog("Nama Kosong!", "Oke", screen.guiStage,
                            0.3f, 0.3f);
                    dialog.tampil();
                } else {
                    screen.dialogManager.tampilSimpan();
                }
            }
        });
        fileMenu.addItem(itemSimpanBaru);

        MenuItem itemLoad = new MenuItem("Load");
        itemLoad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.dialogManager.tampilLoad();
            }
        });
        fileMenu.addItem(itemLoad);


        MenuItem itemUbahUkuran = new MenuItem("Ubah Ukuran");
        itemUbahUkuran.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.dialogManager.tampilUbahUkuran();
            }
        });
        fileMenu.addItem(itemUbahUkuran);

        MenuItem itemKeluar = new MenuItem("Keluar");
        itemKeluar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.dialogManager.tampilKembali();
            }
        });
        fileMenu.addItem(itemKeluar);

        menuBar.addMenu(fileMenu);
    }

    public void tambahViewMenu() {
        Menu viewMenu = new Menu("View");
        MenuItem itemTampilObjek = new MenuItem("Objek");
        itemTampilObjek.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (windowManager.windowObjek.isVisible()) {
                    windowManager.windowObjek.setVisible(false);
                } else {
                    windowManager.windowObjek.setVisible(true);
                }
            }
        });
        viewMenu.addItem(itemTampilObjek);

        MenuItem itemTampilProperties = new MenuItem("Properties");
        itemTampilProperties.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (windowManager.windowProperties.isVisible()) {
                    windowManager.windowProperties.setVisible(false);
                } else {
                    windowManager.windowProperties.setVisible(true);
                }
            }
        });
        viewMenu.addItem(itemTampilProperties);

        MenuItem itemResetWindow = new MenuItem("Reset");
        itemResetWindow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                windowManager.resetWindow();
            }
        });
        viewMenu.addItem(itemResetWindow);

        menuBar.addMenu(viewMenu);
    }

    public void tambahEditMenu() {
        Menu editMenu = new Menu("Edit");

        MenuItem itemUndo = new MenuItem("Undo");
        itemUndo.setShortcut("Ctrl + Z");
        itemUndo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setting.undoManager.undo();
            }
        });
        editMenu.addItem(itemUndo);

        MenuItem itemRedo = new MenuItem("Redo");
        itemRedo.setShortcut("Ctrl + Y");
        itemRedo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setting.undoManager.redo();
            }
        });
        editMenu.addItem(itemRedo);

        menuBar.addMenu(editMenu);
    }
}
