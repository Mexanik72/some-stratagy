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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Andrew on 15.04.2017.
 */

public class GameContainer {
    private static volatile GameContainer instance;

    public ArrayList<Region> regions;
    public ArrayList<Mesh> meshes;
    public Integer selected;

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

        Gdx.app.log("GameContainer", "start");
        meshes = new ArrayList<Mesh>();
        Color color = Color.RED;
Json json = new Json();
        regions = json.fromJson(ArrayList.class, Region.class, Gdx.files.internal("englandWithIndices.json"));
            //JsonValue map = new JsonReader().parse(Gdx.files.internal("parsedEng.json"));
            Gdx.app.log("GameContainer", "downloaded");
        Gdx.app.log("GameContainer", "size: " + regions.size());
            //Regions rs = j.fromJson(Regions.class, Gdx.files.internal("parsedEng.json"));
            for (int i=0; i <  regions.size(); i++) {
                /*JsonValue coordinates = regions.get(i).get("coordinates");
                FloatArray points = new FloatArray();
                FloatArray pointsI = new FloatArray();
                if (coordinates.size < 10000) {
                    for (int c = 0; c < coordinates.size;) {
                        points.add(coordinates.getFloat(c));
                        pointsI.add(coordinates.getFloat(c++));
                        points.add(coordinates.getFloat(c));
                        pointsI.add(coordinates.getFloat(c++));
                    }

                    ShortArray indices = new EarClippingTriangulator().computeTriangles(pointsI);
                    //new DelaunayTriangulator().computeTriangles(pointsI, false);

                    regions.add(new Region(map.get(i).getString("name"), pointsI, indices,
                            new Vector3(0, 0, 0),
                            new Vector3(0, 0, 0)));
                    Gdx.app.log("Indices: ",coordinates.size + " " + indices.size +"" );//+ " " + regions.get(i).name);
                    */Mesh mesh = new Mesh( true, regions.get(i).coordinates.length, regions.get(i).indices.length,
                            new VertexAttribute( VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE ));
                    mesh.setVertices(regions.get(i).coordinates);
                    mesh.setIndices(regions.get(i).indices);
                    meshes.add(mesh);

                //}
            }
            /*try{
                PrintWriter writer = new PrintWriter("englandWithIndices.json", "UTF-8");//without cornwall
                writer.write(json.prettyPrint(regions));
                writer.close();
            } catch (IOException e) {
                // do something
            }*/
            Gdx.app.log("GameContainer", "parsed " + meshes.size());
    }
}
