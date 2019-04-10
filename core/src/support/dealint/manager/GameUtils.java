package support.dealint.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class GameUtils {

    public static float jarak(float x1,float y1,float x2,float y2) {
        return (float) Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    public static double banding(double nilai1, double nilai1min, double nilai1max, double nilai2min, double nilai2max) {
        return (nilai1 - nilai1min) * (nilai2max-nilai2min)/(nilai1max-nilai1min) + nilai2min;
    }

    public static Texture getTexture(float worldWidth, float worldHeight, Color color) {
        Pixmap pmBg = new Pixmap((int) worldWidth,
                (int) worldHeight, Pixmap.Format.RGBA8888);

        pmBg.setColor(color);
        pmBg.fillRectangle(0, 0, pmBg.getWidth(), pmBg.getHeight());

        Texture texture = new Texture(pmBg);
        pmBg.dispose();
        return texture;
    }

    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static boolean mouseOnStage(float w, float h, Camera camera) {
        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
        camera.unproject(vec);

        return vec.x > 0 && vec.x < w && vec.y > 0 && vec.y < h;
    }

    public static Color getColorAlpha(Color color, float alpha) {
        return new Color(Color.argb8888(color.r, color.g, color.b, alpha));
    }

    public static void trace(Object tes) {
        System.out.println(tes.getClass().getName());
    }

    public static void trace(Object tes, String str) {
        System.out.println(tes.getClass().getName() + " : " + str);
    }
}
