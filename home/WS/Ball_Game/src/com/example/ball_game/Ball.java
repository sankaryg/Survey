package com.example.ball_game;
import java.util.Formatter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
/**
 * The bouncing ball.
 * 
 * @author Hock-Chuan Chua
 * @version v0.4 (31 October 2010)
 */
public class Ball {
   float x, y;           // Ball's center x and y (package access)
   float speedX, speedY; // Ball's speed per step in x and y (package access)
   float radius;         // Ball's radius (package access)
   private int color;  // Ball's color
   private static final int DEFAULT_COLOR = Color.BLUE;
   
   // For collision detection and response
   // Maintain the response of the earliest collision detected 
   //  by this ball instance. Only the first collision matters! (package access)
   CollisionResponse earliestCollisionResponse = new CollisionResponse();
private Paint mTextPen;
private Bitmap bitmap;
private Rect src;
   
   /**
    * Constructor: For user friendliness, user specifies velocity in speed and
    * moveAngle in usual Cartesian coordinates. Need to convert to speedX and
    * speedY in Java graphics coordinates for ease of operation.
    */
   public Ball(float x, float y, float radius,Bitmap bmp, float speed, float angleInDegree,
         int color) {
      this.x = x;
      this.y = y;
      // Convert (speed, angle) to (x, y), with y-axis inverted
      this.speedX = (float)(speed * Math.cos(Math.toRadians(angleInDegree)));
      this.speedY = (float)(-speed * (float)Math.sin(Math.toRadians(angleInDegree)));
      this.radius = radius;
      this.color = color;
      this.bitmap = bmp;
      src = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
      mTextPen = new Paint();
      mTextPen.setColor(color);
      mTextPen.setTextSize(14);
   }
   /** Constructor with the default color */
   public Ball(float x, float y, float radius, float speed, float angleInDegree,Bitmap bmp) {
      this(x, y, radius,bmp, speed, angleInDegree, DEFAULT_COLOR);
   }

   // Working copy for computing response in intersect(), 
   // to avoid repeatedly allocating objects.
   private CollisionResponse tempResponse = new CollisionResponse(); 

   /**
    * Check if this ball collides with the container box in the coming time-step.
    * 
    * @param box: outer rectangular container.
    * @param timeLimit: upperbound of the time interval.
    */
   public void intersect(BallWorld box, float timeLimit) {
      // Call movingPointIntersectsRectangleOuter, which returns the 
      // earliest collision to one of the 4 borders, if collision detected.
      CollisionPhysics.pointIntersectsRectangleOuter(x, y, speedX, speedY, radius,
            0, 0, box.getWidth(), box.getHeight(), timeLimit, tempResponse);
      if (tempResponse.t < earliestCollisionResponse.t) {
         earliestCollisionResponse.copy(tempResponse);
      }
   }
   
   // Working copy for computing response in intersect(Ball, timeLimit), 
   // to avoid repeatedly allocating objects.
   private CollisionResponse thisResponse = new CollisionResponse(); 
   private CollisionResponse anotherResponse = new CollisionResponse(); 

   /**
    * Check if this ball collides with the given another ball in the interval 
    * (0, timeLimit].
    * 
    * @param another: another moving ball to be checked for collision.
    * @param timeLimit: upperbound of the time interval.
    */
   public void intersect(Ball another, float timeLimit) {
      // Call movingPointIntersectsMovingPoint() with timeLimit.
      // Use thisResponse and anotherResponse, as the working copies, to store the
      // responses of this ball and another ball, respectively.
      // Check if this collision is the earliest collision, and update the ball's
      // earliestCollisionResponse accordingly.
      CollisionPhysics.pointIntersectsMovingPoint(
            this.x, this.y, this.speedX, this.speedY, this.radius,
            another.x, another.y, another.speedX, another.speedY, another.radius,
            timeLimit, thisResponse, anotherResponse);
      
      if (anotherResponse.t < another.earliestCollisionResponse.t) {
            another.earliestCollisionResponse.copy(anotherResponse);
      }
      if (thisResponse.t < this.earliestCollisionResponse.t) {
            this.earliestCollisionResponse.copy(thisResponse);
      }
   }

   /** 
    * Update the states of this ball for the given time.
    * 
    * @param time: the earliest collision time detected in the system.
    *    If this ball's earliestCollisionResponse.time equals to time, this
    *    ball is the one that collided; otherwise, there is a collision elsewhere.
    */
   public void update(float time) {
      // Check if this ball is responsible for the first collision?
      if (earliestCollisionResponse.t <= time) { // FIXME: threshold?
         // This ball collided, get the new position and speed
         this.x = earliestCollisionResponse.getNewX(this.x, this.speedX);
         this.y = earliestCollisionResponse.getNewY(this.y, this.speedY);
         this.speedX = earliestCollisionResponse.newSpeedX;
         this.speedY = earliestCollisionResponse.newSpeedY;
      } else {
         // This ball does not involve in a collision. Move straight.
         this.x += this.speedX * time;         
         this.y += this.speedY * time;         
      }
      // Clear for the next collision detection
      earliestCollisionResponse.reset();
   }

   /** Draw itself using the given graphics context. */
   public void draw(Canvas g) {
      //g.drawColor(color);
	   Rect rect = new Rect();
	   rect.set((int)x, (int)y, (int)x+bitmap.getWidth(), (int)y+bitmap.getHeight());
	   g.drawBitmap(bitmap, src,rect, mTextPen);
	   //g.drawCircle((int)(x - radius), (int)(y - radius), (int)( radius), mTextPen);
      //g.drawRect((int)(x - radius), (int)(y - radius), (int)(2 * radius),
        //    (int)(2 * radius),null);
   }
   
   /** Return the magnitude of speed. */
   public float getSpeed() {
      return (float)Math.sqrt(speedX * speedX + speedY * speedY);
   }
   
   /** Return the direction of movement in degrees (counter-clockwise). */
   public float getMoveAngle() {
      return (float)Math.toDegrees(Math.atan2(-speedY, speedX));
   }
   
   /** Return mass */
   public float getMass() {
      return radius * radius * radius / 1000f;
   }
   
   /** Return the kinetic energy (0.5mv^2) */
   public float getKineticEnergy() {
      return 0.5f * getMass() * (speedX * speedX + speedY * speedY);
   }

   /** Describe itself. */
   public String toString() {
      sb.delete(0, sb.length());
      formatter.format("@(%3.0f,%3.0f) r=%3.0f V=(%3.0f,%3.0f) " +
            "S=%4.1f \u0398=%4.0f KE=%3.0f", 
            x, y, radius, speedX, speedY, getSpeed(), getMoveAngle(),
            getKineticEnergy());  // \u0398 is theta
      return sb.toString();
   }
   private StringBuilder sb = new StringBuilder();
   private Formatter formatter = new Formatter(sb);

}
