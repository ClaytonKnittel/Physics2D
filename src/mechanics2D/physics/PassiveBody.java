package mechanics2D.physics;

import mechanics2D.shapes.Shape;
import tensor.DVector2;

public abstract class PassiveBody implements PhysicsBody {
	
	private DVector2 pos;
	
	private double phi, mass;
	
	private Shape shape;
	
	protected PassiveBody(double x, double y, double mass, Shape shape) {
		pos = new DVector2(x, y);
		this.mass = mass;
		phi = 0;
		this.shape = shape;
	}
	
	@Override
	public DVector2 pos() {
		return pos;
	}

	@Override
	public void move(DVector2 dPos) {
		pos.add(dPos);
	}

	@Override
	public double angle() {
		return phi;
	}

	@Override
	public void rotate(double dAngle) {
		phi += dAngle;
	}
	
	@Override
	public double mass() {
		return mass;
	}

	@Override
	public Shape shape() {
		return shape;
	}
	
	@Override
	public void update() {
		return;
	}
	
	@Override
	public void interact(PhysicsBody other) {
		
	}
	
	@Override
	public void addForce(Force f) {
		return;
	}
	
	@Override
	public void addImpulse(Force f) {
		return;
	}
	
}
