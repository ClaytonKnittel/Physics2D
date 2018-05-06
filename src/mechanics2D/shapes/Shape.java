package mechanics2D.shapes;

import mechanics2D.graphics.Drawable;

public interface Shape extends Drawable {
	
	void setOwner(Orientable owner);
	
	/**
	 * Collision information returns a collision spot relative to the world, with the direction
	 * pointing away from the surface of this shape.
	 * 
	 * @param s the shape this one is colliding with
	 * @param computeCollisionInfo whether or not to compute the collision information or to simply test if a collision is happening
	 * @return whether or not these two shapes collide
	 */
	boolean colliding(Shape s, boolean computeCollisionInfo);
	
	/**
	 * @return the moment of inertia of this object, excluding the mass factor
	 */
	double moment();
	
}
