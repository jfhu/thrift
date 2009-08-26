package com.doesntexist.milki.abstractModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.doesntexist.milki.Engine;
import com.doesntexist.milki.PieChart;
import com.doesntexist.milki.ThriftGUI;
import com.doesntexist.milki.Utilities;

public class EntryTableModelListener implements TableModelListener {

	private ThriftGUI gui;
	private Engine engine;
	
	public EntryTableModelListener(ThriftGUI gui, Engine engine) {
		this.gui = gui;
		this.engine = engine;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		Utilities.log("Table changed event received."); //$NON-NLS-1$
		engine.sortData();
		
		/* update total amount */
		ArrayList<Entry> data = engine.getData();
		double totalAmount = 0;
		for (Entry en : data) {
			totalAmount += en.getAmount();
		}
		gui.getPieChartOptionPanelSum().setText("$" + new DecimalFormat("0.00").format(totalAmount)); //$NON-NLS-1$ //$NON-NLS-2$
		
		/* update pie chart */
		PieChart pieChart = engine.getPieChart();
		gui.getPieChartPanel().removeAll();
		gui.getPieChartPanel().add(pieChart.getPieChartPanel(), BorderLayout.CENTER);
		gui.getPieChartPanel().validate();
		
		engine.setDataModified(true);
	}
}
