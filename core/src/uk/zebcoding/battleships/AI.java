package uk.zebcoding.battleships;

import java.util.Random;

/**
 * Created by Charlotte on 06/01/2015. (C) Charlotte Thompson 2014. (C) Charlotte Thompson 2015.
 */
public class AI {

    private boolean inAttack = false;
    private byte[] lastHitX = new byte[10];
    private byte[] lastHitY = new byte[10];
    private byte attackDir = 0;

    public void cpuMove() {
        byte[] coords;
        do {
            if (!inAttack) {
                coords = randomMove();
            } else {
                coords = tacticalMove(attackDir);
            }
        } while (doHit(coords, inAttack) == 0);
//        Gdx.app.debug("SKOPFSIOUDG","aoifkshdsvkl");
    }

    private byte[] randomMove() {
        Random r = new Random();

        byte[] coords = new byte[2];
        coords[0] = (byte) r.nextInt(10);
        coords[1] = (byte) r.nextInt(10);

        return coords;
    }

    private byte[] tacticalMove(byte attackDir) {
        byte[] coords = new byte[2];

        switch (attackDir) {
            case 1:
                coords[0] = this.lastHitX[0];
                coords[1] = (byte) (this.lastHitY[0] + 1);
                break;
            case 2:
                coords[0] = (byte) (this.lastHitX[0] + 1);
                coords[1] = this.lastHitY[0];
                break;
            case 3:
                coords[0] = this.lastHitX[0];
                coords[1] = (byte) (this.lastHitY[0] - 1);
                break;
            case 4:
                coords[0] = (byte) (this.lastHitX[0] - 1);
                coords[1] = this.lastHitY[0];
        }

        return coords;
    }

    private byte doHit(byte[] coords, boolean inAttack) {
        byte x = coords[0];
        byte y = coords[1];

        if (x < 0 || y < 0 || x > 9 || y > 9 || Battleships.playerStates[x][y] != 0) {
            if (inAttack) {
                if (this.attackDir != 4) {
                    this.attackDir++;
                } else {
                    this.inAttack = false;
                    this.attackDir = 0;
                }
            }
            return 0;
        }

        byte hit = (byte) (Battleships.playerBoard[x][y] ? 2 : 1);
        if (hit == 2) {
            Battleships.hitNC++;
            this.lastHitX = moveAllBytes(this.lastHitX);
            this.lastHitY = moveAllBytes(this.lastHitY);
            this.lastHitX[0] = coords[0];
            this.lastHitY[0] = coords[1];
            if (!inAttack) {
                this.inAttack = true;
                this.attackDir = 1;
            }
        }

        Battleships.playerStates[x][y] = hit;

        return hit;
    }

    private byte[] moveAllBytes(byte[] bytes) {
        System.arraycopy(bytes, 0, bytes, 1, bytes.length - 1);
        return bytes;
    }

    public boolean[][] setBoard() {
        Boolean failed;
        boolean[][] board = new boolean[10][10];
        Random random = new Random();
        do {
            failed = false;
            int attempts = 0;

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    board[j][i] = false;
                }
            }

            boolean r = random.nextBoolean();
            boolean OK;
            int x = random.nextInt(r ? 6 : 10);
            int y = random.nextInt(r ? 10 : 6);

            for (int i = 0; i < 5; i++) {
                if (r) {
                    board[x + i][y] = true;
                } else {
                    board[x][y + i] = true;
                }
            }

            do {
                OK = true;
                r = random.nextBoolean();
                x = random.nextInt(r ? 7 : 10);
                y = random.nextInt(r ? 10 : 7);

                for (int i = 0; i < 4; i++) {
                    if (r) {
                        if (board[x + i][y]) {
                            OK = false;
                        }
                    } else {
                        if (board[x][y + i]) {
                            OK = false;
                        }
                    }
                }
                if (attempts > 10) {
                    failed = true;
                    break;
                }
                attempts++;
            } while (!OK);

            if (!failed) {
                for (int i = 0; i < 4; i++) {
                    if (r) {
                        board[x + i][y] = true;
                    } else {
                        board[x][y + i] = true;
                    }
                }
            }

            for (int j = 0; j < 2; j++) {
                OK = true;
                if (!failed) {
                    do {
                        r = random.nextBoolean();
                        x = random.nextInt(r ? 8 : 10);
                        y = random.nextInt(r ? 10 : 8);

                        for (int i = 0; i < 3; i++) {
                            if (r) {
                                if (board[x + i][y]) {
                                    OK = false;
                                }
                            } else {
                                if (board[x][y + i]) {
                                    OK = false;
                                }
                            }
                        }
                        if (attempts > 10) {
                            failed = true;
                            break;
                        }
                        attempts++;
                    } while (!OK);
                }

                if (!failed) {
                    for (int i = 0; i < 3; i++) {
                        if (r) {
                            board[x + i][y] = true;
                        } else {
                            board[x][y + i] = true;
                        }
                    }
                }
            }

            if (!failed) {
                do {
                    OK = true;
                    r = random.nextBoolean();
                    x = random.nextInt(r ? 9 : 10);
                    y = random.nextInt(r ? 10 : 9);

                    for (int i = 0; i < 2; i++) {
                        if (r) {
                            if (board[x + i][y]) {
                                OK = false;
                            }
                        } else {
                            if (board[x][y + i]) {
                                OK = false;
                            }
                        }
                    }
                    if (attempts > 10) {
                        failed = true;
                        break;
                    }
                    attempts++;
                } while (!OK);
            }

            if (!failed) {
                for (int i = 0; i < 2; i++) {
                    if (r) {
                        board[x + i][y] = true;
                    } else {
                        board[x][y + i] = true;
                    }
                }
            }

        } while (failed);
        return board;
    }
}
