package mechanics2D.physics;

import java.util.LinkedList;
import java.util.List;

import algorithms.MatrixAlgorithms;
import mechanics2D.shapes.CollisionInformation;
import methods.P;
import structures.InteractionSet;
import structures.InteractionSet.Connection;
import tensor.DMatrixN;
import tensor.DVector2;
import tensor.DVectorN;

public class MultiForceHandler {
	
	// the perpendicular velocity below which a collision is considered a contact
	private static final double threshold = 1;
	
	private InteractionSet<Body, CollisionInformation> contacts;
	
	public MultiForceHandler(List<Body> bodies) {
		contacts = new InteractionSet<>(bodies);
	}
	
	public MultiForceHandler() {
		this(new LinkedList<>());
	}
	
	public void add(Body b1, Body b2, CollisionInformation c) {
		if (isContact(b1, b2, c))
			contacts.add(c, b1, b2);
		else
			PMath.collisionForce(b1, b2, c);
	}
	
	public void resolveContacts() {
		if (contacts.empty())
			return;
		
		List<Connection<Body, CollisionInformation>[]> groups = contacts.groups(b -> !PassiveBody.is(b));
		
		for (Connection<Body, CollisionInformation>[] c : groups) {
			
			DMatrixN a = DMatrixN.interactionMatrix(c, (con1, con2) -> {
				// need to compute the affect of connection 2 on connectio 1
				
				double a1 = 0;
				if (con1.to() == con2.to() || con1.to() == con2.from())
					a1 += aij(con1.to(), con1.connector(con1.to()), con2.connector(con1.to()));
				if (con1.from() == con2.to() || con1.from() == con2.from())
					a1 += aij(con1.from(), con1.connector(con1.from()), con2.connector(con1.from()));
				
				return a1;
			});
//			System.out.println("Matrix:\n" + a);
			
			DVectorN b = DVectorN.functionalMap(c, d -> b(d.to(), d.from(), d.connector()));
			
//			System.out.println("b: " + b);
			
			DVectorN forces = MatrixAlgorithms.solveConstrainedEqn(a, b);

//			DVectorN ac = a.multiply(forces).plus(b);
//			for (int i = 0; i < ac.dim(); i++) {
//				if (ac.get(i) < -.00000001) {
//					P.pl("A:\n" + a + "\n" + b);
//					P.pl("Accel: " + ac.get(i));
//				}
//			}
			
			double strength;
			for (int i = 0; i < forces.dim(); i++) {
				strength = forces.get(i);
				c[i].to().addImpulse(c[i].connector(), -strength);
				c[i].from().addImpulse(c[i].connector(), strength);
			}
		}
		
		contacts.clear();
	}
	
	/**
	 * 
	 * @param body the body both these forces are acting on
	 * @param i
	 * @param j
	 * @return the effect of the force at collision point j on collision point i's acceleration
	 */
	private double aij(Body body, CollisionInformation i, CollisionInformation j) {
		if (i == null || j == null)
			return 0;
		if (PassiveBody.is(body))
			return 0;
		
		DVector2 r = i.loc().minus(body.pos());
		DVector2 rj = j.loc().minus(body.pos());
		return i.dir().dot(j.dir().divide(body.mass()).minus(r.crossPerp(rj.cross(j.dir())).divide(body.moment())));
	}
	
	/**
	 * 
	 * @param b1 the body the normal force is perpendicular to (used to calculate n')
	 * @param b2 the other body
	 * @param c
	 * @return
	 */
	private double b(Body b1, Body b2, CollisionInformation c) { //FIXME
		DVector2 r1 = b1.pos().minus(c.loc());
		DVector2 r2 = b2.pos().minus(c.loc());
		
		DVector2 t1 = b1.dP(r1);
		DVector2 t2 = b2.dP(r2);
		
		DVector2 rCW1 = b1.rCrossDW(r1);
		DVector2 rCW2 = b2.rCrossDW(r2);
		
		DVector2 nPrime = b1.nPrime(b2, c);
		
		DVector2 dP = b1.futureVel().minus(b2.futureVel()).plus(t1).minus(t2); // velocity of p (p1' - p2')
		
		double twist = 2 * nPrime.dot(dP); // 2 n' . (p1' - p2')
		
		double lin = -c.dir().dot(
				b1.acc().minus(b2.acc()).plus(rCW1.minus(rCW2))
				.minus(t1.crossPerp(b1.futureW())).plus(t2.crossPerp(b2.futureW()))); // n . (p1'' - p2'')
		
		double zeroVel = c.dir().dot(b2.vel().minus(b1.vel()));
		
//		P.pl("current vel: " + b2.vel() + "   w: " + b2.w());
//		P.pl("future vel: " + b2.futureVel());
		
//		P.pl("(" + twist + ", " + lin + ", " + zeroVel + ") = " + (twist + lin + zeroVel));
		return twist + lin + zeroVel;
	}
	
	private boolean isContact(Body b1, Body b2, CollisionInformation c) {
		return Math.abs(b1.vel().minus(b2.vel()).dot(c.dir())) < threshold;
	}
	
}
