package mechanics2D.physics;

public class ForceField implements PhysicsConstruct {
	
	private Field field;
	
	public ForceField(Field field) {
		this.field = field;
	}
	
	public void interact(Body body) {
		Body b = (Body) body;
		b.addForce(field.getForce(b));
	}
	
	public Force getForce(Body body) {
		return field.getForce(body);
	}
	
	public interface Field {
		Force getForce(Body b);
	}
	
}
