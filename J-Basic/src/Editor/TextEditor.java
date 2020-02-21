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
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
	private Label label;
	
	public TextEditor() {
		this.setTitle("J-Basic Editor v" + Main.version + " - Developed By Alex Gray - 2020");
		loadBtns();
		// Create text area
		textAreaRegion = new JPanel();
		textArea = new JTextArea(55,77);
		textArea.setEditable(true);
		
		Font font = new Font("Verdana", Font.PLAIN, 13);
		textArea.setFont(font);
		textArea.setForeground(Color.BLACK);
		
		textArea.setFont(font);
		scrollPane = new JScrollPane(textArea);
		textAreaRegion.add(scrollPane);
		this.add(textAreaRegion, BorderLayout.CENTER);
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
		bottomButtonPanel.add(btnRunProgram);
		bottomButtonPanel.add(btnDoc);
		
		label = new Label();
		label.setText("No File Opened");
		buttonPanel.add(label);
	}

	class NewButtonClick implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			textArea.setText("## J-BASIC VERSION " + Main.languageVersion + " ##\n");
			label.setText("Untitled.jb");
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
				System.out.println("Running JBasic File " + saveFileLocation + ": ");
				new JBasicRunner(saveFileLocation);
			}
		}
	}
	
	class DocButtonClick implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(Main.editor,
				    "This feature is coming soon. This will link to my website showing J-Basic documentation",
				    "Invalid Operation",
				    JOptionPane.ERROR_MESSAGE);
			System.out.println("Coming soon!");
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
				saveFileLocation += ".jb"; // Add the J-Basic extention
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
