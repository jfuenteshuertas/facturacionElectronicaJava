/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author javierfuenteshuertas
 */
public class CellRenderer extends DefaultTableCellRenderer implements TableCellRenderer   {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(null);
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Color c = new Color(41, 181, 100 );
        String tempVal=(String) table.getValueAt(row, 6);
        if(tempVal!=null && tempVal.equals("VALIDADO")){
            setBackground(c);
            setForeground(new Color(255,255,255));
        }
        else{
            setBackground(new Color(255,255,255));
            setForeground(new Color(0,0,0));
        }
        return this;
    }
    
}
