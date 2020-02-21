package Editor;
import javax.swing.JFrame;

public class Main {
	
	// Global variables
	static double version = 0.1;
	public static double languageVersion = 0.1;
	static boolean enableGUI = true;
	
	static JFrame editor;
	
	public static void main(String[] args) {
		if(args.length >= 1) enableGUI = false;
		
		editor = new TextEditor();
		editor.setSize(900, 1000);
		editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editor.setVisible(true);
		
	}
	
}
