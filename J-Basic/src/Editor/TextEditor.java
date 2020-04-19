package Editor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import jBasic.JBasicRunner;

public class TextEditor extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel buttonPanel;
	private JPanel bottomButtonPanel;
	private JButton btnNew;
	private JButton btnOpen;
	private JButton btnSave;
	private JButton btnSaveAs;
	private JButton btnRunProgram;
	private JButton btnDoc;
	private JPanel textAreaRegion;
	private JTextArea textArea;
	private JScrollPane scrollPane; 
	private JLabel label;
	private JLabel versionWatermark;
	
	private JPanel sidePanel;
	private JTextArea console;
	private JScrollPane consoleScrollPane;
	
	TextEditor editor;
	
	public TextEditor() {
		editor = this;
		this.setTitle("J-Basic Editor v" + Main.version + " - Developed By Alex Gray - 2020");
		ImageIcon img = new ImageIcon("icon.png");
		this.setIconImage(img.getImage());
		loadBtns();
		// Create text area
		textAreaRegion = new JPanel();
		textArea = new JTextArea(50,60);
		textArea.setEditable(true);
		
		Font font = new Font("Verdana", Font.PLAIN, 13);
		textArea.setForeground(Color.BLACK);
		textArea.setFont(font);
		
		scrollPane = new JScrollPane(textArea);
		textAreaRegion.add(scrollPane);
		this.add(textAreaRegion, BorderLayout.WEST);
		
		//Create the console
		sidePanel = new JPanel();
		console = new JTextArea(15,45);
		console.setEditable(false);
		
		console.setFont(font);
		console.setForeground(Color.BLACK);
		
		consoleScrollPane = new JScrollPane(console);
		sidePanel.add(consoleScrollPane);
		this.add(sidePanel, BorderLayout.EAST);
		clearConsole();
		
		// Add J-BASIC VERSION to new file
		textArea.setText("## J-BASIC VERSION " + Main.languageVersion + " ##\n");
		label.setText("Untitled.jb");
		this.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
				String saveFileLocation = label.getText();
				//If its not the default file, save before close
				if(!saveFileLocation.equals("Untitled.jb")) {
					File file = new File(saveFileLocation);
					try {
						PrintWriter writer = new PrintWriter(file);
						for(String line: textArea.getText().split("\n")) { 
							writer.println(line); 
						}
						writer.close();

					} catch (FileNotFoundException er) {
						System.out.println("Error in TextEditor.Java");
					}

					label.setText(saveFileLocation);	
				}
		    }
		});
	}
	
	public void writeToConsole(String statement) {
		console.setText(console.getText() + "\n" + statement);
		// This prevents the bottom scroll bar from appearing unless its needed
		this.setSize(this.getSize().width + 1, this.getSize().height);
		this.setSize(this.getSize().width - 1, this.getSize().height);
	}
	
	private void clearConsole() {
		console.setText("-- J-Basic Console v" + Main.version + " --");
		// This prevents the bottom scroll bar from appearing unless its needed
		this.setSize(this.getSize().width + 1, this.getSize().height);
		this.setSize(this.getSize().width - 1, this.getSize().height);
	}
	
	public void openWebPage(String url){
		   try {         
		     java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		   }
		   catch (java.io.IOException e) {
		       e.printStackTrace();
		   }
		}
	
	private void loadBtns() { 

		buttonPanel = new JPanel();
		bottomButtonPanel = new JPanel();
		this.add(buttonPanel, BorderLayout.NORTH);
		this.add(bottomButtonPanel, BorderLayout.SOUTH);
		
		btnNew = new JButton();
		btnOpen = new JButton();
		btnSave = new JButton();
		btnSaveAs = new JButton();
		btnRunProgram = new JButton();
		btnDoc = new JButton();

		btnNew.setText("Create New File");
		btnOpen.setText("Open Existing File");
		btnSave.setText("Save File");
		btnSaveAs.setText("Save As...");
		btnRunProgram.setText("Run J-Basic Program");
		btnDoc.setText("Open Documentation");
		
		btnNew.addActionListener(new NewButtonClick());
		btnOpen.addActionListener(new OpenButtonClick());
		btnSave.addActionListener(new SaveButtonClick());
		btnSaveAs.addActionListener(new SaveAsButtonClick());
		btnRunProgram.addActionListener(new RunProgramButtonClick());
		btnDoc.addActionListener(new DocButtonClick());
		
		buttonPanel.add(btnNew);
		buttonPanel.add(btnOpen);
		buttonPanel.add(btnSave);
		buttonPanel.add(btnSaveAs);
		
		label = new JLabel();
		label.setText("No File Opened");
		buttonPanel.add(label);
		
		versionWatermark = new JLabel();
		versionWatermark.setText("\tJ-Basic Version: " + Main.languageVersion + "\t");
		versionWatermark.setHorizontalAlignment(JLabel.LEFT);
		versionWatermark.setVerticalAlignment(JLabel.BOTTOM);
		bottomButtonPanel.add(btnRunProgram);
		bottomButtonPanel.add(versionWatermark);
		bottomButtonPanel.add(btnDoc);
	}

	class NewButtonClick implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			textArea.setText("## J-BASIC VERSION " + Main.languageVersion + " ##\n");
			label.setText("Untitled.jb");
			clearConsole(); //Clear the console when a new file is created
		}
	}
	
	class OpenButtonClick implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser opener = new JFileChooser();
			opener.setCurrentDirectory(new File("."));
			opener.setDialogTitle("Open .jb Files");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("J-Basic Files","jb");
			opener.setFileFilter(filter);
			if(opener.showOpenDialog(btnOpen) == JFileChooser.APPROVE_OPTION) {
				String fileLocation = opener.getSelectedFile().getAbsolutePath();
				if(fileLocation.endsWith(".jb")) {
					File file = new File(fileLocation);
					try { 
						Scanner in = new Scanner(file);
						textArea.setText("");
						while(in.hasNextLine()) {
							textArea.append("" + in.nextLine() + "\n");
						}
						in.close();
						clearConsole(); //Clear the console when a new file is loaded
					}
					catch(FileNotFoundException e) {
						System.out.println("File Not Found!");
					}

					label.setText(file.getName());
				}
				else {
					JOptionPane.showMessageDialog(Main.editor,
						    "This editor only supports J-Basic files!",
						    "Invalid File Error",
						    JOptionPane.ERROR_MESSAGE);
					System.out.println("This Editor only supports J-Basic Files");
				}
			}
		}
	}

	class SaveButtonClick implements ActionListener {

		public void actionPerformed(ActionEvent arg0) { 
			if(label.getText() == "Untitled.jb") { 
				newSave();	
			}
			else { 
				String saveFileLocation = label.getText();
				File file = new File(saveFileLocation);
				try {
					PrintWriter writer = new PrintWriter(file);
					for(String line: textArea.getText().split("\n")) { 
						writer.println(line); 
					}
					writer.close();

				} catch (FileNotFoundException e) {
					System.out.println("Error in TextEditor.Java");
				}

				label.setText(saveFileLocation);

			}
		}
	}

	class SaveAsButtonClick implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			newSave();
		}
	}
	
	class RunProgramButtonClick implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// Save the file before running it
			if(label.getText() == "Untitled.jb") { 
				newSave();	
			}
			else { 
				String saveFileLocation = label.getText();
				File file = new File(saveFileLocation);
				try {
					PrintWriter writer = new PrintWriter(file);
					for(String line: textArea.getText().split("\n")) { 
						writer.println(line); 
					}
					writer.close();

				} catch (FileNotFoundException e) {
					System.out.println("Error in TextEditor.Java");
				}

				label.setText(saveFileLocation);
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
				clearConsole();
				writeToConsole("Running " + saveFileLocation + ": Run time [" + formatter.format(date) + "]");
				new JBasicRunner(saveFileLocation, editor);
			}
		}
	}
	
	class DocButtonClick implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			openWebPage("https://github.com/Alex4008/J-Basic-Editor/blob/master/README.md#documentation");
		}
	}
	
	private void newSave() {
		JFileChooser save = new JFileChooser();
		save.setCurrentDirectory(new File("."));
		save.setDialogTitle("Save");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("J-Basic Files","jb");
		save.setFileFilter(filter);
		if(save.showSaveDialog(btnSave) == JFileChooser.APPROVE_OPTION) {
			String saveFileLocation = save.getSelectedFile().getAbsolutePath();
			if(!saveFileLocation.endsWith(".jb")) {
				saveFileLocation += ".jb"; // Add the J-Basic extension
			}
			File file = new File(saveFileLocation);
			try {
				PrintWriter writer = new PrintWriter(file);
				for(String x: textArea.getText().split("\n")) { 
					writer.println(x); 
				}
				writer.close();
			} catch (FileNotFoundException e) {
				System.out.println("Error 2 TextEditor.java");
			}
			label.setText(file.getName());
		}
	}
}
