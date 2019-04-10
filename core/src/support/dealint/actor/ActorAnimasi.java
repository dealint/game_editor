package support.dealint.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

import support.MeshBoneUtil.Creature;
import support.MeshBoneUtil.CreatureAnimation;
import support.MeshBoneUtil.CreatureManager;
import support.MeshBoneUtil.CreatureModuleUtils;
import support.MeshBoneUtil.CreatureRenderer;

public class ActorAnimasi extends ActorCollision{

    public CreatureManager animasiManager;
    public CreatureRenderer animasiRender;
    private OrthographicCamera camera;
    public Array<String> arrAnimasi;

    public Texture textureAnimasi;

    public Matrix4 pos;
    public float scale;
    public float lastRunTime = 0;
    public int indexAnimasi;

    // TODO: 2/8/2019 ganti flatFilename dengan assetloader

    public ActorAnimasi(boolean rectCollision, OrthographicCamera camera) {
        super(rectCollision);
        this.camera = camera;
        arrAnimasi = new Array<String>();
    }

    public void setAnimasiManager(Texture textureAnimasi, String filename, float scale) {
        this.textureAnimasi = textureAnimasi;
        this.scale = scale;

        //design
        support.CreatureFlatDataJava.rootData flat_data = CreatureModuleUtils.LoadCreatureFlatData(filename);
        Creature new_creature = new Creature(flat_data);
        animasiManager = new CreatureManager(new_creature);

        CreatureAnimation new_animation;
        for (String str : arrAnimasi) {
            new_animation = new CreatureAnimation(flat_data.dataAnimation(), str);
            animasiManager.AddAnimation(new_animation);
        }

        final String VERTEX = Gdx.files.internal("shader/MeshBoneShader.vert").readString();
        final String FRAGMENT = Gdx.files.internal("shader/MeshBoneShader.frag").readString();
        ShaderProgram program = new ShaderProgram(VERTEX, FRAGMENT);

        textureAnimasi.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        // Create the creature render object
        animasiRender = new CreatureRenderer(animasiManager,
                textureAnimasi,
                program,
                camera);

        // Set a transformation matrix for the creature
        pos = new Matrix4();
        pos.scl(scale);
        pos.setTranslation(getX(), getY() + getHeight() / 2, 0);
        animasiRender.SetXform(pos);
        animasiManager.is_playing = false;
    }

    public void flipAnimasi() {
        pos.scale(-(pos.getScaleX() / scale),
                pos.getScaleY() / scale,
                pos.getScaleZ() / scale);
    }

    public void setAnimasi(int animasiIndex, boolean isPlaying, boolean loop) {
        this.indexAnimasi = animasiIndex;
        animasiManager.is_playing = isPlaying;
        animasiManager.MakePointCache(arrAnimasi.get(animasiIndex), 1);
        animasiManager.SetActiveAnimationName(arrAnimasi.get(animasiIndex), false);
        animasiManager.SetShouldLoop(loop);
    }

    public void updatePosisiAnimasi() {
        pos.setTranslation(getX() + getWidth()/2, getY() + getHeight()/2, 0);
    }

    public boolean animasiAktif(int indexAnimasi) {
        return animasiManager.GetActiveAnimationName().equals(arrAnimasi.get(indexAnimasi));
    }

    protected void renderAnimasi(Batch batch) {
        batch.end();
        animasiManager.Update(Gdx.graphics.getDeltaTime());
        animasiRender.Flush();
        batch.begin();
    }
}
