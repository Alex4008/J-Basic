package dataTypes;

public class J_Character {

	String name;
	char value;
	
	public J_Character(String name, char value) {
		this.name = name;
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}
	
	public void setValue(char newValue) {
		value = newValue;
	}
}
