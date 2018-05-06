package mechanics2D.physics;

import java.awt.Color;

import mechanics2D.shapes.Rectangle;
import tensor.DVector2;

public class Box extends Body {
	
	public Box(double x, double y, double vx, double vy, double mass, double length, double width, Color color) {
		super(x, y, vx, vy, mass, new Rectangle(length, width, null, color));
		super.shape().setOwner(this);
	}
	
	public Box(double x, double y, double mass, double length, double width, Color color) {
		this(x, y, 0, 0, mass, length, width, color);
	}
	
	public Box setPos(DVector2 pos) {
		super.pos().add(pos.minus(super.pos()));
		return this;
	}
	
	public double width() {
		return ((Rectangle) super.shape()).length();
	}
	
	public double height() {
		return ((Rectangle) super.shape()).height();
	}
	
}
