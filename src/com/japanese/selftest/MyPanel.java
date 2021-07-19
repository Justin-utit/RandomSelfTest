package com.japanese.selftest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyPanel extends JPanel {

    BufferedImage img;

    String img1 = "res/imgs/h_a.png";
    String img2 = "res/imgs/h_i.png";
    String img3 = "res/imgs/h_u.png";

    boolean evenClick = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frm = new JFrame();
                frm.setSize(600,550);
                frm.setVisible(true);

                frm.setFocusable(true);

                frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frm.add(new MyPanel());

                frm.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                            System.out.println("Hi from KeyListener");
                        }
                    }
                });

            }
        });
    }

    public MyPanel (){
        setSize(600,550);
        setVisible(true);

        // 先載入第一張 png
        loadImage(img1); // 相對路徑字串


        File folder = new File("res/imgs");
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> fileNamelist = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                fileNamelist.add("res/imgs/"+listOfFiles[i].getName());



            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }


//        this.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                System.out.println("keyTyped: '" + e.getKeyChar() + "'");
//            }
//        });


        /*
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 產生隨機數字
                int min = 1;
                int max = fileNamelist.size();
                int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);

                // 點一下，就get a random index
                String pathWzName = fileNamelist.get(random_int-1); // index從0 開始

                // 載入檔案
                loadImage(pathWzName);

                // 更新ui
                updateUI();
            }
        });
        */

    }

    private void loadImage(String imgPath){
        try {
            img = getImage2(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private BufferedImage getImage2(String res) throws IOException {
        InputStream is = new FileInputStream(res);
        BufferedImage img = ImageIO.read(is);
        is.close();
        return img;
    }


    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(img, 0, 0, 600, 550, this);
    }


}
