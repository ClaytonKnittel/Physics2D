package mechanics2D.physics;

import java.util.LinkedList;
import java.util.List;

import mechanics2D.shapes.CollisionInformation;
import structures.InteractionSet;
import structures.InteractionSet.Connection;
import tensor.DMatrixN;
import tensor.DVector2;
import tensor.DVectorN;

public class MultiForceHandler {
	
	// the perpendicular velocity below which a collision is considered a contact
	private static final double threshold = .1;
	
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
				if (connection1 == connection2)
					return 1;
				PhysicsBody b;
				b = connection1.from();
				double a1 = aij(b, connection1.connector(b), connection2.connector(b));
				b = connection1.to();
				a1 += aij(b, connection1.connector(b), connection2.connector(b));
				
				return a1;
			});
			System.out.println("Matrix:\n" + a);
			
			DVectorN b = DVectorN.functionalMap(c, d -> b(d.to(), d.from(), d.connector(d.to())));
			
			System.out.println("b: " + b);
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
		DVector2 r = i.loc().minus(body.pos());
		return i.dir().dot(j.dir().divide(body.mass()).minus(r.crossPerp(r.cross(j.dir())).divide(body.moment())));
	}
	
	private double b(PhysicsBody b1, PhysicsBody b2, CollisionInformation c) {
		DVector2 r1 = b1.pos().minus(c.loc());
		DVector2 r2 = b2.pos().minus(c.loc());
		
		DVector2 t1 = r1.crossPerp(b1.w());
		DVector2 t2 = r2.crossPerp(b2.w());
		
		double twist = -2 * c.dir().crossPerp(b1.w()).dot(b1.vel().minus(b2.vel()).plus(t1).minus(t2));
		
		double lin = c.dir().dot(
				b1.acc().minus(b2.acc())
				.plus(r1.crossPerp(b1.dW()).minus(r2.crossPerp(b2.w())))
				.minus(t1.crossPerp(b1.w())).plus(t2.crossPerp(b2.w())));
		
		return twist + lin;
	}
	
	private boolean isContact(PhysicsBody b1, PhysicsBody b2, CollisionInformation c) {
		return Math.abs(b1.vel().minus(b2.vel()).dot(c.dir())) < threshold;
	}
	
}