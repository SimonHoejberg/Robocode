
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public class Gui {

	private JFrame frame;
	private boolean error;

	public static void main(String[] args) {

		try {
			//Tries to change how the gui looks based on what OS the user has
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Gui gui = new Gui();
		gui.start();
		
	}
	
	public void start(){

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				final JFrame frame = new JFrame("File List");
				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout());
				final JFileChooser fileChooser = new JFileChooser(new File("."));
				final JCheckBox check = new JCheckBox(".Java        ");
				fileChooser.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						return "BTR Files";
					}

					@Override
					public boolean accept(File f) {
						return f.isDirectory() ? true : f.getName().endsWith(".btr");
					}
				});
				fileChooser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(e.getActionCommand().equals("CancelSelection")){
							System.exit(0);
						}
						else if(e.getActionCommand().equals("ApproveSelection")){
							BTR btr = new BTR();
							SetGuiPointerToCompiler(btr);
							try {
								btr.Start(fileChooser.getSelectedFile().getPath(), check.isSelected());
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if(!HasErrors())
								JOptionPane.showMessageDialog(frame, "Compilation complete!!!!");
							System.exit(0);
						}
					}
				});
				fileChooser.setApproveButtonText("Compile");
				panel.add(fileChooser, BorderLayout.CENTER);
				frame.getContentPane().add(panel);
				
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.SOUTH);
				panel_1.setLayout(new BorderLayout(0, 0));
				
				panel_1.add(check, BorderLayout.EAST);
				check.setHorizontalAlignment(SwingConstants.LEFT);
				frame.pack();
				frame.setLocationByPlatform(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				

			}
		});
	}
	
	private boolean HasErrors(){
		return error;
	}
	
	private void SetGuiPointerToCompiler(BTR compiler){
		compiler.SetGuiPointer(this);
	}
	
	public void ShowConsole(String msg){
		JTextArea text = new JTextArea();
		text.setEditable(false);
		text.setText(msg);
		JScrollPane pane = new JScrollPane(text);
		pane.setPreferredSize(new Dimension(500,500));
		JOptionPane.showMessageDialog(frame, pane,"Console",JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
