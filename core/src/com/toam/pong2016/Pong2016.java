package com.toam.pong2016;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Pong2016 extends ApplicationAdapter implements InputProcessor{
    SpriteBatch batch;
    Sprite s_ball, s_paddle;
    Texture img_ball, img_paddle;
    World world;
    Body b_ball, b_paddle;
    Body b_EdgeScreen;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    OrthographicCamera camera;
    BitmapFont font;

    float torque = 0.0f;
    boolean drawSprite = true;

    final float PIXELS_TO_METERS = 100f;
    final float EDGE_OFFSET = 150;

    @Override
    public void create() {

        // width and height dimensions are using body dimensions instead of window dimensions
        float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        // Set the height to just 150 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
        float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS - EDGE_OFFSET/PIXELS_TO_METERS;

        batch = new SpriteBatch();

        img_ball = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/ball.png");
        img_paddle = new Texture("/home/luisfilipedias/Android/Apps/Pong/android/assets/paddle.png");

        s_ball = new Sprite(img_ball);
        s_paddle = new Sprite(img_paddle);

        s_ball.setPosition(-s_ball.getWidth()/2,-s_ball.getHeight()/2);
        s_paddle.setPosition( -s_paddle.getWidth()/2, -Gdx.graphics.getHeight()/2 - s_paddle.getHeight()/2);

        world = new World(new Vector2(0, -1f),true);

        BodyDef s_ballDef = new BodyDef();
        s_ballDef.type = BodyDef.BodyType.DynamicBody;
        s_ballDef.position.set((s_ball.getX() + s_ball.getWidth()/2) /
                        PIXELS_TO_METERS,
                (s_ball.getY() + s_ball.getHeight()/2) / PIXELS_TO_METERS);

        BodyDef s_paddleDef = new BodyDef();
        s_paddleDef.type = BodyDef.BodyType.StaticBody;
        s_paddleDef.position.set(0,0);

        BodyDef s_edgeDef = new BodyDef();
        s_edgeDef.type = BodyDef.BodyType.StaticBody;
        s_edgeDef.position.set(0,0);

        b_ball = world.createBody(s_ballDef);
        b_paddle = world.createBody(s_paddleDef);
        b_EdgeScreen = world.createBody(s_edgeDef);

        PolygonShape ballShape = new PolygonShape();
        ballShape.setAsBox(s_ball.getWidth() / 2 / PIXELS_TO_METERS, s_ball.getHeight()
                / 2 / PIXELS_TO_METERS);

        PolygonShape paddleShape = new PolygonShape();
        paddleShape.setAsBox(s_paddle.getWidth() / 2 / PIXELS_TO_METERS, s_paddle.getHeight()
                / 2 / PIXELS_TO_METERS);

        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(-w/2,-h/2,w/2,-h/2);

        FixtureDef fixtureBallDef = new FixtureDef();
        fixtureBallDef.shape = ballShape;
        fixtureBallDef.density = 0.1f;
        fixtureBallDef.restitution = 0.5f;

        FixtureDef fixturePaddleDef = new FixtureDef();
        fixturePaddleDef.shape = paddleShape;
        fixturePaddleDef.density = 0.1f;
        fixturePaddleDef.restitution = 0.5f;

        FixtureDef fixtureEdgeDef = new FixtureDef();
        fixtureEdgeDef.shape = edgeShape;

        b_ball.createFixture(fixtureBallDef);
        b_paddle.createFixture(fixturePaddleDef);
        b_EdgeScreen.createFixture(fixtureEdgeDef);

        ballShape.dispose();
        paddleShape.dispose();
        edgeShape.dispose();

        Gdx.input.setInputProcessor(this);

        debugRenderer = new Box2DDebugRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());
    }

    private float elapsed = 0;
    @Override
    public void render() {
        camera.update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);

        b_ball.applyTorque(torque,true);

        s_ball.setPosition((b_ball.getPosition().x * PIXELS_TO_METERS) - s_ball.
                        getWidth()/2 ,
                (b_ball.getPosition().y * PIXELS_TO_METERS) -s_ball.getHeight()/2 );

        s_paddle.setPosition((b_paddle.getPosition().x * PIXELS_TO_METERS) - s_paddle.
                        getWidth()/2 ,
                (b_paddle.getPosition().y * PIXELS_TO_METERS) -s_paddle.getHeight()/2 );

        s_ball.setRotation((float)Math.toDegrees(b_ball.getAngle()));

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        debugMatrix = batch.getProjectionMatrix().cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0);
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

        font.draw(batch,
                "Restitution: " + b_ball.getFixtureList().first().getRestitution(),
                -Gdx.graphics.getWidth()/2,
                Gdx.graphics.getHeight()/2 );
        batch.end();

        debugRenderer.render(world, debugMatrix);
    }

    @Override
    public void dispose() {
        img_ball.dispose();
        img_paddle.dispose();
        world.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {


        if(keycode == Input.Keys.RIGHT)
            b_ball.setLinearVelocity(1f, 0f);
        if(keycode == Input.Keys.LEFT)
            b_ball.setLinearVelocity(-1f,0f);

        if(keycode == Input.Keys.UP)
            b_ball.applyForceToCenter(0f,10f,true);
        if(keycode == Input.Keys.DOWN)
            b_ball.applyForceToCenter(0f, -10f, true);

        // On brackets ( [ ] ) apply torque, either clock or counterclockwise
        if(keycode == Input.Keys.RIGHT_BRACKET)
            torque += 0.1f;
        if(keycode == Input.Keys.LEFT_BRACKET)
            torque -= 0.1f;

        // Remove the torque using backslash /
        if(keycode == Input.Keys.BACKSLASH)
            torque = 0.0f;

        // If user hits spacebar, reset everything back to normal
        if(keycode == Input.Keys.SPACE|| keycode == Input.Keys.NUM_2) {
            b_ball.setLinearVelocity(0f, 0f);
            b_ball.setAngularVelocity(0f);
            torque = 0f;
            s_ball.setPosition(0f,0f);
            b_ball.setTransform(0f,0f,0f);
        }

        if(keycode == Input.Keys.COMMA) {
            b_ball.getFixtureList().first().setRestitution(b_ball.getFixtureList().first().getRestitution()-0.1f);
        }
        if(keycode == Input.Keys.PERIOD) {
            b_ball.getFixtureList().first().setRestitution(b_ball.getFixtureList().first().getRestitution()+0.1f);
        }
        if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.NUM_1)
            drawSprite = !drawSprite;

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning"
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        b_ball.applyForce(1f,1f,screenX,screenY,true);
        //s_ball.applyTorque(0.4f,true);
        return true;
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
