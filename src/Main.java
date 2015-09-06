/**
 * Created by Bersik on 16.03.2015.
 */
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public final class Main {

    private Image img;
    JScrollPane scroll;
    JSpinner spinnerAccuracy;

    int state = 1;
    boolean scrollUpdateB = false;
    int sizeWidth = 1300;
    int sizeHeight = 682;
    int imgWidth = 1100;
    int imgHeight = 650;

    int x1,y1;

    private void clear(){
        img.clearAndStop();
    }

    public Main(){

        Form form = new Form(sizeWidth, sizeHeight);
        img = new Image(imgWidth, imgHeight,sizeHeight - 29 - 19,sizeWidth - 198 - 19);

        AdjustmentListener adjustmentListener = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (state == 2 && scrollUpdateB) {
                    updateMandelbrot();
                }
            }
        };

        scroll = new JScrollPane(img);
        scroll.setViewportView(img);
        scroll.setBounds(0, 0, sizeWidth - 198, sizeHeight - 29);
        scroll.setPreferredSize(new Dimension(50, 50));
        scroll.getHorizontalScrollBar().addAdjustmentListener(adjustmentListener);
        scroll.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
        form.add(scroll);

        img.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (state == 2) {
                    x1 = e.getX();
                    y1 = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (state == 2) {
                    //img.drawMandelbrotZoom((int) spinnerAccuracy.getValue(), x1, y1, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JLabel label = new JLabel("Кількість поколінь");
        label.setBounds(sizeWidth - 180, 125, 100, 30);
        form.add(label);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel());
        spinner.setValue(1);
        spinner.setBounds(sizeWidth - 80, 130, 40, 20);
        form.add(spinner);

        JLabel labelAccuracy = new JLabel("Точність");
        labelAccuracy.setBounds(sizeWidth - 180, 125, 100, 30);
        labelAccuracy.setVisible(false);
        form.add(labelAccuracy);

        JCheckBox scrollUpdate = new JCheckBox("ScrollUpdate");
        scrollUpdate.setVisible(false);
        scrollUpdate.setBounds(sizeWidth - 180, 150, 100, 30);
        scrollUpdate.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (scrollUpdate.isSelected())
                    scrollUpdateB = true;
                else
                    scrollUpdateB = false;
            }
        });
        form.add(scrollUpdate);

        spinnerAccuracy = new JSpinner(new SpinnerNumberModel());
        spinnerAccuracy.setValue(1000);
        spinnerAccuracy.setVisible(false);
        spinnerAccuracy.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (state == 2 && scrollUpdateB)
                    updateMandelbrot();
            }
        });
        spinnerAccuracy.setBounds(sizeWidth - 100, 130, 60, 20);
        form.add(spinnerAccuracy);

        JLabel label2 = new JLabel("Затритмка");
        label2.setBounds(sizeWidth - 180, 150, 100, 30);
        form.add(label2);

        JSpinner sleepTime = new JSpinner(new SpinnerNumberModel());
        sleepTime.setValue(50);
        sleepTime.setBounds(sizeWidth - 80, 155, 40, 20);
        form.add(sleepTime);


        JButton buttonDraw = new JButton("Намалювати");
        buttonDraw.setBounds(sizeWidth - 190, 190, 160, 30);
        buttonDraw.addActionListener(event -> {
            clear();
            switch (state) {
                case 1:
                    img.drawFractalKoch((int) spinner.getValue(), (int) sleepTime.getValue());
                    break;
                case 2:
                    img.drawMandelbrot((int) spinnerAccuracy.getValue());
                    break;
                case 3:
                    img.drawSierpinskiCarpet((int) spinner.getValue(), (int) sleepTime.getValue());
                    break;
                default:
                    break;
            }
        });
        form.add(buttonDraw);

        JButton buttonDrawClouds = new JButton("Хмара");
        buttonDrawClouds.setBounds(sizeWidth - 190, 270, 160, 30);
        buttonDrawClouds.addActionListener(event -> {
            img.drawMandelbrotCloud();
        });
        form.add(buttonDrawClouds);

        JButton buttonSave = new JButton("Зберегти");
        buttonSave.setBounds(sizeWidth - 190, 230, 160, 30);
        buttonSave.addActionListener(event -> {
            img.saveToFile();
        });
        form.add(buttonSave);

        JRadioButton radioButton1 = new JRadioButton("Крива Коха");
        radioButton1.setBounds(sizeWidth - 160, 30, 140, 30);
        radioButton1.addItemListener(e -> {
            int b_state = e.getStateChange();
            if (b_state == ItemEvent.SELECTED) {
                state = 1;
                spinner.setVisible(true);
                label.setVisible(true);
                label2.setVisible(true);
                sleepTime.setVisible(true);

                spinnerAccuracy.setVisible(false);
                labelAccuracy.setVisible(false);
                scrollUpdate.setVisible(false);
            }
        });
        radioButton1.setSelected(true);
        form.add(radioButton1);

        JRadioButton radioButton2 = new JRadioButton("Мандельброт");
        radioButton2.setBounds(sizeWidth - 160, 60, 140, 30);
        radioButton2.addItemListener(e -> {
            int b_state = e.getStateChange();
            if (b_state == ItemEvent.SELECTED) {
                state = 2;
                spinner.setVisible(false);
                label.setVisible(false);
                label2.setVisible(false);
                sleepTime.setVisible(false);
                spinnerAccuracy.setVisible(true);
                labelAccuracy.setVisible(true);
                scrollUpdate.setVisible(true);

            }
        });
        form.add(radioButton2);

        JRadioButton radioButton3 = new JRadioButton("Серпінський");
        radioButton3.setBounds(sizeWidth - 160, 90, 140, 30);
        radioButton3.addItemListener(e -> {
            int b_state = e.getStateChange();
            if (b_state == ItemEvent.SELECTED) {
                state = 3;
                spinner.setVisible(true);
                label.setVisible(true);
                label2.setVisible(true);
                sleepTime.setVisible(true);
                spinnerAccuracy.setVisible(false);
                labelAccuracy.setVisible(false);
                scrollUpdate.setVisible(false);
            }
        });
        form.add(radioButton3);

        ButtonGroup group = new ButtonGroup();
        group.add(radioButton1);
        group.add(radioButton2);
        group.add(radioButton3);

        JPanel colorPanel = new JPanel();
        colorPanel.setBounds(sizeWidth - 60, sizeHeight - 155, 20, 20);
        colorPanel.setBackground(Color.black);
        form.add(colorPanel);

        final JButton selectColor = new JButton("Вибрати колір");
        selectColor.setBounds(sizeWidth - 180, sizeHeight - 160, 110, 30);
        selectColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Виберіть колір", Color.black);
            if (color != null) {
                colorPanel.setBackground(color);
                img.setColor(color);
            }
        });
        form.add(selectColor);

        final JButton stop = new JButton("Зупинити");
        stop.setBounds(sizeWidth - 180, sizeHeight - 120, 140, 30);
        stop.addActionListener(event -> img.stop());
        form.add(stop);

        final JButton clear = new JButton("Очистити");
        clear.setBounds(sizeWidth - 180, sizeHeight - 80, 140, 30);
        clear.addActionListener(event -> clear());
        form.add(clear);

        JLabel label3 = new JLabel("x");
        label3.setBounds(sizeWidth - 180,sizeHeight - 260, 20, 20);
        form.add(label3);
        JSpinner width = new JSpinner(new SpinnerNumberModel());
        width.setValue(1100);
        width.setBounds(sizeWidth - 170, sizeHeight - 260, 60, 20);
        form.add(width);
        label3 = new JLabel("y");
        label3.setBounds(sizeWidth - 100, sizeHeight - 260, 20, 20);
        form.add(label3);
        JSpinner height= new JSpinner(new SpinnerNumberModel());
        height.setValue(650);
        height.setBounds(sizeWidth - 90, sizeHeight - 260, 60, 20);
        form.add(height);

        final JButton changeSize = new JButton("Змінити розміри");
        changeSize.setBounds(sizeWidth - 180, sizeHeight - 230, 140, 30);
        changeSize.addActionListener(event -> {
            imgWidth = (int) width.getValue();
            imgHeight = (int) height.getValue();
            changeSize(imgWidth, imgHeight);
        });
        form.add(changeSize);

        clear();
    }

    private void updateMandelbrot() {
        img.drawMandelbrot(
                (int) spinnerAccuracy.getValue(),
                scroll.getHorizontalScrollBar().getValue(),
                scroll.getVerticalScrollBar().getValue(),
                scroll.getWidth()-19,
                scroll.getHeight()-19
        );
    }

    private void changeSize(int x,int y) {
        img.setNewSize(x, y);
        scroll.setViewportView(img);
        scroll.setBounds(0, 0, sizeWidth - 198, sizeHeight - 29);
        scroll.setPreferredSize(new Dimension(50, 50));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new  Main();
        });
    }
}
