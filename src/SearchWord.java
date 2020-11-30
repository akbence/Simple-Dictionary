
import model.DictionaryRow;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SearchWord extends JFrame{
	private static final long serialVersionUID = 1L;
    private JTextField tfWord;
    private JTextField  tfMeaning;
    private JTextField tfPronunciation;
    private JTextField tfSource;
    private JButton btnSearch;
    private JScrollPane sp;

    public SearchWord() {
        super("Search Word");

        GridBagLayout gbl  = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;

        tfWord = new JTextField(20);
        tfMeaning = new JTextField();
        tfPronunciation = new JTextField();
        tfSource = new JTextField();
        btnSearch = new JButton("Search");
        btnSearch.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 if (  tfWord.getText().length() > 0 ) {
                     DbHandler dbHandler = DbHandler.getDbHandler();
                     DictionaryRow foundRow = dbHandler.findDictRowByWord(tfWord.getText());
                     if (foundRow != null) {
                         tfMeaning.setText(foundRow.getMeaning());
                         tfPronunciation.setText(foundRow.getPronunciation());
                         tfSource.setText(foundRow.getSource());
                     }
                 else
                       JOptionPane.showMessageDialog( SearchWord.this, "Word  Not Found. Please try again!","Search Word", JOptionPane.INFORMATION_MESSAGE);
                 }
                 else
                     JOptionPane.showMessageDialog( SearchWord.this, "Please enter word from dictionary!","Search Word", JOptionPane.ERROR_MESSAGE);
            }
         }
        );

        Container c = getContentPane();
        c.setLayout(gbl);

        // add tfWord
        gbc.anchor = GridBagConstraints.EAST;
        c.add( new JLabel("Search Word :"),gbc);
        gbc.anchor = GridBagConstraints.WEST;
        c.add(tfWord);
        gbc.anchor = GridBagConstraints.EAST;
        c.add( btnSearch);


        // add tfMeaning
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        c.add( new JLabel("Meaning :"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        tfMeaning.setColumns(30);
        sp = new JScrollPane(tfMeaning, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        c.add(sp, gbc);

        // add tfPronunciation
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        c.add( new JLabel("Pronunciation :"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        tfPronunciation.setColumns(30);
        sp = new JScrollPane(tfPronunciation, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        c.add(sp, gbc);

        // add tfSource
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        c.add( new JLabel("Source :"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        tfSource.setColumns(30);
        sp = new JScrollPane(tfSource, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        c.add(sp, gbc);

        pack(); // get requried size based on components
    }

}
