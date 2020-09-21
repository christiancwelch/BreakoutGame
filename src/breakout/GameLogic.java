package breakout;

import java.util.Iterator;
import java.util.Random;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class GameLogic {

  private boolean ballLaunched = false;
  private boolean gamePaused = false;

  private Level level;
  private int levelNum;
  private Group myRoot;
  private int currentScore = 0;





    //take in a level
  public GameLogic(int level, Group root) {
    levelNum = level;
    myRoot = root;
    //maybe could add a class to package these guys together
    setUpLevel(levelNum, root);
  }

  public void setUpLevel(int levelNum, Group root){
    level = LevelConfig.setUpLevel(levelNum, root);
  }

  public static int getRandomNumber(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min) + min;
  }

 //stays in game logic
  public void handleKeyInput(KeyCode code) {
    //set up condition for when ball is not launched, ball gets moved too
    if (code.equals(KeyCode.LEFT) || code.equals(KeyCode.RIGHT)) {
      if (!gamePaused) {
        level.getPaddle().movePaddle(code);
        if (!ballLaunched) {
          for (Ball ball : level.getBalls()) {
            ball.moveBallWithPaddle(code);
          }
        }
      }
    }
    if (code.equals(KeyCode.SHIFT)) {
      setBallLaunched(true);
    }
    if (code.equals(KeyCode.SPACE)) {
      setGamePaused();
    }
    if (code.equals(KeyCode.R)) {
      resetGame();
    }
    if (code.equals(KeyCode.P)) {
      if(!gamePaused && ballLaunched){
      level.addPowerup();
    }}
    if (code.equals(KeyCode.S)){
      resetGame();
      levelNum++;
      setUpLevel(levelNum, level.getRoot());
    }
    if(code.equals(KeyCode.B)){
      level.addBall(level.getBalls().size() + 1);
      setBallLaunched(true);
    }
  }

  private void setGamePaused() {
    gamePaused = !gamePaused;
  }

  private void setBallLaunched(boolean status) {
      for (Ball ball : level.getBalls()) {
        if(!ball.isBallLaunched()) {
          ball.setLaunch();
        }
      }

    ballLaunched = status;
  }

  public void moveBall(double elapsedTime) {
    if (ballLaunched && !gamePaused) {
      for (Ball ball : level.getBalls()) {
        ball.moveBall(elapsedTime);
      }
    }
  }

  public void resetGame() {
    setBallLaunched(false);
    for (Ball ball : level.getBalls()) {
      ball.reset();
      level.remove(ball);
    }
    level.getPaddle().reset();
    level.reset();
    currentScore = 0;

  }

  public void checkBallBlockCollision(Ball ball) {
      Iterator<Block> itr = level.getBlockList().iterator();
      while (itr.hasNext()) {
        Block block = itr.next();
        if (ball.checkBallObjectCollision(block)) {
          block.handleHit(level);
          if (block.isBlockBroken()) {
            //TODO score changes here
            currentScore += 1;
            level.getDisplay().updateScore(currentScore, myRoot);
            level.remove(block);
            itr.remove();
            checkGameWon();
          }
        }
      }
  }




  public void dropPowerups(double elapsedTime) {
    if (!gamePaused) {
      for (Powerup powerup : level.getPowerups()) {
        powerup.drop(elapsedTime);
        if(powerup.powerUpPaddle(level.getPaddle())){
          level.remove(powerup);
        }
      }
    }
  }

  public void checkGameWon(){
    if(level.getBlockList().isEmpty()){
      Text won = new Text(Game.SIZE/2 - 50, Game.SIZE/2, "You won this level!\nPress S to continue");
      won.setId("WonText");
      level.getRoot().getChildren().add(won);
    }
  }








  public void checkCollision() {
    for (Ball ball : level.getBalls()) {
      checkBallBlockCollision(ball);
      ball.checkBallObjectCollision(level.getPaddle());
    }
  }

  public void checkBallDroppedThroughBottom() {
    int numOfBalls = level.getBalls().size();
    for (Ball ball : level.getBalls()) {
      if (ball.checkBallDroppedThroughBottom()) {
        numOfBalls -= 1;
        //add something to account for if we want loss to only be on one ball ?
      }
    }
    if (numOfBalls == 0) {
      resetGame();
    }
  }
}

