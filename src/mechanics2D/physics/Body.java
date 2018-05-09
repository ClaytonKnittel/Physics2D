package mechanics2D.physics;

import mechanics2D.shapes.AbstractShape;
import mechanics2D.shapes.CollisionInformation;
import mechanics2D.shapes.Orientable;
import mechanics2D.shapes.Shape;
import tensor.DVector2;

import java.util.LinkedList;

public abstract class Body implements PhysicsBody {
	
	private DVector2 pos, vel;
	private double phi, w;
	
	private PhysicalOrientable future;
	
	// angle + angular velocity + moment of inertia
	private double I;
	
	private double mass;
	
	private LinkedList<Force> netForce, netImpulse;
	
	private Shape shape;
	
	private CollisionList collisions;
	
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
		resetForces();
		
		collisions = new CollisionList();
	}
	
	public void setRestitution(double r) {
		this.restitution = r;
	}
	
	@Override
	public double restitution() {
		return restitution;
	}
	
	@Override
	public void addCollision(PhysicsBody other, CollisionInformation c) {
		collisions.add(other, c);
	}
	
	public void resolveCollisions() {
		for (CollisionList.BodyCollisionPair bc : collisions)
			PMath.collisionForce(this, bc.other(), bc.collision());
		collisions.clear();
	}
	
	public boolean interact(PhysicsBody other) {	// find collisions
		if (other.shape().colliding(shape(), true)) {
			for (CollisionInformation c : AbstractShape.getCollisions())
				addCollision(other, c);
			return true;
		}
		return false;
	}
	
	public static boolean is(Object o) {
		return Body.class.isAssignableFrom(o.getClass());
	}
	
	@Override
	public DVector2 pos() {
		return pos;
	}
	
	public DVector2 vel() {
		return vel;
	}
	
	@Override
	public void move(DVector2 move) {
		pos.add(move);
	}
	
	@Override
	public double angle() {
		return phi;
	}
	
	public void setAngle(double phi) {
		this.phi = phi;
	}
	
	public double w() {
		return w;
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
	
	@Override
	public void addForce(Force force) {
		netForce.add(force);
	}
	
	@Override
	public void addImpulse(Force force) {
		netImpulse.add(force);
	}
	
	@Override
	public Orientable futureState() {
		return future;
	}
	
	private void resetForces() {
		netForce.clear();
		netImpulse.clear();
	}
	
	public void applyForces() {
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
	}
	
	@Override
	public void computeFutureState() {
		future = new PhysicalOrientable(pos, vel, phi, w);
		for (Force f : netForce) {
			future.vel.add(f.force().times(PMath.dt / mass));
			
			future.w += f.torque() * PMath.dt / I;
		}
		for (Force f : netImpulse) {
			future.vel.add(f.force().divide(mass));
			
			future.w += f.torque() / I;
		}
		future.pos.add(future.vel.times(PMath.dt));
		future.phi += w * PMath.dt;
		resetForces();
	}
	
	public void update() {
		applyForces();
		pos.add(vel.times(PMath.dt));
		phi += w * PMath.dt;
	}
	
	public double energy() {
		return mass * vel.mag2() / 2 + I * w * w / 2;
	}
	
	public String toString() {
		return shape.toString();
	}
	
	private static class PhysicalOrientable implements Orientable {
		
		private DVector2 pos, vel;
		private double phi, w;
		
		PhysicalOrientable(DVector2 pos, DVector2 vel, double phi, double w) {
			this.pos = new DVector2(pos);
			this.vel = new DVector2(vel);
			this.phi = phi;
			this.w = w;
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
