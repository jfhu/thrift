package com.doesntexist.milki.abstractModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.ui.DateChooserPanel;

import com.doesntexist.milki.Engine;
import com.doesntexist.milki.Messages;
import com.doesntexist.milki.Utilities;

public class EntryTableModel extends AbstractTableModel {
	private JPanel tablePanel;
	private JTextField filterText;
	private JTable table;
	private TableRowSorter<EntryTableModel> sorter;
	private Engine engine;
	private EntryTableModel model;
	
	private String[] columnNames = 
		{Messages.getString("EntryTableModel.Valid"), Messages.getString("EntryTableModel.Account"), Messages.getString("EntryTableModel.Amount"), Messages.getString("EntryTableModel.Category"), Messages.getString("EntryTableModel.Remark"), Messages.getString("EntryTableModel.Date") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	
	private ArrayList<Entry> data = new ArrayList<Entry>();

	public EntryTableModel() {
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
		this.data = engine.getData();
	}
	
	public void initialPanel() {
		tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		
		//Create a table with a sorter
		model = new EntryTableModel();
		sorter = new TableRowSorter<EntryTableModel>(model);
		table = new JTable(model);
		table.setRowSorter(sorter);
		table.getModel().addTableModelListener(engine.getEntryTableModelListener());
		table.setPreferredScrollableViewportSize(
				new Dimension(table.getPreferredSize().width, 400));
	    table.setFillsViewportHeight(true);
	    table.setDefaultEditor(Date.class, new EntryDateEditor(new SimpleDateFormat("yyyy-M-d"))); //$NON-NLS-1$
	    model.fireTableDataChanged();

	    //selection
	    table.getSelectionModel().addListSelectionListener(
	    		new ListSelectionListener() {
					@Override
					public void valueChanged(final ListSelectionEvent e) {
						int viewRow = table.getSelectedRow();
						if (viewRow < 0) {
//							Utilities.log("Selection got filtered away. " + viewRow);
						} else {
							int modelRow = 
								table.convertRowIndexToModel(viewRow);
//							Utilities.log(String.format(
//									"Selected Row in view: %d; "
//									+ "Selected Row in model: %d.",
//									viewRow, modelRow));
						}
					}
	    		});
	    JScrollPane scrollPane = new JScrollPane(table);
	    tablePanel.add(scrollPane);
	    tablePanel.setOpaque(true);
	    
	    //Filter section
	    JPanel form = new JPanel(new BorderLayout());
	    JLabel l1 = new JLabel(Messages.getString("EntryTableModel.Filter"), SwingConstants.TRAILING); //$NON-NLS-1$
	    form.add(l1, BorderLayout.WEST);
	    filterText = new JTextField();
	    filterText.getDocument().addDocumentListener(
	    		new DocumentListener() {
					@Override
					public void removeUpdate(final DocumentEvent e) {
						newFilter();
					}
					@Override
					public void insertUpdate(final DocumentEvent e) {
						newFilter();
					}
					@Override
					public void changedUpdate(final DocumentEvent e) {
						newFilter();
					}
	    		});
	    l1.setLabelFor(filterText);
	    form.add(filterText, BorderLayout.CENTER);
	    JButton clearButton = new JButton("X"); //$NON-NLS-1$
	    clearButton.setBorder(null);
	    clearButton.setContentAreaFilled(false);
	    Font buttonFont = clearButton.getFont();
	    clearButton.setFont(new Font(buttonFont.getName(), buttonFont.getStyle(), 9));
	    clearButton.setPreferredSize(new Dimension(14,clearButton.getPreferredSize().height));
	    clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterText.setText(""); //$NON-NLS-1$
			}
		});
	    form.add(clearButton, BorderLayout.EAST);
	    tablePanel.add(form);
	    
	    //set column width
	    TableColumn validColumn = table.getColumnModel().getColumn(0);
	    TableColumn accountColumn = table.getColumnModel().getColumn(1);
	    TableColumn amountColumn = table.getColumnModel().getColumn(2);
	    TableColumn categoryColumn = table.getColumnModel().getColumn(3);
	    TableColumn remarkColumn = table.getColumnModel().getColumn(4);
	    TableColumn dateColumn = table.getColumnModel().getColumn(5);
	    validColumn.setMaxWidth(45);
	    accountColumn.setMaxWidth(120);
	    amountColumn.setMaxWidth(70);
	    categoryColumn.setMaxWidth(100);
	    remarkColumn.setMinWidth(100);
	    dateColumn.setMaxWidth(150);
	    dateColumn.setMinWidth(100);
	    
	    //set combo box columns
	    ArrayList<Account> accountList = engine.getAccountList();
	    ArrayList<Category> categoryList = engine.getCategoryList();
	    model.data = engine.getData();
	    /* Set account list combo box */
	    JComboBox comboBox = new JComboBox();
	    for (Account ac : accountList) {
	    		comboBox.addItem(ac.getId());
	    }
	    accountColumn.setCellEditor(new DefaultCellEditor(comboBox));
	    /* Set category list combo box */
	    comboBox = new JComboBox();
	    for (Category ca : categoryList) {
	    		comboBox.addItem(ca.getId());
	    }
	    categoryColumn.setCellEditor(new DefaultCellEditor(comboBox));
	}
	
	public void update() {
		table.updateUI();
//		fireTableDataChanged();
		newFilter();
	}
	
	private void newFilter() {
		RowFilter<EntryTableModel, Object> rf = null;
//		try {
//			rf = RowFilter.regexFilter(filterText.getText(), 0);
//		} catch (java.util.regex.PatternSyntaxException e) {
//			return;
//		}
		rf = new RowFilter<EntryTableModel, Object>() {
			@Override
			public boolean include(final
					Entry<? extends EntryTableModel, ? extends Object> entry) {
				/* Special judgment for Date */
				String forTest = new SimpleDateFormat("yyyyMMddMMMMEEEE").format(((Date) entry.getValue(entry.getValueCount()-1))) //$NON-NLS-1$
					+ new SimpleDateFormat("MMMMEEEE", Locale.US).format(((Date) entry.getValue(entry.getValueCount()-1))); //$NON-NLS-1$
				/* Sample: 20090826°ËÔÂÐÇÆÚÈýAugustWednesday */
				if (forTest.toLowerCase().contains(filterText.getText().toLowerCase())) {
					return true;
				}
				/* For other fields, simply check the string */
				for (int i = entry.getValueCount() - 2; i >= 0; i--) {
					if (entry.getStringValue(i).toLowerCase().contains(
							filterText.getText().toLowerCase())) {
						return true;
					}
				}
				return false;
			 }
		};
//	TODO	TO FIX BUG
		table.clearSelection();
		sorter.setRowFilter(rf);
	}
	
	public JPanel getPanel() {
		return tablePanel;
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col].toString();
    }
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row >= data.size()) {
			return null;
		}
		Entry o = data.get(row);
		if (col == 0) {
			return o.isValid();
		} else if (col == 1) {
			return o.getAccountId();
		} else if (col == 2) {
			return o.getAmount();
		} else if (col == 3 ) {
			return o.getCategoryId();
		} else if (col == 4) {
			return o.getRemark();
		} else if (col == 5) {
			return o.getDate();
		} else {
			return null;
		}
	}
	
	@Override
	public Class getColumnClass(int c) {
		if (data.size() > 0) {
			return getValueAt(0, c).getClass();
		} else {
			return new Object().getClass();
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		Entry o = data.get(row);
		if (col == 0) {
			o.setValid((Boolean) value);
		} else if (col == 1) {
			o.setAccountId((String) value);
		} else if (col == 2) {
			o.setAmount((Double) value);
		} else if (col == 3 ) {
			o.setCategoryId((String) value);
		} else if (col == 4) {
			o.setRemark((String) value);
		} else if (col == 5) {
			o.setDate((Date) value);
		}
//		fireTableCellUpdated(row, col);
		fireTableDataChanged();
//		printDebugData();
	}

	public JTable getTable() {
		return table;
	}
	
	public EntryTableModel getModel() {
		return model;
	}

	public String getFilterText() {
		return filterText.getText();
	}

	private void printDebugData() {
        int numRows = getRowCount();
        
        for (int i=0; i < numRows; i++) {
            System.out.print("row " + i + ": "); //$NON-NLS-1$ //$NON-NLS-2$
            System.out.print(data.get(i));
            System.out.println();
        }
        System.out.println("--------------------------"); //$NON-NLS-1$
    }
	
	/*public static void main(String[] argrs) {
		JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        EntryTableModel table = new EntryTableModel();
        table.initialPanel();
        table.model.data.add(new Entry(true,"AAA","BBB",1234,"CCC","DDD",new Date(109,7,10)));
        frame.setContentPane(table.getPanel());

        frame.pack();
        frame.setVisible(true);
	}
	*/
	
}
