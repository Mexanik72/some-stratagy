package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

import java.util.ArrayList;

/**
 * Created by Andrew on 10.04.2017.
 */

public class Listener implements GestureListener {

    private float zoomMax;
    private float zoomMin;
    private float zoomInitial;

    private float initialScale = 2f;

    private OrthographicCamera camera;
    public Listener(OrthographicCamera camera) {
        this.camera = camera;

        Rectangle table = GameContainer.getInstance().view;
        camera.zoom = table.getWidth() / Gdx.graphics.getWidth();
        zoomMax = camera.zoom + 0.07f;
        zoomMin = camera.zoom - 0.5f;
        zoomInitial = camera.zoom;
        camera.position.set(table.getWidth()/2, table.getHeight()/2, 0.0f);
        camera.update();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        initialScale = camera.zoom;
        camera.update();
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

        camera.unproject(touchPos);
        Gdx.app.log("tan", "X: " + touchPos.x + ", Y: " + touchPos.y);
        Gdx.app.log("tan", "selected: " + GameContainer.getInstance().selected);

        ArrayList<Region> regions = GameContainer.getInstance().regions;
        GameContainer.getInstance().selected = null;
        for (int i=0; i < regions.size(); i++) {
            Region region = regions.get(i);
            if (region.bounds.contains(touchPos)) {
                GameContainer.getInstance().selected = i;
                break;
            }
        }
//        if (GameContainer.getInstance().getSelectedCard() != null) {
//            Gdx.app.log("before", touchPos.x + " " + touchPos.y);
//
//            Gdx.app.log("after", GameContainer.getInstance().getReturnBtn().getX() + " " +
//                    GameContainer.getInstance().getReturnBtn().getY() );
//
//            if (GameContainer.getInstance().getReturnBtn().contains(
//                    touchPos.x, Gdx.graphics.getHeight() - touchPos.y)) {
//                GameContainer.getInstance().setSelectedCard(null);
//            }
//        } else {
//            camera.unproject(touchPos);
//            GameContainer.getInstance().selectCard(touchPos);
//        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        FloatArray poi = new FloatArray(GameContainer.getInstance().regions.get(0).coordinates);
        camera.position.set(poi.get(0),poi.get(1),0);
        Gdx.app.log("ok", poi.get(0) + ", " +poi.get(1));
        camera.update();
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        /*flinging = true;
        velX = camera.zoom * velocityX * 0.5f;
        velY = camera.zoom * velocityY * 0.5f;*/
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        float scaledViewportWidthHalfExtent = camera.viewportWidth * camera.zoom * 0.5f;
        float scaledViewportHeightHalfExtent = camera.viewportHeight * camera.zoom * 0.5f;

        float newCameraX = camera.position.x + (-deltaX*camera.zoom);
        float newCameraY = camera.position.y + ( deltaY*camera.zoom);

        Rectangle table = GameContainer.getInstance().view;
        //if (scaledViewportWidthHalfExtent < newCameraX &&
//                newCameraX < table.getWidth() - scaledViewportWidthHalfExtent &&
//                table.getHeight() - scaledViewportHeightHalfExtent > newCameraY &&
//                newCameraY > scaledViewportHeightHalfExtent) {
            camera.position.set(newCameraX, newCameraY, 0.0f);
        //}
        Gdx.app.log("pan", "newCameraX: " + newCameraX + ", newCameraY: " + newCameraY);
        camera.update();
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        Gdx.app.log("CameraController::zoom()", "-- initialDistance:" + camera.zoom);

        float ratio = initialDistance / distance;
        float newZoom = initialScale * ratio;
/*        if ( newZoom - camera.zoom > 0.2f) {
            camera.zoom = zoomInitial;
            camera.position.set(1920/2, 1080/2, 0.0f);
        } else if (newZoom < zoomMax && newZoom > zoomMin) {
            camera.zoom = newZoom;
            if ((Math.round(newZoom * 100) / 100f) > 1f) {
                Rectangle table = GameContainer.getInstance().view;
                camera.position.set(table.getWidth()/2, table.getHeight()/2, 0.0f);
            }
        }*/
        camera.zoom = newZoom;
        camera.update();
        Gdx.app.log("CameraController::zoom()", "-- initialDistance:" + (float)Math.round(newZoom * 100) / 100f);

        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
