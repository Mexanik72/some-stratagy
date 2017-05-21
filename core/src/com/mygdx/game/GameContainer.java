package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ShortArray;

import java.util.ArrayList;

/**
 * Created by Andrew on 15.04.2017.
 */

public class GameContainer {
    private static volatile GameContainer instance;

    public ArrayList<Region> regions;
    public ArrayList<Mesh> meshes;

    public static GameContainer getInstance() {
        GameContainer localInstance = instance;
        if (localInstance == null) {
            synchronized (GameContainer.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new GameContainer();
                }
            }
        }
        return localInstance;
    }

    public Rectangle view = new Rectangle(0, 0, 1920, 1080);

    private GameContainer() {
        regions = new ArrayList<Region>();

        meshes = new ArrayList<Mesh>();
        Color color = Color.RED;
Json json = new Json();
        //regions = json.fromJson(ArrayList.class, Region.class, Gdx.files.internal("parsedEng.json"));
            Gdx.app.log("GameContainer", "start");
            JsonValue map = new JsonReader().parse(Gdx.files.internal("parsedEng.json"));
            Gdx.app.log("GameContainer", "downloaded");
        Gdx.app.log("GameContainer", "size: " + regions.size());
            //Regions rs = j.fromJson(Regions.class, Gdx.files.internal("parsedEng.json"));
            for (int i=0; i <  map.size; i++) {
                JsonValue coordinates = map.get(i).get("coordinates");
                FloatArray points = new FloatArray();
                FloatArray pointsI = new FloatArray();
                if (coordinates.size < 10000) {
                for (int c = 0; c < coordinates.size;) {
                    points.add(coordinates.getFloat(c));
                    pointsI.add(coordinates.getFloat(c++));
                    points.add(coordinates.getFloat(c));
                    pointsI.add(coordinates.getFloat(c++));
                }
                    regions.add(new Region(map.get(i).getString("name"), pointsI,
                            new Vector3(0, 0, 0),
                            new Vector3(0, 0, 0)));
                    ShortArray indices = new EarClippingTriangulator().computeTriangles(pointsI);
                    //new DelaunayTriangulator().computeTriangles(pointsI, false);
                    Gdx.app.log("Indices: ", indices.size +"" );//+ " " + regions.get(i).name);
                    Mesh mesh = new Mesh( true, coordinates.size, indices.size,
                            new VertexAttribute( VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE ));
                    mesh.setVertices(points.toArray());
                    mesh.setIndices(indices.toArray());
                    meshes.add(mesh);
                }
            }

            /*FloatArray points = new FloatArray();
            points.add(50);
            points.add(50);
            points.add(color.toFloatBits());
            points.add(200);
            points.add(80);
            points.add(color.toFloatBits());
            points.add(60);
            points.add(300);
            points.add(color.toFloatBits());
            Mesh mesh = new Mesh( true, 4000, 0,
                    new VertexAttribute( VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE ),
                    new VertexAttribute( VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE ));
            mesh.setVertices(points.toArray());
            meshes.add(mesh);*/
            Gdx.app.log("GameContainer", "parsed " + meshes.size());
    }

    private void geoParser() {
        try {
            JsonValue map = new JsonReader().parse(Gdx.files.internal("England.json")).get("features");
            ArrayList<Region> regions = new ArrayList<Region>();
            for (JsonValue region : map) {
                JsonValue coordinates = region.get("geometry").get("coordinates");
                FloatArray points = new FloatArray();
                Gdx.app.log("type", region.get("geometry").getString("type"));
                recurs(points, coordinates, region.get("geometry").getString("type").equals("MultiPolygon"));
                //regions.add(new Region(region.get("properties").getString("LAD13NM"), points));
            }
            Json json = new Json();
            Gdx.app.log("Result", json.prettyPrint(regions));

            Gdx.app.log("Result", Gdx.files.getLocalStoragePath());
            FileHandle file = Gdx.files.local("out.json");
            file.writeString(json.prettyPrint(regions), false);
        } catch (Exception e) {
            Gdx.app.error("geoParser", e.getMessage());
            e.printStackTrace();
        }
    }
    private void recurs(FloatArray result, JsonValue value, boolean multi) {
        if (value.isArray()) {
            int pos = 0;
            Gdx.app.log("ds", multi + "");
            if (multi) {
                int size = 0;
                for (int i = 0; i < value.size; i++) {
                    JsonValue val = value.get(i);
                    JsonValue js = val.get(0);
                    Gdx.app.log("size", js.size + "");
                    if (js.size > size) {
                        size = js.size;
                        pos = i;
                    }
                }
            }
            JsonValue val;
            if (multi) {
                val = value.get(pos).get(0);
            } else {
                val = value.get(0);
            }
            for (JsonValue v : val) {
                result.add((int)(v.getFloat(0)*1000000));
                result.add((int)(v.getFloat(1)*1000000));
            }
        }
    }
}
