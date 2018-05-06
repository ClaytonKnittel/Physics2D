package mechanics2D.shapes;

import java.awt.Graphics;

import mechanics2D.graphics.Drawable;

public interface IsShape extends Drawable {
	
	default void draw(Graphics g) {
		shape().draw(g);
	}
	
	Shape shape();
	
}
