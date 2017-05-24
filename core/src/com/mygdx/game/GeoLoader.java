package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

/**
 * Created by Andrew on 23.05.2017.
 */

public class GeoLoader extends AsynchronousAssetLoader<Regions, GeoLoader.JsonLoaderParameter> {

    private Regions regions;

    public GeoLoader(FileHandleResolver resolver) {
        super(resolver);
        regions = new Regions();
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, JsonLoaderParameter parameter) {
        regions.regions = new Json().fromJson(ArrayList.class, Region.class, file);
    }

    @Override
    public Regions loadSync(AssetManager manager, String fileName, FileHandle file, JsonLoaderParameter parameter) {
        Regions regions = this.regions;
        this.regions = null;

        return regions;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, JsonLoaderParameter parameter) {
        return null;
    }

    static public class JsonLoaderParameter extends AssetLoaderParameters<Regions> {

    }

}