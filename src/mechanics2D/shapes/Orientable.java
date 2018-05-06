package mechanics2D.shapes;

import tensor.DMatrix2;
import tensor.DVector2;

public interface Orientable extends Locatable {
	
	double angle();
	
	void rotate(double dAngle);
	
	default DVector2 toSpaceFrame(DVector2 bodyVec) {
		return toSpaceFrame(bodyVec, pos(), angle());
	}
	
	default DVector2 toBodyFrame(DVector2 spaceVec) {
		return toBodyFrame(spaceVec, pos(), angle());
	}
	
	static DVector2 toSpaceFrame(DVector2 bodyVec, DVector2 bodyOrigin, double bodyAngle) {
		return DMatrix2.rotate(bodyAngle).multiply(bodyVec).plus(bodyOrigin);
	}
	
	static DVector2 toBodyFrame(DVector2 spaceVec, DVector2 bodyOrigin, double bodyAngle) {
		return DMatrix2.rotate(-bodyAngle).multiply(spaceVec.minus(bodyOrigin));
	}
	
}
