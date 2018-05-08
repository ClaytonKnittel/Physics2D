package mechanics2D.physics;

public class InteractiveForce implements PhysicsConstruct {
	
	private Interactive force;
	
	public InteractiveForce(Interactive force) {
		this.force = force;
	}
	
	public void interact(PhysicsBody b1, PhysicsBody b2) {
		Force f = force.interact(b1, b2);
		b1.addForce(f);
		b2.addForce(f.opposite());
	}
	
	public Force getForce(PhysicsBody b1, PhysicsBody b2) {
		return force.interact(b1, b2);
	}
	
	/**
	 * Gives the force of body 2 on body 1
	 * 
	 * @author claytonknittel
	 *
	 */
	public static interface Interactive {
		Force interact(PhysicsBody b1, PhysicsBody b2);
	}
	
}
