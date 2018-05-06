package mechanics2D.shapes;

import tensor.DVector2;

public interface Locatable {
	
	DVector2 pos();
	
	void move(DVector2 dPos);
	
}
