package mechanics2D.physics;

import java.util.LinkedList;

public class CollisionTracker {
	
	private LinkedList<Pair> recentCollisions;
	private static final int wait = 0;		// wait 5 physics iterations before allowing another collision
	
	public CollisionTracker() {
		recentCollisions = new LinkedList<>();
	}
	
	public void add(PhysicsBody b1, PhysicsBody b2) {
		recentCollisions.add(new Pair(b1, b2));
	}
	
	public boolean shouldCollide(PhysicsBody b1, PhysicsBody b2) {
		return !recentCollisions.contains(new Pair(b1, b2));
	}
	
	public void update() {
		for (Pair p : recentCollisions) {
			p.update();
		}
		while (!recentCollisions.isEmpty()) {
			if (recentCollisions.getFirst().destroy())
				recentCollisions.removeFirst();
			else
				break;
		}
	}
	
	private static class Pair {
		private PhysicsBody b1, b2;
		private int lifeTime;
		
		Pair(PhysicsBody b1, PhysicsBody b2) {
			this.b1 = b1;
			this.b2 = b2;
			lifeTime = wait;
		}
		
		boolean destroy() {
			return lifeTime <= 0;
		}
		
		void update() {
			lifeTime--;
		}
		
		@Override
		public boolean equals(Object o) {
			Pair p = (Pair) o;
			return (p.b1 == b1 && p.b2 == b2);// || (p.b1 == b2 && p.b2 == b1);
		}
	}
	
}
