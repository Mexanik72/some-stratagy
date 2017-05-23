package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.GameContainer;
import com.mygdx.game.Listener;
import com.mygdx.game.Region;
import com.badlogic.gdx.math.EarClippingTriangulator;

import java.util.ArrayList;

/**
 * Created by Andrew on 15.04.2017.
 */

public class PlayState extends State {
    ArrayList<PolygonSprite> poly;
    PolygonSpriteBatch polyBatch;
    Texture textureSolid;
    ShapeRenderer shapeRenderer;
    ShaderProgram shaderProgram;

    float tableHeight;
    float tableWidth;



    PlayState(GameStateManager gsm) {
        super(gsm);
        camera.setToOrtho(false);
        poly = new ArrayList<PolygonSprite>();
        shapeRenderer = new ShapeRenderer();
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.LIGHT_GRAY); // DE is red, AD is green and BE is blue.
        pix.fill();
        textureSolid = new Texture(pix);

       /* for (Region region : GameContainer.getInstance().regions) {
            PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
                    region.cordinatesArray,
                    new EarClippingTriangulator().computeTriangles(region.cordinatesArray).toArray()
            );
            poly.add(new PolygonSprite(polyReg));
        }*/
        polyBatch = new PolygonSpriteBatch();

        tableWidth = GameContainer.getInstance().view.width;
        tableHeight = GameContainer.getInstance().view.height;

        shaderProgram = new ShaderProgram(Gdx.files.internal("sh.vert"), Gdx.files.internal("sh.frag"));
        if (!shaderProgram.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shaderProgram.getLog());

        Listener listener = new Listener(camera);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new GestureDetector(listener));
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isCatchBackKey()) {
            gsm.pop();
        }

    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS) ||
                (Gdx.input.isKeyPressed(Input.Keys.P))) {
            if (camera.zoom > 0.2) {
                camera.zoom -= 0.1;
                camera.update();
                Gdx.app.log("kk", "plus" + camera.zoom);
            }
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom += 0.1;
            camera.update();
            Gdx.app.log("kk", "minus2" + camera.zoom);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0.373f, 0.443f, 0.451f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderProgram.begin();
        /*shaderProgram.setUniformf("u_viewportInverse", new Vector2(1f / 100, 1f / 100));
        shaderProgram.setUniformf("u_offset", 1f);
        shaderProgram.setUniformf("u_step", Math.min(1f, 100 / 70f));*/
        shaderProgram.setUniformf("u_color", new Vector3(0.19f,0.19f,0.19f));

        //update the projection matrix so our triangles are rendered in 2D
        shaderProgram.setUniformMatrix("u_projTrans", camera.combined);
        ArrayList<Mesh> meshes = GameContainer.getInstance().meshes;
        Integer selected = GameContainer.getInstance().selected;
        for (Integer i=0; i < meshes.size(); i++) {
            Mesh mesh = meshes.get(i);
            if (selected != null) {
                if (selected.equals(i)) {
                    Gdx.app.log("playState", "selected" + i);
                    shaderProgram.setUniformf("u_color", new Vector3(1f, 0.19f, 0.19f));
                } else {
                    shaderProgram.setUniformf("u_color", new Vector3(0.19f, 0.19f, 0.19f));
                }
            }
            mesh.render(shaderProgram, GL20.GL_TRIANGLES);
        }
        shaderProgram.end();
        /*polyBatch.setProjectionMatrix(camera.combined);
        polyBatch.begin();
        for (PolygonSprite sprite : poly) {
            sprite.draw(polyBatch);
        }
        polyBatch.end();*/

        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glLineWidth(1f);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        /*for (Region region : GameContainer.getInstance().regions) {
            //if (camera.frustum.pointInFrustum(region.min) ||
              //      camera.frustum.pointInFrustum(region.max)) {
            if (region.coordinates.length < 5000)
                shapeRenderer.polyline(region.coordinates);
            //}
        }*/

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        polyBatch.dispose();
    }
}
