package com.japanese.selftest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class ac1
{
    public static void main(String args[])
    {
        JFrame f=new JFrame();
        f.setVisible(true);
        f.setSize(400,400);
        f.setLayout(new FlowLayout());

        final JButton b=new JButton("button");
        f.add(b);

        f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"clickButton");

        f.getRootPane().getActionMap().put("clickButton",new AbstractAction(){
            public void actionPerformed(ActionEvent ae)
            {
                b.doClick();
                System.out.println("button clicked");
            }
        });
    }
}