import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow implements ActionListener, ChangeListener {
    private final JFrame window;
    private MenuPanel menuPanel;
    private SortingPanel sortingPanel;
    public static final int MAX = 256;
    public static final int MIN = 1;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public MainWindow() {
        window = new JFrame();
        window.setLayout(new BorderLayout(10, 5));
        window.setTitle("Sorting Visualiser");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(1200, 800);
        window.setLocationRelativeTo(null);

        createMenuButtons();
        createVisualiserPanel();
    }

    private void createMenuButtons() {
        menuPanel = new MenuPanel();
        menuPanel.addActionListeners(this);
        menuPanel.addChangeListeners(this);
        window.add(menuPanel.getNorthPanel(), BorderLayout.NORTH);
        window.add(menuPanel.getSouthPanel(), BorderLayout.SOUTH);
    }

    private void createVisualiserPanel() {
        sortingPanel = new SortingPanel();
        window.add(sortingPanel, BorderLayout.CENTER);
    }

    public JFrame getWindow() {
        return window;
    }

    public void show() {
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        window.setUndecorated(true);
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.log(Level.INFO, e.toString());
        if (e.getActionCommand().equals("generate")) {
            sortingPanel.generateArray();
        } else if (e.getActionCommand().equals("upload")) {
            int[] array = menuPanel.getArray();
            menuPanel.getLength().setValue(array.length);
            sortingPanel.setArray(array);
        } else if (e.getActionCommand().equals("start")) {
            sortingPanel.start(menuPanel.getSortingMethod());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSlider) {
            if (((JSlider)e.getSource()).getName().equals("length")) {
                sortingPanel.generateArray(((JSlider) e.getSource()).getValue());
                logger.log(Level.INFO, "Length=" + ((JSlider) e.getSource()).getValue() + ":" + e);
            } else if (((JSlider)e.getSource()).getName().equals("speed")) {
                sortingPanel.setSpeed(((JSlider) e.getSource()).getValue());
                logger.log(Level.INFO, "Speed=" + ((JSlider) e.getSource()).getValue() + " : " + e);
            }
        }
    }
}
