/**
 * @author milki
 */
package com.doesntexist.milki;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/

/**
 * The Class PreferencesFrame.
 */
public class PreferencesDialog  extends JDialog {
	public PreferencesDialog(final JFrame parent) {
		super((JFrame) parent, "∆´∫√…Ë÷√", true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dispose");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.META_DOWN_MASK), "dispose");
		getRootPane().getActionMap().put("dispose", new AbstractAction() { 
				public void actionPerformed(final ActionEvent e) {
					Utilities.log("Dispose the preference."); 
					dispose(); 
				} 
		}); 
		
		setPanel();
		
		this.setResizable(false);
		this.setSize(487, 313);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void setPanel() {
		{
			
		}
	}
}

