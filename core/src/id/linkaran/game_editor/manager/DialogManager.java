package id.linkaran.game_editor.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import id.linkaran.game_editor.MenuScreen;
import id.linkaran.game_editor.utils.AlertDialog;
import id.linkaran.game_editor.utils.InputDialog;
import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.utils.SimpleDialog;
import id.linkaran.game_editor.actor.Benda;
import id.linkaran.game_editor.core.LevelScreen;
import id.linkaran.game_editor.core.LevelSetting;
import support.dealint.manager.GameUtils;

public class DialogManager {

    private Manager manager;
    private LevelSetting setting;
    private LevelScreen screen;

    public DialogManager(Manager manager) {
        this.manager = manager;
        this.setting = manager.levelSetting;
        this.screen = manager.levelScreen;
    }

    public void cariGambar(final VisImage image, final Array<FileHandle> arr, final String str, final VisSelectBox sb) {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES_AND_DIRECTORIES);
        FileTypeFilter ff = new FileTypeFilter(true);
        ff.addRule("png", "png");
        fileChooser.setFileTypeFilter(ff);

        screen.guiStage.addActor(fileChooser.fadeIn());
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle fh = Gdx.files.local("xml/dealint_editor/image/" + str + daftarNamaIndex + ".png");
                file.first().copyTo(fh);
                arr.set(daftarNamaIndex, fh);
                image.setDrawable(new Texture(file.first()));
                for (Benda benda : setting.arrBenda) {
                    if (benda.nama.equals(sb.getSelected())) {
                        benda.texture = new Texture(file.first());
                    }
                }
            }
        });
    }

    private int daftarNamaIndex = 0;

    public void tampilUbahBenda(final SelectBox selectBox) {
        daftarNamaIndex = 0;
        final VisTextField tfNama = new VisTextField("");
        VisTextButton btTambah, btUbah, btHapus;
        VisTable table1 = new VisTable();

        VisTable table2 = new VisTable();
        final VisImage image = new VisImage();
        VisTextButton btSave, btBatal, btUbahGambar;

        final VisDialog dialog;
        final VisSelectBox sb = new VisSelectBox();

        final Array<String> arrNama;
        final Array<FileHandle> arrImg;
        final String str;

        dialog = new VisDialog("Ubah Nama Objek?");
        arrNama = setting.arrNamaObjek;
        arrImg = setting.arrImgObjek;
        str = "objek";

        float w = Gdx.graphics.getWidth() * 0.7f;
        float h = Gdx.graphics.getHeight() * 0.7f;
        float w2 = w / 2;

        dialog.pad(w * 0.05f);
        dialog.add(table1).padRight(w * 0.05f).width(w / 2).height(h * 0.9f).top();
        dialog.add(table2).width(w / 2).height(h * 0.9f);

        //table 1
        sb.setItems(arrNama);
        sb.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tfNama.setText(sb.getSelected() + "");
                daftarNamaIndex = sb.getSelectedIndex();
                image.setDrawable(new Texture(arrImg.get(daftarNamaIndex)));
            }
        });

        btSave = new VisTextButton("Simpan");
        btSave.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setting.simpanDaftarNama();
                dialog.hide();
                return true;
            }
        });
        btUbahGambar = new VisTextButton("Ubah Gambar");
        btUbahGambar.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                cariGambar(image, arrImg, str, sb);
                return true;
            }
        });

        btBatal = new VisTextButton("Batal");
        btBatal.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dialog.hide();
                return true;
            }
        });

        image.setDrawable(GameUtils.getTexture(100, 100, Color.WHITE));

        table1.add(btSave).width(w2).padBottom(h * 0.05f).row();
        table1.add(btBatal).width(w2).padBottom(h * 0.05f).row();
        table1.add(sb).width(w2).padBottom(h * 0.05f).row();
        table1.add(image).width(w2 / 2).height(w2 / 2).padBottom(h * 0.02f).row();
        table1.add(btUbahGambar).width(w2);

        //table 2
        tfNama.setText(arrNama.first());
        btTambah = new VisTextButton("Tambah");
        btTambah.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (arrNama.contains(tfNama.getText(), false)) {
                    AlertDialog alertDialog = new AlertDialog("Data sudah ada!", "Ya",
                            screen.guiStage, 0.5f, 0.5f);
                    alertDialog.tampil();

                    alertDialog.show(screen.guiStage);
                } else {
                    arrNama.add(tfNama.getText());
                    selectBox.setItems(arrNama);
                    sb.setItems(arrNama);
                    arrImg.add(Gdx.files.internal("sprite/" + str + ".png"));
                }
                return true;
            }
        });

        btUbah = new VisTextButton("Ubah");
        btUbah.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                arrNama.set(daftarNamaIndex, tfNama.getText());
                selectBox.setItems(arrNama);
                sb.setItems(arrNama);
                for (Benda benda : setting.arrBenda) {
                    benda.update();
                }
                return true;
            }
        });

        btHapus = new VisTextButton("Hapus");
        btHapus.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (arrNama.size > 1) {
                    arrNama.removeIndex(daftarNamaIndex);
                    selectBox.setItems(arrNama);
                    sb.setItems(arrNama);
                }
                return true;
            }
        });

        table2.add(tfNama).width(w2)
                .padBottom(w * 0.02f).row();
        table2.add(btTambah).width(w2)
                .padBottom(w * 0.01f).row();
        table2.add(btUbah).width(w2)
                .padBottom(w * 0.01f).row();
        table2.add(btHapus).width(w2)
                .padBottom(w * 0.01f);

        dialog.fadeIn();
        dialog.centerWindow();
        dialog.setMovable(false);
        dialog.show(screen.guiStage);
    }

    public void tampilKembali() {
        final SimpleDialog simpleDialog = new SimpleDialog("Kembali?", "Ya", "Batal",
                screen.guiViewport, 0.3f, 0.1f);

        simpleDialog.btPositive.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.game.setScreen(manager.menuScreen);
                return true;
            }
        });

        simpleDialog.btNegative.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                simpleDialog.hide();
                return true;
            }
        });
        simpleDialog.tampil(screen.guiStage);
    }

    public void tampilSimpan() {
        final InputDialog dialog = new InputDialog("Simpan?", "Ya", "Batal",
                screen.guiViewport, 0.3f, 0.1f);

        dialog.btPositive.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String text = dialog.textField.getText();
                if (!text.equals("")) {
                    tampilPastikanSimpan(text);
                    dialog.hide();

                } else {
                    AlertDialog dialog = new AlertDialog("Nama tidak boleh kosong", "Oke",
                            screen.guiStage, 0.4f, 0.5f);
                    dialog.tampil();
                }

                return true;
            }
        });
        dialog.btNegative.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dialog.hide();
                return true;
            }
        });
        dialog.fadeIn();
        dialog.centerWindow();
        dialog.setMovable(false);
        dialog.show(screen.guiStage);
    }

    public void tampilLoad() {
        final InputDialog dialog = new InputDialog("Load?", "Ya", "Batal",
                screen.guiViewport, 0.3f, 0.1f);

        dialog.btPositive.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String text = dialog.textField.getText();
                if (!text.equals("")) {
                    if (MenuScreen.fileAda(text)) {
                        tampilPastikanLoad(text);
                        dialog.hide();
                    } else {
                        AlertDialog dialog = new AlertDialog("File tidak ada", "Oke",
                                screen.guiStage, 0.4f, 0.5f);
                        dialog.tampil();
                    }
                } else {
                    AlertDialog dialog = new AlertDialog("Nama tidak boleh kosong", "Oke",
                            screen.guiStage, 0.4f, 0.5f);
                    dialog.tampil();
                }

                return true;
            }
        });
        dialog.btNegative.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dialog.hide();
                return true;
            }
        });
        dialog.fadeIn();
        dialog.centerWindow();
        dialog.setMovable(false);
        dialog.show(screen.guiStage);
    }

    public void tampilPastikanSimpan(final String nama) {
        final SimpleDialog simpleDialog = new SimpleDialog("Yakin Simpan?", "Ya", "Batal",
                screen.guiViewport, 0.3f, 0.1f);
        simpleDialog.btPositive.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.namaFile = nama;
                screen.menuManager.namafile.setText(nama);
                setting.simpan(true);
                simpleDialog.hide();
                return true;
            }
        });
        simpleDialog.btNegative.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                simpleDialog.hide();
                return true;
            }
        });
        simpleDialog.tampil(screen.guiStage);
    }

    public void tampilPastikanLoad(final String nama) {
        final SimpleDialog simpleDialog = new SimpleDialog("Yakin Load?", "Ya", "Batal",
                screen.guiViewport, 0.3f, 0.1f);
        simpleDialog.btPositive.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.namaFile = nama;
                screen.menuManager.namafile.setText(nama);
                setting.load(true, "");
                simpleDialog.hide();
                return true;
            }
        });
        simpleDialog.btNegative.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                simpleDialog.hide();
                return true;
            }
        });
        simpleDialog.tampil(screen.guiStage);
    }

    public void tampilUbahUkuran() {
        final VisDialog dialog = new VisDialog("Ubah ukuran");
        final VisTextField tfWidth, tfHeight;
        final VisTextButton btUbah, btBatal, btHorizontal;
        tfWidth = new VisTextField(setting.ukuranW + "");
        tfHeight = new VisTextField(setting.ukuranH + "");

        btHorizontal = new VisTextButton("horizontal");
        if (manager.horizontal) {
            btHorizontal.setText("horizontal");
        } else {
            btHorizontal.setText("vertical");
        }
        btHorizontal.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (btHorizontal.getText().toString().equals("horizontal")) {
                    btHorizontal.setText("vertical");
                } else {
                    btHorizontal.setText("horizontal");
                }
                return true;
            }
        });
        btUbah = new VisTextButton("Ubah");
        btUbah.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tfWidth.getText().equals("") || tfHeight.getText().equals("")) {
                    AlertDialog alertDialog = new AlertDialog("Ukuran tidak boleh kosong",
                            "Oke", screen.guiStage, 0.2f, 0.4f);
                    alertDialog.tampil();
                } else {
                    manager.horizontal = btHorizontal.getText().toString().equals("horizontal");
                    GameUtils.trace(this, manager.horizontal+"");
                    setting.ubahUkuranCanvas(Float.parseFloat(tfWidth.getText()) * manager.getWidth(),
                            Float.parseFloat(tfHeight.getText()) * manager.getHeight());
                    dialog.fadeOut();
                }
            }
        });
        btBatal = new VisTextButton("Batal");
        btBatal.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dialog.fadeOut();
                return true;
            }
        });

        dialog.clearChildren();
        dialog.setSize(Gdx.graphics.getWidth() * 0.2f, Gdx.graphics.getHeight() * 0.4f);
        dialog.add(tfWidth).width(dialog.getWidth() * 0.8f).padTop(10).padRight(dialog.getWidth() * 0.1f).padLeft(dialog.getWidth() * 0.1f).padBottom(10).colspan(2).row();
        dialog.add(tfHeight).width(dialog.getWidth() * 0.8f).padRight(dialog.getWidth() * 0.1f).padLeft(dialog.getWidth() * 0.1f).padBottom(10).colspan(2).row();
        dialog.add(btHorizontal).width(dialog.getWidth() * 0.8f).padRight(dialog.getWidth() * 0.1f).padLeft(dialog.getWidth() * 0.1f).padBottom(10).colspan(2).row();

        dialog.add(btUbah).width(dialog.getWidth() * 0.35f).padRight(dialog.getWidth() * 0.1f).padLeft(dialog.getWidth() * 0.1f).padBottom(10);
        dialog.add(btBatal).width(dialog.getWidth() * 0.35f).padRight(dialog.getWidth() * 0.1f).padBottom(10).row();

        dialog.center();
        dialog.fadeIn();
        dialog.show(screen.guiStage);
    }
}