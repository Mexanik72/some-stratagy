package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

import java.util.ArrayList;

/**
 * Created by Andrew on 13.05.2017.
 */

public class Region {
    String name;
    public FloatArray coordinates;
    public float[] cordinatesArray;
    public Vector3 min;
    public Vector3 max;

    public Region(String name, FloatArray points, Vector3 min, Vector3 max) {
        this.name = name;
        this.coordinates = points;
        this.min = min;
        this.max = max;
        this.cordinatesArray = coordinates.toArray();
    }

}
