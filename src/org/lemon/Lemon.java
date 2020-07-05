package org.lemon;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.lemon.gui.ApplicationFrame;


/**
 * @author Ramesh Poudel
 * @version 1.1
 * @since 2020
 * */
public class Lemon {

	public static void main(String[] args) throws IOException {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				
				} catch (Exception ex3) {
					ex3.printStackTrace();
					
					try {
						/*if nimbus is not supported*/
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}
				
				try {
					new ApplicationFrame();
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
