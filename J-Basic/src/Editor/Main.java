package Editor;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {
	
	// Global variables
	static double version = 0.2;
	public static double languageVersion = 0.2;
	static boolean enableGUI = true;
	
	static JFrame editor;
	
	public static void main(String[] args) {
		if(args.length >= 1) enableGUI = false;
		editor = new TextEditor();
		editor.setSize(1225, 975);
		editor.setMinimumSize(new Dimension(1225, 975));
		editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editor.setVisible(true);
	}
}
