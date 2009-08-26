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
import java.net.URI;
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

/**
 * The Class ThriftGUI.
 */
public class ThriftGUI extends JFrame {
	/** The main frame. */
	private static JFrame jFrame;
	
	/** The title. */
	private static String title = new String("Thrift - �󣬵�֮��Ҳ���ޣ���֮��Ҳ");
	
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
	private JMenu editMenu = new JMenu("�༭", true);
	private JMenu fileMenu = new JMenu("�ļ�", true);
	
	/** The preferences item. */
	private JMenuItem preferencesItem = new JMenuItem("ƫ������...");
	private JMenuItem saveDataItem = new JMenuItem("����");
	
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
		
		engine = new Engine();
		exchangeRateRefresher.start();
		
		setPanels();
		setMenus();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
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
		
		pieChartPanel = engine.getPieChart().getPieChartPanel();
		pieChartPanel.setPreferredSize(new Dimension(540, 290));

		pieChartOptionPanelUp.setLayout(new FlowLayout());
		pieChartOptionPanelUp.add(new JLabel("Բ��ͼ:"));
		JComboBox pieChartOptionComboBox = new JComboBox();
		pieChartOptionComboBox.addItem("ȫ��-������");
		pieChartOptionComboBox.addItem("ȫ��-���˻�");
		pieChartOptionComboBox.addItem("һ����-������");
		pieChartOptionComboBox.addItem("һ����-���˻�");
		pieChartOptionComboBox.addItem("һ��-������");
		pieChartOptionComboBox.addItem("һ��-���˻�");
		pieChartOptionComboBox.addItem("һ��-������");
		pieChartOptionComboBox.addItem("һ��-���˻�");
		pieChartOptionComboBox.setToolTipText("ѡ��Բ��ͼ��ʾ��Χ");
		pieChartOptionPanelUp.add(pieChartOptionComboBox);
		pieChartOptionPanelDown.setLayout(new FlowLayout());
		JButton addEntry = new JButton("+");
		addEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.addEntry(new Entry(false, "Acc", "Cat", 0.0, "C", "Remark", new Date()));
				engine.getEntryTableModel().update();
			}
		});
		JButton removeEntry = new JButton("-");
		removeEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTable table = engine.getEntryTableModel().getTable();
				int[] n=table.getSelectedRows();
				for (int i = n.length-1; i >= 0; i--) {
					engine.getData().remove(table.convertRowIndexToModel(n[i]));
					Utilities.log(engine.getData());
					Utilities.log(n[i] +"," +table.convertRowIndexToModel(n[i]));
				}
//				engine.getEntryTableModel().getTable().clearSelection();
				engine.getEntryTableModel().update();
			}
		});
		addEntry.setToolTipText("����һ����¼");
		removeEntry.setToolTipText("�Ƴ�ѡ�еļ�¼");
		pieChartOptionPanelDown.add(addEntry);
		pieChartOptionPanelDown.add(removeEntry);
		pieChartOptionPanelDown.add(new JLabel("�ϼ�:"));
		pieChartOptionPanelDown.add(pieChartOptionPanelSum);
		pieChartOptionPanelSum.setText("1290");
		pieChartOptionPanelSum.setHorizontalAlignment(JLabel.RIGHT);
		pieChartOptionPanelDown.add(new JLabel("��Ԫ"));
		
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
					JOptionPane.showMessageDialog(null, "�������ݳɹ���");
					Utilities.log("Data saved.");
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "��������ʧ�ܣ�", "����", JOptionPane.ERROR_MESSAGE);
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

