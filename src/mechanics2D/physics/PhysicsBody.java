package mechanics2D.physics;

import mechanics2D.shapes.CollisionInformation;
import mechanics2D.shapes.IsShape;
import mechanics2D.shapes.Orientable;
import tensor.DVector2;

public interface PhysicsBody extends IsShape, Orientable, Interactive, PhysicsConstruct {
	
	DVector2 vel();
	double w();
	
	void update();
	double mass();
	double moment();
	void addForce(Force f);
	void addImpulse(Force f);
	double restitution();
	
	boolean interact(PhysicsBody other);
	
	/**
	 * Computes where this body will be next frame, ignoring contact / collision forces.
	 * Used by the collision detection algorithms
	 */
	void computeFutureState();
	DVector2 futureVel();
	double futureW();
	/**
	 * To be called after the future state has already been calculated
	 * and more forces need to be taken into account
	 */
	void appendFutureState();
	DVector2 acc();	// computed from the computed future state
	double dW();
	
	DVector2 nPrime(PhysicsBody b2, CollisionInformation c);
	
	static boolean is(Object o) {
		return PhysicsBody.class.isAssignableFrom(o.getClass());
	}
}
