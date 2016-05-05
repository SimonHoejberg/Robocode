
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

public class Main {

	private JFrame frame;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
							Hello h = new Hello();
							try {
								h.Start(fileChooser.getSelectedFile().getPath(), check.isSelected());
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							JOptionPane.showMessageDialog(frame, "Compilation complete!!!!");
							System.exit(0);
						}
					}
				});
				fileChooser.setApproveButtonText("Compile");
				
				check.setHorizontalAlignment(SwingConstants.RIGHT);
				//fileChooser.setAccessory(check);
				panel.add(fileChooser, BorderLayout.CENTER);
				panel.add(check, BorderLayout.SOUTH);
				//frame.getContentPane().add(fileChooser);
				frame.getContentPane().add(panel);
				frame.pack();
				frame.setLocationByPlatform(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);

			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
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
