package download;

/**
 * Created by ������ on 10.12.2014.
 */
import download.frames.ChoseDownloadFileFrame;
import download.tableModel.DownloadTableModel;
import download.utility.ProgressRenderer;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.filters.Purifier;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import url_parser.HTMLSAXParser;
import url_parser.LoadURLparserHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

// The Download Manager.
public class DownloadManager extends JFrame
        implements Observer
{
    // Add download text field.
    private JTextField addTextField;

    // Download table's data model.
    private DownloadTableModel tableModel;

    // Table showing downloads.
    private JTable table;

    // These are the buttons for managing the selected download.
    private JButton pauseButton, resumeButton;
    private JButton cancelButton, clearButton;

    // Currently selected download.
    private Download selectedDownload;

    // Flag for whether or not table selection is being cleared.
    private boolean clearing;

    // Constructor for Download Manager.
    private DownloadManager()
    {
        // Set application title.
        setTitle("Download Manager");

        // Set window size.
        setSize(640, 480);

        // Handle window closing events.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });

        // Set up file menu.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit",
                KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Set up add panel.
        JPanel addPanel = new JPanel();
        addTextField = new JTextField(30);
        addPanel.add(addTextField);
        JButton addButton = new JButton("Add Download");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionAdd();
            }
        });
        addPanel.add(addButton);

        // Set up Downloads table.
        tableModel = new DownloadTableModel();
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new
                                                                   ListSelectionListener() {
                                                                       public void valueChanged(ListSelectionEvent e) {
                                                                           tableSelectionChanged();
                                                                       }
                                                                   });
        // Allow only one row at a time to be selected.
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set up ProgressBar as renderer for progress column.
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true); // show progress text
        table.setDefaultRenderer(JProgressBar.class, renderer);

        // Set table's row height large enough to fit JProgressBar.
        table.setRowHeight(
                (int) renderer.getPreferredSize().getHeight());

        // Set up downloads panel.
        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setBorder(
                BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table),
                BorderLayout.CENTER);

        // Set up buttons panel.
        JPanel buttonsPanel = new JPanel();
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPause();
            }
        });
        pauseButton.setEnabled(false);
        buttonsPanel.add(pauseButton);
        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionResume();
            }
        });
        resumeButton.setEnabled(false);
        buttonsPanel.add(resumeButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });
        cancelButton.setEnabled(false);
        buttonsPanel.add(cancelButton);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionClear();
            }
        });
        clearButton.setEnabled(false);
        buttonsPanel.add(clearButton);

        // Add panels to display.
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(addPanel, BorderLayout.NORTH);
        getContentPane().add(downloadsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }

    // Exit this program.
    private void actionExit() {
        System.exit(0);
    }

    // Add a new download.
    private void actionAdd() {
        File file = choseDirectory();
        URL verifiedUrl = verifyUrl(addTextField.getText());
        if (verifiedUrl != null) {
            AbstractSAXParser parser = new HTMLSAXParser();
            URLConnection newconnection = null;
            InputStream inputStream = null;
            InputSource inputSource = null;
            LoadURLparserHandler parserHandler = null;
            try {
                newconnection = verifiedUrl.openConnection();
                inputStream = newconnection.getInputStream();
                inputSource = new InputSource(inputStream);
                XMLDocumentFilter purifier = new Purifier();
                XMLDocumentFilter[] filters = { purifier };
                parserHandler  = new LoadURLparserHandler();
                parser.setContentHandler(parserHandler);
                parser.setProperty("http://cyberneko.org/html/properties/filters",filters);
                parser.parse(inputSource);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXNotSupportedException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (SAXNotRecognizedException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (SAXException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            Map<String,String> mapOfUrl = parserHandler.getMapOfUrl();
            if(mapOfUrl.size()==0){
                JOptionPane.showMessageDialog(this,
                        "Nothing file to download on this page", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            Set<String> setOfUrl = mapOfUrl.keySet();
            ArrayList<Download> listOfDownloads = new ArrayList<>();
            for(String fileName:setOfUrl)
                listOfDownloads.add(new Download( verifyUrl("http://www.ex.ua" + mapOfUrl.get(fileName)),new File(file.getPath()+File.separator+fileName)));
            ChoseDownloadFileFrame choseDownloadFileFrame = new ChoseDownloadFileFrame(this,listOfDownloads);
            listOfDownloads = choseDownloadFileFrame.getListOfUrl();
            tableModel.setDownloadList(listOfDownloads);
            tableModel.startAll();
            addTextField.setText(""); // reset add text field

        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid Download URL", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Verify download URL.
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://www.ex.ua"))
            return null;

        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        return verifiedUrl;

    }

    // Called when table row selection changes.
    private void tableSelectionChanged() {
    /* Unregister from receiving notifications
       from the last selected download. */
        if (selectedDownload != null)
            selectedDownload.deleteObserver(DownloadManager.this);

    /* If not in the middle of clearing a download,
       set the selected download and register to
       receive notifications from it. */
        if (!clearing && table.getSelectedRow() > -1) {
            selectedDownload =
                    tableModel.getDownload(table.getSelectedRow());
            selectedDownload.addObserver(DownloadManager.this);
            updateButtons();
        }
    }

    // Pause the selected download.
    private void actionPause() {
        selectedDownload.pause();
        updateButtons();
    }

    // Resume the selected download.
    private void actionResume() {
        selectedDownload.resume();
        updateButtons();
    }

    // Cancel the selected download.
    private void actionCancel() {
        selectedDownload.cancel();
        updateButtons();
    }

    // Clear the selected download.
    private void actionClear() {
        clearing = true;
        tableModel.clearDownload(table.getSelectedRow());
        clearing = false;
        selectedDownload = null;
        updateButtons();
    }

    /* Update each button's state based off of the
       currently selected download's status. */
    private void updateButtons() {
        if (selectedDownload != null) {
            int status = selectedDownload.getStatus();
            switch (status) {
                case Download.DOWNLOADING:
                    pauseButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case Download.PAUSED:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case Download.ERROR:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
                    break;
                default: // COMPLETE or CANCELLED
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
            }
        } else {
            // No download is selected in table.
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            cancelButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    }

    /* Update is called when a Download notifies its
       observers of any changes. */
    public void update(Observable o, Object arg) {
        // Update buttons if the selected download has changed.
        if (selectedDownload != null && selectedDownload.equals(o))
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    updateButtons();
                }
            });
    }

    // Run the Download Manager.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DownloadManager manager = new DownloadManager();
                manager.setVisible(true);
            }
        });
    }
    private File choseDirectory(){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.showSaveDialog(this);
            return fileChooser.getSelectedFile();
    }
}

