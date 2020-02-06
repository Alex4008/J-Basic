package dataTypes;

public class J_Integer {

	String name;
	int value;
	
	public J_Integer(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int newValue) {
		value = newValue;
	}
}
