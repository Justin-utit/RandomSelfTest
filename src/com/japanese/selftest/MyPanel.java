package com.japanese.selftest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyPanel extends JPanel {

    BufferedImage img;

    String img1 = "res/imgs/h_a.png";
    String img2 = "res/imgs/h_i.png";
    boolean evenClick = false;

    public MyPanel (){
        setSize(600,400);
        setVisible(true);
        loadImage(img1);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(evenClick){
                    loadImage(img1);
                    evenClick = false;
                } else {
                    loadImage(img2);
                    evenClick = true;
                }

                updateUI();
            }
        });
    }

    private BufferedImage getImage2(String res) throws IOException {
        InputStream is = new FileInputStream(res);
        BufferedImage img = ImageIO.read(is);
        is.close();
        return img;
    }

    private void loadImage(String imgPath){
        try {
            // "/imgs/h_a.png"
            // res/imgs/h_a.png

//            img = ImageIO.read(MyPanel.class.getResource(imgPath));

             img = getImage2(imgPath);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(img, 0, 0, 600, 400, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frm = new JFrame();
                frm.setSize(600,500);
                frm.setVisible(true);
                frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frm.add(new MyPanel());
            }
        });
    }
}
