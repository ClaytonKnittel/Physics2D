package mechanics2D.shapes;

import java.awt.Color;
import java.awt.Graphics;

import mechanics2D.graphics.Drawable;
import tensor.DVector2;

public class CollisionInformation implements Drawable {
	
	private DVector2 dir, loc;
	
	private static final double scale = 10;
	
	public CollisionInformation(DVector2 loc, DVector2 dir) {
		this.dir = dir;
		this.loc = loc;
	}
	
	public CollisionInformation(DVector2 loc, double dir) {
		this(loc, new DVector2(Math.cos(dir), Math.sin(dir)));
	}
	
	public DVector2 dir() {
		return dir;
	}
	
	public DVector2 loc() {
		return loc;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		DVector2 end = loc.plus(dir.times(scale));
		g.drawLine((int) loc.x(), (int) loc.y(), (int) end.x(), (int) end.y());
	}
	
	@Override
	public String toString() {
		return "location:  " + loc + "\tdirection:  " + dir;
	}
	
}
