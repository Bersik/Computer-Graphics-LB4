/**
 * Created by Bersik on 16.03.2015.
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Image extends JPanel {
    public static int cx,cy;
    private BufferedImage imag;
    private Graphics2D graphics;
    private int height;
    private int width;
    private Color color;
    int rankFinal;
    int sleep;
    Thread workThread;

    int zx,zy;
    int height_,width_;

    public void setColor(Color color){
        this.color = color;
        graphics.setColor(color);
    }

    public Image(int width, int height,int height_,int width_) {
        super();
        this.width=width;
        this.height=height;
        setPreferredSize(new Dimension(width, height));
        imag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = imag.createGraphics();
        color = Color.BLACK;
        this.height_ = height_;
        this.width_ = width_;
        clear();
    }

    public void setNewSize(int width,int height){
        this.width=width;
        this.height=height;
        zx = zy = 0;
        setPreferredSize(new Dimension(width, height));
        imag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = imag.createGraphics();
        clear();
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imag, 0, 0, this);
    }

    public void clear() {
        graphics.setColor(Color.white);
        graphics.fillRect(-cx, -cy, width, height);
        graphics.setColor(color);
        this.repaint();
        zx = zy = 0;
    }

    public void clearAndStop(){
        stop();
        clear();
    }

    public void drawFractalKoch(int generatorCount,int sleep) {
        final int x1=10,
                y1=this.height-10,
                x2=this.width-10,
                y2=this.height-10;
        this.sleep = sleep;
        this.rankFinal = generatorCount;
        clearAndStop();
        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Koch(x1,y1,x2,y2,0,0);
            }
        });
        workThread.start();
    }

    public void stop() {
        if (workThread!=null) {
            workThread.stop();
            try{workThread.join();}catch (Exception e){}
        }
    }

    private void fractalKoch(int x1, int y1, int x5, int y5,int lev){
        int deltaX, deltaY, x2, y2, x3, y3, x4, y4;
        if (lev == 0){
            try{Thread.sleep(30);}catch (Exception e){}
            this.graphics.drawLine(x1, y1, x5, y5);
            this.repaint();
        }
        else{
            deltaX = x5 - x1;
            deltaY = y5 - y1;

            x2 = x1 + deltaX / 3;
            y2 = y1 + deltaY / 3;

            x3 = (int) (0.5 * (x1+x5) + Math.sqrt(3) * (y1-y5)/6);
            y3 = (int) (0.5 * (y1+y5) + Math.sqrt(3) * (x5-x1)/6);

            x4 = x1 + 2 * deltaX /3;
            y4 = y1 + 2 * deltaY /3;

            fractalKoch(x1, y1, x2, y2,lev - 1);
            fractalKoch(x2, y2, x3, y3,lev - 1);
            fractalKoch(x3, y3, x4, y4,lev - 1);
            fractalKoch(x4, y4, x5, y5, lev - 1);
        }
    }

    private void Koch(int x1, int y1, int x2, int y2,int rankCurrent,double rotate)
    {
        if (rankCurrent == 0)
        {
            graphics.drawLine(x1, y1, x2, y2);
            try{Thread.sleep(sleep*6);}catch (Exception e){}
            Koch(x1, y1, x2, y2,rankCurrent + 1,0);
            this.repaint();
            return;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;

        // 13 - 1/3-part
        // 23 - 2/3-part
        int x13 = x1 + dx / 3;
        int y13 = y1 + dy / 3;
        int x23 = x1 + dx * 2 / 3;
        int y23 = y1 + dy * 2 / 3;

        // Middle points
        int mx = x1 + dx / 2;
        int my = y1 + dy / 2;

        // Third triangle point
        double a = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) / 3.0;
        double h = a * Math.sqrt(3) / 2.0;
        double xBeforeRotate = mx - mx; // It's vertical first
        double yBeforeRotate = my - h - my;

        // Rotating it to normal position
        int x = (int)(xBeforeRotate * Math.cos(rotate) - yBeforeRotate * Math.sin(rotate)) + mx;
        int y = (int)(xBeforeRotate * Math.sin(rotate) + yBeforeRotate * Math.cos(rotate)) + my;

        // Removing old part of the line
        graphics.setColor(Color.white);
        graphics.drawLine(x1, y1, x2, y2);
        graphics.setColor(color);

        // Drawing 4 needed lines
        graphics.drawLine(x1, y1, x13, y13);
        graphics.drawLine(x13, y13, x, y);
        graphics.drawLine(x, y, x23, y23);
        graphics.drawLine(x23, y23, x2, y2);
        this.repaint();
        // Showing what we built
        try{Thread.sleep(sleep*4);}catch (Exception e){}

        // Build next rank
        if (rankCurrent < rankFinal)
        {
            Koch(x1, y1, x13, y13, rankCurrent + 1, rotate);
            this.repaint();
            try{Thread.sleep(sleep);}catch (Exception e){}

            Koch(x13, y13, x, y, rankCurrent + 1, rotate - Math.PI / 3.0);
            this.repaint();
            try{Thread.sleep(sleep);}catch (Exception e){}

            Koch(x, y, x23, y23, rankCurrent + 1, rotate + Math.PI / 3.0);
            this.repaint();
            try{Thread.sleep(sleep);}catch (Exception e){}

            Koch(x23, y23, x2, y2, rankCurrent + 1, rotate);
            this.repaint();
            try{Thread.sleep(sleep);}catch (Exception e){}

        }

    }

    public void drawMandelbrot(int accuracy) {
        clearAndStop();
        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Mandelbrot(accuracy,0,0,width,height);
            }
        });
        workThread.start();
    }

    public void drawMandelbrot(int accuracy,int x1,int y1,int width,int height) {
        stop();
        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Mandelbrot(accuracy, x1, y1, x1 + width, y1 + height);
            }
        });
        workThread.start();
    }

    public void drawMandelbrotZoom(int accuracy,int x1,int y1,int x2,int y2){
        stop();

        double k = width / (double)(x2-x1);
        double x1_ = zx + x1 * k;
        double y1_ = zy + y1 * k;
        double x2_ = x1_ + width_;
        double y2_ = y1_ + height_;
        zx +=x1_;
        zy +=y1_;
        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                MandelbrotZoom(accuracy, (int) x1_, (int) y1_, (int) x2_, (int) y2_);
            }
        });
        workThread.start();
    }

    public void Mandelbrot(int accuracy,int x1,int y1,int x2,int y2)
    {
        int depth = accuracy;
        double cx = width/2;
        double cy = height/2;

        // Going through square with side equal to SizeY
        /*wight / 2 - height / 2*/

        for (int i = x1; i < x2; i++)
        {
            for (int j = y1; j < y2; j++)
            {
                // Checking current point for belongin to the Mandelbrot's set
                // C = x + iy
                double x = (i - cx) / (double)cy * 1.5;
                double y = (j - cy) / (double)cy * 1.5;

                double zRe = 0, zIm = 0;
                boolean belongToSet = true;
                for (int k = 0; k < depth; k++)
                {
                    // Calculating Zn = Zn-1^2 + C
                    double buf = zRe * zRe  - zIm * zIm  + x;
                    zIm = 2 * zRe * zIm + y;
                    zRe = buf;
                    if ((4 < zRe * zRe + zIm * zIm) )
                    {
                        belongToSet = false;
                        break;
                    }
                }
                if (belongToSet)
                    imag.setRGB(i, j, color.getRGB());
                //imag.setRGB(i, j, iter | (iter << 8));
            }
            this.repaint();
        }
    }

    public void MandelbrotZoom(int accuracy,int x1,int y1,int x2,int y2)
    {
        int depth = accuracy;
        double cx = width/2;
        double cy = height/2;

        // Going through square with side equal to SizeY
        /*wight / 2 - height / 2*/

        for (int i = x1; i < x2; i++)
        {
            for (int j = y1; j < y2; j++)
            {
                // Checking current point for belongin to the Mandelbrot's set
                // C = x + iy
                double x = (i - cx) / (double)cy * 2;
                double y = (j - cy) / (double)cy * 2;

                double zRe = 0, zIm = 0;
                boolean belongToSet = true;
                int iter = accuracy;
                for (int k = 0; k < depth; k++)
                {
                    // Calculating Zn = Zn-1^2 + C
                    double buf = zRe * zRe - zIm * zIm + x;
                    zIm = 2 * zRe * zIm + y;
                    zRe = buf;
                    iter--;

                    if ((4 < zRe * zRe + zIm * zIm) )
                    {
                        break;
                    }
                }
                imag.setRGB(i - zx, j - zy, iter | (iter << 8));
            }
            this.repaint();
        }
    }

    public void drawSierpinskiCarpet(int value, int sleepTimeValue){
        clearAndStop();
        sleep = sleepTimeValue;
        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                graphics.setColor(color);
                graphics.fillRect(0, 0, width, height);
                graphics.setColor(Color.white);
                rankFinal = (int) (width /Math.pow(3,value));
                sierpinskiCarpet(0, 0, width, height);
            }
        });
        workThread.start();
    }

    private void sierpinskiCarpet(int xTL, int yTL, int width, int height) {

        if (width>rankFinal && height>rankFinal) {
            int w=width/3, h=height/3;
            graphics.fillRect( xTL+w, yTL+h, w, h );
            this.repaint();
            try{Thread.sleep(sleep);}catch (Exception e){}
            for (int k=0;k<9;k++) if (k!=4) {
                int i=k/3, j=k%3;
                sierpinskiCarpet(xTL + i * w, yTL + j * h, w, h); // recursion
            }
        }
    }

    public void saveToFile() {
        File file = new File("images");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            File outputfile = new File(String.format("images/%s.png",dateFormat.format(new Date())));
            ImageIO.write(imag, "png", outputfile);
        } catch (IOException e) {

        }
    }

    public void drawMandelbrotCloud(){
        clearAndStop();
        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                drawClouds();
            }
        });
        workThread.start();
    }

    private void drawClouds() {
        double cx = width/2;
        double cy = height/2;
        double SX = 0.005;
        double SY = 0.005;
        double DX = -cx;
        double DY = -cy;
        int COUNT_ITER = 10000;
        int BAIL_OUT = 4;
        int STEP_X = 1;
        int STEP_Y = 1;

        for (int i = 0; i < width; i += STEP_X) {
            for (int j = 0; j < height; j += STEP_Y) {
                double c = SX * (i + DX);
                double d = SY * (j + DY);
                double x = c;
                double y = d;
                double t;
                int k = 0;
                graphics.setColor(new Color((float) Math.random(),
                        (float) Math.random(), (float) Math.random()));
                this.repaint();
                while (x * x + y * y < BAIL_OUT && k < COUNT_ITER) {
                    t = x * x - y * y + c;
                    y = 2 * x * y + d;
                    x = t;
                    graphics.drawOval((int) (x / SX - DX), (int) (y / SY - DY), 1, 1);
                    this.repaint();
                    ++k;
                }
            }
        }
    }

}