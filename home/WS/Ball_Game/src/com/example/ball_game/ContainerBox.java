package com.example.ball_game;
import android.graphics.Canvas;
import android.graphics.Color;
/**
 * A rectangular container box, containing the bouncing ball.  
 * 
 * @author Hock-Chuan Chua
 * @version 31 October 2010
 */
public class ContainerBox {
   int minX, maxX, minY, maxY;  // Box's bounds (package access)
   private int colorFilled;   // Box's filled color (background)
   private int colorBorder;   // Box's border color
   private static final int DEFAULT_COLOR_FILLED = Color.BLACK;
   private static final int DEFAULT_COLOR_BORDER = Color.YELLOW;
   
   /** Constructors */
   public ContainerBox(int x, int y, int width, int height, int colorFilled, int colorBorder) {
      minX = x;
      minY = y;
      maxX = x + width - 1;
      maxY = y + height - 1;
      this.colorFilled = colorFilled;
      this.colorBorder = colorBorder;
   }
   /** Constructor with the default color */
   public ContainerBox(int x, int y, int width, int height) {
      this(x, y, width, height, DEFAULT_COLOR_FILLED, DEFAULT_COLOR_BORDER);
   }
   
   /** Set or reset the boundaries of the box. */
   public void set(int x, int y, int width, int height) {
      minX = x;
      minY = y;
      maxX = x + width - 1;
      maxY = y + height - 1;
   }

   /** Draw itself using the given graphic context. */
   public void draw(Canvas g) {
      g.drawColor(colorFilled);
      g.drawRect(minX, minY, maxX - minX - 1, maxY - minY - 1,null);
      g.drawColor(colorBorder);
      g.drawRect(minX, minY, maxX - minX - 1, maxY - minY - 1,null);
   }
}
