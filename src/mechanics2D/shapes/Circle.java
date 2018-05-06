package mechanics2D.shapes;

import java.awt.Color;
import java.awt.Graphics;

import tensor.DVector2;

public class Circle extends AbstractShape {
	
	private double radius;
	
	public Circle(double radius, Orientable owner, Color color) {
		super(owner, color);
		this.radius = radius;
	}
	
	public Circle(float radius, Color color) {
		this(radius, null, color);
	}
	
	public double radius() {
		return radius;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color());
		int r2 = (int) (2 * radius);
		g.fillOval((int) (owner().pos().x() - radius), (int) (owner().pos().y() - radius), r2, r2);
	}
	
	@Override
	public double moment() {
		return 2d * radius * radius / 5d;
	}
	
	@Override
	public boolean colliding(Shape s, boolean computeCollisionInfo) {
		if (s instanceof Circle)
			return collidingCircles((Circle) s, computeCollisionInfo);
		if (s instanceof Rectangle)
			return s.colliding(this, computeCollisionInfo);
		return false;
	}
	
	private boolean collidingCircles(Circle c, boolean computeCollisionInfo) {
		double di = radius + c.radius;
		DVector2 diff = c.owner().pos().minus(owner().pos());
		
		boolean colliding = di * di >= diff.mag2();
		
		if (computeCollisionInfo && colliding) {
			System.out.println("Circle " + c);
			AbstractShape.addCollisionInfo(new CollisionInformation(owner().pos().plus(diff.times(radius / di)), diff.normalized()));
		}
		return colliding;
	}
	
}
