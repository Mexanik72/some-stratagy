package com.mygdx.game;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

import java.util.ArrayList;

/**
 * Created by Andrew on 13.05.2017.
 */

public class Region {
    public String name;
    public float[] coordinates;
    public short[] indices;
    public float minX;
    public float maxX;
    public float minY;
    public float maxY;
    public Mesh mesh;
    BoundingBox bounds;

    public Region(){

    }

    public Region(String name, FloatArray points, ShortArray indices, Mesh mesh) {
        this.name = name;
        this.coordinates = points.toArray();
        this.indices = indices.toArray();
        this.mesh = mesh;
    }

    public void setMesh(Mesh mesh, BoundingBox bounds) {
        this.mesh = mesh;
        this.bounds = bounds;
    }
    public BoundingBox getBounds() {
        return bounds;
    }
}
