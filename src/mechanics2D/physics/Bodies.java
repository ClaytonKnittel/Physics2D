package mechanics2D.physics;

import java.util.LinkedList;

import mechanics2D.graphics.Drawable;
import structures.Act;
import structures.Web;

public class Bodies {
	
	private Web<PhysicsBody> bodies;
	private LinkedList<ForceField> fields;
	private CollisionTracker ct;
	
	public Bodies() {
		bodies = new Web<>();
		fields = new LinkedList<>();
		ct = new CollisionTracker();
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
	
	public void add(ForceField f) {
		fields.add(f);
	}
	
	public boolean contains(PhysicsBody b) {
		return bodies.contains(b);
	}
	
	public void update() {
		bodies.actPairs((b1, b2) -> {
			if (ct.shouldCollide(b1, b2))
				if (b1.interact(b2))
					ct.add(b1, b2);
		});
		
		for (ForceField f : fields) {
			bodies.act(b -> f.interact(b));
		}
		
		bodies.act(b -> b.update());
		
		ct.update();
	}
	
	public void act(Act<PhysicsBody> a) {
		bodies.act(a);
	}
	
}
