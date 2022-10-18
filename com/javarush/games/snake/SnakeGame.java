package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {

    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private int turnDelay;
    private Snake snake;
    private Apple apple;
    private boolean isGameStopped;
    private static final int GOAL = 28;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        turnDelay = 300;
        score = 0;
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        isGameStopped = false;
        drawScene();
        setTurnTimer(turnDelay);
        setScore(score);
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.LIGHTSALMON, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            score += 5;
            setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
            createNewApple();
        }
        if (!snake.isAlive) gameOver();
        if (snake.getLength() > GOAL) win();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.SPACE) && isGameStopped) createGame();
        if (key.equals(Key.LEFT)) snake.setDirection(Direction.LEFT);
        else if (key.equals(Key.RIGHT)) snake.setDirection(Direction.RIGHT);
        else if (key.equals(Key.UP)) snake.setDirection(Direction.UP);
        else if (key.equals(Key.DOWN)) snake.setDirection(Direction.DOWN);
        else return;

    }

    private void createNewApple() {
        apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        while(snake.checkCollision(apple)) {
            apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        }
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BEIGE, "GAME OVER", Color.BLACK, 75);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BEIGE, "YOU WIN", Color.BLACK, 75);

    }
}
