/**
 * Created by Bersik on 15.03.2015.
 */
import javax.swing.*;
import java.awt.*;

public class Form extends JFrame{
    public Form(int sizeWidth,int sizeHeight){
        super("Lab2");
        Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (sz.width - sizeWidth) / 2;
        int locationY = (sz.height - sizeHeight) / 2;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setBounds(locationX, locationY, sizeWidth, sizeHeight);
        setVisible(true);
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
    }
}

