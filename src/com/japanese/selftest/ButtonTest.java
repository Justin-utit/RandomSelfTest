package com.japanese.selftest;

import java.awt.event.*;
import javax.swing.*;


public class ButtonTest extends JPanel implements KeyListener{

    private static final long serialVersionUID = 1L;
    private JLabel msg;
    private JButton obj, ori, pro, ent;

    public ButtonTest() {
        obj = createButton("Object", KeyEvent.VK_O);
        ori = createButton("Oriented", KeyEvent.VK_R);
        pro = createButton("Programming", KeyEvent.VK_P);
        ent = createButton("Enter", KeyEvent.VK_ENTER);
        msg = new JLabel("Status Message");

        add(obj);
        add(ori);
        add(pro);
        add(ent);
        add(msg);
    }

    private JButton createButton(String text, int key) {
        final JButton button = new JButton(text);
        button.setMnemonic(key);
        button.addKeyListener(this);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMessage(button.getText());
            }
        });
        return button;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_O: updateMessage(obj.getText()); break;
            case KeyEvent.VK_R: updateMessage(ori.getText()); break;
            case KeyEvent.VK_P: updateMessage(pro.getText()); break;

            case KeyEvent.VK_ENTER: updateMessage(ent.getText()); break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    private void updateMessage(String text) {
        msg.setText(text);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Button Test");
        frame.setLocation(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ButtonTest());
        frame.pack();
        frame.setVisible(true);
    }
}