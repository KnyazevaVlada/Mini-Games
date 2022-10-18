package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 10;
    private final GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped;
    private static final String GAMEOVER = "Failed";
    private static final String WIN = "Congratulations!";
    private int countClosedTiles = SIDE * SIDE;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.LIGHTGRAY);
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;

    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject gameObject = gameField[x][y];
                if (!gameObject.isMine) {
                    for (GameObject neighbor : getNeighbors(gameObject)) {
                        if (neighbor.isMine) gameObject.countMineNeighbors++;
                    }
                }
            }
        }
    }

    private void openTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        if (gameObject.isOpen || gameObject.isFlag || isGameStopped) return;
        gameObject.isOpen = true;
        if (!gameObject.isMine) score += 5;
        setScore(score);
        countClosedTiles--;
        setCellColor(x, y, Color.CORNFLOWERBLUE);
        if (gameObject.isMine) {
            setCellValueEx(gameObject.x, gameObject.y, Color.CORAL, MINE);
            gameOver();
            return;
        } else if (gameObject.countMineNeighbors == 0) {
            setCellValue(gameObject.x, gameObject.y, "");
            List<GameObject> neighbors = getNeighbors(gameObject);
            for (GameObject neighbor : neighbors) {
                if (!neighbor.isOpen) {
                    openTile(neighbor.x, neighbor.y);
                }
            }
        } else {
            setCellNumber(x, y, gameObject.countMineNeighbors);
        }
        if (countClosedTiles == countMinesOnField) win();
    }

    private void markTile(int x, int y) {
        GameObject gameObject = gameField[y][x];
        if (gameObject.isOpen || countFlags == 0 && !gameObject.isFlag || isGameStopped) {
            return;
        }
        if (!gameObject.isFlag) {
            gameObject.isFlag = true;
            countFlags--;
            setCellValue(gameObject.x, gameObject.y, FLAG);
            setCellColor(x, y, Color.LIGHTGREEN);
        } else {
            gameObject.isFlag = false;
            countFlags++;
            setCellValue(gameObject.x, gameObject.y, "");
            setCellColor(x, y, Color.LIGHTGRAY);
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.CORAL, GAMEOVER, Color.BLACK, 60);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.CORAL, WIN, Color.BLACK, 50);
    }

    private void restart() {
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        countMinesOnField = 0;
        score = 0;
        setScore(score);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        openTile(x, y);
        if (isGameStopped) restart();
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }
}