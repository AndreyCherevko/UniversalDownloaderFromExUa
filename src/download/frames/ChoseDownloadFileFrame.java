package download.frames;

import download.Download;
import download.tableModel.TestTable;
import download.utility.CheckBoxRenderer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by Андрей on 10.12.2014.
 */
public class ChoseDownloadFileFrame extends JDialog implements ActionListener,MouseListener {
    private JTable table = null;
    private TestTable tableModel = null;
    private JButton add = null;
    private JButton clear = null;
    private ArrayList<Download> listOfUrl = null;
    private boolean isDispose = false;

    public ChoseDownloadFileFrame(JFrame parent, ArrayList<Download> listOfUrl) throws HeadlessException {
        super(parent, "Download files", true);
        this.listOfUrl = listOfUrl;
        MakeGui();
    }

    private void MakeGui() {
        BorderLayout layout = new BorderLayout(25, 25);
        setLayout(layout);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                actionClosing();
            }
        });
        setAlwaysOnTop(true);
        JScrollPane jScrollPane = new JScrollPane(getTable());
        add(jScrollPane, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.add(getAddButton());
        jPanel.add(getClearButton());
        add(jPanel, BorderLayout.SOUTH);
        JLabel header = new JLabel("Select files to upload");
        add(header, BorderLayout.NORTH);
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (sSize.getWidth() / 3), (int) (sSize.getHeight() / 2));
        setLocation((int) (sSize.getWidth() / 2), (int) (sSize.getHeight() / 2));
        setVisible(true);
    }

    private JButton getClearButton() {
        if (clear == null) {
            clear = new JButton("Delete");
            clear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actionClear();
                }
            });
        }
        return clear;
    }

    private void actionClear() {
        int selectedrow = table.getSelectedRow();
        tableModel.clearDownload(selectedrow);
    }

    private JTable getTable() {
        if (table == null) {
            tableModel = new TestTable(listOfUrl);
            table = new JTable(tableModel);
            table.addMouseListener(this);
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setMinWidth(50);
            columnModel.getColumn(0).setPreferredWidth(50);
            columnModel.getColumn(0).setMaxWidth(50);
            columnModel.getColumn(0).setCellRenderer(new CheckBoxRenderer());
            columnModel.getColumn(1).setMinWidth(225);
            columnModel.getColumn(1).setPreferredWidth(225);
            columnModel.getColumn(1).setMaxWidth(225);
            columnModel.getColumn(2).setMinWidth(90);
            columnModel.getColumn(2).setPreferredWidth(90);
            columnModel.getColumn(2).setMaxWidth(90);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            table.doLayout();
        }
        return table;
    }


    private JButton getAddButton() {
        if (add == null) {
            add = new JButton("Download");
            add.addActionListener(this);
        }
        return add;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        tableModel.deleteNotSelected();
        listOfUrl = tableModel.getDownloadList();
        dispose();
    }


    private void actionClosing() {
        if (!isDispose) {
            isDispose = true;
            dispose();
        }
    }

    public ArrayList<Download> getListOfUrl() {
        if (isDispose)
            return new ArrayList<Download>();
        dispose();
        isDispose = true;
        return listOfUrl;
    }

    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        Object data = tableModel.getValueAt(row, column);
        JCheckBox checkBox = (JCheckBox) data;
        if (checkBox.isSelected()) {
            tableModel.setValueAt(true, row, 0);
        } else {
            tableModel.setValueAt(false, row, 0);

        }

    }

    public void tableChanged() {
        int row = table.getSelectedRow();
        Download current = tableModel.getDownloadList().get(row);
        if (current.isSelected()) {
            tableModel.setValueAt(false, row, 0);
        } else {
            tableModel.setValueAt(true, row, 0);

        }

    }
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2 ){
            tableChanged();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
