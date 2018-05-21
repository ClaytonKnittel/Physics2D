package mechanics2D.physics;

import mechanics2D.shapes.CollisionInformation;
import mechanics2D.shapes.Shape;

public abstract class PassiveBody extends Body {
	
	protected PassiveBody(double x, double y, double mass, Shape shape) {
		super(x, y, 0, 0, mass, shape);
	}
	
	public static boolean is(Object o) {
		return PassiveBody.class.isAssignableFrom(o.getClass());
	}
	
	@Override
	public void addForce(Force force) {
		return;
	}
	
	@Override
	public void addImpulse(Force force) {
		return;
	}
	
	@Override
	public void addImpulse(CollisionInformation c, double strength) {
		return;
	}
	
	@Override
	public boolean interact(Body other) {
		if (!PassiveBody.is(other))
			return other.interact(this);
		return false;
	}
	
}
