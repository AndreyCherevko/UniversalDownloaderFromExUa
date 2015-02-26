package download.tableModel;

import download.Download;
import download.utility.FileSizeGetter;
import download.utility.FileTypeGetter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Андрей on 10.12.2014.
 */
public class TestTable extends AbstractTableModel implements Observer {
    private static final String[] columnNames = {"Selected","Name", "Size",
            "Type"};

    public TestTable(ArrayList<Download> downloadList) {
        this.downloadList = downloadList;
    }

    public ArrayList<Download> getDownloadList() {
        return downloadList;
    }

    // These are the classes for each column's values.
    private static final Class[] columnClasses = {JCheckBox.class,String.class, int.class, String.class};

    // The table's list of downloads.
    private ArrayList<Download> downloadList =
            new ArrayList<Download>();

    public int getColumnCount() {
        return columnNames.length;
    }

    // Get a column's name.
    public String getColumnName(int col) {
        return columnNames[col];
    }

    // Get a column's class.
    public Class<?> getColumnClass(int col) {
        return columnClasses[col];
    }

    // Get table's row count.
    public int getRowCount() {
        return downloadList.size();
    }

    // Get value for a specific row and column combination.
    public Object getValueAt(int row, int col) {
        final Download download = downloadList.get(row);
        switch (col) {
            case 0:
                return new Boolean(download.isSelected());
            //File name
            case 1:
                return download.savefile.getName();
            //size
            case 2:
                return FileSizeGetter.getSize(download.getSize());
            case 3: // Status
                return FileTypeGetter.getInstance().getType(download.savefile.getName());
        }
        return "";
    }
    public void clearDownload(int row) {
        downloadList.remove(row);

        // Fire table row deletion notification to table.
        fireTableRowsDeleted(row, row);
    }

    @Override
    public boolean isCellEditable(int i, int i2) {
        return false;
    }
    public void deleteNotSelected(){
        Iterator<Download> it = downloadList.iterator();
        Download current = null;
        while(it.hasNext()) {
            current=it.next();
            if (!current.isSelected())
                it.remove();
        }
    }

    @Override
    public void setValueAt(Object o, int row, int col) {
        Boolean isSelected = (Boolean)o;
        Download download = downloadList.get(row);
        download.setSelected(isSelected);
        fireTableRowsUpdated(row,0);
    }

    @Override
    public void update(Observable observable, Object o) {
        int index = downloadList.indexOf(o);
        fireTableRowsUpdated(index, index);
    }
}
