package com.hescha.game;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.hescha.game.model.Direction;

public class MyGestureListener implements GestureDetector.GestureListener {
    public static Direction move;
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        float minFlingVelocity = 50f; // Выберите минимальную скорость события fling

        if (Math.abs(velocityX) > minFlingVelocity || Math.abs(velocityY) > minFlingVelocity) {
            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                if (velocityX > 0) {
                    move = Direction.RIGHT;
                } else {
                    move = Direction.LEFT;
                }
            } else {
                if (velocityY > 0) {
                    move = Direction.DOWN;
                } else {
                    move = Direction.UP;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
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
