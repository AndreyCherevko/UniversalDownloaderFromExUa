package download.utility;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Андрей on 12.12.2014.
 */
public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        if ((Boolean)value)
            setSelected(true);
        else
            setSelected(false);
        setMargin(new Insets(0, 16, 0, 0));
        setIconTextGap(0);

        setBackground(new Color(255, 255, 255, 0));
        return this;
    }
}
