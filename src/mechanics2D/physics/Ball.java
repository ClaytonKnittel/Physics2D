package mechanics2D.physics;

import java.awt.Color;

import mechanics2D.shapes.Circle;
import tensor.DVector2;

public class Ball extends Body {
	
	public Ball(double x, double y, double vx, double vy, double mass, double radius, Color c) {
		super(x, y, vx, vy, mass, new Circle((float) radius, c));
	}
	
	public Ball(double x, double y, double mass, double radius, Color c) {
		this(x, y, 0, 0, mass, radius, c);
	}
	
	public String toString() {
		return "Circle:  pos: " + super.pos() + "\t radius: " + ((Circle) shape()).radius();
	}
	
}
