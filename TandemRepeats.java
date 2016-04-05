package tandemrepeats;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

@SuppressWarnings("serial")
public class TandemRepeats extends JPanel {
    JTextArea textArea;
    JTextArea displayArea;

    public TandemRepeats() {
        super(new GridBagLayout());
        GridBagLayout gridbag = (GridBagLayout)getLayout();
        GridBagConstraints c = new GridBagConstraints();

        textArea = new JTextArea();
        textArea.getDocument().addDocumentListener(new MyDocumentListener());
        textArea.getDocument().putProperty("name", "Text Area");
        
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
        textArea.setFont(font);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 30));

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);
        displayScrollPane.setPreferredSize(new Dimension(650, 30));
        
        displayArea.setFont(font);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.gridheight = 2;
        gridbag.setConstraints(scrollPane, c);
        add(scrollPane);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(displayScrollPane, c);
        add(displayScrollPane);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.0;
        c.gridheight = 1;
        c.weighty = 0.0;

        setPreferredSize(new Dimension(700, 200));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    class MyDocumentListener implements DocumentListener {
        final String newline = "\n";

        public void insertUpdate(DocumentEvent e) {
            try {
				updateLog(e, "inserted into");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
        }
        public void removeUpdate(DocumentEvent e) {
            try {
				updateLog(e, "removed from");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
        }
        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events.
        }

        public void updateLog(DocumentEvent e, String action) throws BadLocationException {
        	// clear previous text
        	displayArea.setText("");
        	
        	// display modified compressed string
        	Document doc = (Document)e.getDocument();
            String str = e.getDocument().getText(0, doc.getLength());
            
            String compressedString = compressString(str);
            
            displayArea.append(compressedString);
            displayArea.setCaretPosition(displayArea.getDocument().getLength());
        }
		private String compressString(String string) {
			StringBuilder compressedStringBuilder = new StringBuilder();
			int i = 0, j = 0, n = 0, k = 0, subarray_length = 0, repeat_length = 0, index = 0;
			int z[] = new int[string.length()];
			
			for (i = 0; i < string.length() - 1; i++) {	
				subarray_length = i;
				// decide the length of possible pattern based on index
				n = (i % 2 == 0) ? (((string.length() - i) / 2) + i + 1) : (((string.length() - i) / 2) + i + 1);
				for(j = i + 1; j < n; j++) {

					subarray_length++;
					repeat_length = 0;
					index = subarray_length;

					for(k = i; k < subarray_length; k++) {
						if(string.charAt(k) == string.charAt(index)) {
							repeat_length++;
						}
						else {
							repeat_length = 0;
							break;
						}
						index++;
					}
					if(z[i] < repeat_length) {
						z[i] = repeat_length;
					}
				}
			}

			// save compressed string in StringBuilder
			for(i = 0; i < z.length; i++) {
				if(z[i] > 0) {
					compressedStringBuilder.append("(");
					for(j = i; j < z[i] + i; j++) {
						compressedStringBuilder.append(string.charAt(j));
					}
					compressedStringBuilder.append(")2");
					i = i + (2 * z[i]) - 1;
				}
				else {
					compressedStringBuilder.append(string.charAt(i));
				}
			}
			
			return compressedStringBuilder.toString();
		}
    }

    private static void createAndShowGUI() {
        // create and set up the window.
        JFrame frame = new JFrame("Find Tandem Repeats");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create and set up the content pane.
        JComponent newContentPane = new TandemRepeats();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        // display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
    	createAndShowGUI();
    }
}