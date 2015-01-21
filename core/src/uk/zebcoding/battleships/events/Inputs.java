package uk.zebcoding.battleships.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import uk.zebcoding.battleships.Battleships;

/**
 * Created by Charlotte on 13/12/2014.
 * (C) Charlotte Thompson 2014.
 */
public class Inputs implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && Battleships.getGamestate() < 10) {
            switch (Battleships.getGamestate()) {
                case 0:
                    if (Battleships.attemptPlace(getTwentyFourth(screenX), getTwentyFourth(screenY) - 12,
                                                 Battleships.getRotated(), 5)) {
                        Battleships.setGamestate(1);
                    }
                    return true;
                case 1:
                    if (Battleships.attemptPlace(getTwentyFourth(screenX), getTwentyFourth(screenY) - 12,
                                                 Battleships.getRotated(), 4)) {
                        Battleships.setGamestate(2);
                    }
                    return true;
                case 2:
                    if (Battleships.attemptPlace(getTwentyFourth(screenX), getTwentyFourth(screenY) - 12,
                                                 Battleships.getRotated(), 3)) {
                        Battleships.setGamestate(3);
                    }
                    return true;
                case 3:
                    if (Battleships.attemptPlace(getTwentyFourth(screenX), getTwentyFourth(screenY) - 12,
                                                 Battleships.getRotated(), 3)) {
                        Battleships.setGamestate(4);
                    }
                    return true;
                case 4:
                    if (Battleships.attemptPlace(getTwentyFourth(screenX), getTwentyFourth(screenY) - 12,
                                                 Battleships.getRotated(), 2)) {
                        Battleships.setGamestate(5);
                    }
                    return true;
            }

            int x;
            int y;
            if (screenX > 24 && screenY > 24) {
                x = getTwentyFourth(screenX);
                y = getTwentyFourth(screenY);

                if (x < 10 && y < 10) {
                    if (Battleships.setStates(x, y)) {
                        Battleships.cpu1.cpuMove();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private int getTwentyFourth(int screenP) {
        return screenP / 24 - 1;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Battleships.setMouseX(screenX);
        Battleships.setMouseY(548 - screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        Battleships.setRotated();
        return true;
    }
}
