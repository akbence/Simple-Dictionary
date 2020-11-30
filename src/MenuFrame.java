
import model.DictionaryRow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MenuFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static JScrollPane mainWordstableScrollPane;
    private JTextField tfCurrPage;
    private JTextField tfCurrFilter;
    private JLabel maxPageLabel;
    private JRadioButton noneButton;
    private JRadioButton meaningButton;
    private JRadioButton pronunciationButton;
    private JRadioButton bookButton;
    private static int currPage = 1;
    private static int maxPage = 1;
    private static int offset = 0;
    private static int limit = 50;
    private FilterType filterType = FilterType.NONE;
    public MenuFrame() throws Exception {
        super("Dictionary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar mb = new JMenuBar();
        // create menu
        JMenu mnuDictionary = new JMenu("Dictionary");
        mb.add(mnuDictionary);

        // options in Dictionary Menu
        JMenuItem option = new JMenuItem("Add Word...");
        option.setIcon( getImage("add.gif"));
        option.setAccelerator( KeyStroke.getKeyStroke("F5"));
        mnuDictionary.add(option);
        option.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addWord();
            }
        });

        // options in Dictionary Menu
        option = new JMenuItem("Delete Word...");
        option.setIcon( getImage("delete.gif"));
        option.setAccelerator( KeyStroke.getKeyStroke("F6"));
        mnuDictionary.add(option);
        option.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteWord();
            }
        });

        mnuDictionary.addSeparator();

        // options in Dictionary Menu
        option = new JMenuItem("Search Word...");
        option.setIcon( getImage("search.gif"));
        option.setAccelerator( KeyStroke.getKeyStroke("F7"));
        mnuDictionary.add(option);
        option.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                searchWord();
            }
        });

        mnuDictionary.addSeparator();

        option = new JMenuItem("Exit");
        mnuDictionary.add(option);
        option.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

//        addStorageMenu(mb);
        addToolbar();
        setJMenuBar(mb);

        // load dictionary from disk
        Dictionary.loadFromDisk();
        calcPagination();
        showDictionaryTable(rootPane);
    }

    private void showDictionaryTable(JRootPane rootPane) {
        List<String> headings = new ArrayList<>();
        headings.add("Word");
        headings.add("Meaning");
        headings.add("Pronounciation");
        headings.add("Source");
        headings.add("Created");

        DbHandler dbHandler = DbHandler.getDbHandler();
        List<DictionaryRow> rows = dbHandler.getDictionaryRows(limit, offset, filterType, tfCurrFilter.getText());


        DefaultTableModel tableModel = new DefaultTableModel();
        headings.forEach(tableModel::addColumn);
        int counter = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (DictionaryRow row : rows) {
            tableModel.insertRow(counter, new String[]{row.getWord(), row.getMeaning(), row.getPronunciation(), row.getSource(), formatter.format(row.getDateOfAdded())});
            counter++;
        }
        JTable table = new JTable(tableModel);
        mainWordstableScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rootPane.getContentPane().add(mainWordstableScrollPane);
        rootPane.getContentPane().revalidate();

    }

    public void exit() {
        if (Dictionary.isModified()) {
            int option = JOptionPane.showConfirmDialog(MenuFrame.this, "You have some pending changes. Do you want to write them to disk and then exit?",
                    "Save", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {  // exit after save
                Dictionary.saveToDisk();
                System.exit(0);
            }
            else if (option == JOptionPane.NO_OPTION) // exit without saving
            {
               System.exit(0);
            }
        // if cancel then do not exit
        } else {
            System.exit(0);
        }
    }

    public ImageIcon getImage(String filename){
    	try {
        return new ImageIcon(
                this.getClass().getResource(filename));
    	}
    	catch(Exception ex) {
    		System.out.println( ex.getMessage());
    		return null;
    	}
    }

    public void centerToParent(JFrame parent, JFrame child) {
        Dimension pd = parent.getSize();
        Dimension cd = child.getSize();
        int x = (int) (pd.getWidth() - cd.getWidth()) / 2;
        int y = (int) (pd.getHeight() - cd.getHeight()) / 2;
        child.setLocation(x, y);

    }

    public void addWord() {
        AddWord w = new AddWord();
        centerToParent(MenuFrame.this, w);
        w.setVisible(true);
    }

    public void deleteWord() {
        DeleteWord w = new DeleteWord();
        centerToParent(MenuFrame.this, w);
        w.setVisible(true);
    }

    public void searchWord() {
        SearchWord w = new SearchWord();
        centerToParent(MenuFrame.this, w);
        w.setVisible(true);
    }

    public void addToolbar() {
        JToolBar tb = new JToolBar();
        JButton b = new JButton( getImage("add.gif"));
        b.setPreferredSize( new Dimension(32,32));
        tb.add(b);
        b.setToolTipText("Add Word");
        b.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 addWord();
            }

        });

        b = new JButton( getImage("delete.gif"));
        b.setPreferredSize( new Dimension(32,32));
        tb.add(b);
        b.setToolTipText("Delete Word");
        b.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 deleteWord();
            }

        });

        b = new JButton( getImage("search.gif"));
        b.setPreferredSize( new Dimension(32,32));
        tb.add(b);
        b.setToolTipText("Search Word");
        b.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 searchWord();
            }

        });


        b = new JButton( getImage("refresh.gif"));
        tb.add(b);
        b.setToolTipText("Refresh table");
        b.addActionListener(e -> refreshMainTable());

        tb.addSeparator();

        b = new JButton( getImage("csv.gif"));
        tb.add(b);
        b.setToolTipText("Export to CSV");
        b.addActionListener(e -> CsvHandler.exportDictionaryToCsv());

        JPanel displayPanel =new JPanel( new GridBagLayout() );
        b = new JButton("Go to Page:");
        displayPanel.add(b);
        b.setToolTipText("Go to given page");
        b.addActionListener(e -> refreshMainTable());

        tfCurrPage = new JTextField(5);
        //tfCurrPage.setPreferredSize( new Dimension(20,10));
        tfCurrPage.setText("1");
        tfCurrPage.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = tfCurrPage.getText();
                int l = value.length();
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
                    tfCurrPage.setEditable(true);
                } else {
                    tfCurrPage.setText("");
                }
            }
        });
        displayPanel.add(tfCurrPage);

        maxPageLabel =new JLabel("Max page: " + maxPage);
        displayPanel.add(maxPageLabel);
        tb.add(displayPanel);

        JPanel filterPanel =new JPanel( new GridBagLayout() );
        b = new JButton("Filter:");
        b.setToolTipText("Enter Filter");
        b.addActionListener(e -> setFiltering());
        filterPanel.add(b);

        tfCurrFilter = new JTextField(30);
        //tfCurrPage.setPreferredSize( new Dimension(20,10));
        tfCurrFilter.setText("");
        filterPanel.add(tfCurrFilter);
        //RADIOBUTTON
        noneButton = new JRadioButton("None");
        noneButton.setVisible(true);
        noneButton.setActionCommand("word");
        noneButton.setSelected(true);

        meaningButton = new JRadioButton("Meaning");
        noneButton.setActionCommand("Meaning");

        pronunciationButton = new JRadioButton("Pronunciation");
        noneButton.setActionCommand("Pronunciation");


        bookButton = new JRadioButton("Source: book");
        noneButton.setActionCommand("Source: book");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(noneButton);
        buttonGroup.add(meaningButton);
        buttonGroup.add(pronunciationButton);
        buttonGroup.add(bookButton);

        JPanel radioButtonPanel = new JPanel();
        BoxLayout radioButtonPanelBoxLayout = new BoxLayout(radioButtonPanel,BoxLayout.X_AXIS);
        radioButtonPanel.setLayout(radioButtonPanelBoxLayout);
        radioButtonPanel.add(noneButton);
        radioButtonPanel.add(meaningButton);
        radioButtonPanel.add(pronunciationButton);
        radioButtonPanel.add(bookButton);

        filterPanel.add(radioButtonPanel);
        tb.add(filterPanel);

        getContentPane().add(tb, BorderLayout.NORTH);
    }

    private void setFiltering() {
        if(noneButton.isSelected()){
            filterType = FilterType.NONE;
        }
        if(meaningButton.isSelected()){
            filterType = FilterType.MEANING;
        }
        if(pronunciationButton.isSelected()){
            filterType = FilterType.PRONOUNCIATION;
        }
        if(bookButton.isSelected()){
            filterType = FilterType.BOOKSOURCE;
        }
        refreshMainTable();
        calcPagination();
    }

    private void refreshMainTable() {
        calcPagination();
        rootPane.getContentPane().remove(mainWordstableScrollPane);
        showDictionaryTable(rootPane);
    }

    private void calcPagination() {
        if (tfCurrPage.getText().equals("")) {
            currPage = 1;
        } else {
            currPage = Integer.parseInt(tfCurrPage.getText());
        }
        DbHandler dbHandler = DbHandler.getDbHandler();
        int allWords = dbHandler.getFilteredDictionaryCount(filterType, tfCurrFilter.getText());
        maxPage = (int) Math.ceil(allWords / limit);
        if (currPage > maxPage) {
            currPage = maxPage;
            tfCurrPage.setText(""+ currPage);
        }
        offset = limit * (currPage - 1);
        maxPageLabel.setText("Max page: " + maxPage);
    }

    //
//    public void addStorageMenu(JMenuBar mb) {
//
//        JMenu mnuStorage = new JMenu("Storage");
//
//        // options in Storage Menu
//        JMenuItem option = new JMenuItem("Save Dictionary");
//        option.setIcon( getImage("save.gif"));
//        option.setAccelerator( KeyStroke.getKeyStroke("F2"));
//        mnuStorage.add(option);
//        option.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                boolean result = Dictionary.saveToDisk();
//                if (result) {
//                    JOptionPane.showMessageDialog(MenuFrame.this, "Saved Dictionary Successfully!", "Feedback",
//                            JOptionPane.INFORMATION_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(MenuFrame.this, "Could Not Save Dictionary Successfully! Error --> " + Dictionary.getMessage(), "Feedback",
//                            JOptionPane.INFORMATION_MESSAGE);
//                }
//            }
//        });
//
//
//        option = new JMenuItem("Load Dictionary");
//        option.setIcon( getImage("load.gif"));
//        option.setAccelerator( KeyStroke.getKeyStroke("F3"));
//        mnuStorage.add(option);
//        option.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                boolean result = Dictionary.loadFromDisk();
//                if (result) {
//                    JOptionPane.showMessageDialog(MenuFrame.this, "Loaded Dictionary Successfully!", "Feedback",
//                            JOptionPane.INFORMATION_MESSAGE);
//                } else {
//                    JOptionPane.showMessageDialog(MenuFrame.this, "Could Not Load Dictionary Successfully! Error --> " + Dictionary.getMessage(), "Feedback",
//                            JOptionPane.INFORMATION_MESSAGE);
//                }
//            }
//        });
//
//        mb.add(mnuStorage);
//
//    }

    public static void main(String[] args) throws Exception {
        MenuFrame f = new MenuFrame();
        f.setVisible(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

    }
}
