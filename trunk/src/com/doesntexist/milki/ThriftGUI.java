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
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.doesntexist.milki.abstractModel.Entry;
import com.doesntexist.milki.abstractModel.EntryTableModel;
import com.doesntexist.milki.abstractModel.EntryTableModelListener;

/**
 * The Class ThriftGUI.
 */
public class ThriftGUI extends JFrame {
	/** The main frame. */
	private static ThriftGUI jFrame;
	
	/** The title. */
	private static String title = new String("Thrift - 俭，德之共也；侈，恶之大也");
	
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
	private JLabel pieChartOptionPanelSum = new JLabel();
	
	/* JMenus below */
	/** The menu bar. */
	private JMenuBar menuBar = new JMenuBar();
	
	/** The edit menu. */
	private JMenu editMenu = new JMenu("编辑", true);
	private JMenu fileMenu = new JMenu("文件", true);
	
	/** The preferences item. */
	private JMenuItem preferencesItem = new JMenuItem("偏好设置...");
	private JMenuItem saveDataItem = new JMenuItem("保存");
	
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
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (engine.isDataModified()) {
					int choice = JOptionPane.showConfirmDialog(null, 
							"还有未保存的记录,是否保存?", "警告", JOptionPane.YES_NO_CANCEL_OPTION);
					if (choice == JOptionPane.YES_OPTION) {
						try {
							engine.saveData();
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, "保存数据失败！", "错误", JOptionPane.ERROR_MESSAGE);
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
		
		engine = new Engine();
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
		statusBar.setLayout(new FlowLayout());
		statusBar.add(sExchangeRateDisplay);
		
		JPanel pieChartInnerPanel = engine.getPieChart().getPieChartPanel();
		pieChartPanel = new JPanel(new BorderLayout());
		pieChartPanel.setPreferredSize(new Dimension(540, 290));
		pieChartPanel.add(pieChartInnerPanel, BorderLayout.CENTER);

		pieChartOptionPanelUp.setLayout(new FlowLayout());
		pieChartOptionPanelUp.add(new JLabel("圆饼图:"));
		JComboBox pieChartOptionComboBox = new JComboBox();
		pieChartOptionComboBox.addItem("全部-按分类");
		pieChartOptionComboBox.addItem("全部-按账户");
		pieChartOptionComboBox.addItem("一个月-按分类");
		pieChartOptionComboBox.addItem("一个月-按账户");
		pieChartOptionComboBox.addItem("一周-按分类");
		pieChartOptionComboBox.addItem("一周-按账户");
		pieChartOptionComboBox.addItem("一天-按分类");
		pieChartOptionComboBox.addItem("一天-按账户");
		pieChartOptionComboBox.setToolTipText("选择圆饼图显示范围");
		pieChartOptionPanelUp.add(pieChartOptionComboBox);
		pieChartOptionPanelDown.setLayout(new FlowLayout());
		JButton addEntry = new JButton("+");
		addEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utilities.log("Filter text: " + engine.getEntryTableModel().getFilterText());
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
		JButton removeEntry = new JButton("-");
		removeEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = engine.getEntryTableModel().getTable();
				int[] n = table.getSelectedRows();
				if (n.length < 1) {
					JOptionPane.showMessageDialog(null, "请先选择记录");
					return;
				}
				int confirm = JOptionPane.showConfirmDialog(null,
						"你真的想移除这" + ((n.length>1)?n.length:"") + "条记录吗?", "确认移除", 
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
		addEntry.setToolTipText("增加一条记录");
		removeEntry.setToolTipText("移除选中的记录");
		pieChartOptionPanelDown.add(addEntry);
		pieChartOptionPanelDown.add(removeEntry);
		pieChartOptionPanelDown.add(new JLabel("合计: "));
		pieChartOptionPanelDown.add(pieChartOptionPanelSum);
		pieChartOptionPanelSum.setText("N/A");
		pieChartOptionPanelSum.setHorizontalAlignment(JLabel.RIGHT);
//		pieChartOptionPanelDown.add(new JLabel("加元"));
		
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
				Utilities.log("Saving data.");
				try {
					engine.saveData();
					JOptionPane.showMessageDialog(null, "保存数据成功！");
					Utilities.log("Data saved.");
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "保存数据失败！", "错误", JOptionPane.ERROR_MESSAGE);
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
						InputEvent.META_DOWN_MASK), "preferences");
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
	
	public JLabel getPieChartOptionPanelSum() {
		return pieChartOptionPanelSum;
	}
	
	public JPanel getPieChartPanel() {
		return pieChartPanel;
	}

	/**
	 * The main method.
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

