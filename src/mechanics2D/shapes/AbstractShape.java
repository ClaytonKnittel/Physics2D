package mechanics2D.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import mechanics2D.Main;
import mechanics2D.math.Transformable;
import tensor.DVector2;

public abstract class AbstractShape implements Shape, Transformable {
	
	private Orientable owner;
	private Color color;
	
	// to be set by any call to colliding, if asked by the caller, to subsequently
	// be retrieved by a call to getCollisionInfo from Shape
	private static LinkedList<CollisionInformation> collisionInfo;
	
	public AbstractShape(Orientable owner, Color color) {
		this.owner = owner;
		this.color = color;
		collisionInfo = new LinkedList<>();
	}
	
	public Color color() {
		return color;
	}
	
	public Orientable owner() {
		return owner;
	}
	
	protected void addCollisionInfo(DVector2 bodyPos, double bodyAngle) {
		addCollisionInfo(new CollisionInformation(owner().toSpaceFrame(bodyPos), bodyAngle + owner().angle()));
	}
	
	protected static void addCollisionInfo(CollisionInformation c) {
		collisionInfo.add(c);
		Main.pos = c.loc();
		Main.dir = c.dir();
	}
	
	public static void drawCollisions(Graphics g) {
		for (CollisionInformation c : collisionInfo)
			c.draw(g);
	}
	
	public static final boolean areCollisions() {
		return !collisionInfo.isEmpty();
	}
	
	/**
	 * 
	 * @return the collision info determined from the call to colliding, which is assumed
	 * to have been done immediately prior
	 */
	public static final CollisionInformation popCollisionInfo() {
		return collisionInfo.removeFirst();
	}
	
	@Override
	public void setOwner(Orientable owner) {
		this.owner = owner;
	}
	
	@Override
	public Orientable transform(Orientable wrt) {
		return new Orientable() {
			public DVector2 pos() {
				return wrt.toBodyFrame(owner().pos());
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
