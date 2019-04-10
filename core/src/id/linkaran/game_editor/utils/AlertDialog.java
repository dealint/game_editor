package id.linkaran.game_editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class AlertDialog extends VisDialog {

    public VisTextButton btPositive;
    private float percentWidth, percentHeight;
    private Stage stage;
    private float w, h;

    public AlertDialog(String text, String textPositive, Stage stage, float percentWidth, float percentHeight) {
        super(text);
        this.percentWidth = percentWidth;
        this.percentHeight = percentHeight;
        this.stage = stage;
        btPositive = new VisTextButton(textPositive);
        btPositive.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeOut();
                super.clicked(event, x, y);
            }
        });
        setView();
    }

    public void tampil() {
        center();
        fadeIn();
        setMovable(false);
        show(stage);
    }

    private void setView() {
        w = Gdx.graphics.getWidth() * percentWidth;
        h = Gdx.graphics.getHeight() * percentHeight;

        clearChildren();
        add(btPositive).width(w/2).center();
    }
}
