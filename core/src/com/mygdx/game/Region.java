package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

import java.util.ArrayList;

/**
 * Created by Andrew on 13.05.2017.
 */

public class Region {
    public String name;
    public float[] coordinates;
    public float minX;
    public float maxX;
    public float minY;
    public float maxY;

    public Region(){

    }

    public Region(String name, FloatArray points, Vector3 min, Vector3 max) {
        this.name = name;
        this.coordinates = points.toArray();
        this.minX = min.x;
        this.maxX = max.x;
        this.minY = min.y;
        this.maxY = max.y;
    }

}
