package mechanics2D.shapes;

import java.awt.Graphics;

import mechanics2D.physics.PMath;
import tensor.DMatrix2;
import tensor.DVector2;

public class Rectangle extends AbstractShape {
	
	private double length, height;
	
	private DVector2 ll, lr, ul, ur;
	
	public Rectangle(double length, double height, Orientable owner, java.awt.Color color) {
		super(owner, color);
		this.length = length;
		this.height = height;
	}
	
	private void setCorners() {
		ll = new DVector2(owner().pos().x() - length / 2, owner().pos().y() + height / 2);
		lr = new DVector2(owner().pos().x() + length / 2, owner().pos().y() + height / 2);
		ul = new DVector2(owner().pos().x() - length / 2, owner().pos().y() - height / 2);
		ur = new DVector2(owner().pos().x() + length / 2, owner().pos().y() - height / 2);
	}
	
	public double length() {
		return length;
	}
	
	public double height() {
		return height;
	}

	@Override
	public void draw(Graphics g) {
		DVector2 ll, lr, ul, ur;
		DMatrix2 rot = DMatrix2.rotate(owner().angle());
		
		setCorners();
		ll = rot.multiply(this.ll.minus(owner().pos())).plus(owner().pos());
		lr = rot.multiply(this.lr.minus(owner().pos())).plus(owner().pos());
		ul = rot.multiply(this.ul.minus(owner().pos())).plus(owner().pos());
		ur = rot.multiply(this.ur.minus(owner().pos())).plus(owner().pos());
		
		g.setColor(color());
		g.fillPolygon(new int[] {(int) ll.x(), (int) lr.x(), (int) ur.x(), (int) ul.x()}, 
				new int[] {(int) ll.y(), (int) lr.y(), (int) ur.y(), (int) ul.y()}, 4);
	}
	
	@Override
	public boolean colliding(Shape s, boolean computeCollisionInfo) {
		if (s instanceof Rectangle)
			return collidingRects((Rectangle) s, true, computeCollisionInfo);
		if (s instanceof Circle)
			return collidingCircle((Circle) s, computeCollisionInfo);
		return false;
	}
	
	/**
	 * This is a special case of the separating axis theorem (SAT)
	 * @param r the rectangle to be compared to this
	 * @param first true when called outside this method, false when called within
	 * @return whether or not these rectangles collide
	 */
	private boolean collidingRects(Rectangle r, boolean first, boolean compute) {
		Orientable rPos = r.transform(super.owner());
//		Orientable rPos = transform(r.owner());
		
		double hlength = r.length / 2;
		double hheight = r.height / 2;
		
		double theta = rPos.angle();
		DVector2 rpos = rPos.pos();
		double x = rpos.x();
		double y = rpos.y();
		
		double cos = Math.cos(theta);
		double sin = Math.sin(theta);
		
		double hlcos = hlength * cos;
		double hhcos = hheight * cos;
		double hlsin = hlength * sin;
		double hhsin = hheight * sin;
		
		// first test projection onto x-axis (will need to compare to length / 2)
		double dif = Math.abs(hlcos) + Math.abs(hhsin);
		if (notIntersecting(x - dif, x + dif, -this.length / 2, this.length / 2))
			return false;
		
		// then test projection onto y-axis (will need to compare to height / 2)
		dif = Math.abs(hlsin) + Math.abs(hhcos);
		if (notIntersecting(y - dif, y + dif, -this.height / 2, this.height / 2))
			return false;
		
		if (compute) {
			// find all intersecting points
			double px, py;
			int a = 1, b = 1;
			
			for (byte i = 0; i < 4; i++) {
				px = x - b * hhsin + a * hlcos; // determine location of individual rectangle vertex
				py = y + a * hlsin + b * hhcos;
				
				if (Math.abs(px) <= this.length / 2 && Math.abs(py) <= this.height / 2) {
					double dir;
					if (Math.abs(px / this.length) > Math.abs(py / this.height))	// colliding with left or right wall
						dir = px > 0 ? 0 : Math.PI;
					else															// colliding with top or bottom wall
						dir = py > 0 ? Math.PI / 2 : 3 * Math.PI / 2;
					
//					addCollisionInfo(new CollisionInformation(
//							toSpaceFrame(new DVector2(px, py), owner().pos(), owner().angle()), dir + owner().angle()));
					addCollisionInfo(new DVector2(px, py), dir);
				}
				if (a == -1)
					b *= -1;
				a *= -1;
			}
		}
		
		if (!first)
			return true;
		
		if (r.collidingRects(this, false, compute))
			return true;
		return false;
	}
	
	private boolean collidingCircle(Circle c, boolean computeCollisionInfo) {
		Orientable rPos = c.transform(super.owner());
		
		DVector2  pos = rPos.pos();
		double x = pos.x();
		double y = pos.y();
		double hlen = length / 2;
		double hheight = height / 2;
		if (x < 0)
			hlen *= -1;
		if (y < 0)
			hheight *= -1;
		
		if (Math.abs(x) > Math.abs(hlen)) {
			if (Math.abs(y) > Math.abs(hheight)) {					// colliding with corner of rectangle
				DVector2 to = new DVector2(x - hlen, y - hheight);
				
				if (to.mag2() > PMath.square(c.radius()))
					return false;
				
				addCollisionInfo(new DVector2(hlen, hheight), to.angle());
			} else {												// colliding with side of rectangle
				if (Math.abs(x - hlen) > c.radius())
					return false;
				
				addCollisionInfo(new DVector2(hlen, y), x > 0 ? 0 : Math.PI);
			}
		} else if (Math.abs(y) > Math.abs(hheight)) {				// colliding with top/bottom of rectangle
			if (Math.abs(y - hheight) > c.radius())
				return false;
			
			addCollisionInfo(new DVector2(x, hheight), y > 0 ? Math.PI / 2 : 3 * Math.PI / 2);
		} else {													// shouldn't happen
			System.err.println("Circle inside rectangle");
			return false;
		}
		return true;
	}
	
	private boolean notIntersecting(double min1, double max1, double min2, double max2) {
		return min2 > max1 || min1 > max2;
	}
	
	@Override
	public double moment() {
		return (length * length + height * height) / 12;
	}

}
