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

import com.doesntexist.milki.abstractModel.Entry;

public class PieChart {
	JPanel panel;
	ArrayList<Entry> data;
	
	public PieChart() {
		
	}
	
	public PieChart(ArrayList<Entry> data) {
		this.data = data;
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
				null, dataset, true, true, false);
		return chart;   
	}   

	public  JPanel getPieChartPanel() {
		JFreeChart chart = createChart(generateRandomDataset());
		chart.setBackgroundPaint(null);
		chart.getLegend().setPosition(RectangleEdge.RIGHT);
		
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setForegroundAlpha(0.5F);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}\n{1}\n{2}", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.0%")));
		plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
		plot.setNoDataMessage("没有数据");
		plot.setBackgroundPaint(null);
		plot.setStartAngle(Math.random()*360);
		
		panel = new ChartPanel(chart);
//		Utilities.log(panel.getPreferredSize().toString());
		return panel;
	}

	public static void main(final String[] args) {   
		JFrame demo = new JFrame();
		demo.setContentPane(new PieChart().getPieChartPanel());
		demo.pack();
		demo.setVisible(true);   
	}   
}
