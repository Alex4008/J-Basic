package dataTypes;

public class J_String {
	String name;
	String value;

	public J_String(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String newValue) {
		value = newValue;
	}
}
