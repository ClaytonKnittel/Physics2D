package mechanics2D;

import java.awt.Color;

import algorithms.MatrixAlgorithms;
import mechanics2D.physics.PMath;
import mechanics2D.physics.Wall;
import mechanics2D.graphics.Screen;
import mechanics2D.input.Controller;
import mechanics2D.math.ThreadMaster;
import mechanics2D.physics.Ball;
import mechanics2D.physics.Box;
import mechanics2D.physics.Force;
import mechanics2D.physics.ForceField;
import mechanics2D.physics.InteractiveForce;
import mechanics2D.tests.DirectionDrawer;
import methods.P;
import structures.Reversible;
import tensor.DMatrixN;
import tensor.DVector2;
import tensor.DVectorN;

import static java.awt.Color.*;

public class Main {
	
	public static DVector2 dir = new DVector2(1, 0), pos = new DVector2(100, 100);
	
	public static double random() {
		return 500 * Math.random() - 250;
	}
	
	public static class Pair implements Reversible<Pair> {
		int x, y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Pair reverse() {
			return new Pair(y, x);
		}
	}
	
	public static void main(String args[]) {
		
//		DMatrixN a = new DMatrixN(8, 8, 0.06666666666666668,	-0.033333333333071445,	0.0,	0.0,	0.0,	-9.40360215257851E-8,	9.40360215257851E-8,	-0.03331833727882451,
//				-0.033333333333071445,	0.06666666666666668,	-0.03333333333330137,	0.0,	3.809814854652954E-8,	-3.809814854652954E-8,	0.0,	0.0,
//				0.0,	-0.033333333333301386,	0.06666666666666667,	8.425330054093703E-8,	-8.42533005407202E-8,	0.0,	0.0,	0.0,
//				0.0,	0.0,	8.425330054093703E-8,	0.03333333333333333,	0.0,	0.0,	0.0,	0.0,
//				0.0,	3.80981485463127E-8,	-8.425330054093703E-8,	0.0,	0.03333333333333333,	0.0,	0.0,	0.0,
//				-9.40360215257851E-8,	-3.80981485463127E-8,	0.0,	0.0,	0.0,	0.03333333333333333,	0.0,	0.0,
//				9.40360215257851E-8,	0.0,	0.0,	0.0,	0.0,	0.0,	0.03333333333333333,	-9.998500067498473E-4,
//				-0.03331833727882451,	0.0,	0.0,	0.0,	0.0,	0.0,	-9.998500067498475E-4,	0.03333333333333333);
//		DVectorN bb = new DVectorN(7.832820934656307E-18, -6.034402631259766E-18, -55.612710313298884, -0.19522461596659935, -0.19522461596659912, -0.1952246159665991, -0.19522461596659912, 2.0449435843893128E-17);
//		
////		DMatrixN a = new DMatrixN(1, -1, -1, 1);
////		DVectorN bb = new DVectorN(-1, -1);
//		
//		P.pl("A:\n" + a + "\n");
//		
//		DVectorN answer = MatrixAlgorithms.solveConstrainedEqn(a, bb);
//		
//		P.pl("answer: " + answer);
//		P.pl("Accels: " + a.multiply(answer).plus(bb));
//		
//		System.exit(0);
		
		Screen s = new Screen(600, 500);
		
		Color[] colors = new Color[] {
				RED, GREEN, BLUE, ORANGE, YELLOW, CYAN, BLACK, DARK_GRAY, LIGHT_GRAY, GRAY, MAGENTA, PINK
		};
		Ball[] balls = new Ball[36];
		int l = 0;
		for (int x = 100; x < 500; x += 70) {
			for (int y = 100; y < 500; y += 70) {
				balls[l] = new Ball(x, y, random(), random(), 30, 14, colors[(int) (Math.random() * colors.length)]);
				balls[l++].setRestitution(Math.random() * .2 + .7);
			}
		}
		
		Ball b1 = new Ball(300, 250, 0, 0, 30, 14, colors[5]);
		Ball b2 = new Ball(272, 250, 0, 0, 30, 14, colors[1]);
		Ball b3 = new Ball(357, 250, -50, 0, 30, 14, colors[2]);
		Ball b4 = new Ball(240, 250, 0, 0, 30, 14, colors[3]);
		
		b1.setRestitution(.7);
		b2.setRestitution(.8);
		b3.setRestitution(.9);
		b4.setRestitution(.87);
		
		Box box = new Box(200, 400, 40, 70, 20, 100, 40, Color.ORANGE);
		box.setAngle(0);
		box.setW(.1);
		box.setRestitution(.8);
		
		Box box2 = new Box(200, 330, 40, 0, 20, 100, 30, Color.BLUE);
		box2.setAngle(0);
		box2.setW(.07);
		box2.setRestitution(.8);
		
		ForceField gravity = new ForceField(b -> new Force(DVector2.ZERO, DVector2.Y.times(200 * b.mass())));
		
		InteractiveForce attractor = new InteractiveForce((body1, body2) -> new Force(DVector2.ZERO, body2.pos().minus(body1.pos())));
		
		//Wall mid = new Wall(350, 400, 0, 105, 10, Color.CYAN);
		
		Wall south = new Wall(300, 490, 0, 600, 20, Color.DARK_GRAY);
		Wall north = new Wall(300, 30, 0, 600, 20, Color.DARK_GRAY);
		Wall west = new Wall(10, 250, 0, 20, 500, Color.DARK_GRAY);
		Wall east = new Wall(590, 250, 0, 20, 500, Color.DARK_GRAY);
		
		south.setAngle(.05);
		
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
		
		//s.add(b1, b2, b3, b4);
		s.add(balls);
		//s.add(box, box2);
		//s.add(box2);
		//s.add(mid);
		s.add(south, north, east, west);
		s.add(dir);
		s.addPhysics(gravity);
		
		ThreadMaster graphics = new ThreadMaster(() -> {
			s.render();
		}, Screen.dt, false, "graphics");
		ThreadMaster physics = new ThreadMaster(() -> {
			s.physUpdate();
		}, PMath.dt, false, "physics");
		ThreadMaster info = new ThreadMaster(() -> {
			P.pl(graphics);
			P.pl(physics);
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
