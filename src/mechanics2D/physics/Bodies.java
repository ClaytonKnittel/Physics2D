package mechanics2D.physics;

import java.util.LinkedList;

import mechanics2D.graphics.Drawable;
import mechanics2D.shapes.AbstractShape;
import mechanics2D.shapes.CollisionInformation;
import methods.P;
import structures.Act;
import structures.Web;

public class Bodies {
	
	private Web<Body> bodies;
	private LinkedList<ForceField> fields;
	private LinkedList<InteractiveForce> interactions;
	private MultiForceHandler contactForceHandler;
	
	public Bodies() {
		bodies = new Web<>();
		fields = new LinkedList<>();
		interactions = new LinkedList<>();
		contactForceHandler = new MultiForceHandler();
	}
	
	private void addPhysicsBody(Body...b) {
		for (Body bo : b)
			bodies.add(bo);
	}
	
	public void add(PhysicsConstruct... cs) {
		add(false, cs);
	}
	
	private void add(boolean suspendContactForceUpdate, PhysicsConstruct...cs) {
		for (PhysicsConstruct c : cs) {
			if (c instanceof Body)
				addPhysicsBody((Body) c);
			else if (c instanceof ForceField)
				addFF((ForceField) c);
			else if (c instanceof InteractiveForce)
				addIF((InteractiveForce) c);
			else
				throw new IllegalArgumentException(c.getClass() + " is not an acceptable PhysicsConstruct");
		}
		if (!suspendContactForceUpdate)
			updateContactForceHandler();
	}
	
	private void updateContactForceHandler() {
		contactForceHandler = new MultiForceHandler(bodies.toList());
	}
	
	public void attemptAdd(Drawable... drawables) {
		for (Drawable d : drawables) {
			if (PhysicsConstruct.class.isAssignableFrom(d.getClass()))
				add(true, (PhysicsConstruct) d);
		}
		updateContactForceHandler();
	}
	
	private void addFF(ForceField f) {
		fields.add(f);
	}
	
	private void addIF(InteractiveForce f) {
		interactions.add(f);
	}
	
	public boolean contains(Body b) {
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
			if (AbstractShape.hasCollisions()) {
				for (CollisionInformation c : AbstractShape.getCollisions()) {
					//P.pl(c);
					contactForceHandler.add(b1, b2, c);
				}
				//P.pl("");
				AbstractShape.clearCollisions();
			}
		});
		
		bodies.act(b -> b.appendFutureState());
		
		contactForceHandler.resolveContacts();
		
		bodies.act(b -> b.update());
	}
	
	public void act(Act<Body> a) {
		bodies.act(a);
	}
	
}
