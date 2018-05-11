package mechanics2D.shapes;

import java.awt.Color;
import java.util.LinkedList;

import mechanics2D.Main;
import mechanics2D.math.Transformable;
import tensor.DVector2;

public abstract class AbstractShape implements Shape, Transformable {
	
	private Orientable owner;
	private Color color;
	
	/**
	 * to be used to temporarily store collisionInformations.
	 * These are calculated when asked to after a call to colliding(Shape other, boolean compute),
	 * and they will be computed and temporarily stored in this if compute is true.
	 */
	private static LinkedList<CollisionInformation> lastCollisions = new LinkedList<>();
	
	// to be set by any call to colliding, if asked by the caller, to subsequently
	// be retrieved by a call to getCollisionInfo from Shape
	
	public AbstractShape(Orientable owner, Color color) {
		this.owner = owner;
		this.color = color;
	}
	
	public Color color() {
		return color;
	}
	
	public Orientable owner() {
		return owner;
	}
	
	protected CollisionInformation transed(CollisionInformation c) {
		return new CollisionInformation(owner.toSpaceFrame(c.loc()), c.dir().angle() + owner().angle());
	}
	
	protected void addCollision(CollisionInformation c) {
		Main.pos = c.loc();
		Main.dir = c.dir();
		lastCollisions.add(c);
	}
	
	public static Iterable<CollisionInformation> getCollisions() {
		return lastCollisions;
	}
	
	public static void clearCollisions() {
		lastCollisions.clear();
	}
	
	@Override
	public void setOwner(Orientable owner) {
		this.owner = owner;
	}
	
	@Override
	public Orientable transform(Orientable wrt) {
		return new Orientable() {
			public DVector2 pos() {
				return wrt.toBodyFrame(owner.pos());
			}
			public void move(DVector2 dPos) {
				return;
			}

			public double angle() {
				return owner.angle() - wrt.angle();
			}

			public void rotate(double dAngle) {
				return;
			}
		};
	}
}
