package mechanics2D.physics;

import mechanics2D.shapes.CollisionInformation;
import tensor.DVector2;

public class PMath {
	
	public static double dt = 0.0009765625;
	
	public static double G = 10000;
	
	/**
	 * 
	 * @param b1 Body 1
	 * @param b2 Body 2
	 * @return force of b2 on b1 due to gravity
	 */
	public static void gForce(PhysicsBody b1, PhysicsBody b2) {
		DVector2 r = b2.pos().minus(b1.pos());
		double d = r.mag2();
		r.normalize();
		r.scale(G * b1.mass() * b2.mass() / d);
		Force f = new Force(r);
		b1.addForce(f);
		b2.addForce(f.opposite());
	}
	
	public static double gPotential(PhysicsBody b1, PhysicsBody b2) {
		double d = b2.pos().minus(b1.pos()).mag();
		return -G * b1.mass() * b2.mass() / d;
	}
	
	public static void collisionForce(PhysicsBody pb1, PhysicsBody pb2, CollisionInformation c) {
		Force[] forces;
		if (Body.is(pb1) && Body.is(pb2))
			forces = collisionForce((Body) pb1, (Body) pb2, c);
		else if (Body.is(pb1))
			forces = collisionForce((Body) pb1, (PassiveBody) pb2, c);
		else if (Body.is(pb2))
			forces = collisionForce((PassiveBody) pb1, (Body) pb2, c);
		else
			return;
		
		pb1.addImpulse(forces[0]);
		pb2.addImpulse(forces[1]);
	}
	
	private static Force[] collisionForce(Body b1, Body b2, CollisionInformation c) {
		double e = 1;
		
		DVector2 r1 = c.loc().minus(b1.pos());
		DVector2 r2 = c.loc().minus(b2.pos());
		
		double p1 = r1.cross(c.dir());
		double p2 = r2.cross(c.dir());
		
		double f = (1 + e) * (b1.vel().minus(b2.vel()).dot(c.dir()) + b1.w() * p1 - b2.w() * p2) / (1 / b1.mass() + 1 / b2.mass() + square(p1) / b1.moment() + square(p2) / b2.moment());
		System.out.println("Force: \t" + f);
		
		return new Force[] {new Force(r1, c.dir().times(-f)), new Force(r2, c.dir().times(f))};
	}
	
	private static Force[] collisionForce(PassiveBody wall, Body b2, CollisionInformation c) {
		return new Force[] {null, collisionForce(b2, wall, c)[0]};
	}
	
	private static Force[] collisionForce(Body b1, PassiveBody wall, CollisionInformation c) {
		double e = 1;
		
		DVector2 r1 = c.loc().minus(b1.pos());
		
		double p1 = r1.cross(c.dir());
		
		double f = (1 + e) * (b1.vel().dot(c.dir()) + b1.w() * p1) / (1 / b1.mass() + square(p1) / b1.moment());
		System.out.println(f + " " + (b1.mass() * b1.vel().mag() * 2));
		
		return new Force[] {new Force(r1, c.dir().times(-f)), null};
	}
	
	public static double square(double d) {
		return d * d;
	}
	
}
