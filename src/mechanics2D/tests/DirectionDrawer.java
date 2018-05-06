package mechanics2D.tests;

import java.awt.Color;
import java.awt.Graphics;

import mechanics2D.graphics.Drawable;
import tensor.DVector2;

public class DirectionDrawer implements Drawable {
	
	private Setter s;
	private DVector2 dir, loc;
	private float scale;
	private Color color;
	
	public DirectionDrawer(Setter s, float scale, Color color) {
		this.s = s;
		this.scale = scale;
		this.color = color;
	}
	
	public void draw(Graphics g) {
		loc = s.loc();
		dir = s.dir().times(scale).plus(loc);
		
		g.setColor(color);
		g.drawLine((int) loc.x(), (int) loc.y(), (int) dir.x(), (int) dir.y());
	}
	
	public static interface Setter {
		DVector2 dir();
		DVector2 loc();
	}
	
}
