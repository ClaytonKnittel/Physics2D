package mechanics2D.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Controller implements KeyListener {
	
	private Map<Integer, Action> pressMap;
	private Map<Integer, Action> releaseMap;
	
	public Controller(KeyAction[] actions) {
		pressMap = new HashMap<>(actions.length);
		releaseMap = new HashMap<>(actions.length);
		int key;
		for (KeyAction k : actions) {
			key = KeyEvent.getExtendedKeyCodeForChar(k.key);
			pressMap.put(key, k.press);
			releaseMap.put(key, k.release);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (pressMap.containsKey(e.getKeyCode()))
			pressMap.get(e.getKeyCode()).act();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (releaseMap.containsKey(e.getKeyCode()))
			releaseMap.get(e.getKeyCode()).act();
	}
	
	public static class KeyAction {
		
		private char key;
		private Action press, release;
		
		public KeyAction(char key, Action press, Action release) {
			this.key = key;
			this.press = press;
			this.release = release;
		}
		
	}
	
	public static interface Action {
		void act();
	}
	
}
