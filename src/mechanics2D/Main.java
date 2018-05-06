package mechanics2D;

import java.awt.Color;

import mechanics2D.physics.PMath;
import mechanics2D.physics.Wall;
import mechanics2D.graphics.Screen;
import mechanics2D.input.Controller;
import mechanics2D.math.ThreadMaster;
import mechanics2D.physics.Ball;
import mechanics2D.physics.Box;
import mechanics2D.physics.Force;
import mechanics2D.physics.ForceField;
import mechanics2D.tests.ConditionalDrawer;
import mechanics2D.tests.DirectionDrawer;
import tensor.DVector2;

import static java.awt.Color.*;

public class Main {
	
	public static DVector2 dir = new DVector2(1, 0), pos = new DVector2(100, 100);
	
	public static double random() {
		return 500 * Math.random() - 250;
	}
	
	public static void main(String args[]) {
		Screen s = new Screen(600, 500);
		
		Color[] colors = new Color[] {
				RED, GREEN, BLUE, ORANGE, YELLOW, CYAN, BLACK, DARK_GRAY, LIGHT_GRAY, GRAY, MAGENTA, PINK
		};
		Ball[] balls = new Ball[16];
		int l = 0;
		for (int x = 100; x < 500; x += 100) {
			for (int y = 100; y < 500; y += 100)
				balls[l++] = new Ball(x, y, random(), random(), 30, 14, colors[(int) (Math.random() * colors.length)]);
		}
		
		
		Box box = new Box(200, 400, 40, -70, 20, 100, 40, Color.ORANGE);
		box.setW(0.1);
		
		Box box2 = new Box(400, 220.1, 50, -50, 20, 30, 100, Color.BLUE);
		box2.setAngle(Math.PI / 4);
		box2.setW(24);
		
		ForceField gravity = new ForceField(b -> {
			return new Force(b.pos(), DVector2.Y.times(980 * b.mass()));
		});
		
		//Wall mid = new Wall(350, 400, 0, 105, 10, Color.CYAN);
		
		Wall south = new Wall(300, 490, 0, 600, 20, Color.DARK_GRAY);
		Wall north = new Wall(300, 30, 0, 600, 20, Color.DARK_GRAY);
		Wall west = new Wall(10, 250, 0, 20, 500, Color.DARK_GRAY);
		Wall east = new Wall(590, 250, 0, 20, 500, Color.DARK_GRAY);
		
//		ConditionalDrawer d = new ConditionalDrawer(v -> {
//			DVector2 e = box.toBodyFrame(v);
//			if (Math.abs(e.x()) < box.width() / 2 && Math.abs(e.y()) < box.height() / 2)
//				return true;
//			e = box2.toBodyFrame(v);
//			if (Math.abs(e.x()) < box2.width() / 2 && Math.abs(e.y()) < box2.height() / 2)
//				return true;
//			return false;
//		}, 4, 600, 500, Color.BLACK);
//		
//		ConditionalDrawer d2 = new ConditionalDrawer(v -> {
//			box2.setPos(v);
//			return box.shape().colliding(box2.shape(), false);
//		}, 4, 600, 500, Color.BLACK);
		
		DirectionDrawer dir = new DirectionDrawer(new DirectionDrawer.Setter() {
			public DVector2 loc() {
				return Main.pos;
			}
			public DVector2 dir() {
				return Main.dir;
			}
		}, 30, Color.BLACK);
		
		s.add(balls);
		//s.add(box);
		//s.add(box2);
		//s.add(mid);
		s.add(south, north, east, west);
		s.add(dir);
		s.add(gravity);
		
		ThreadMaster graphics = new ThreadMaster(() -> {
			s.render();
		}, Screen.dt, false, "graphics");
		ThreadMaster physics = new ThreadMaster(() -> {
			s.physUpdate();
		}, PMath.dt, false, "physics");
		ThreadMaster info = new ThreadMaster(() -> {
			System.out.println(graphics);
			System.out.println(physics);
			//System.out.println(box.energy() + box2.energy() + PMath.gPotential(box, box2));
		}, 1, false, "info");
		
		Controller controller = new Controller(
			new Controller.KeyAction[] {
				new Controller.KeyAction('d', () -> physics.setSpeed(.02f), () -> physics.setSpeed(1))
			}
		);
		s.add(controller);
		
		graphics.start();
		physics.start();
		info.start();
	}
	
	
	
}
