package mechanics2D.physics;

public class ForceField implements PhysicsConstruct {
	
	private Field field;
	
	public ForceField(Field field) {
		this.field = field;
	}
	
	public void interact(PhysicsBody body) {
		PhysicsBody b = (PhysicsBody) body;
		b.addForce(field.getForce(b));
	}
	
	public Force getForce(PhysicsBody body) {
		return field.getForce(body);
	}
	
	public interface Field {
		Force getForce(PhysicsBody b);
	}
	
}
