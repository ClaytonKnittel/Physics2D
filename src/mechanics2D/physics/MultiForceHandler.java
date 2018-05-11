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
	
	private InteractionSet<PhysicsBody, CollisionInformation> contacts;
	
	public MultiForceHandler(List<PhysicsBody> bodies) {
		contacts = new InteractionSet<>(bodies);
	}
	
	public MultiForceHandler() {
		this(new LinkedList<>());
	}
	
	public void add(PhysicsBody b1, PhysicsBody b2, CollisionInformation c) {
		if (isContact(b1, b2, c))
			contacts.add(c, b1, b2);
		else
			PMath.collisionForce(b1, b2, c);
	}
	
	public void resolveContacts() {
		if (contacts.empty())
			return;
		
		List<Connection<PhysicsBody, CollisionInformation>[]> groups = contacts.groups();
		
		for (Connection<PhysicsBody, CollisionInformation>[] c : groups) {
			
			DMatrixN a = DMatrixN.interactionMatrix(c, (connection1, connection2) -> {
				// need to compute the affect of connection 2 on connectio 1
				double a1 = aij(connection1.from(), connection1.connector(), connection2.connector());
				a1 += aij(connection1.to(), connection1.connector(), connection2.connector());
				
				return a1;
			});
			System.out.println("Matrix:\n" + a);
			
			//FIXME
			DVectorN b = DVectorN.functionalMap(c, d -> b(d.to(), d.from(), d.connector()));
			
			System.out.println("b: " + b);
			
			DVectorN forces = MatrixAlgorithms.solveConstrainedEqn(a, b);
			//P.pl(forces);
			
			double strength;
			for (int i = 0; i < forces.dim(); i++) {
				strength = forces.get(i);
				P.pl("Strengtha: " + strength);
				c[i].to().addImpulse(new Force(c[i].connector(), -strength));
				c[i].from().addImpulse(new Force(c[i].connector(), strength));
				c[i].from().appendFutureState();
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
	private double aij(PhysicsBody body, CollisionInformation i, CollisionInformation j) {
		if (i == null || j == null)
			return 0;
		if (PassiveBody.is(body))
			return 0;
		
		DVector2 r = i.loc().minus(body.pos());
		DVector2 rj = j.loc().minus(body.pos());
		System.out.println(rj);
		return i.dir().dot(j.dir().divide(body.mass()).minus(r.crossPerp(rj.cross(j.dir())).divide(body.moment())));
	}
	
	/**
	 * 
	 * @param b1 the body the normal force is perpendicular to (used to calculate n')
	 * @param b2 the other body
	 * @param c
	 * @return
	 */
	private double b(PhysicsBody b1, PhysicsBody b2, CollisionInformation c) { //FIXME
		DVector2 r1 = b1.pos().minus(c.loc());
		DVector2 r2 = b2.pos().minus(c.loc());
		
		DVector2 t1 = r1.crossPerp(b1.w());
		DVector2 t2 = r2.crossPerp(b2.w());
		
		DVector2 nPrime = b1.nPrime(b2, c);
		
		DVector2 dP = b1.vel().minus(b2.vel()).plus(t1).minus(t2); // velocity of p (p1' - p2')
		
		double twist = 2 * nPrime.dot(dP); // n' . (p1' - p2')
		
		double lin = -c.dir().dot(
				b1.acc().minus(b2.acc())
				.plus(r1.crossPerp(b1.dW()).minus(r2.crossPerp(b2.dW())))
				.minus(t1.crossPerp(b1.w())).plus(t2.crossPerp(b2.w()))); // n . (p1'' - p2'')
		
		double zeroVel = c.dir().dot(dP) * 0;
		
		P.pl("acvc: " + b2.currentVel());
		
		P.pl("(" + twist + ", " + lin + ", " + zeroVel + ") = " + (twist + lin + zeroVel));
		return twist + lin + zeroVel;
	}
	
	private boolean isContact(PhysicsBody b1, PhysicsBody b2, CollisionInformation c) {
		return Math.abs(b1.currentVel().minus(b2.currentVel()).dot(c.dir())) < threshold;
	}
	
}
