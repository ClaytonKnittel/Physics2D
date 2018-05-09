package mechanics2D.math;

import mechanics2D.shapes.Orientable;

public interface Transformable {
	
	/**
	 * Transforms this object in the future with respect to the supplied Orientable
	 * in the future.
	 * 
	 * @param wrt the Orientable to transform this object with respect to
	 * @return this object in the frame of reference of this orientable
	 */
	Orientable transform(Orientable wrt);
	
}
