package mechanics2D.physics;

public class ForceField implements Interactive {
	
	private Field field;
	
	public ForceField(Field field) {
		this.field = field;
	}
	
	public boolean interact(Interactive body) {
		if (PhysicsBody.is(body)) {
			PhysicsBody b = (PhysicsBody) body;
			b.addForce(field.getForce(b));
		}
		return true;
	}
	
	public Force getForce(PhysicsBody body) {
		return field.getForce(body);
	}
	
	public interface Field {
		Force getForce(PhysicsBody b);
	}
	
}
