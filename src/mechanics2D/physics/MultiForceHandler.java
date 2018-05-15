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
		
		List<Connection<Body, CollisionInformation>[]> groups = contacts.groups();
		
		for (Connection<Body, CollisionInformation>[] c : groups) {
			
			DMatrixN a = DMatrixN.interactionMatrix(c, (connection1, connection2) -> {
				// need to compute the affect of connection 2 on connectio 1
				double a1 = aij(connection1.from(), connection1.connector(), connection2.connector());
				a1 += aij(connection1.to(), connection1.connector(), connection2.connector());
				
				return a1;
			});
//			System.out.println("Matrix:\n" + a);
			
			//FIXME
			DVectorN b = DVectorN.functionalMap(c, d -> b(d.to(), d.from(), d.connector()));
			
//			System.out.println("b: " + b);
			
			DVectorN forces = MatrixAlgorithms.solveConstrainedEqn(a, b);
			//P.pl(forces);
			
			double strength;
			for (int i = 0; i < forces.dim(); i++) {
				strength = forces.get(i);
//				P.pl("Strengtha: " + strength + "\n");
				c[i].to().addImpulse(new Force(c[i].connector(), c[i].to().pos(), -strength));
				c[i].from().addImpulse(new Force(c[i].connector(), c[i].from().pos(), strength));
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
		
		DVector2 t1 = r1.crossPerp(b1.futureW());
		DVector2 t2 = r2.crossPerp(b2.futureW());
		
		DVector2 nPrime = b1.nPrime(b2, c);
		
		DVector2 dP = b1.futureVel().minus(b2.futureVel()).plus(t1).minus(t2); // velocity of p (p1' - p2')
		
		double twist = 2 * nPrime.dot(dP); // n' . (p1' - p2')
		
		double lin = -c.dir().dot(
				b1.acc().minus(b2.acc())
				.plus(r1.crossPerp(b1.dW()).minus(r2.crossPerp(b2.dW())))
				.minus(t1.crossPerp(b1.futureW())).plus(t2.crossPerp(b2.futureW()))); // n . (p1'' - p2'')
		
		double zeroVel = c.dir().dot(b2.vel().minus(b1.vel())) * 0;
		
		P.pl("current vel: " + b2.vel());
//		P.pl("future vel: " + b2.futureVel());
		
//		P.pl("(" + twist + ", " + lin + ", " + zeroVel + ") = " + (twist + lin + zeroVel));
		return twist + lin + zeroVel;
	}
	
	private boolean isContact(Body b1, Body b2, CollisionInformation c) {
		return Math.abs(b1.vel().minus(b2.vel()).dot(c.dir())) < threshold;
	}
	
}
