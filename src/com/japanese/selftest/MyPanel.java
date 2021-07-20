package com.japanese.selftest;

import jaco.mp3.player.MP3Player;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


public class MyPanel extends JPanel implements KeyListener{

    static BufferedImage img;

    String cover = "res/imgs/a_h_a.png"; // 首頁(封面圖)
    static String startAlphabet;
    static String endAlphabet;

    // 指定範圍的圖檔與音檔
    static ArrayList<String> rangedAudiolist = new ArrayList<>();
    static ArrayList<String> rangedImagelist = new ArrayList<>();

    // fire button on enter
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER: // 設定範圍 (圖檔&音檔)
                setAlphabetRange();
                break;
            case KeyEvent.VK_SPACE:
                // System.out.println("播放圖檔 (switch case)");
                setImage();
                updateUI();
                break;
        }
    }

    public MyPanel (){
        setSize(600,550);
        setVisible(true);
        // set component Font
        componentsSetFont();
        // set components
        addComponentsToPanel();
        // 先載入第一張 png
        loadImage(cover); // 相對路徑字串
        // 點panel刷圖檔(算多的)
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setImage();
                // 更新ui
                updateUI();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frm = new JFrame("點擊切換 字卡");
                frm.setSize(600,550);
                frm.setVisible(true);
                frm.setFocusable(true);
                frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frm.setLocationRelativeTo(null); // center the window
                frm.add(new MyPanel()); // Panel 初始化 (click 觸發圖檔)
                // space 觸發音檔
                frm.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                           System.out.println("Hi from KeyListener (main)");
                           playAudio();
                        }
                    }
                });
            }
        });
    }



    private static void playAudio(){
        // 產生隨機數字
        int min = 1;
        int max = rangedAudiolist.size();
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        // 點一下，就get a random index
        String pathWzName = rangedAudiolist.get(random_int-1); // index從0 開始
        // String bip = "res/audio/a.mp3";
        File file = new File(pathWzName);
        MP3Player mp3Player = new MP3Player(file);
        mp3Player.play();
    }

    private static void setImage() {
        // 產生隨機數字
        int min = 1;
        int max = rangedImagelist.size();
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        // 點一下，就get a random index
        String pathWzName = rangedImagelist.get(random_int-1); // index從0 開始
        // 載入檔案
        loadImage(pathWzName);
    }
    private static void loadImage(String imgPath){
        try {
            // img = getImage2(imgPath);
            InputStream is = new FileInputStream(imgPath);
            img = ImageIO.read(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }










    // 宣告 components (for Panel)
    JTextField jt = new JTextField(2);
    JLabel jl = new JLabel("起:");
    JTextField jt2 = new JTextField(2);
    JLabel jl2 = new JLabel("迄:");
    JLabel jl3 = new JLabel(" - ");
    JLabel jl4 = new JLabel(" 例如: ka - so ");
    JButton btn = new JButton("設定範圍 / 下一張");

    private void setAlphabetRange() {
        System.out.println("fire button on enter");
        // 取得指定的頭尾
        String from = StringUtils.trimToNull(jt.getText());
        String to = StringUtils.trimToNull(jt2.getText());
        startAlphabet = from;
        endAlphabet = to;
        int start = myMap.get(startAlphabet); // 不指定 就註解掉
        int end = myMap.get(endAlphabet);
        System.out.print("start to end: " + startAlphabet + " to " + endAlphabet);
        System.out.println(" ("+start+ " to "+end+")");

        // 設定音檔範圍:
        File folder = new File("res/audio");
        File[] listOfAudioFiles = folder.listFiles();
        // by default sorts pathnames lexicographically. If you want to sort them differently, you can define your own comparator.
        Arrays.sort(listOfAudioFiles); // 按字典順序
        // 把音檔路徑，存入list
        for (int i = start; i < (end+1); i++) {
            System.out.print(i + ", "); // 5-14
            if (listOfAudioFiles[i].isFile()) {
                System.out.println("audio File " + listOfAudioFiles[i].getName());
                rangedAudiolist.add("res/audio/"+listOfAudioFiles[i].getName()); // 結果加到靜態清單
            } else if (listOfAudioFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfAudioFiles[i].getName());
            }
        }

        // 設定圖檔範圍:
        File imgFolder = new File("res/imgs");
        File[] listOfImageFiles = imgFolder.listFiles();
        // by default sorts pathnames lexicographically. If you want to sort them differently, you can define your own comparator.
        Arrays.sort(listOfImageFiles); // 按字典順序
        // 把圖檔路徑，存入list
        for (int i = start; i < (end+1); i++) {
            System.out.print(i + ", "); // 5-14
            if (listOfImageFiles[i].isFile()) {
                System.out.println("image File " + listOfImageFiles[i].getName());
                rangedImagelist.add("res/imgs/"+listOfImageFiles[i].getName()); // 結果加到靜態清單
            } else if (listOfImageFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfImageFiles[i].getName());
            }
        }
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(img, 0, -80, 600, 550, this);
    }

    private static final Map<String, Integer> myMap;
    static {
        Map<String, Integer> aMap = new HashMap<>();
        aMap.put("a",0);
        aMap.put("o",4);
        aMap.put("ka",5);
        aMap.put("ko",9);
        aMap.put("sa",10);
        aMap.put("so",14);
        aMap.put("ta",15);
        aMap.put("to",19);
        myMap = Collections.unmodifiableMap(aMap);
    }

    private void componentsSetFont() {
        // Label 加進 Panel
        Font font = new Font("Serif", Font.BOLD,16);
        jl.setFont(font);
        jl2.setFont(font);
        jt.setFont(font);
        jt2.setFont(font);
        jl3.setFont(font);
        jl4.setFont(font);
        btn.addKeyListener(this); // 必要，keyPressed才能觸發enter,space
        btn.setFont(font);
    }

    private void addComponentsToPanel() {
        // Panel 加進 Label & TextField
        this.add(jl);
        this.add(jt);
        this.add(jl3); // -
        this.add(jl2);
        this.add(jt2);
        this.add(jl4); // 例如: ka - so
        this.add(btn);
    }

    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }
}
