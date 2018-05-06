package mechanics2D.physics;

import mechanics2D.shapes.IsShape;
import mechanics2D.shapes.Orientable;

public interface PhysicsBody extends IsShape, Orientable {
	void interact(PhysicsBody other);
	void update();
	double mass();
	void addForce(Force f);
	void addImpulse(Force f);
}
