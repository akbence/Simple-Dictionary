
import model.DictionaryRow;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddWord extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField tfWord;
    private JTextField tfPronunciation;
    private JTextField tfMeaning;
    private JTextField tfSource;
    private JButton btnAdd;

    public AddWord() {
        super("Add Word");

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        tfWord = new JTextField(60);
        tfPronunciation = new JTextField(60);
        tfMeaning = new JTextField(40);
        tfSource = new JTextField(50);
        btnAdd = new JButton("Add Word");
        btnAdd.addActionListener(e -> {
                    if (tfWord.getText().length() > 0 && tfMeaning.getText().length() > 0) {
                        DictionaryRow row = new DictionaryRow(tfWord.getText(), tfPronunciation.getText(), tfMeaning.getText(), tfSource.getText());
                        DbHandler dbHandler = DbHandler.getDbHandler();
                        dbHandler.persistWord(row);
                        JOptionPane.showMessageDialog(AddWord.this, "Added Word Successfully!", "Add Word", JOptionPane.INFORMATION_MESSAGE);
                        tfWord.setText("");
                        tfPronunciation.setText("");
                        tfMeaning.setText("");
                        tfSource.setText("");
                        tfWord.requestFocus();
                    } else {
                        JOptionPane.showMessageDialog(AddWord.this, "Please enter word and meaning!", "Add Word", JOptionPane.ERROR_MESSAGE);
                        tfWord.requestFocus();
                    }
                }
        );

        Container c = getContentPane();
        c.setLayout(gbl);

        // add tfWord
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        c.add(new JLabel("Enter Word :"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        c.add(tfWord);

        // add tfPronunciation
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        c.add(new JLabel("Enter Pronunciation :"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        c.add(tfPronunciation, gbc);

        // add taMeaning
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        c.add(new JLabel("Enter Meaning :"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        c.add(tfMeaning, gbc);


        // add taSource
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        c.add(new JLabel("Enter Source :"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        c.add(tfSource, gbc);


        // add button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        c.add(btnAdd, gbc);

        pack(); // get requried size based on components
    }

}
