package com.japanese.selftest;



import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Program {

    ImageIcon img;

    public Program() {
        img = createImg("res/imgs/test.jpg");
    }
    public void start() {
        JOptionPane.showMessageDialog(null, "Image","",JOptionPane.PLAIN_MESSAGE, img);

    }
    private ImageIcon createImg(String path) {
        ImageIcon temp = new ImageIcon(Toolkit.getDefaultToolkit().getClass().getResource(path));
        return temp;
    }
    public static void main(String[] args) {
        Program program = new Program();
        program.start();
    }

}