package mechanics2D;

public class ThreadMaster implements Runnable {
	
	private Thread thread;
	
	private Updatable u;
	
	private long delay;
	private long delta;
	private double frequency;
	
	private float speed;
	
	private int frames;
	
	private boolean useMain;
	
	private static boolean running = true;
	
	public ThreadMaster(Updatable u, double frequency, boolean useMainThread) {
		this.u = u;
		
		this.delta = System.currentTimeMillis();
		this.frequency = frequency;
		this.speed = 1;
		
		setDelay();
		
		this.frames = 0;
		this.useMain = useMainThread;
		if (!useMainThread)
			this.thread = new Thread(this);
	}
	
	public ThreadMaster(Updatable u, double frequency, boolean useMainThread, String name) {
		this(u, frequency, useMainThread);
		if (!useMainThread)
			thread.setName(name);
	}

	public static void quit() {
		running = false;
	}
	
	public static boolean running() {
		return running;
	}
	
	private void setDelay() {
		this.delay = (long) Math.round(frequency * 1000 / speed);
	}
	
	public void setSpeed(float factor) {
		this.speed = factor;
		setDelay();
	}
	
	private int reset() {
		int ret = frames;
		frames = 0;
		return ret;
	}
	
	public int getFrames() {
		return reset();
	}
	
	public void start() {
		thread.start();
	}
	
	public void run() {
		while (running) {
			if (delta > System.currentTimeMillis()) {
				Thread.yield();
				continue;
			}
			delta = System.currentTimeMillis() + delay;
			frames++;
			u.update();
		}
	}
	
	public static interface Updatable {
		void update();
	}
	
	public String toString() {
		if (useMain)
			return Thread.currentThread().getName() + " " + getFrames();
		else
			return thread.getName() + " " + getFrames();
	}
	
}
