package mechanics2D.physics;

import mechanics2D.shapes.CollisionInformation;
import mechanics2D.shapes.IsShape;
import mechanics2D.shapes.Orientable;

public interface PhysicsBody extends IsShape, Orientable, Interactive, PhysicsConstruct {
	void update();
	double mass();
	void addForce(Force f);
	void addImpulse(Force f);
	void resolveCollisions();
	void addCollision(PhysicsBody other, CollisionInformation collision);
	double restitution();
	
	boolean interact(PhysicsBody other);
	
	/**
	 * Computes where this body will be next frame, ignoring contact / collision forces.
	 * Used by the collision detection algorithms
	 */
	void computeFutureState();
	
	static boolean is(Object o) {
		return PhysicsBody.class.isAssignableFrom(o.getClass());
	}
}
