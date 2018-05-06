package mechanics2D.graphics;

import java.awt.event.KeyListener;

import javax.swing.JFrame;

import mechanics2D.physics.Bodies;

public class Screen extends JFrame {
	private static final long serialVersionUID = -207531079449884642L;
	
	public static float dt = .01f;
	private Drawer drawer;
	private Bodies bodies;
	
	public Screen(int width, int height) {
		super.setSize(width, height);
		super.setResizable(false);
		super.setTitle("2D physics");
		super.setVisible(true);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		super.createBufferStrategy(2);
		drawer = new Drawer(width, height, super.getBufferStrategy());
		bodies = new Bodies();
	}
	
	public void add(Drawable...drawables) {
		drawer.add(drawables);
		bodies.attemptAdd(drawables);
	}
	
	public void add(KeyListener k) {
		this.addKeyListener(k);
	}
	
	public void physUpdate() {
		bodies.update();
	}
	
	public void render() {
		drawer.draw();
	}
	
}
