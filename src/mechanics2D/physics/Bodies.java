package mechanics2D.physics;

import java.util.LinkedList;

import mechanics2D.graphics.Drawable;
import mechanics2D.shapes.AbstractShape;
import structures.Act;
import structures.Web;

public class Bodies {
	
	private Web<PhysicsBody> bodies;
	private LinkedList<ForceField> fields;
	private LinkedList<InteractiveForce> interactions;
	//private CollisionTracker ct;
	
	public Bodies() {
		bodies = new Web<>();
		fields = new LinkedList<>();
		interactions = new LinkedList<>();
		//ct = new CollisionTracker();
	}
	
	private void addPhysicsBody(PhysicsBody...b) {
		for (PhysicsBody bo : b)
			bodies.add(bo);
	}
	
	public void add(PhysicsConstruct... cs) {
		for (PhysicsConstruct c : cs) {
			if (c instanceof PhysicsBody)
				addPhysicsBody((PhysicsBody) c);
			else if (c instanceof ForceField)
				addFF((ForceField) c);
			else if (c instanceof InteractiveForce)
				addIF((InteractiveForce) c);
			else
				throw new IllegalArgumentException(c.getClass() + " is not an acceptable PhysicsConstruct");
		}
	}
	
	public void attemptAdd(Drawable... drawables) {
		for (Drawable d : drawables) {
			if (PhysicsConstruct.class.isAssignableFrom(d.getClass()))
				add((PhysicsConstruct) d);
		}
	}
	
	private void addFF(ForceField f) {
		fields.add(f);
	}
	
	private void addIF(InteractiveForce f) {
		interactions.add(f);
	}
	
	public boolean contains(PhysicsBody b) {
		return bodies.contains(b);
	}
	
	public void update() {
		for (ForceField f : fields)
			bodies.act(b -> f.interact(b));
		for (InteractiveForce f : interactions)
			bodies.actPairs((b1, b2) -> f.interact(b1, b2));
		
		bodies.act(b -> b.computeFutureState());
		
		bodies.actPairs((b1, b2) -> {
			b1.interact(b2);
			AbstractShape.clearCollisions();
		});
		
		bodies.act(b -> b.resolveCollisions());
		
		bodies.act(b -> b.update());
		
		//ct.update();
	}
	
	public void act(Act<PhysicsBody> a) {
		bodies.act(a);
	}
	
}
