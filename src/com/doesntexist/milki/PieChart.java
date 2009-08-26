package com.doesntexist.milki;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;

import com.doesntexist.milki.abstractModel.Account;
import com.doesntexist.milki.abstractModel.Category;
import com.doesntexist.milki.abstractModel.Entry;

public class PieChart {
	private JPanel panel;
	private ArrayList<Entry> data;
	private ArrayList<Category> categoryList;
	private ArrayList<Account> accountList;
	private double startAngle = Math.random()*360;
	
	public PieChart() {
	}
	
	public PieChart(ArrayList<Entry> data, ArrayList<Category> categoryList, ArrayList<Account> accountList) {
		this.data = data;
		this.categoryList = categoryList;
		this.accountList = accountList;
	}
	
	private DefaultPieDataset getDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
//		TODO deal with the combo box
		/* if combo box ends with "分类" */
		String[] section = new String[categoryList.size()];
		for (int i = 0; i < section.length; i++) {
			section[i] = categoryList.get(i).getId();
		}
		double[] data = new double[section.length];
		for (Entry e : this.data) {
			if (e.isValid() == false) {
				continue;
			}
			for (int i = 0; i < section.length; i++) {
				if (e.getCategoryId().equals(section[i])) {
					data[i] += e.getAmount();
					continue;
				}
			}
		}
		for (int i = 0; i < section.length; i++) {
			dataset.setValue(section[i], data[i]);
		}
		return dataset;
	}
	
	private DefaultPieDataset generateRandomDataset() {
		String[] section = new String[] { "图书","食物","水果","交通","其它"};
		double[] data = new double[section.length];   
		for (int i = 0; i < data.length; i++) {
			data[i] = 10 + Double.parseDouble(new DecimalFormat("0.0").format((Math.random() * 100)));
		}

		 DefaultPieDataset dataset = new DefaultPieDataset();   
		 for (int i = 0; i < data.length; i++) {
		        dataset.setValue(section[i], data[i]);   
		 }
		 return dataset;   
	}
	
	private static JFreeChart createChart(DefaultPieDataset dataset) {
		JFreeChart chart = ChartFactory.createPieChart3D(
				null, dataset, true, false, false);
		return chart;   
	}   

	public  JPanel getPieChartPanel() {
		Utilities.log("Generating pie chart...");
		JFreeChart chart = createChart(getDataset());
		chart.setBackgroundPaint(null);
		chart.getLegend().setPosition(RectangleEdge.RIGHT);
		
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setForegroundAlpha(0.5F);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}\n${1}\n{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.0%")));
		plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
		plot.setNoDataMessage("没有数据");
		plot.setBackgroundPaint(null);
		plot.setStartAngle(startAngle);
		
		panel = new ChartPanel(chart);
		return panel;
	}

	/*public static void main(final String[] args) {   
		JFrame demo = new JFrame();
		demo.setContentPane(new PieChart().getPieChartPanel());
		demo.pack();
		demo.setVisible(true);   
	}   
	*/
}
