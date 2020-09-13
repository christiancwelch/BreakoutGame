package breakout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import javafx.scene.Group;
import jdk.swing.interop.SwingInterOpUtils;


public class LevelConfig {
  private final
  static int BLOCK_WIDTH = 50;
  private final static int BLOCK_HEIGHT = 10;
  private static int PADDLE_WIDTH = 75;
  private static int PADDLE_XPOS = Main.SIZE/2 - PADDLE_WIDTH/2;
  private static int PADDLE_YPOS = 300;
  public Paddle myPaddle;
  public Ball myBall;
  private List<String[]> readBlockFile(String dataSource){
    List<String[]> blocks = new ArrayList<>();
    InputStream textFile = null;
    try {
      textFile = Objects.requireNonNull(LevelConfig.class.getClassLoader().getResource(dataSource))
              .openStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String[] block;
    Scanner scan = new Scanner(textFile);
    while (scan.hasNextLine()) {
      block = scan.nextLine().split(",");
      blocks.add(block);
    }
    return blocks;
  }
  void setUpBlocks(Group root, String dataSource){
    int rowNum, colNum = 0;
    List<String[]> blockFile = readBlockFile(dataSource);
    for(String[] row : blockFile){
      rowNum = 0;
      for(String blockType : row){
        Block block = getBlock(blockType,rowNum,colNum);
        block.setId(String.format("%d,%d",rowNum,colNum));
        root.getChildren().add(block);
        rowNum++;
      }
      colNum++;
    }
  }
  private Block getBlock(String blockType, int rowNum, int colNum){
    if(blockType.equals("0")){
      return new ExampleBlock(rowNum,colNum,BLOCK_WIDTH, BLOCK_HEIGHT);
    }
    return null;
  }

  private void setUpBall(Group root){
    myBall = new Ball(Main.SIZE/2,295,5);
    myBall.setId("myBall");
    root.getChildren().add(myBall);
  }
  private void setUpPaddle(Group root){
    System.out.println(PADDLE_XPOS);
    myPaddle = new Paddle(PADDLE_XPOS,PADDLE_YPOS,75,10);
    myPaddle.setId("myPaddle");
    root.getChildren().add(myPaddle);
  }

  public void setUpLevel(int level, Group root){
    if(level == 1){
      //change later to just be one statement
      setUpBlocks(root,"level1.txt");
      setUpPaddle(root);
      setUpBall(root);
    }
  }
  public static void main(String args[]){
    LevelConfig level = new LevelConfig();
    level.readBlockFile("level1.txt");
  }
}
