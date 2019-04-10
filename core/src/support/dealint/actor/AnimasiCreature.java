package support.dealint.actor;

import com.badlogic.gdx.graphics.Texture;

public interface AnimasiCreature {

    void setAnimasiManager(Texture textureAnimasi, String filename, float scale);
    void flipAnimasi();
    void setAnimasi(int indexAnimasi, boolean isPlaying, boolean loop);
    void updatePosisiAnimasi();
    boolean animasiAktif(int indexAnimasi);
}
