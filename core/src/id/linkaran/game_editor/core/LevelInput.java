package id.linkaran.game_editor.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.actor.Benda;
import id.linkaran.game_editor.actor.Cursor;
import support.dealint.manager.GameInput;
import support.dealint.manager.GameUtils;

public class LevelInput extends GameInput implements Manager.HasManager {

    Manager manager;
    LevelSetting setting;

    //enable selection bernilai true jika benda atau item sedang tidak di drag
    boolean enableSelection = true;

    //begin selection bernilai true jika mouse ditekan saat state = dipilih
    boolean beginSelection = false;

    //drag group benda
    boolean mulaiDragBenda, mulaiDragStage, ctrlDitekan;
    Benda bendaDiDrag;

    @Override
    public void setManager(Manager manager) {
        this.manager = manager;
        this.manager = manager;
        setting = manager.levelSetting;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float touchX = getTouchX(screenX, screenY);
        float touchY = getTouchY(screenX, screenY);
        float canvasX = touchX - manager.levelScreen.mainLayout.getX();
        float canvasY = touchY - manager.levelScreen.mainLayout.getY();

        Vector3 vec=new Vector3(screenX, screenY,0);
        manager.levelScreen.camera.unproject(vec);
        Cursor cursor = manager.levelScreen.cursor;
        if (GameUtils.mouseOnStage(setting.worldWidth, setting.worldHeight, manager.levelScreen.camera)) {
            cursor.setPosition(MathUtils.clamp(vec.x, 0, setting.worldWidth - cursor.getWidth()),
                    MathUtils.clamp(vec.y, 0, setting.worldHeight - cursor.getHeight()));
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float touchX = getTouchX(screenX, screenY);
        float touchY = getTouchY(screenX, screenY);
        float canvasX = touchX - manager.levelScreen.mainLayout.getX();
        float canvasY = touchY - manager.levelScreen.mainLayout.getY();

        if (button == Input.Buttons.LEFT &&
                GameUtils.mouseOnStage(setting.worldWidth, setting.worldHeight, manager.levelScreen.camera)) {
            if (setting.state == LevelSetting.State.OBJEK) {
                //tambah musuh
                setting.undoManager.tambahStateUndo();
                setting.tambahBenda(canvasX,
                        canvasY,
                        setting.bendaW,
                        setting.bendaH,
                        setting.jenisObjek);

            } else if (setting.state == LevelSetting.State.SALIN) {
                if (setting.bendaDipilih != null && setting.arrBendaDipilih.size == 0) {
                    //tambahkan salinan jika hanya satu benda yang dipilih
                    setting.undoManager.tambahStateUndo();
                    setting.tambahSalin(canvasX, canvasY);
                } else {
                    manager.levelScreen.windowManager.objekState(2);
                }

            } else if (setting.state == LevelSetting.State.PILIH) {
                //jika tidak sedang tambah benda, item, atau salinan
                //seleksi beberapa benda
                if (enableSelection) {
                    bendaDiDrag = getBendaRect(touchX, touchY, setting.arrBenda, true);
                    if (bendaDiDrag != null) {
                        if (setting.arrBendaDipilih.contains(bendaDiDrag, true)) {
                            mulaiDragBenda = true;
                        }
                    } else {
                        //batalkan seleksi jika area kosong
                        resetArrBendaDipilih();
                        setting.setBendaDipilih(null);
                    }

                    //mulai seleksi
                    beginSelection = true;
                    setting.selectionTools.setVisible(true);
                    setting.selectionTools.setPosition(touchX, touchY);
                    setting.selectionTools.updateTemp();
                }

                if (setting.arrBendaDipilih.size != 0) {
                    //jika ada beberapa benda diseleksi
                    for (Benda benda : setting.arrBendaDipilih) {
                        benda.updateJarak(canvasX, canvasY);
                    }
                }
            }

        } else {
            //klik diluar stage atau klik kanan
            manager.levelScreen.windowManager.objekState(2);
            resetArrBendaDipilih();
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    public void resetArrBendaDipilih() {
        for (Benda benda : setting.arrBendaDipilih) {
            benda.dipilih = false;
        }
        setting.arrBendaDipilih.clear();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float touchX = getTouchX(screenX, screenY);
        float touchY = getTouchY(screenX, screenY);
        float canvasX = touchX - manager.levelScreen.mainLayout.getX();
        float canvasY = touchY - manager.levelScreen.mainLayout.getY();

        if (mulaiDragStage) {
            float x = Gdx.input.getDeltaX();
            float y = Gdx.input.getDeltaY();

            gameScreen.camera.translate(-x,y);
        } else {
            if (mulaiDragBenda && bendaDiDrag != null) {
                for (Benda benda : setting.arrBendaDipilih) {
                    benda.gerakBersama(canvasX, canvasY);
                }
            } else if (setting.state == LevelSetting.State.PILIH && enableSelection && beginSelection) {
                //update kotak selection
                setting.selectionTools.update(touchX, touchY);
            }
        }

        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        float touchX = getTouchX(screenX, screenY);
        float touchY = getTouchY(screenX, screenY);

        if (!mulaiDragStage) {
            if (setting.state == LevelSetting.State.PILIH && enableSelection && beginSelection) {
                pilihBeberapaBenda();
                setting.selectionTools.setVisible(false);
                setting.selectionTools.setSize(0, 0);
                setting.selectionTools.setPosition(-10, -10);
                setting.selectionTools.resetTemp();
            }
            beginSelection = false;
            mulaiDragBenda = false;
            bendaDiDrag = null;
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    public void pilihBeberapaBenda() {
        for (Benda benda : setting.arrBenda) {
            if (Intersector.overlaps(benda.rect, setting.selectionTools.rect)) {
                if (!setting.arrBendaDipilih.contains(benda, true)) {
                    benda.dipilih = true;
                    setting.arrBendaDipilih.add(benda);
                }
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.CONTROL_LEFT) {
            ctrlDitekan = true;
        }

        if (ctrlDitekan) {
            if (keycode == Input.Keys.Z) {
                setting.undoManager.undo();
            }
            if (keycode == Input.Keys.Y) {
                setting.undoManager.redo();
            }
        }

        if (keycode == Input.Keys.SPACE) {
            manager.levelScreen.windowManager.objekState(2);
            mulaiDragStage = true;
        }

        if (keycode == Input.Keys.FORWARD_DEL) {
            if (setting.bendaDipilih != null) {

                setting.undoManager.tambahStateUndo();
                setting.arrBenda.removeValue(setting.bendaDipilih, true);
                setting.bendaDipilih.remove();
            }

            if (setting.arrBendaDipilih.size != 0) {

                setting.undoManager.tambahStateUndo();
                for (Benda benda : setting.arrBendaDipilih) {
                    setting.arrBenda.removeValue(benda, true);
                    benda.remove();
                }
                setting.arrBendaDipilih.clear();
            }
        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.CONTROL_LEFT) {
            ctrlDitekan = false;
        }
        if (keycode == Input.Keys.SPACE) {
            mulaiDragStage = false;
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean scrolled(int amount) {
        if (ctrlDitekan) {
            if ((gameScreen.camera.zoom + amount)>0 && (gameScreen.camera.zoom + amount)<5) {
                if (amount > 0) {
                    gameScreen.camera.zoom += 0.5;
                } else {
                    gameScreen.camera.zoom -= 0.5;
                }
            }
        }
        return super.scrolled(amount);
    }

    private Benda getBendaRect(float touchX, float touchY, Array<Benda> actors, boolean group) {
        for (Benda actor : actors) {
            if (isBendaRectTouched(touchX, touchY, actor, group)) {
                return actor;
            }
        }
        return null;
    }

    private boolean isBendaRectTouched(float touchX, float touchY, Benda actor, boolean group){
        float x, y;
        float w = actor.getWidth();
        float h = actor.getHeight();

        if (group) {
            x = actor.getStagePos().x;
            y = actor.getStagePos().y;
        } else {
            x = actor.getX();
            y = actor.getY();
        }

        return (touchX >= x) && touchX <= (x + w) && touchY >= y && touchY <= (y + h);
    }
}
