package uk.zebcoding.battleships;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uk.zebcoding.battleships.events.Inputs;

import java.util.Random;

public class Battleships extends ApplicationAdapter {
	private SpriteBatch batch;
	private BitmapFont font;
	private BitmapFont winFont;
	private BitmapFont shFont;
	private BitmapFont winShFont;
	private Texture pin;
	private Texture pinown;
	private Texture ship;
	private Texture miss;
	private Texture missNoBg;
	private Texture hit;
	private Texture hitNoBg;
	private static int gamestate, mouseX, mouseY, hitN, counter;
	private static boolean rotated;

	/**
	 * 0: empty.
	 * 1: miss.
	 * 2: hit.
	 */
	private static int[][] cpuStates = new int[10][10];
	private static int[][] playerStates = new int[10][10];

	/**
	 * false: no ship.
	 * true: ship.
	 */
	private static boolean[][] cpuBoard = new boolean[10][10];
	private static boolean[][] playerBoard = new boolean[10][10];

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("console.fnt"), Gdx.files.internal("console.png"), false);
		shFont = new BitmapFont(Gdx.files.internal("console.fnt"), Gdx.files.internal("console.png"), false);
		winFont = new BitmapFont(Gdx.files.internal("console.fnt"), Gdx.files.internal("console.png"), false);
		winShFont = new BitmapFont(Gdx.files.internal("console.fnt"), Gdx.files.internal("console.png"), false);
		font.setColor(Color.BLACK);
		font.setScale(1F);
		winFont.setColor(Color.WHITE);
		winFont.setScale(1F);
		shFont.setColor(Color.DARK_GRAY);
		shFont.setScale(1F);
		winShFont.setColor(Color.LIGHT_GRAY);
		winShFont.setScale(1F);
		pin = new Texture("hole.png");
		hit = new Texture("hit.png");
		hitNoBg = new Texture("hitnobg.png");
		miss = new Texture("miss.png");
		missNoBg = new Texture("missnobg.png");
		pinown = new Texture("pinown.png");
		ship = new Texture("ship.png");

		gamestate = 0;
		mouseX = 0;
		mouseY = 0;
		hitN = 0;

		cpuStates = initBoard();
		playerStates = initBoard();

		cpuBoard = setBoard();

		Gdx.input.setInputProcessor(new Inputs());
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		shFont.dispose();
		pin.dispose();
	}

	@Override
	public void render() {
		if (hitN >= 17) {
			gamestate = 10;
		}

		batch.begin();

		Gdx.gl.glClearColor(0.101960784F, 0.207843137F, 0.509803922F, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		int i1;
		int j1;
		for (int i = 0; i < 10; i++) {
			i1 = i * 24;
			shFont.draw(batch, Integer.toString(9 - i), 8, 8 + 24 * i + 284);
			font.draw(batch, Integer.toString(9 - i), 7, 9 + 24 * i + 284);

			shFont.draw(batch, Integer.toString(9 - i), 8, 8 + 24 * i);
			font.draw(batch, Integer.toString(9 - i), 7, 9 + 24 * i);

			for (int j = 0; j < 10; j++) {
				j1 = j * 24 + 24;
				batch.draw(cpuStates[j][9 - i] == 0 ? pin : cpuStates[j][9 - i] == 1 ? miss : hit, j1, i1 + 284);
				batch.draw((playerBoard[j][9 - i] ? ship : pinown), j1, i1);
				if (playerStates[j][9 - i] == 1) {
					batch.draw(missNoBg, j1, i1);
				} else if (playerStates[j][9 - i] == 2) {
					batch.draw(hitNoBg, j1, i1);
				}
			}
		}

		for (int j = 0; j < 10; j++) {
			shFont.draw(batch, Integer.toString(j), 32 + 24 * j, 247 + 284);
			font.draw(batch, Integer.toString(j), 31 + 24 * j, 248 + 284);

			shFont.draw(batch, Integer.toString(j), 32 + 24 * j, 247);
			font.draw(batch, Integer.toString(j), 31 + 24 * j, 248);
		}

		switch (gamestate) {
			case 0:
				batch.draw(ship, -12 + mouseX, -12 + mouseY);
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24), -12 + mouseY + (!rotated ? 0 : 24));
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24 * 2), -12 + mouseY + (!rotated ? 0 : 24 * 2));
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24 * 3), -12 + mouseY + (!rotated ? 0 : 24 * 3));
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24 * 4), -12 + mouseY + (!rotated ? 0 : 24 * 4));
				break;
			case 1:
				batch.draw(ship, -12 + mouseX, -12 + mouseY);
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24), -12 + mouseY + (!rotated ? 0 : 24));
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24 * 2), -12 + mouseY + (!rotated ? 0 : 24 * 2));
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24 * 3), -12 + mouseY + (!rotated ? 0 : 24 * 3));
				break;
			case 2:
				batch.draw(ship, -12 + mouseX, -12 + mouseY);
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24), -12 + mouseY + (!rotated ? 0 : 24));
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24 * 2), -12 + mouseY + (!rotated ? 0 : 24 * 2));
				break;
			case 3:
				batch.draw(ship, -12 + mouseX, -12 + mouseY);
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24), -12 + mouseY + (!rotated ? 0 : 24));
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24 * 2), -12 + mouseY + (!rotated ? 0 : 24 * 2));
				break;
			case 4:
				batch.draw(ship, -12 + mouseX, -12 + mouseY);
				batch.draw(ship, -12 + mouseX + (rotated ? 0 : 24), -12 + mouseY + (!rotated ? 0 : 24));
				break;
		}

		if (gamestate == 10) {
			String win = "You Win!";
			winFont.draw(batch, win, 99, 272);
		}

		batch.end();
	}

	private static int[][] initBoard() {
		int[][] board = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				board[i][j] = 0;
			}
		}

		return board;
	}

	public static boolean setStates(int x, int y) {
		if (cpuStates[x][y] == 0) {
			int hit = cpuBoard[x][y] ? 2 : 1;
			if (hit == 2) hitN++;
			cpuStates[x][y] = hit;
			return true;
		}
		return false;
	}

	public static boolean[][] setBoard() {
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
						if (board[x + i][y]) OK = false;
					} else {
						if (board[x][y + i]) OK = false;
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
								if (board[x + i][y]) OK = false;
							} else {
								if (board[x][y + i]) OK = false;
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
							if (board[x + i][y]) OK = false;
						} else {
							if (board[x][y + i]) OK = false;
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

	public static boolean attemptPlace(int x, int y, boolean r, int l) {
		if (!canPlace(x, y, r, l)) {
			return false;
		} else {
			for (int i = 0; i < l; i++) {
				if (r) {
					playerBoard[x][y - i] = true;
				} else {
					playerBoard[x + i][y] = true;
				}
			}

			return true;
		}
	}

	public static boolean canPlace(int x, int y, boolean r, int l) {
		if (x < 0 || x > 9 || y < 0 || y > 9) {
			return false;
		}

		for (int i = 0; i < l; i++) {
			if (r) {
				if (y - i < 0) return false;
				if (playerBoard[x][y - i]) return false;
			} else {
				if (x + i > 9) return false;
				if (playerBoard[x + i][y]) return false;
			}
		}

		return true;
	}

	public static void setMouseX(int x) {
		mouseX = x;
	}

	public static void setMouseY(int y) {
		mouseY = y;
	}

	public static boolean getRotated() {
		return rotated;
	}

	public static void setRotated() {
		rotated = !rotated;
	}

	public static int getGamestate() {
		return gamestate;
	}

	public static void setGamestate(int gamestate) {
		Battleships.gamestate = gamestate;
	}

	public static void cpuMove() {
		int x, y;
		Random r = new Random();
		do {
			x = r.nextInt(10);
			y = r.nextInt(10);
		} while (playerStates[x][y] != 0);
		Gdx.app.debug("BATT", "Completed");
		playerStates[x][y] = (playerBoard[x][y] ? 2 : 1);
	}
}
