package com.toam.pong2016;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Pong2016_backup extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img_ball, img_paddle;
	Sprite s_ball, s_paddle;
    World world;
    /* edge is a simplifaction, is the imaginary line along the paddle x axis */
    Body b_ball, b_paddle, b_edge;
    OrthographicCamera camera;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    boolean drawSprite = true;

    float torque = 0.0f;

    final float PIXELS_TO_METERS = 1;
    final float EDGE_OFFSET = 150;

    @Override
	public void create () {

        // width and height dimensions are using body dimensions instead of window dimensions
//        float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        // Set the height to just 150 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
  //      float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS - EDGE_OFFSET/PIXELS_TO_METERS;

		batch = new SpriteBatch();

		img_ball = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/ball.png");
	//	img_paddle = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/paddle.png");

        /* create the sprites from the image to handle, and set initial position */
        s_ball = new Sprite(img_ball);
      //  s_paddle = new Sprite(img_paddle);

        s_ball.setPosition( -s_ball.getWidth()/2, -s_ball.getHeight()/2);

//		s_paddle.setPosition( -s_paddle.getWidth()/2, -Gdx.graphics.getHeight()/2 - s_paddle.getHeight()/2);

        /* create the world where the action will happen with 1f of vertical force */
        world = new World(new Vector2(0,-98f), true);

        /* create the bodies, static for paddle dinamic for the ball. sizes match the sprite sizes (or should) */
        BodyDef bodyDef = new BodyDef();
  //      BodyDef bodyDef2 = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(s_ball.getX(), s_ball.getY());
        //(s_ball.getX() + s_ball.getWidth() / 2) /
        //                PIXELS_TO_METERS,
          //      (s_ball.getY() + s_ball.getHeight() / 2) / PIXELS_TO_METERS);

//        bodyDef2.type = BodyDef.BodyType.StaticBody;
  //      bodyDef2.position.set((s_paddle.getX() + s_paddle.getWidth()/2) /
    //                    PIXELS_TO_METERS,
      //          (s_paddle.getY() + s_paddle.getHeight()/2) / PIXELS_TO_METERS);

        /* create body on the world according to definition */
        b_ball = world.createBody(bodyDef);
        //b_paddle = world.createBody(bodyDef2);
        //b_edge = world.createBody(bodyDef2);

        /* give a shape to the body */
        PolygonShape shape1 = new PolygonShape();
        //PolygonShape shape2 = new PolygonShape();
        //EdgeShape edgeShape = new EdgeShape();

        shape1.setRadius(s_ball.getHeight()/2);
        //shape2.setAsBox(s_paddle.getWidth() / 2 / PIXELS_TO_METERS, s_paddle.getHeight()
          //      / 2 / PIXELS_TO_METERS);

        /* customize body shape and other characteristics */
        FixtureDef fixtureDef = new FixtureDef();
        //FixtureDef fixtureDef2 = new FixtureDef();
        //FixtureDef fixtureDefEdge = new FixtureDef();

        //edgeShape.set(-w/2,-h/2,w/2,-h/2);

        fixtureDef.shape = shape1;
        fixtureDef.density = 0.1f;

        //fixtureDef2.shape = shape2;
        //fixtureDef2.density = 0.1f;

//        fixtureDefEdge.shape = edgeShape;

        b_ball.createFixture(fixtureDef);
  //      b_paddle.createFixture(fixtureDef2);
    //    b_edge.createFixture(fixtureDefEdge);

        /* dispose shapes, no need for them anymore */
        shape1.dispose();
      //  shape2.dispose();
        //edgeShape.dispose();

        // Create a Box2DDebugRenderer, this allows us to see the physics
        // simulation controlling the scene
        debugRenderer = new Box2DDebugRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());

        Gdx.input.setInputProcessor(this);
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //camera.update();
        world.step(1f / 60f, 6, 2);

        // apply torque, initially zero has no influence
        //b_ball.applyTorque(torque, true);

		// apply body rotation to sprite
        //s_ball.setRotation((float)Math.toDegrees(b_ball.getAngle()));
        // apply body position t sprite

        System.out.println("\n\nBody ball pos.\nx: " + b_ball.getPosition().x + "\ny: " + b_ball.getPosition().y);
        System.out.println("\n\nBody paddle pos.\nx: " + b_paddle.getPosition().x + "\ny: " + b_paddle.getPosition().y);

        /*s_ball.setPosition((b_ball.getPosition().x * PIXELS_TO_METERS) - s_ball.getWidth()/2,
                (b_ball.getPosition().y * PIXELS_TO_METERS) - s_ball.getHeight()/2);
        s_paddle.setPosition((b_paddle.getPosition().x * PIXELS_TO_METERS) - s_paddle.getWidth()/2,
                (b_paddle.getPosition().y * PIXELS_TO_METERS) - s_paddle.getHeight()/2);

        batch.begin();

        if(drawSprite) {
            batch.draw(s_ball, s_ball.getX(), s_ball.getY(), s_ball.getOriginX(),
                    s_ball.getOriginY(),
                    s_ball.getWidth(), s_ball.getHeight(), s_ball.getScaleX(), s_ball.
                            getScaleY(), s_ball.getRotation());
            batch.draw(s_paddle, s_paddle.getX(), s_paddle.getY(), s_paddle.getOriginX(),
                    s_paddle.getOriginY(),
                    s_paddle.getWidth(), s_paddle.getHeight(), s_paddle.getScaleX(), s_paddle.
                            getScaleY(), s_paddle.getRotation());
        }

		batch.end();


        // for debug purposes
        batch.setProjectionMatrix(camera.combined);
        // Scale down the sprite batches projection matrix to box2D size
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
        // Now render the physics world using our scaled down matrix
        // Note, this is strictly optional and is, as the name suggests, just
        // for debugging purposes
        debugRenderer.render(world, debugMatrix);
    */}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
        if( keycode == Input.Keys.ESCAPE)
            drawSprite = !drawSprite;

		return true;
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
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
