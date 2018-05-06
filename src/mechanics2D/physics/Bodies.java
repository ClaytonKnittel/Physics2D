package mechanics2D.physics;

import mechanics2D.graphics.Drawable;
import structures.Web;

public class Bodies {
	
	private Web<PhysicsBody> bodies;
	
	public Bodies() {
		bodies = new Web<>();
	}
	
	public void add(PhysicsBody...b) {
		for (PhysicsBody bo : b)
			bodies.add(bo);
	}
	
	public void attemptAdd(Drawable... drawables) {
		for (Drawable d : drawables) {
			if (PhysicsBody.class.isAssignableFrom(d.getClass()))
				bodies.add((PhysicsBody) d);
		}
	}
	
	public void update() {
		bodies.actPairs((b1, b2) -> b1.interact(b2));
		bodies.act(b -> b.update());
	}
	
}
