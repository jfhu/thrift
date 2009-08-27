/**
 * @author milki
 */
package com.doesntexist.milki;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableModel;

import com.doesntexist.milki.abstractModel.Entry;
import com.doesntexist.milki.abstractModel.EntryTableModel;
import com.doesntexist.milki.abstractModel.EntryTableModelListener;

/**
 * The Class ThriftGUI.
 */
public class ThriftGUI extends JFrame {
	/** Deals with I18N and L10N */
	private Messages local = new Messages();
	
	/** The main frame. */
	private static ThriftGUI jFrame;
	
	/** The title. */
	private static String title;
	
	/** The Constant width. */
	private static final int WIDTH = 1000;
	
	/** The Constant height. */
	private static final int HEIGHT = 700;
	
	/* JFrames / JDialogs below */
	/** The preference pane. */
	private JDialog preferencePane;
	
	/* JPanels below */
	/** The date selector. */
	private JPanel dateSelector = new JPanel();
	
	/** The pie chart panel. */
	private JPanel pieChartPanel = new JPanel();
	
	/** The entry list. */
	private JPanel entryList = new JPanel();
	
	/** The status bar. */
	private JPanel statusBar = new JPanel();
	
	/** The pie chart option panel. */
	private JPanel pieChartOptionPanel = new JPanel();
	private JPanel pieChartOptionPanelUp = new JPanel();
	private JPanel pieChartOptionPanelDown = new JPanel();
	private static JLabel pieChartOptionPanelSum = new JLabel();
	
	final JComboBox pieChartOptionComboBox = new JComboBox();
	
	/* JMenus below */
	/** The menu bar. */
	private JMenuBar menuBar = new JMenuBar();
	
	/** The edit menu. */
	private JMenu editMenu;
	private JMenu fileMenu;
	
	/** The preferences item. */
	private JMenuItem preferencesItem;
	private JMenuItem saveDataItem;
	
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
					displayLabel.setText("<html>"  //$NON-NLS-1$
							+ engine.getExchangeRateLongString() 
							+ 	" <a href=\"http://www.xe.com\">XE.com"  //$NON-NLS-1$
							+ "</a></html>"); //$NON-NLS-1$
				Thread.sleep(100);
				} catch (Exception e) {
					Utilities.log("Error refresh exchange rate."); //$NON-NLS-1$
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
		title = new String(Messages.getString("ThriftGUI.title")); //$NON-NLS-1$
		editMenu = new JMenu(Messages.getString("ThriftGUI.Edit"), true); //$NON-NLS-1$
		fileMenu = new JMenu(Messages.getString("ThriftGUI.File"), true); //$NON-NLS-1$
		preferencesItem = new JMenuItem(Messages.getString("ThriftGUI.Preferences")); //$NON-NLS-1$
		saveDataItem = new JMenuItem(Messages.getString("ThriftGUI.Save")); //$NON-NLS-1$
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
		setSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (engine.isDataModified()) {
					int choice = JOptionPane.showConfirmDialog(null, 
							Messages.getString("ThriftGUI.WarnBeforeExit"), Messages.getString("ThriftGUI.Warning"), JOptionPane.YES_NO_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
					if (choice == JOptionPane.YES_OPTION) {
						try {
							engine.saveData();
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, Messages.getString("ThriftGUI.DataSaveFail"), Messages.getString("ThriftGUI.Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
							setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						}
					} else if (choice == JOptionPane.NO_OPTION) {
						setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					} else {
						setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					}
				}
				super.windowClosing(e);
			}
		});
		
		engine = new Engine(this);
		exchangeRateRefresher.start();
		engine.setEntryTableModelListener(new EntryTableModelListener(this, engine));
		
		setPanels();
		setMenus();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		/* initially set this false */
		engine.setDataModified(false);
	}

	/**
	 * Sets the panels.
	 */
	private void setPanels() {
		JPanel northPanel = new JPanel(new BorderLayout());
		JPanel northWestPanel = new JPanel(new BorderLayout());
		
		dateSelector.add(engine.getCalendar());
		
		sExchangeRateDisplay.setText(engine.getExchangeRateLongString());
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(2000);
		sExchangeRateDisplay.setToolTipText(Messages.getString("ThriftGUI.Loading")); //$NON-NLS-1$
		sExchangeRateDisplay.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.xe.com")); //$NON-NLS-1$
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		statusBar.setLayout(new FlowLayout());
		statusBar.add(sExchangeRateDisplay);
		
		JPanel pieChartInnerPanel = engine.getPieChart().getPieChartPanel(0, engine.getCalendar().getDate());
		pieChartPanel = new JPanel(new BorderLayout());
		pieChartPanel.setPreferredSize(new Dimension(540, 290));
		pieChartPanel.add(pieChartInnerPanel, BorderLayout.CENTER);

		pieChartOptionPanelUp.setLayout(new FlowLayout());
		pieChartOptionPanelUp.add(new JLabel(Messages.getString("ThriftGUI.PieChart"))); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.AllByCategory")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.AllByAccount")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.MonthByCategory")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.MonthByAccount")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.WeekByCategory")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.WeekByAccount")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.DayByCategory")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.DayByAccount")); //$NON-NLS-1$
		pieChartOptionComboBox.addItem(Messages.getString("ThriftGUI.AccordToFilter")); //$NON-NLS-1$
		pieChartOptionComboBox.setToolTipText(Messages.getString("ThriftGUI.SetPieChartDisplay")); //$NON-NLS-1$
		pieChartOptionComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//update the chart when clicked the combo box
				updateTheChart();
			}
		});
		pieChartOptionPanelUp.add(pieChartOptionComboBox);
		pieChartOptionPanelDown.setLayout(new FlowLayout());
		JButton addEntry = new JButton("+"); //$NON-NLS-1$
		addEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utilities.log("Filter text: " + engine.getEntryTableModel().getFilterText()); //$NON-NLS-1$
				Entry newEntry = engine.checkFilterTextMatch(engine.getEntryTableModel().getFilterText());
				Calendar cal = Calendar.getInstance();
				cal.setTime(newEntry.getDate());
				cal.set(Calendar.HOUR, 8);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				newEntry.setDate(cal.getTime());
				engine.addEntry(newEntry);
				engine.getEntryTableModel().update();
				engine.getEntryTableModel().getModel().fireTableDataChanged();
			}
		});
		JButton removeEntry = new JButton("-"); //$NON-NLS-1$
		removeEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = engine.getEntryTableModel().getTable();
				int[] n = table.getSelectedRows();
				if (n.length < 1) {
					JOptionPane.showMessageDialog(null, Messages.getString("ThriftGUI.NoEntrySelect")); //$NON-NLS-1$
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null,
						Messages.getString("ThriftGUI.RemoveEntryConfirmA") + ((n.length>1)?n.length:"") + Messages.getString("ThriftGUI.RemoveEntryConfirmB"), Messages.getString("ThriftGUI.RemoveDialogTitle"),  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirm == JOptionPane.NO_OPTION) {
					return;
				}
				for (int i = n.length-1; i >= 0; i--) {
					engine.getData().remove(table.convertRowIndexToModel(n[i]));
				}
				engine.getEntryTableModel().update();
				engine.getEntryTableModel().getModel().fireTableDataChanged();
			}
		});
		addEntry.setToolTipText(Messages.getString("ThriftGUI.AddEntryToolTip")); //$NON-NLS-1$
		removeEntry.setToolTipText(Messages.getString("ThriftGUI.RemoveEntryToolTip")); //$NON-NLS-1$
		pieChartOptionPanelDown.add(addEntry);
		pieChartOptionPanelDown.add(removeEntry);
		pieChartOptionPanelDown.add(new JLabel(Messages.getString("ThriftGUI.TotalAmount"))); //$NON-NLS-1$
		pieChartOptionPanelDown.add(pieChartOptionPanelSum);
		pieChartOptionPanelSum.setText(Messages.getString("ThriftGUI.NotAvailable")); //$NON-NLS-1$
		pieChartOptionPanelSum.setHorizontalAlignment(JLabel.RIGHT);
//		pieChartOptionPanelDown.add(new JLabel("¼ÓÔª"));
		
		pieChartOptionPanel.setLayout(new BorderLayout());
		pieChartOptionPanel.add(pieChartOptionPanelUp, BorderLayout.NORTH);
		pieChartOptionPanel.add(pieChartOptionPanelDown, BorderLayout.SOUTH);
		
		northWestPanel.add(dateSelector, BorderLayout.NORTH);
		northWestPanel.add(pieChartOptionPanel, BorderLayout.SOUTH);
		northPanel.add(pieChartPanel, BorderLayout.CENTER);
		northPanel.add(northWestPanel, BorderLayout.WEST);
		
		engine.getEntryTableModel().initialPanel();
		entryList = engine.getEntryTableModel().getPanel();
		
		add(entryList, BorderLayout.CENTER);
		add(northPanel, BorderLayout.NORTH);
		add(statusBar, BorderLayout.SOUTH);
	}
	
	/**
	 * Sets the menus.
	 */
	private void setMenus() {
		/* File */
		saveDataItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, InputEvent.META_DOWN_MASK));
		saveDataItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utilities.log("Saving data."); //$NON-NLS-1$
				try {
					engine.saveData();
					JOptionPane.showMessageDialog(null, Messages.getString("ThriftGUI.SaveDataSucceed")); //$NON-NLS-1$
					Utilities.log("Data saved."); //$NON-NLS-1$
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, Messages.getString("ThriftGUI.SaveDateFailed"), Messages.getString("ThriftGUI.Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				} 
			}
		});
		fileMenu.add(saveDataItem);
		
		/* Edit */
		/* - Preferences... */
		preferencesItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_COMMA, InputEvent.META_DOWN_MASK));
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, 
						InputEvent.META_DOWN_MASK), "preferences"); //$NON-NLS-1$
		getRootPane().getActionMap().put("preferences", new AbstractAction() {  //$NON-NLS-1$
				public void actionPerformed(final ActionEvent e) {
					Utilities.log("Lauch preferences pane by keyboard."); //$NON-NLS-1$
					preferencePane = new PreferencesDialog(jFrame);
				} 
		});
		preferencesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Utilities.log("Lauch preferences pane from menu."); //$NON-NLS-1$
				preferencePane = new PreferencesDialog(jFrame);
			}
		});
		editMenu.add(preferencesItem);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		setJMenuBar(menuBar);
	}
	
	/**
	 * Creates and show GUI.
	 */
	public static void createAndShowGUI() {
		jFrame = new ThriftGUI();
	}
	
	public static JLabel getPieChartOptionPanelSum() {
		return pieChartOptionPanelSum;
	}
	
	public JPanel getPieChartPanel() {
		return pieChartPanel;
	}

	public int getComboSelectedIndex() {
		return pieChartOptionComboBox.getSelectedIndex();
	}

	public void updateTheChart() {
		if (engine != null) {
			Utilities.log("Updating the chart.");
			getPieChartPanel().removeAll();
			getPieChartPanel().add(engine.getPieChart().
					getPieChartPanel(getComboSelectedIndex(), engine.getCalendar().getDate()), BorderLayout.CENTER);
			getPieChartPanel().validate();
		}
	}
	
	/**
	 * The main method.
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (System.getProperty("os.name").equals("Mac OS X")) { //$NON-NLS-1$ //$NON-NLS-2$
					Utilities.log("Changed to Mac look and feel."); //$NON-NLS-1$
					try {
						UIManager.setLookAndFeel(
								"javax.swing.plaf.metal.MetalLookAndFeel"); //$NON-NLS-1$
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				createAndShowGUI();
			}
		});
	}
}

