package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;

public class Game extends Application {

  public static final String TITLE = "Breakout";
  public static final int SIZE = 350;
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  public static final Paint BACKGROUND = Color.AZURE;
  public static final int TOP_OF_STAGE = 0;

  private final Group root = new Group();
  private final GameLogic gameLogic = new GameLogic(1, root);

  /**
   * Start the program.
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    // attach scene to the stage and display it
    Scene myScene = setupScene();
    stage.setScene(myScene);
    stage.setTitle(TITLE);
    stage.show();
    // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  // Create the game's "scene": what shapes will be in the game and their starting properties
  Scene setupScene() {
    Scene scene = new Scene(root, Game.SIZE, Game.SIZE, Game.BACKGROUND);
    // respond to input
    scene.setOnKeyPressed(e -> gameLogic.handleKeyInput(e.getCode()));
    return scene;
  }

  // Handle the game's "rules" for every "moment":
  // - movement, how do things change over time
  // - collisions, did things intersect and, if so, what should happen
  // - goals, did the game or level end?
  void step(double elapsedTime)  {
    gameLogic.moveBall(elapsedTime);
    gameLogic.checkCollision();
    gameLogic.dropPowerups(elapsedTime);
    try {
      gameLogic.updateHighScore();
    } catch (FileNotFoundException e) {

    }
  }
}



