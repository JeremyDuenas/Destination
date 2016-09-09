package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class destinationGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	Texture[] sparrow;
	Texture topTube;
	Texture bottomTube;
	int flapState = 0;
	float sparrowY = 0;
	float velocity = 0;
	Circle sparrowCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

	int gameState = 0;
	float gravity = 2;


	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] bottomTubeRectangles;

	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("background new.png");
		gameover = new Texture("gameover.png");
		//shapeRenderer = new ShapeRenderer();
		sparrowCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		sparrow = new Texture[2];
		sparrow[0] = new Texture("sparrow v2 part2 nonfire.png");
		sparrow[1] = new Texture("sparrow v2 trans.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube new trans.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		startGame();
	}

	public void startGame() {
		sparrowY = Gdx.graphics.getHeight() / 2 - sparrow[0].getHeight() / 2;
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - (Gdx.graphics.getHeight() * .5f));
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState == 1) {
			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;
				Gdx.app.log("Score", String.valueOf(score));
				if (scoringTube < numberOfTubes - 1) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}
			if (Gdx.input.justTouched()) {
				velocity = -25;
			}
			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - (Gdx.graphics.getHeight() * .2f));
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}
			if (sparrowY > 0) {
				velocity = velocity + gravity;
				sparrowY -= velocity;
			} else {
				gameState = 2;
			}
		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {
			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
			if (Gdx.input.justTouched()) {

				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}
		batch.draw(sparrow[flapState], Gdx.graphics.getWidth() / 2 - sparrow[flapState].getWidth() / 2, sparrowY);
		font.draw(batch, String.valueOf(score), 100, 200);
		sparrowCircle.set(Gdx.graphics.getWidth() / 2, sparrowY + sparrow[flapState].getHeight() / 2, sparrow[flapState].getWidth() / 2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(sparrowCircle.x, sparrowCircle.y, sparrowCircle.radius);
		for (int i = 0; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			if (Intersector.overlaps(sparrowCircle, bottomTubeRectangles[i])) {
				gameState = 2;
			}
		}
		batch.end();
		//shapeRenderer.end();
	}
}