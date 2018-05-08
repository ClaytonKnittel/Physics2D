package mechanics2D.physics;

import java.util.Iterator;
import java.util.LinkedList;

import mechanics2D.shapes.CollisionInformation;

public class CollisionList implements Iterable<CollisionList.BodyCollisionPair> {
	
	private LinkedList<BodyCollisionPair> collisions;
	
	public CollisionList() {
		collisions = new LinkedList<>();
	}
	
	public boolean empty() {
		return collisions.isEmpty();
	}
	
	public void add(PhysicsBody b, CollisionInformation c) {
		collisions.add(new BodyCollisionPair(b, c));
	}
	
	/**
	 * removes all collisions this object has with b
	 * @param b
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void remove(PhysicsBody b) {
		while (collisions.remove(b));
	}
	
	public void remove(PhysicsBody b, CollisionInformation c) {
		collisions.remove(new BodyCollisionPair(b, c));
	}
	
	public void clear() {
		collisions.clear();
	}
	
	public static class BodyCollisionPair {
		private PhysicsBody other;
		private CollisionInformation collision;
		
		public BodyCollisionPair(PhysicsBody other, CollisionInformation c) {
			this.other = other;
			this.collision = c;
		}
		
		public PhysicsBody other() {
			return other;
		}
		
		public CollisionInformation collision() {
			return collision;
		}
		
		public boolean equals(Object o) {
			if (o instanceof PhysicsBody)
				return other == o;
			if (o instanceof BodyCollisionPair) {
				BodyCollisionPair b = (BodyCollisionPair) o;
				return b.other == other && b.collision == collision;
			}
			return false;
		}
	}

	@Override
	public Iterator<BodyCollisionPair> iterator() {
		return collisions.iterator();
	}
	
}
