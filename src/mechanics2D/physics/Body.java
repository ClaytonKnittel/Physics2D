package mechanics2D.physics;

import mechanics2D.shapes.Circle;
import mechanics2D.shapes.CollisionInformation;
import mechanics2D.shapes.IsShape;
import mechanics2D.shapes.Orientable;
import mechanics2D.shapes.Rectangle;
import mechanics2D.shapes.Shape;
import tensor.DVector2;

import java.util.LinkedList;

public abstract class Body implements IsShape, Orientable, Interactive, PhysicsConstruct {
	
	private DVector2 pos, vel;
	private double phi, w;
	
	private PhysicalOrientable future;
	
	// angle + angular velocity + moment of inertia
	private double I;
	
	private double mass;
	
	private LinkedList<Force> netForce, netImpulse;
	
	private Shape shape;
	
	private double restitution;
	
	protected Body(double x, double y, double vx, double vy, double mass, Shape shape) {
		pos = new DVector2(x, y);
		vel = new DVector2(vx, vy);
		phi = 0;
		w = 0;
		this.mass = mass;
		this.shape = shape;
		I = shape.moment() * mass;
		shape.setOwner(this);
		
		this.restitution = 1;
		
		netForce = new LinkedList<>();
		netImpulse = new LinkedList<>();
	}
	
	public void setRestitution(double r) {
		this.restitution = r;
	}
	
	public double restitution() {
		return restitution;
	}
	
	public boolean interact(Body other) {	// find collisions
		return other.shape().colliding(shape(), true);
	}
	
	public static boolean is(Object o) {
		return Body.class.isAssignableFrom(o.getClass());
	}
	
	@Override
	public DVector2 pos() {
		if (future == null)
			return pos;
		return future.pos;
	}
	
	public DVector2 vel() {
		return vel;
	}
	
	public DVector2 futureVel() {
		if (future == null)
			return vel;
		return future.vel;
	}
	
	@Override
	public void move(DVector2 move) {
		pos.add(move);
	}
	
	@Override
	public double angle() {
		if (future == null)
			return phi;
		return future.phi;
	}
	
	public void setAngle(double phi) {
		this.phi = phi;
	}
	
	public double w() {
		return w;
	}
	
	public double futureW() {
		if (future == null)
			return w;
		return future.w;
	}
	
	public void setW(double w) {
		this.w = w;
	}
	
	@Override
	public void rotate(double angle) {
		phi += angle;
	}
	
	public double moment() {
		return I;
	}
	
	public double mass() {
		return mass;
	}
	
	@Override
	public Shape shape() {
		return shape;
	}
	
	public void addForce(Force force) {
		netForce.add(force);
	}
	
	public void addImpulse(Force force) {
		netImpulse.add(force);
	}
	
	public void addImpulse(CollisionInformation c, double strength) {
		addImpulse(new Force(c, pos(), strength));
	}
	
	private void resetForces() {
		netForce.clear();
		netImpulse.clear();
	}
	
	private void applyForces() {
		vel = future.vel;
		w = future.w;
		for (Force f : netForce) {
			vel.add(f.force().times(PMath.dt / mass));
			
			w += f.torque() * PMath.dt / I;
		}
		for (Force f : netImpulse) {
			vel.add(f.force().divide(mass));
			
			w += f.torque() / I;
		}
		resetForces();
		future = null;
	}
	
	public void computeFutureState() {
		future = new PhysicalOrientable(pos, vel, DVector2.ZERO, phi, w, 0);
		compute();
	}
	
	public void appendFutureState() {
		compute();
	}
	
	private void compute() {
		for (Force f : netForce) {
			future.acc.add(f.force().divide(mass));
			
			future.dW += f.torque() / I;
		}
		for (Force f : netImpulse) {
			future.acc.add(f.force().divide(mass * PMath.dt));
			
			future.dW += f.torque() / I / PMath.dt;
		}
		future.vel = vel.plus(future.acc.times(PMath.dt));
		future.pos = pos.plus(future.vel.times(PMath.dt));
		
		future.w = w + future.dW * PMath.dt;
		future.phi = phi + future.w * PMath.dt;

		resetForces();
	}
	
	public DVector2 acc() {
		return future.acc.times(PMath.dt);
	}
	
	public double dW() {
		return future.dW * PMath.dt;
	}
	
	public void update() {
		applyForces();
		pos.add(vel.times(PMath.dt));
		phi += w * PMath.dt;
	}
	
	public DVector2 nPrime(Body b2, CollisionInformation c) {
		if (PassiveBody.is(b2))
			return b2.nPrime(this, c);
		if (shape() instanceof Rectangle) {
			return c.dir().crossPerp(w);
		} else {
			return DVector2.ZERO;
		}
	}
	
	/**
	 * @param r1 a point relative to the center of this body
	 * @return r1'', the second time derivative of this contact point on the surface of this body
	 */
	public DVector2 dP(DVector2 r1) {
		if (!(shape instanceof Circle))
			return r1.crossPerp(futureW());
		return DVector2.ZERO;
	}
	
	public DVector2 rCrossDW(DVector2 r1) {
		if (!(shape instanceof Circle))
			return r1.crossPerp(future.dW);
		return DVector2.ZERO;
	}
	
	public double energy() {
		return mass * vel.mag2() / 2 + I * w * w / 2;
	}
	
	public String toString() {
		return shape.toString();
	}
	
	private static class PhysicalOrientable implements Orientable {
		
		private DVector2 pos, vel, acc;
		private double phi, w, dW;
		
		PhysicalOrientable(DVector2 pos, DVector2 vel, DVector2 acc, double phi, double w, double dW) {
			this.pos = new DVector2(pos);
			this.vel = new DVector2(vel);
			this.acc = new DVector2(acc);
			this.phi = phi;
			this.w = w;
			this.dW = dW;
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
		
	}
	
}
