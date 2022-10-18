package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private int x;
    private int y;
    private List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\uD83D\uDFE3";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public void setDirection(Direction direction) {
        if (direction == Direction.UP && this.direction == Direction.DOWN) {
            return;
        } else if (direction == Direction.LEFT && this.direction == Direction.RIGHT) {
            return;
        } else if (direction == Direction.RIGHT && this.direction == Direction.LEFT) {
            return;
        } else if (direction == Direction.DOWN && this.direction == Direction.UP) {
            return;
        }
        if (this.direction == Direction.LEFT && snakeParts.get(0).x == snakeParts.get(1).x) return;
        else if (this.direction == Direction.RIGHT && snakeParts.get(0).x == snakeParts.get(1).x) return;
        else if (this.direction == Direction.UP && snakeParts.get(0).y == snakeParts.get(1).y) return;
        if (this.direction == Direction.DOWN && snakeParts.get(0).y == snakeParts.get(1).y) return;

        this.direction = direction;
    }

    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
        GameObject sn1 = new GameObject(x, y);
        GameObject sn2 = new GameObject(x + 1, y);
        GameObject sn3 = new GameObject(x + 2, y);
        snakeParts.add(sn1);
        snakeParts.add(sn2);
        snakeParts.add(sn3);
    }

    public void draw(Game game) {
        Color color = isAlive ? Color.LAVENDER : Color.RED;
        for (GameObject snakePart : snakeParts) {
            if (snakePart.equals(snakeParts.get(0))) {
                game.setCellValueEx(snakePart.x, snakePart.y, Color.NONE, HEAD_SIGN, color, 70);
            } else {
                game.setCellValueEx(snakePart.x, snakePart.y, Color.NONE, BODY_SIGN, color, 70);
            }
        }
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        if (newHead.x < 0 || newHead.x >= SnakeGame.WIDTH || newHead.y >= SnakeGame.HEIGHT || newHead.y < 0) {
            isAlive = false;
            return;
        }
        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }
        snakeParts.add(0, newHead);
        if(newHead.x == apple.x && newHead.y == apple.y){
            apple.isAlive = false;
            return;
        }
        removeTail();
    }

    public GameObject createNewHead() {
        GameObject oldHead = snakeParts.get(0);
        if (direction == Direction.LEFT) {
            return new GameObject(oldHead.x - 1, oldHead.y);
        } else if (direction == Direction.RIGHT) {
            return new GameObject(oldHead.x + 1, oldHead.y);
        } else if (direction == Direction.UP) {
            return new GameObject(oldHead.x, oldHead.y - 1);
        } else {
            return new GameObject(oldHead.x, oldHead.y + 1);
        }
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject) {
        for (GameObject snakePart : snakeParts) {
            if (gameObject.x == snakePart.x && gameObject.y == snakePart.y) return true;
        }
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
