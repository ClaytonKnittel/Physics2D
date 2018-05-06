package mechanics2D.physics;

import mechanics2D.shapes.CollisionInformation;
import tensor.DVector2;

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
