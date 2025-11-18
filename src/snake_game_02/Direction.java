package snake_game_02;

import java.awt.event.KeyEvent;

public enum Direction {
	
	LEFT(KeyEvent.VK_LEFT),
	RIGHT(KeyEvent.VK_RIGHT),
	UP(KeyEvent.VK_UP),
	DOWN(KeyEvent.VK_DOWN);

	private final int keycode;
	
	Direction(int keycode) {
		this.keycode = keycode;
	}
	
	public int getKeyCode() {
		return keycode;
	}
	
	public static Direction fromKeyCode(int keycode) {
		for (Direction direction : Direction.values()) {
			if (direction.getKeyCode() == keycode) {
				return direction;
			}
		}
		return null;
	}
}
