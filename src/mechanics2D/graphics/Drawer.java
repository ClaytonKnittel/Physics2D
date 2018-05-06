package mechanics2D.graphics;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class Drawer {
	
	private int width, height;
	private BufferStrategy b;
	
	private LinkedList<Drawable> entities;
	
	public Drawer(int width, int height, BufferStrategy b) {
		this.width = width;
		this.height = height;
		this.b = b;
		entities = new LinkedList<>();
	}
	
	public void add(Drawable...drawables) {
		for (Drawable d : drawables)
			entities.add(d);
	}
	
	public void draw() {
		Graphics g = b.getDrawGraphics();
		
		g.clearRect(0, 0, width, height);
		for (Drawable d : entities)
			d.draw(g);
		
		g.dispose();
		b.show();
	}
	
}
