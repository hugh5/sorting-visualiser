import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.logging.Logger;

public class MenuPanel {
    private JPanel northPanel;
    private JPanel southPanel;
    private JButton generate;
    private JSlider length;
    private JLabel lengthLabel;
    private JComboBox<SortingMethod> sortingMethod;
    private JButton upload;
    private JTextField setArray;

    private JButton start;
    private JButton forward;
    private JButton backward;
    private JSlider speed;
    private JLabel speedLabel;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public MenuPanel() {
        createNorthPanel();
        createSouthPanel();
    }

    private void createNorthPanel() {
        northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        URL generateURL = getClass().getResource("assets/random.png");
        if (generateURL != null) {
            ImageIcon generateIcon = new ImageIcon(generateURL);
            generateIcon = new ImageIcon(generateIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            generate = new JButton("New Array", generateIcon);
        } else {
            generate = new JButton("New Array");
        }
        northPanel.add(generate);

        length = new JSlider(MainWindow.MIN, MainWindow.MAX);
        northPanel.add(length);
        length.setValue(MainWindow.MAX / 2);

        lengthLabel = new JLabel();
        northPanel.add(lengthLabel);
        lengthLabel.setText("Length:" + length.getValue());

        URL dividerURL = getClass().getResource("assets/divider.png");
        ImageIcon dividerIcon = null;
        if (dividerURL != null) {
            dividerIcon = new ImageIcon(dividerURL);
            dividerIcon = new ImageIcon(dividerIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            northPanel.add(new JLabel(dividerIcon));
        } else {
            northPanel.add(new JLabel("|"));
        }

        URL uploadURL = getClass().getResource("assets/upload.png");
        if (uploadURL != null) {
            ImageIcon uploadIcon = new ImageIcon(uploadURL);
            uploadIcon = new ImageIcon(uploadIcon.getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
            upload = new JButton("Set Array", uploadIcon);
        } else {
            upload = new JButton("Set Array");
        }
        northPanel.add(upload);

        setArray = new JTextField("[15, 7, 2, 26, 55, 56, 47, 11]", 30);
        northPanel.add(setArray);

        if (dividerIcon != null) {
            northPanel.add(new JLabel(dividerIcon));
        } else {
            northPanel.add(new JLabel("|"));
        }

        sortingMethod = new JComboBox<>(SortingMethod.values());
        northPanel.add(sortingMethod);
    }

    private void createSouthPanel() {
        southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

//        URL backURL = getClass().getResource("assets/backward.png");
//        if (backURL != null) {
//            ImageIcon backIcon = new ImageIcon(backURL);
//            backIcon = new ImageIcon(backIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
//            backward = new JButton(backIcon);
//        } else {
//            backward = new JButton("Step Backward");
//        }
//        southPanel.add(backward);

        URL startURL = getClass().getResource("assets/start.png");
        if (startURL != null) {
            ImageIcon startIcon = new ImageIcon(startURL);
            startIcon = new ImageIcon(startIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            start = new JButton(startIcon);
        } else {
            start = new JButton("Start");
        }
        southPanel.add(start);

//        URL forwardURL = getClass().getResource("assets/forward.png");
//        if (forwardURL != null) {
//            ImageIcon forwardIcon = new ImageIcon(forwardURL);
//            forwardIcon = new ImageIcon(forwardIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
//            forward = new JButton(forwardIcon);
//        } else {
//            forward = new JButton("Step Forward");
//        }
//        southPanel.add(forward);

        speed = new JSlider(1, 10);
        southPanel.add(speed);

        speedLabel = new JLabel();
        southPanel.add(speedLabel);
        speedLabel.setText("Speed:" + speed.getValue());
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JSlider getLength () {
        return length;
    }

    public int[] getArray() {
        String[] values = setArray.getText().replace("[","").replace("]","").split(",");
        int[] array = new int[Math.min(values.length, MainWindow.MAX)];
        int i = 0;
        for (String s : values) {
            if (i > MainWindow.MAX) break;
            try {
                array[i++] = Integer.parseInt(s.strip());
            } catch (NumberFormatException ignored) {}
        }
        return array;
    }

    public SortingMethod getSortingMethod() {
        return (SortingMethod) sortingMethod.getSelectedItem();
    }

    public void addChangeListeners(ChangeListener changeListener) {
        length.setName("length");
        length.addChangeListener(e -> lengthLabel.setText("Length:" + length.getValue()));
        length.addChangeListener(changeListener);

        speed.setName("speed");
        speed.addChangeListener(e -> speedLabel.setText("Speed:" + speed.getValue()));
        speed.addChangeListener(changeListener);
    }

    public void addActionListeners(ActionListener actionListener) {
        generate.setActionCommand("generate");
        generate.addActionListener(actionListener);
        upload.setActionCommand("upload");
        upload.addActionListener(actionListener);
        setArray.setActionCommand("upload");
        setArray.addActionListener(actionListener);

        start.setActionCommand("start");
        start.addActionListener(actionListener);
//        backward.setActionCommand("backward");
//        backward.addActionListener(actionListener);
//        forward.setActionCommand("forward");
//        forward.addActionListener(actionListener);
    }
}
