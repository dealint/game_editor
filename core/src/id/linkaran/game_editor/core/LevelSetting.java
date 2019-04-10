package id.linkaran.game_editor.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.StringWriter;

import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.actor.Benda;
import id.linkaran.game_editor.actor.SelectionTools;
import id.linkaran.game_editor.manager.UndoManager;
import id.linkaran.game_editor.manager.WindowManager;
import support.dealint.actor.Layout;
import support.dealint.manager.GameSetting;
import support.dealint.manager.GameUtils;

public class LevelSetting extends GameSetting implements Manager.HasManager {

    public enum State {
        OBJEK, SALIN, PILIH
    }

    public State state;
    private Manager manager;
    public LevelScreen screen;
    public WindowManager windowManager;

    public UndoManager undoManager;

    public Array<String> arrNamaObjek;
    public Array<FileHandle> arrImgObjek;
    public Array<Benda> arrBenda;
    public Array<Benda> arrBendaDipilih;
    public Array<Layout> arrMap;

    public int ukuranW = 1, ukuranH = 1;
    public float bendaW = 50, bendaH = 50;

    public int jenisObjek;
    public Benda bendaDipilih;
    public int jumlahObjek = 0, jumlahBagian = 0;

    public SelectionTools selectionTools;

    @Override
    public void setManager(Manager manager) {
        this.manager = manager;
        screen = manager.levelScreen;
        windowManager = screen.windowManager;

        state = State.PILIH;
        undoManager = new UndoManager(manager);

        arrNamaObjek = new Array<String>();
        arrNamaObjek.add("-");
        arrImgObjek = new Array<FileHandle>();

        arrBenda = new Array<Benda>();
        arrBendaDipilih = new Array<Benda>();
    }

    public void setArrImage() {
        for(int i=0; i < arrNamaObjek.size; i++) {
            FileHandle img = Gdx.files.local("xml/dealint_editor/image/objek" + i + ".png");
            if (img.exists()) {
                arrImgObjek.add(img);
            } else {
                arrImgObjek.add(Gdx.files.internal("sprite/objek.png"));
            }
        }
    }

    public String simpan(boolean simpanDariFile) {
        FileHandle fileXml;
        StringWriter writer = new StringWriter();
        XmlWriter xmlWriter = new XmlWriter(writer);

        if (simpanDariFile) {
            fileXml = Gdx.files.local("xml/dealint_editor/" + manager.namaFile + ".xml");
        } else {
            fileXml = null;
        }

        cekBendaDiluar();

        try {
            xmlWriter.element("level").attribute("nama", manager.namaFile);
            xmlWriter.attribute("w", screen.mainLayout.getWidth());
            xmlWriter.attribute("h", screen.mainLayout.getHeight());
            xmlWriter.attribute("horizontal", manager.horizontal);

            for (Benda benda : arrBenda) {
                xmlWriter
                        .element("benda")
                        .attribute("x", Math.floor(benda.getX()))
                        .attribute("y", Math.floor(benda.getY()))
                        .attribute("w", benda.getWidth())
                        .attribute("h", benda.getHeight())
                        .attribute("nama", benda.nama)
                        .attribute("jenisObjek", benda.jenis);
                xmlWriter.pop();
            }

            xmlWriter.pop();
            xmlWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (simpanDariFile) {
            fileXml.writeString("<?xml version = \"1.0\" encoding = \"UTF-8\" ?>\n" + writer.toString(), false);
            return "";
        } else {
            return writer.toString();
        }
    }

    public void load(boolean loadDariFile, String str) {
        FileHandle fileXml;
        XmlReader.Element element;
        XmlReader xmlReader;
        if (loadDariFile) {
            fileXml = Gdx.files.local("xml/dealint_editor/" + manager.namaFile + ".xml");
            xmlReader = new XmlReader();
            element = xmlReader.parse(fileXml);
        } else {
            xmlReader = new XmlReader();
            element = xmlReader.parse(str);
        }

        ukuranW = (int) (Float.parseFloat(element.getAttribute("w"))/manager.getWidth());
        ukuranH = (int) (Float.parseFloat(element.getAttribute("h"))/manager.getHeight());
        manager.horizontal = element.getBoolean("horizontal");

        bersihBenda();

        for (XmlReader.Element e : element.getChildrenByName("benda")) {
            tambahBenda(
                    Float.parseFloat(e.getAttribute("x")),
                    Float.parseFloat(e.getAttribute("y")),
                    Float.parseFloat(e.getAttribute("w")),
                    Float.parseFloat(e.getAttribute("h")),
                    Integer.parseInt(e.getAttribute("jenisObjek"))
            );
        }

        ubahUkuranCanvas(ukuranW * manager.getWidth(), ukuranH * manager.getHeight());
    }

    public void simpanDaftarNama() {
        FileHandle fileXml = Gdx.files.local("xml/dealint_editor/daftar_nama.xml");
        StringWriter writer = new StringWriter();
        XmlWriter xmlWriter= new XmlWriter(writer);

        try {
            xmlWriter.element("daftar_nama");
            for (String str : arrNamaObjek) {
                xmlWriter
                        .element("nama_objek")
                        .attribute("nama", str)
                        .pop();
            }
            xmlWriter.pop();
            xmlWriter.flush();

        } catch (Exception e) {
            System.out.print("gagal");
        }

        fileXml.writeString("<?xml version = \"1.0\" encoding = \"UTF-8\" ?>\n" + writer.toString(), false);
    }

    public void loadDaftarNama() {
        FileHandle fileXml = Gdx.files.local("xml/dealint_editor/daftar_nama.xml");
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element daftar_nama = xmlReader.parse(fileXml);

        arrNamaObjek.clear();
        for (XmlReader.Element e : daftar_nama.getChildrenByName("nama_objek")) {
            arrNamaObjek.add(e.getAttribute("nama"));
        }
        screen.windowManager.sbObjek.setItems(arrNamaObjek);

        setArrImage();
    }

    private Benda bendaBaruDibuat;
    public void tambahBenda(float x, float y, float w, float h, int jenis) {
        bendaBaruDibuat = new Benda(manager, GameUtils.getTexture(100, 100, Color.BLUE));

        bendaBaruDibuat.texture = new Texture(arrImgObjek.get(jenis));
        bendaBaruDibuat.nama = arrNamaObjek.get(jenis);
        bendaBaruDibuat.setSize(w, h);
        bendaBaruDibuat.jenis = jenis;
        bendaBaruDibuat.setPosition(x, y);
        bendaBaruDibuat.update();

        screen.mainLayout.addActor(bendaBaruDibuat);
        arrBenda.add(bendaBaruDibuat);

        setBendaDrag(bendaBaruDibuat);
    }

    private void setBendaDrag(final Benda benda) {
        benda.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (!manager.levelInput.mulaiDragStage) {
                    if (state == State.PILIH && arrBendaDipilih.size == 0) {
                        //jika benda yang dipiih hanya satu
                        benda.drag(x, y);
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!manager.levelInput.mulaiDragStage) {
                    if (arrBendaDipilih.size == 0) {
                        setBendaDipilih(benda);
                        manager.levelInput.enableSelection = false;
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                undoManager.tambahStateUndo();
                super.dragStart(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!manager.levelInput.mulaiDragStage) {
                    manager.levelInput.enableSelection = true;
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    public void setBendaDipilih(Benda b) {
        bendaDipilih = b;
        if (bendaDipilih != null) {
            b.dipilih = true;
            jumlahBagian = 0;
            for (Benda benda : arrBenda) {
                if (benda != bendaDipilih) {
                    benda.dipilih = false;
                }
                if (benda.nama.equals(bendaDipilih.nama)) {
                    jumlahBagian++;
                }
            }
            // TODO: 09-Apr-19 tampilkan jumlah benda
            //screen.taJumlahBagian.setText("Jumlah " + bendaDipilih.nama + " : " + jumlahBagian);
        } else {
            for (Benda benda : arrBenda) {
                benda.dipilih = false;
            }
            //screen.taJumlahBagian.setText("-");
        }
    }

    public void tambahSalin(float x, float y) {
        tambahBenda(x, y, bendaDipilih.getWidth(), bendaDipilih.getHeight(),
                bendaDipilih.jenis);
    }

    public void ubahUkuranCanvas(float w, float h) {
        manager.levelScreen.mainLayout.setSize(w, h);
        manager.levelScreen.scrollPane.setSize(manager.getWidth(), manager.getHeight());
        manager.levelScreen.scrollPane.setActor(manager.levelScreen.mainLayout);
        ukuranW = (int) (manager.levelScreen.mainLayout.getWidth()/manager.getWidth());
        ukuranH = (int) (manager.levelScreen.mainLayout.getHeight()/manager.getHeight());
    }

    public void cekBendaDiluar() {
        Array<Benda> arrTempBenda = new Array<Benda>();
        for (Benda benda : arrBenda) {
            if (benda.getX() > manager.levelScreen.mainLayout.getWidth() ||
                    benda.getX() + benda.getWidth() < 0||
                    benda.getY() > manager.levelScreen.mainLayout.getHeight() ||
                    benda.getY() + benda.getHeight() < 0) {
                arrTempBenda.add(benda);
            }
        }

        if (arrTempBenda.size > 0) {
            for (Benda benda : arrTempBenda) {
                arrBenda.removeValue(benda, true);
                benda.remove();
            }
        }
    }

    public void bersihBenda() {
        for (Benda benda : arrBenda) {
            benda.remove();
        }
        arrBenda.clear();
    }
}
