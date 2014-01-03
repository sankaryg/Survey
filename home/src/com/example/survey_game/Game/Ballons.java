package com.example.survey_game.Game;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.example.survey_game.Util.Constants;
import com.example.survey_game_fuction.Feature;

public class Ballons implements Serializable{
	int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

	private static final int BMP_ROWS = 1;

	public static final int BMP_COLUMNS = 8;

	private static int MAX_SPEED = 5;

	private GameView gameView;

	private Bitmap bmp;

	private float x = 0;

	private float y = 0;

	private float xSpeed;

	private float ySpeed;

	public  int currentFrame = 0;

	private int width;

	private int height;

	private int rotation;

	private double _radians;

	public double _velocityX;

	public double _velocityY;

	private float _mass;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	Paint paint;

	public int getCurrentFrame() {
		return currentFrame;
	}

	/*public static void setCurrentFrame(int currentFrame) {
		Ballons.currentFrame = currentFrame;
	}*/
	private boolean remove;

	private int srcY;

	private Feature feature;

	private Rect src;

	private Rect dst;

	private Vector velocity;

	private Vector position;

	private float scale;

	private Typeface tf;

	private int fontSize;

	private String type;

	private float angle;

	public Bitmap getBmp() {
		return bmp;
	}

	public boolean isRemove() {
		return remove;
	}

	public Ballons(GameView gameView, Bitmap bmp, double radius,double rotation2,
			 Feature feature2, float f, String font, int fontsize) {

		this.width = bmp.getWidth() / BMP_COLUMNS;
		scale = gameView.getResources().getDisplayMetrics().density;
		 tf = Typeface.createFromAsset(gameView.getContext().getAssets(),font);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(32);
		paint.setTypeface(tf);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		this.height = (bmp.getHeight() / 2) / BMP_ROWS;
		this.feature = feature2;
		this.gameView = gameView;
		this.fontSize = fontsize;
		this.angle = (float) rotation2;
		if(fontSize<=12)
			fontSize =16;
		this.bmp = bmp;
		//this.type = str1;
		this._mass = (int) radius;

		Random rnd = new Random();

		x = (float) (Math.random()*(gameView.getWidth()-width-20));

		y = (float) (Math.random()*(gameView.getHeight()-height-20));
		//this.velocity = new Vector(0, 0);
		//this.position = new Vector(x, y);
		this.xSpeed = (float)( f);//*Math.cos(Math.toRadians(angle)));
	    this.ySpeed = (float)(-f);//* (float)Math.sin(Math.toRadians(angle)));
		/*if(i>0)
		MAX_SPEED = i;//Integer.parseInt(i);
		xSpeed = MAX_SPEED;//rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
		this.rotation = (int) radius;
		this._radians = this.rotation * Math.PI / 180;
		ySpeed = MAX_SPEED;//rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
*/		//this.set_velocityX(Math.cos(this._radians) * this.MAX_SPEED);
		//this.set_velocityY(Math.sin(this._radians) * this.MAX_SPEED);

	}

	private void update() {
		Log.d("update", Constants.threeBalloons+"_"+currentFrame);
		if(!Constants.threeBalloons)
		{
		if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {

			xSpeed = -xSpeed;

		}

		x = x + xSpeed;

		if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {

			ySpeed = -ySpeed;

		}

		y = y + ySpeed;

		
		}
		currentFrame = ++currentFrame % BMP_COLUMNS;
	}
	/** Return the magnitude of speed. */
	   public float getSpeed() {
	      return (float)Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
	   }
	   
	   /** Return the direction of movement in degrees (counter-clockwise). */
	   public float getMoveAngle() {
	      return (float)Math.toDegrees(Math.atan2(-ySpeed, xSpeed));
	   }
	   
	   /** Return mass */
	   public float getMass() {
	      return _mass * _mass * _mass / 1000f;
	   }
	   
	   /** Return the kinetic energy (0.5mv^2) */
	   public float getKineticEnergy() {
	      return 0.5f * getMass() * (xSpeed * xSpeed + ySpeed * ySpeed);
	   }
	public void onDraw(Canvas canvas) {

		if(!gameView.isPaused()){
		gameView.gameUpdate();//update();

		int srcX = currentFrame * width;
		if (remove)
			srcY = 1 * height;
		else
			srcY = 0 * height;
		
		src = new Rect(srcX, srcY, srcX + width, srcY + height);

		dst = new Rect((int)x, (int)y, (int)(x + width), (int)(y + height));

		canvas.drawBitmap(bmp, src, dst, paint);
		 Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
         // text color - #3D3D3D
         paint.setColor(Color.rgb(110,110, 110));
         // text size in pixels
         paint.setTypeface(tf);
         paint.setTextSize((int) (fontSize * scale));
         // text shadow
         paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
         paint.setColor(Color.BLACK);
         // draw text to the Canvas center
         Rect bounds = new Rect();
         if(feature == null){
        	 String str = "None";
        	 paint.getTextBounds(str, 0, str.length(), bounds);
         }else
         paint.getTextBounds(feature.getFeature().toString(), 0, feature.getFeature().length(), bounds);
         int x1 = (bmp.getWidth() - bounds.width())/6;
         int y1 = (bmp.getHeight() + bounds.height())/5;
        
         //canvas.drawText(feature.getFeature().toString(), x1+ width / 4 * scale, y1+ height * scale, paint);
		//drawTextToBitmap(canvas, bmp, feature.getFeature().toString(),src,dst);
         if(feature == null){
        	 String str = "None";
        	 
        	 canvas.drawText(str , x + width / 4, y + height , paint);
         }else
         canvas.drawText(feature.getFeature() , x + width / 4, y + height , paint);
		}
	}
	  
	public Rect getDst() {
		return dst;
	}
	public void cleanup(){
		gameView = null;
		bmp.recycle();
	}
	 CollisionResponse earliestCollisionResponse = new CollisionResponse();
	private CollisionResponse tempResponse = new CollisionResponse(); 

	   /**
	    * Check if this ball collides with the container box in the coming time-step.
	    * 
	    * @param box: outer rectangular container.
	    * @param timeLimit: upperbound of the time interval.
	    */
	   public void intersect(GameView box, float timeLimit) {
		   if(!Constants.threeBalloons)
			{
		   CollisionPhysics.pointIntersectsRectangleOuter(x, y, xSpeed, ySpeed, 0,
	            0, 0, box.getWidth() - width,  box.getHeight() - height, timeLimit, tempResponse);
	      if (tempResponse.t < earliestCollisionResponse.t) {
	         earliestCollisionResponse.copy(tempResponse);
	      }
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
	   public void intersect(Ballons another, float timeLimit) {
	      // Call movingPointIntersectsMovingPoint() with timeLimit.
	      // Use thisResponse and anotherResponse, as the working copies, to store the
	      // responses of this ball and another ball, respectively.
	      // Check if this collision is the earliest collision, and update the ball's
	      // earliestCollisionResponse accordingly.
		   if(!Constants.threeBalloons)
			{
	      CollisionPhysics.pointIntersectsMovingPoint(
	            this.x, this.y, this.xSpeed, this.ySpeed, this._mass,
	            another.x, another.y, another.xSpeed, another.ySpeed, another._mass,
	            timeLimit, thisResponse, anotherResponse);
	      
	      if (anotherResponse.t < another.earliestCollisionResponse.t) {
	            another.earliestCollisionResponse.copy(anotherResponse);
	      }
	      if (thisResponse.t < this.earliestCollisionResponse.t) {
	            this.earliestCollisionResponse.copy(thisResponse);
	      }
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
		   if(!Constants.threeBalloons)
			{
		   if (earliestCollisionResponse.t <= time) { // FIXME: threshold?
	         // This ball collided, get the new position and speed
	         this.x = earliestCollisionResponse.getNewX(this.x, this.xSpeed);
	         this.y = earliestCollisionResponse.getNewY(this.y, this.ySpeed);
	         this.xSpeed = earliestCollisionResponse.newSpeedX;
	         this.ySpeed = earliestCollisionResponse.newSpeedY;
	      } else {
	         // This ball does not involve in a collision. Move straight.
	         this.x += this.xSpeed * time;         
	         this.y += this.ySpeed * time;         
	      }
	      currentFrame = ++currentFrame % BMP_COLUMNS;
	      // Clear for the next collision detection
	      earliestCollisionResponse.reset();
			}
	   }
	 /*public static final boolean checkcirclecollide(double x1, double y1, float radius1, double x2, double y2, float radius2){
	        return Math.abs((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) < (radius1 + radius2) * (radius1 + radius2);
	    }
	 public void response(Ballons ballon){
		 double d = Math.sqrt(Math.pow(this.getX() - ballon.getX(), 2) + Math.pow(this.getY() - ballon.getY(), 2));
		 double nx = (ballon.getX() - this.getX()) / d;
		 double ny = (ballon.getY() - this.getY()) / d;
		 double p = 2 * (this.get_velocityX()* nx + this.get_velocityY() * ny - ballon.get_velocityX() * nx - ballon.get_velocityY() * ny) / (this.get_mass() + ballon.get_mass());
		 this.x = (int) (this.getX() - p * this.get_mass() * nx);
		 this.y = (int) (this.getY() - p * this.get_mass() * ny);
		 ballon.x = (int) (ballon.getX() - p * ballon.get_mass() * nx);
		 ballon.y = (int) (ballon.getY() - p * ballon.get_mass() * ny);
	 }
	public void checkReflectCollide(Ballons ballon){
		float xd = this.getX() - ballon.getX();
		float yd = this.getY() - ballon.getY();
		if(getDistance(xd, yd)<20){
			resolveCollision(this,ballon);
		}
	} public static void intersect(Ballons a, Ballons b) {
        //ref http://gamedev.stackexchange.com/questions/20516/ball-collisions-sticking-together
        double xDist, yDist;
        xDist = a.x - b.x;
        yDist = a.y - b.y;
        double distSquared = xDist * xDist + yDist * yDist;
        // Check the squared distances instead of the the distances, same
        // result, but avoids a square root.
        if (distSquared <= (a.width/2 + b.width/2) * (a.width/2 + b.width/2)) {
            double speedXocity = b.xSpeed - a.xSpeed;
            double speedYocity = b.ySpeed - a.ySpeed;
            double dotProduct = xDist * speedXocity + yDist * speedYocity;
            // Neat vector maths, used for checking if the objects moves towards
            // one another.
            if (dotProduct > 0) {
                double collisionScale = dotProduct / distSquared;
                double xCollision = xDist * collisionScale;
                double yCollision = yDist * collisionScale;
                // The Collision vector is the speed difference projected on the
                // Dist vector,
                // thus it is the component of the speed difference needed for
                // the collision.
                double combinedMass = a.get_mass() + b.get_mass();
                double collisionWeightA = 2 * b.get_mass() / combinedMass;
                double collisionWeightB = 2 * a.get_mass() / combinedMass;
                a.xSpeed += (collisionWeightA * xCollision);
                a.ySpeed += (collisionWeightA * yCollision);
                b.xSpeed -= (collisionWeightB * xCollision);
                b.ySpeed -= (collisionWeightB * yCollision);
               
                if (a.xSpeed <= 0) {

        			a.xSpeed = -MAX_SPEED;

        		}else if(a.xSpeed > MAX_SPEED){
        			a.xSpeed = MAX_SPEED;
        		}


                if (a.ySpeed <= 0) {

        			a.ySpeed = -MAX_SPEED;

        		}else if(a.ySpeed > MAX_SPEED){
        			a.ySpeed = MAX_SPEED;
        		}
                

                if (b.xSpeed <= 0) {

        			b.xSpeed = -MAX_SPEED;

        		}else if(b.xSpeed > MAX_SPEED){
        			b.xSpeed = MAX_SPEED;
        		}


                if (b.ySpeed <= 0) {

        			b.ySpeed = -MAX_SPEED;

        		}else if(b.ySpeed > MAX_SPEED){
        			b.ySpeed = MAX_SPEED;
        		}

            }
        }
    }
	public void resolveCollision(Ballons ballons1, Ballons ballon2) {
		// TODO Auto-generated method stub
	float nX1 = ballons1.x;
    float nY1 = ballons1.y;
    double nDistX = ballon2.x - nX1;
    double nDistY = ballon2.y - nY1;
    
    double nDistance = Math.sqrt ( nDistX * nDistX + nDistY * nDistY );
    float nRadiusA = ballons1.width/2;
    float nRadiusB = ballon2.width/2;
    //var nRadius:Number = 10;
    
    double nNormalX = nDistX/nDistance;
    double nNormalY = nDistY/nDistance;
    
    float nMidpointX = ( nX1 + ballon2.x )/2;
    float nMidpointY = ( nY1 + ballon2.y )/2;
    
    ballons1.x = (int) (nMidpointX - nNormalX * nRadiusA);
    ballons1.y = (int) (nMidpointY - nNormalY * nRadiusA);
    ballon2.x = (int) (nMidpointX + nNormalX * nRadiusB);
    ballon2.y = (int) (nMidpointY + nNormalY * nRadiusB);
    
    double nVector = ( ( ballons1.xSpeed - ballon2.xSpeed ) * nNormalX )+ ( ( ballons1.ySpeed - ballon2.ySpeed ) * nNormalY );
    double nVelX = nVector * nNormalX;
    double nVelY = nVector * nNormalY;
    
    ballons1.xSpeed -= nVelX;
    ballons1.ySpeed -= nVelY;
    ballon2.xSpeed += nVelX;
    ballon2.ySpeed += nVelY;
	}

	public boolean colliding(Ballons ball)
	{
		float xd = this.getX() - ball.getX();
		float yd = this.getY() - ball.getY();

		
		float sumRadius = this.getBmp().getWidth()/2 + ball.getBmp().getWidth()/2 ;
		float sqrRadius = sumRadius * sumRadius;

		float distSqr = (xd * xd) + (yd * yd);

		if (distSqr <= sqrRadius)
		{
			return true;
		}

		return false;

	}

	
	
	private double getDistance(float xd, float yd) {
		// TODO Auto-generated method stub
		return ((xd * xd) + (yd * yd));
	}

	public boolean hitTestCircle(Ballons tampBall1, Ballons tempBall2) {
		boolean retval = false;

		double distance = getDistance(tampBall1.getX() - tempBall2.getX(),
				tampBall1.getY() - tempBall2.getY());

		if (distance <= ((tampBall1._mass + tempBall2._mass)*(tampBall1._mass + tempBall2._mass))) {
			retval = true;
		}
		return retval;
	}

	
	public double get_velocityX() {
		return _velocityX;
	}

	public void set_velocityX(double _velocityX) {
		this._velocityX = _velocityX;
	}

	public double get_velocityY() {
		return _velocityY;
	}

	public void set_velocityY(double _velocityY) {
		this._velocityY = _velocityY;
	}

	public int get_mass() {
		return _mass;
	}

	public void set_mass(int _mass) {
		this._mass = _mass;
	}*/
	   public boolean isCollition(float x2, float y2) {

			return x2 > x && x2 < x + width && y2 > y && y2 < y + height;

		}

	public void setRemove(boolean remove) {
		// TODO Auto-generated method stub
		this.remove = remove;
		currentFrame =0;
	}
	public Feature getFeature() {
		return feature;
	}
}
