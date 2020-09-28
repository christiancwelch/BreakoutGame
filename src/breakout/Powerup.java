package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public abstract class Powerup extends Circle {

  private boolean powerUpHappened = false;

  public static final Paint BALL_POWER_UP_COLOR = Color.BLUEVIOLET;
  public static final Paint PADDLE_POWER_UP_COLOR = Color.HOTPINK;
  public static final Paint EXTRA_LIFE_POWER_UP_COLOR = Color.GREENYELLOW;

  public static final int POWERUP_SIZE = 10;
  private int dropSpeed = 200;

  public Powerup(double centerX, double centerY){
    super(centerX,centerY,POWERUP_SIZE);
    setDropDirection();
  }

  protected boolean hitPaddle(Paddle paddle){
    return this.getBoundsInParent().intersects(paddle.getBoundsInParent());
  }
  public boolean poweredUp(Level level, Paddle paddle){
    if(hitPaddle(paddle) && !powerUpHappened){
      doPowerup(level);
      powerUpHappened = true;
      return true;
    }
    return false;
  }

  public abstract void doPowerup(Level level);

  private int setDropDirection(){
    if(this.getCenterY() > Game.SIZE/2){
      dropSpeed *= -1;
    }
    return dropSpeed;
  }

  public void drop(double elapsedTime){
    this.setCenterY(this.getCenterY() + dropSpeed * elapsedTime);
  }
}
