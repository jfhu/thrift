/**
 * @author milki
 */
package com.doesntexist.milki;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * The Class ThriftGUI.
 */
public class ThriftGUI extends JFrame {
	private static JFrame jFrame;
	
	/** The title. */
	private static String title = new String("Thrift - 俭，德之共也；侈，恶之大也");
	
	/** The Constant width. */
	private static final int WIDTH = 1000;
	
	/** The Constant height. */
	private static final int HEIGHT = 700;
	/* JFrames / JDialogs below */
	private JDialog preferencePane;
	
	/* JPanels below */
	/** The date selector. */
	private JPanel dateSelector = new JPanel();
	
	/** The pie chart. */
	private JPanel pieChart = new JPanel();
	
	/** The entry list. */
	private JPanel entryList = new JPanel();
	
	/** The status bar. */
	private JPanel statusBar = new JPanel();
	
	/* JMenus below */
	/** The menu bar. */
	private JMenuBar menuBar = new JMenuBar();
	
	/** The edit menu. */
	private JMenu editMenu = new JMenu("Edit", true);
	
	/** The preferences item. */
	private JMenuItem preferencesItem = new JMenuItem("Preferences...");
	
	/** The s exchange rate display. */
	private JLabel sExchangeRateDisplay = new JLabel() {
		public Point getToolTipLocation(final MouseEvent event) {
			return new Point(event.getPoint().x, event.getPoint().y - 18);
		}
	};
	
	/** The exchange rate refresher. */
	private ExchangeRateRefresher exchangeRateRefresher = 
		new ExchangeRateRefresher(sExchangeRateDisplay);
	
	/**
	 * The Class ExchangeRateRefresher.
	 */
	class ExchangeRateRefresher extends Thread {
		/** The display label. */
		private JLabel displayLabel;
		/**
		 * Instantiates a new exchange rate refresher.
		 * 
		 * @param displayLabel the display label
		 */
		public ExchangeRateRefresher(final JLabel displayLabel) {
			this.displayLabel = displayLabel;
		}
		/**
		 * Run.
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			while (true) {
				try {
					sExchangeRateDisplay.setToolTipText(
							engine.getExchangeDate());
					displayLabel.setText("<html>" 
							+ engine.getExchangeRateLongString() 
							+ 	" <a href=\"http://www.xe.com\">XE.com" 
							+ "</a></html>");
				Thread.sleep(100);
				} catch (Exception e) {
					Utilities.log("Error refresh exchange rate.");
				}
			}
			
		}
	}
	
	/** The engine. */
	private static Engine engine;
	
	/**
	 * Instantiates a new thrift GUI.
	 */
	public ThriftGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		setSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		engine = new Engine();
		exchangeRateRefresher.start();
		
		setPanels();
		setMenus();
		
		dateSelector.requestFocus();
		setVisible(true);
	}

	/**
	 * Sets the panels.
	 */
	private void setPanels() {
		dateSelector.add(engine.getCalendar());
		
		sExchangeRateDisplay.setText(engine.getExchangeRateLongString());
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(1000);
		sExchangeRateDisplay.setToolTipText("Loading...");
		sExchangeRateDisplay.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.xe.com"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		statusBar.add(sExchangeRateDisplay);
		
		add(dateSelector, BorderLayout.WEST);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	/**
	 * Sets the menus.
	 */
	private void setMenus() {
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, InputEvent.META_DOWN_MASK), "preferences");
		getRootPane().getActionMap().put("preferences", new AbstractAction() { 
				public void actionPerformed(final ActionEvent e) {
					Utilities.log("Lauch preferences pane by keyboard.");
					preferencePane = new PreferencesDialog(jFrame);
				} 
		});
		preferencesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Utilities.log("Lauch preferences pane from menu.");
				preferencePane = new PreferencesDialog(jFrame);
			}
		});
		editMenu.add(preferencesItem);
		
		menuBar.add(editMenu);
		setJMenuBar(menuBar);
	}
	
	/**
	 * Creates and show GUI.
	 */
	public static void createAndShowGUI() {
		jFrame = new ThriftGUI();
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (System.getProperty("os.name").equals("Mac OS X")) {
					Utilities.log("Changed to Mac look and feel.");
					try {
						UIManager.setLookAndFeel(
								"javax.swing.plaf.metal.MetalLookAndFeel");
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				createAndShowGUI();
			}
		});
	}
}

