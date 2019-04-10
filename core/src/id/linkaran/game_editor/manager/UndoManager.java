package id.linkaran.game_editor.manager;

import com.badlogic.gdx.utils.Array;

import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.core.LevelSetting;

public class UndoManager {

    Manager manager;
    LevelSetting setting;
    Array<String> arrSimpan, arrLoad;

    public UndoManager(Manager manager) {
        this.manager = manager;
        this.setting = manager.levelSetting;
        arrSimpan = new Array<String>();
        arrLoad = new Array<String>();
    }

    private void tambahStateRedo() {
        arrLoad.add(setting.simpan(false));
        cekSize();
    }

    public void tambahStateUndo() {
        arrSimpan.add(setting.simpan(false));
        cekSize();
    }

    public void undo() {
        if (arrSimpan.size > 0) {
            String data = arrSimpan.pop();
            tambahStateRedo();
            setting.load(false, data);
        }
    }

    public void redo() {
        if (arrLoad.size > 0) {
            String data = arrLoad.pop();
            tambahStateUndo();
            setting.load(false, data);
        }
    }

    public void cekSize() {
        if (arrSimpan.size > 20) {
            arrSimpan.removeIndex(0);
        }

        if (arrLoad.size > 20) {
            arrLoad.removeIndex(0);
        }
    }
}
