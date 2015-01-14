package com.maslo.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	final Drop game;
	Texture dropIm;
	Texture bucketIm;
	Sound sound;
	Music music;

	OrthographicCamera camera;
	//SpriteBatch batch;
	Rectangle bucket;

	Vector3 touchPos;
	Array<Rectangle> arrayDrops;
	long lastDropTime;
	 int dropsGathered;

	public GameScreen(final Drop gam)  {
		game=gam;
		//Получаем изображения и музыку
		dropIm = new Texture(Gdx.files.internal("droplet.png"));
		bucketIm = new Texture(Gdx.files.internal("bucket.png"));
		sound = Gdx.audio.newSound(Gdx.files.internal("mix11.wav"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		//Запускаем музыку
		music.setLooping(true);
		//music.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false);

		//batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		arrayDrops = new Array<Rectangle>();
		spawRaindrop();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(bucketIm, bucket.x, bucket.y);
		for (Rectangle raindrop : arrayDrops) {
			game.batch.draw(dropIm, raindrop.x, raindrop.y);
		}
		game.batch.end();

		if (Gdx.input.isTouched()) {
			touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawRaindrop();

		Iterator<Rectangle> iter = arrayDrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0)
				iter.remove();
			if (raindrop.overlaps(bucket)) {
				sound.play();
				dropsGathered++;

				iter.remove();
			}
		}

	}

	private void spawRaindrop() {
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(0, Gdx.graphics.getWidth() - 64);
		rainDrop.y = Gdx.graphics.getHeight();
		rainDrop.width = 64;
		rainDrop.height = 64;
		arrayDrops.add(rainDrop);
		lastDropTime = TimeUtils.nanoTime();

	}

	@Override
	public void dispose() {
		dropIm.dispose();
		bucketIm.dispose();
		sound.dispose();
		music.dispose();
		//batch.dispose();
	}

	@Override
	public void show() {
		music.play();
		
	}



	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}
