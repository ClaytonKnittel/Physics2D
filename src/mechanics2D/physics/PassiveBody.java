package mechanics2D.physics;

import mechanics2D.shapes.Orientable;
import mechanics2D.shapes.Shape;
import tensor.DVector2;

public abstract class PassiveBody implements PhysicsBody {
	
	private DVector2 pos;
	
	private double angle, mass;
	
	private Shape shape;
	
	private double restitution;
	
	protected PassiveBody(double x, double y, double mass, Shape shape) {
		pos = new DVector2(x, y);
		angle = 0;
		this.mass = mass;
		this.shape = shape;
		this.restitution = 1;
	}
	
	public void setRestitution(double r) {
		this.restitution = r;
	}
	
	@Override
	public double restitution() {
		return restitution;
	}
	
	@Override
	public DVector2 pos() {
		return pos;
	}
	
	@Override
	public DVector2 vel() {
		return DVector2.ZERO;
	}
	
	@Override
	public DVector2 acc() {
		return DVector2.ZERO;
	}

	@Override
	public void move(DVector2 dPos) {
		pos.add(dPos);
	}

	@Override
	public double angle() {
		return angle;
	}

	@Override
	public void rotate(double dAngle) {
		angle += dAngle;
	}
	
	@Override
	public double w() {
		return 0;
	}
	
	@Override
	public double dW() {
		return 0;
	}
	
	@Override
	public double mass() {
		return mass;
	}
	
	@Override
	public double moment() {
		return 0;
	}

	@Override
	public Shape shape() {
		return shape;
	}
	
	@Override
	public void computeFutureState() {
		return;
	}
	
	@Override
	public void appendFutureState() {
		return;
	}
	
	@Override
	public Orientable futureState() {
		return this;
	}
	
	@Override
	public void update() {
		return;
	}
	
	@Override
	public boolean interact(PhysicsBody other) {
		if (Body.is(other))
			return other.interact(this);
		return false;
	}
	
	@Override
	public void addForce(Force f) {
		return;
	}
	
	@Override
	public void addImpulse(Force f) {
		return;
	}
	
	public static class ShouldNotCallException extends RuntimeException {
		private static final long serialVersionUID = -2347170058814076943L;

		public ShouldNotCallException(String arg) {
			super(arg);
		}
	}
	
}
