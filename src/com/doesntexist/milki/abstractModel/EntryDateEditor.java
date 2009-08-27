package com.doesntexist.milki.abstractModel;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.doesntexist.milki.Utilities;

public class EntryDateEditor extends DefaultCellEditor {
	DateFormat format;
	Date value;
	
	public EntryDateEditor(DateFormat format) {
		super(new JTextField());
		getComponent().setName("Table.editor"); //$NON-NLS-1$
		if (format != null) {
			this.format = format;
		} else {
			Utilities.log("No date format specified."); //$NON-NLS-1$
			format = new SimpleDateFormat();
		}
	}
	
	public boolean stopCellEditing() {
		String s = (String)super.getCellEditorValue();
		if ("".equals(s)) { //$NON-NLS-1$
			super.stopCellEditing();
		}
		try {
			value = format.parse(s);
		} catch (ParseException e) {
			((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
			return false;
		}
		return super.stopCellEditing();
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int col) {
		this.value = null;
		if (value instanceof Date) {
			value = format.format((Date) value);
		}
		((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
		return super.getTableCellEditorComponent(table, value, isSelected, row, col);
	}
	
	public Object getCellEditorValue() {
		return value;
	}
}
