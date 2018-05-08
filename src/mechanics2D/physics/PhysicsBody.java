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
	
	static boolean is(Object o) {
		return PhysicsBody.class.isAssignableFrom(o.getClass());
	}
}
