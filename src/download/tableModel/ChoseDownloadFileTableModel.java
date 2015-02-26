package download.tableModel;

import download.Download;
import download.utility.FileSizeGetter;
import download.utility.FileTypeGetter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by Андрей on 10.12.2014.
 */
public class ChoseDownloadFileTableModel extends AbstractTableModel {
    private static final String[] columnNames = {"Name", "Size",
            "Type"};

    public ChoseDownloadFileTableModel(ArrayList<Download> downloadList) {
        this.downloadList = downloadList;
    }

    public ArrayList<Download> getDownloadList() {
        return downloadList;
    }

    // These are the classes for each column's values.
    private static final Class[] columnClasses = {String.class, int.class, String.class};

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
            //File name
            case 0:
                return download.savefile.getName();
            //size
            case 1:
                return FileSizeGetter.getSize(download.getSize());
            case 2: // Status
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
}
