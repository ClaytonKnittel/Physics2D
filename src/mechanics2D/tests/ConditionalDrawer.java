package mechanics2D.tests;

import java.awt.Color;
import java.awt.Graphics;

import mechanics2D.graphics.Drawable;
import tensor.DVector2;

public class ConditionalDrawer implements Drawable {
	
	private PositionTest test;
	private int resolution, width, height;
	private Color color;
	
	public ConditionalDrawer(PositionTest test, int resolution, int width, int height, Color color) {
		this.test = test;
		this.resolution = resolution;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		for (int i = 0; i < width; i += resolution) {
			for (int j = 0; j < height; j += resolution) {
				if (test.test(new DVector2(i, j)))
					g.drawRect(i, j, 0, 0);
			}
		}
	}
	
	public static interface PositionTest {
		boolean test(DVector2 pos);
	}
	
}
