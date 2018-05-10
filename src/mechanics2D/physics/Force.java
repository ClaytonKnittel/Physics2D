package mechanics2D.physics;

import mechanics2D.shapes.CollisionInformation;
import methods.P;
import tensor.DVector2;

/**
 * All forces are relative to the center of mass of the objects they act on,
 * but are still in the space frame.
 * 
 * @author claytonknittel
 *
 */
public class Force extends CollisionInformation {
	
	private double torque;
	
	public Force(DVector2 loc, DVector2 force) {
		super(loc, force);
		torque = loc.cross(force);
	}
	
	public Force(DVector2 force) {
		super(DVector2.ZERO, force);
		torque = 0;
	}
	
	public Force (CollisionInformation c, double strength) {
		this(c.loc(), c.dir().times(strength));
	}
	
	@Override
	public Force reverse() {
		return opposite();
	}
	
	public Force opposite() {
		return new Force(loc(), dir().times(-1));
	}
	
	public DVector2 force() {
		return super.dir();
	}
	
	public double torque() {
		return torque;
	}
	
}
