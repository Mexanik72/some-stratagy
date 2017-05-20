package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameScreen;

/**
 * Created by Andrew on 15.04.2017.
 */

public class MenuState extends State {
    private Texture background;
    private Texture playBtn;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("menu-background.jpg");
        playBtn = new Texture("play-btn.png");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        Integer height = Gdx.graphics.getHeight() - 50;
        sb.begin();
        sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.draw(playBtn, Gdx.graphics.getWidth() / 2  - (height / 2),
                Gdx.graphics.getHeight() / 2  - (height / 2),
                height, height);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();

    }
}
