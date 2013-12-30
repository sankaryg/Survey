package collisionphysics;

public class CollisionPhysics {
	   // Working copy for computing response in intersect(ContainerBox box), 
	   // to avoid repeatedly allocating objects.
	   private static CollisionResponse tempResponse = new CollisionResponse();
	private static float[] pointLineResult;
	private static double[] rotateResult; 
	   
	   /**
	    * Detect collision for a moving point bouncing inside a rectangular container,
	    * within the given timeLimit.
	    * If collision is detected within the timeLimit, compute collision time and 
	    * response in the given CollisionResponse object. Otherwise, set collision time
	    * to infinity.
	    * The result is passed back in the given CollisionResponse object.
	    */
	   static
	   {
	     tempResponse = new CollisionResponse();

	     pointLineResult = new float[2];

	     rotateResult = new double[2];
	   }
	   public static void pointIntersectsRectangleOuter(
	         float pointX, float pointY, float speedX, float speedY, float radius,
	         float rectX1, float rectY1, float rectX2, float rectY2,
	         float timeLimit, CollisionResponse response) {
	      
	      response.reset();  // Reset detected collision time to infinity
	      
	      // A outer rectangular container box has 4 borders. 
	      // Need to look for the earliest collision, if any.
	  
	      // Right border
	      pointIntersectsLineVertical(pointX, pointY, speedX, speedY, radius,
	            rectX2, timeLimit, tempResponse);
	      if (tempResponse.t < response.t) {
	         response.copy(tempResponse);  // Copy into resultant response
	      }
	      // Left border
	      pointIntersectsLineVertical(pointX, pointY, speedX, speedY, radius,
	            rectX1, timeLimit, tempResponse);
	      if (tempResponse.t < response.t) {
	         response.copy(tempResponse);
	      }
	      // Top border
	      pointIntersectsLineHorizontal(pointX, pointY, speedX, speedY, radius,
	            rectY1, timeLimit, tempResponse);
	      if (tempResponse.t < response.t) {
	         response.copy(tempResponse);
	      }
	      // Bottom border
	      pointIntersectsLineHorizontal(pointX, pointY, speedX, speedY, radius,
	            rectY2, timeLimit, tempResponse);
	      if (tempResponse.t < response.t) {
	         response.copy(tempResponse);
	      }
	   }
	   public static void pointIntersectsMovingPoint(float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, float p2X, float p2Y, float p2SpeedX, float p2SpeedY, float p2Radius, float timeLimit, CollisionResponse p1Response, CollisionResponse p2Response)
	   {
	     assert ((p1Radius >= 0.0F) && (p2Radius >= 0.0F)) : "Negative radius!";
	     assert (timeLimit > 0.0F) : "Non-positive time!";

	     p1Response.reset();
	     p2Response.reset();

	     float t = pointIntersectsMovingPointDetection(
	       p1X, p1Y, p1SpeedX, p1SpeedY, p1Radius, 
	       p2X, p2Y, p2SpeedX, p2SpeedY, p2Radius);

	     if ((t > 0.0F) && (t <= timeLimit))
	     {
	       pointIntersectsMovingPointResponse(
	         p1X, p1Y, p1SpeedX, p1SpeedY, p1Radius, 
	         p2X, p2Y, p2SpeedX, p2SpeedY, p2Radius, 
	         p1Response, p2Response, t);
	     }
	   }
	   
	   private static float pointIntersectsMovingPointDetection(float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, float p2X, float p2Y, float p2SpeedX, float p2SpeedY, float p2Radius)
	   {
	     double centerX = p1X - p2X;
	     double centerY = p1Y - p2Y;
	     double speedX = p1SpeedX - p2SpeedX;
	     double speedY = p1SpeedY - p2SpeedY;
	     double radius = p1Radius + p2Radius;
	     double radiusSq = radius * radius;
	     double speedXSq = speedX * speedX;
	     double speedYSq = speedY * speedY;
	     double speedSq = speedXSq + speedYSq;

	     double termB2minus4ac = radiusSq * speedSq - (centerX * speedY - centerY * speedX) * (
	       centerX * speedY - centerY * speedX);
	     if (termB2minus4ac < 0.0D)
	     {
	       return 3.4028235E+38F;
	     }

	     double termMinusB = -speedX * centerX - speedY * centerY;
	     double term2a = speedSq;
	     double rootB2minus4ac = Math.sqrt(termB2minus4ac);
	     double sol1 = (termMinusB + rootB2minus4ac) / term2a;
	     double sol2 = (termMinusB - rootB2minus4ac) / term2a;

	     if ((sol1 > 0.0D) && (sol2 > 0.0D))
	       return (float)Math.min(sol1, sol2);
	     if (sol1 > 0.0D)
	       return (float)sol1;
	     if (sol2 > 0.0D) {
	       return (float)sol2;
	     }

	     return 3.4028235E+38F;
	   }

	   private static void pointIntersectsMovingPointResponse(float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, float p2X, float p2Y, float p2SpeedX, float p2SpeedY, float p2Radius, CollisionResponse p1Response, CollisionResponse p2Response, float t)
	   {
	     p1Response.t = t;
	     p2Response.t = t;

	     double p1ImpactX = p1Response.getImpactX(p1X, p1SpeedX);
	     double p1ImpactY = p1Response.getImpactY(p1Y, p1SpeedY);
	     double p2ImpactX = p2Response.getImpactX(p2X, p2SpeedX);
	     double p2ImpactY = p2Response.getImpactY(p2Y, p2SpeedY);

	     double lineAngle = Math.atan2(p2ImpactY - p1ImpactY, p2ImpactX - p1ImpactX);

	     double[] result = rotate(p1SpeedX, p1SpeedY, lineAngle);
	     double p1SpeedP = result[0];
	     double p1SpeedN = result[1];
	     result = rotate(p2SpeedX, p2SpeedY, lineAngle);
	     double p2SpeedP = result[0];
	     double p2SpeedN = result[1];

	     if (p1SpeedP - p2SpeedP <= 0.0D)
	     {
	       p1Response.reset();
	       p2Response.reset();
	       return;
	     }

	     double p1Mass = p1Radius * p1Radius * p1Radius;
	     double p2Mass = p2Radius * p2Radius * p2Radius;
	     double diffMass = p1Mass - p2Mass;
	     double sumMass = p1Mass + p2Mass;

	     double p1SpeedPAfter = (diffMass * p1SpeedP + 2.0D * p2Mass * p2SpeedP) / sumMass;
	     double p2SpeedPAfter = (2.0D * p1Mass * p1SpeedP - diffMass * p2SpeedP) / sumMass;

	     double p1SpeedNAfter = p1SpeedN;
	     double p2SpeedNAfter = p2SpeedN;

	     result = rotate(p1SpeedPAfter, p1SpeedNAfter, -lineAngle);
	     p1Response.newSpeedX = (float)result[0];
	     p1Response.newSpeedY = (float)result[1];
	     result = rotate(p2SpeedPAfter, p2SpeedNAfter, -lineAngle);
	     p2Response.newSpeedX = (float)result[0];
	     p2Response.newSpeedY = (float)result[1];
	   }
	   private static double[] rotate(double x, double y, double theta)
	   {
	     double sinTheta = Math.sin(theta);
	     double cosTheta = Math.cos(theta);
	     rotateResult[0] = (x * cosTheta + y * sinTheta);
	     rotateResult[1] = (-x * sinTheta + y * cosTheta);
	     return rotateResult;
	   }

	   private static double getSpeed(double speedX, double speedY)
	   {
	     return Math.sqrt(speedX * speedX + speedY * speedY);
	   }
	   public static void pointIntersectsPoint(float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p1Radius, float p2X, float p2Y, float p2Radius, float timeLimit, CollisionResponse p1Response)
	   {
	     assert (p1Radius >= 0.0F) : "Negative radius!";
	     assert (timeLimit > 0.0F) : "Non-positive time";

	     p1Response.reset();

	     float t = pointIntersectsMovingPointDetection(
	       p1X, p1Y, p1SpeedX, p1SpeedY, p1Radius, 
	       p2X, p2Y, 0.0F, 0.0F, p2Radius);

	     if ((t > 0.0F) && (t <= timeLimit))
	     {
	       pointIntersectsPointResponse(p1X, p1Y, p1SpeedX, p1SpeedY, p2X, p2Y, p1Response, t);
	     }
	   }

	   private static void pointIntersectsPointResponse(float p1X, float p1Y, float p1SpeedX, float p1SpeedY, float p2X, float p2Y, CollisionResponse p1Response, float t)
	   {
	     p1Response.t = t;

	     double p1ImpactX = p1Response.getImpactX(p1X, p1SpeedX);
	     double p1ImpactY = p1Response.getImpactY(p1Y, p1SpeedY);

	     double lineAngle = Math.atan2(p2Y - p1ImpactY, p2X - p1ImpactX);

	     double[] result = rotate(p1SpeedX, p1SpeedY, lineAngle);
	     double p1SpeedP = result[0];
	     double p1SpeedN = result[1];

	     if (p1SpeedP <= 0.0D)
	     {
	       p1Response.reset();
	       return;
	     }

	     double p1SpeedPAfter = -p1SpeedP;
	     double p1SpeedNAfter = p1SpeedN;

	     result = rotate(p1SpeedPAfter, p1SpeedNAfter, -lineAngle);
	     p1Response.newSpeedX = (float)result[0];
	     p1Response.newSpeedY = (float)result[1];
	   }
	   /**
	    * Detect collision for a moving point hitting a horizontal line,
	    * within the given timeLimit.
	    */
	   public static void pointIntersectsLineVertical(
	         float pointX, float pointY, float speedX, float speedY, float radius,
	         float lineX, float timeLimit, CollisionResponse response) {
	  
	      response.reset();  // Reset detected collision time to infinity
	  
	      // No collision possible if speedX is zero
	      if (speedX == 0) {
	         return;
	      }
	  
	      // Compute the distance to the line, offset by radius.
	      float distance;
	      if (lineX > pointX) {
	         distance = lineX - pointX - radius; 
	      } else {
	         distance = lineX - pointX + radius; 
	      }
	      
	      float t = distance / speedX;  // speedX != 0
	      // Accept 0 < t <= timeLimit
	      if (t > 0 && t <= timeLimit) {
	         response.t = t;
	         response.newSpeedX = -speedX;  // Reflect horizontally
	         response.newSpeedY = speedY;   // No change vertically
	      }
	   }
	  
	   /**
	    * @see movingPointIntersectsLineVertical().
	    */
	   public static void pointIntersectsLineHorizontal(
	         float pointX, float pointY, float speedX, float speedY, float radius,
	         float lineY, float timeLimit, CollisionResponse response) {

	      response.reset();  // Reset detected collision time to infinity
	  
	      // No collision possible if speedY is zero
	      if (speedY == 0) {
	         return;
	      }
	  
	      // Compute the distance to the line, offset by radius.
	      float distance;
	      if (lineY > pointY) {
	         distance = lineY - pointY - radius; 
	      } else {
	         distance = lineY - pointY + radius; 
	      }
	      
	      float t = distance / speedY;  // speedY != 0
	      // Accept 0 < t <= timeLimit
	      if (t > 0 && t <= timeLimit) {
	         response.t = t;
	         response.newSpeedY = -speedY;  // Reflect vertically
	         response.newSpeedX = speedX;   // No change horizontally
	      }
	   }
	}