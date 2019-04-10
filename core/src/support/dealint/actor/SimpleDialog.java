package support.dealint.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SimpleDialog extends Dialog {

    public TextButton btPositive, btNegative;
    private float percentWidth, percentHeight;
    private Viewport viewport;
    private float w, h;

    public SimpleDialog(String text, Skin skin, TextButton btPositive, TextButton btNegative, Viewport viewport, float percentWidth, float percentHeight) {
        super(text, skin);
        this.percentWidth = percentWidth;
        this.percentHeight = percentHeight;
        this.viewport = viewport;
        this.btPositive = btPositive;
        this.btNegative = btNegative;
        setView();
    }

    private void setView() {
        w = Gdx.graphics.getWidth() * percentWidth;
        h = Gdx.graphics.getHeight() * percentHeight;
        float pad = Gdx.graphics.getWidth() * 0.01f;

        add(btPositive).width(w/2 - pad *3).pad(pad).height(h*0.5f);
        add(btNegative).width(w/2 - pad * 3).padRight(pad).height(h*0.5f);
    }
}
