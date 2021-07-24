package com.japanese.selftest;

import jaco.mp3.player.MP3Player;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;


public class MyPanel extends JPanel implements KeyListener{

    static BufferedImage img;

    String cover = "res/imgs/a_h_a.png"; // 首頁(封面圖)
    static String startAlphabet;
    static String endAlphabet;

    // 指定範圍的圖檔與音檔
    static ArrayList<String> rangedAudiolist = new ArrayList<>();
    static ArrayList<String> rangedImagelist = new ArrayList<>();

    static int width = 650;
    static int height = 750;

    private static String pronunciation; // 念不出來時的發音提示
    private static String imageOfCurrentSound; // 寫不出來時的圖像提示

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
                playSingleAudio("res/swish1.mp3");
                updateUI();
                break;
            case KeyEvent.VK_H: // 右手順
                playSingleAudio(pronunciation); // "res/audio/d_e.mp3"
                break;
        }
    }



    public MyPanel (){
        setSize(width,height);
        setVisible(true);
        // set component Font
        componentsSetFont();
        // set components
        addComponentsToPanel();
        // 先載入第一張 png
        loadImage(cover); // 相對路徑字串

        // 點panel: 聽到字寫不出來時，點中間可以看到現在念的字。
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 顯示當前發音的字樣
                // System.out.println("imageOfCurrentSound: " + imageOfCurrentSound);
                loadImage(imageOfCurrentSound);
                playSingleAudio("res/swish1.mp3");
                // 更新ui
                updateUI(); // 這個只有一般的方法，無法從static的方法呼叫
            }
        });
    }

    static long programStartTime;

    public static void main(String[] args) {
        programStartTime = System.nanoTime(); // 記錄程式啟動時間
        // 關程式時，記錄程式使用時間
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("In shutdown hook");
                long timeSpentInMin = getTimeSpentInMin();
                writeLogs(timeSpentInMin);
            }
        }, "Shutdown-thread"));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frm = new JFrame("【暴力記憶】");
                frm.setSize(width,height);
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
//                           System.out.println("Hi from KeyListener (main)");
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

//        System.out.println(max);
        if(max==0) { // 提示先設範圍
            JOptionPane.showMessageDialog(null, label, "InfoBox: playAudio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        // 點一下，就get a random index
        String audioPathWzName = rangedAudiolist.get(random_int-1); // index從0 開始
        String imagePathWzName = rangedImagelist.get(random_int-1);
        imageOfCurrentSound = imagePathWzName; // 聽寫發音時，該發音對應的圖會存起來

        System.out.println(audioPathWzName);
        playSingleAudio(audioPathWzName);
    }

    private static void playSingleAudio(String pathWzName) {
        File file = new File(pathWzName);
        MP3Player mp3Player = new MP3Player(file);
        mp3Player.play();
    }

    private static void setImage() {
        // 產生隨機數字
        int min = 1;
        int max = rangedImagelist.size();

//        System.out.println(max);
        if(max==0) { // 提示先設範圍
            JOptionPane.showMessageDialog(null, label, "InfoBox: setImage()", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        // 點一下，就get a random index
        String pathWzImageName = rangedImagelist.get(random_int-1); // index從0 開始
        String pathWzAudioName = rangedAudiolist.get(random_int-1);
        System.out.println(pathWzImageName);
//        System.out.println(pathWzAudioName);
        pronunciation = pathWzAudioName; // 念不出時，可以發出該字的音

        // 載入檔案
        loadImage(pathWzImageName);
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

    private void setAlphabetRange() {
        // 先清空舊的清單
        rangedAudiolist.clear();
        rangedImagelist.clear();

//        System.out.println("fire button on enter");
        // 取得指定的頭尾
        String from = StringUtils.trimToNull(jt.getText());
        String to = StringUtils.trimToNull(jt2.getText());

        if(from==null || to ==null) { // 提示先設範圍
            JOptionPane.showMessageDialog(null, label, "InfoBox: setAlphabetRange()", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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

        JLabel label = new JLabel("已設定範圍: " + startAlphabet + " - " +endAlphabet);
        label.setFont(font18);
        JOptionPane.showMessageDialog(null, label, "InfoBox: setAlphabetRange()", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(img, 0, -80, width, height, this);
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

    // 宣告 components (for Panel)
    JLabel emptyLable = new JLabel("     ");
    JLabel jl = new JLabel("起:");
    JTextField jt = new JTextField(2);
    JLabel jl2 = new JLabel("迄:");
    JTextField jt2 = new JTextField(2);
    JLabel jl3 = new JLabel(" - ");
    JLabel jl4 = new JLabel(" 例如: ka - so ");
    JButton btn = new JButton("設定範圍 / 下一張");
    JButton btn2 = new JButton("使用說明");

    Font font16 = new Font("Serif", Font.BOLD,16);
    Font font18 = new Font("Serif", Font.BOLD,18);
    static JLabel label = new JLabel("請先設定範圍");

    private void componentsSetFont() {
        jl.setFont(font16);
        jl2.setFont(font16);
        jt.setFont(font16);
        jt2.setFont(font16);
        jl3.setFont(font16);
        jl4.setFont(font16);
        btn.addKeyListener(this); // 必要，keyPressed才能觸發enter,space
        btn.setFont(font16);

        btn2.setFont(font16);

        label.setFont(font16);
    }

    private void addComponentsToPanel() {

        // Panel 加進 Label & TextField
        btn2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("點下說明!!");

                JLabel label = new JLabel("<HTML>1. 先設定範圍 (例如 ka - so 全小寫)<br>" +
                        "2. 按空白鍵，開始切換字卡 (H鍵發音)<br>" +
                        "3. 單點tab(進發音模式) > 按空白鍵，開始發音 (點中間看發音的字) <br>" +
                        "</HTML>" );
                label.setFont(font16);
                JOptionPane.showMessageDialog(null, label, "InfoBox: 使用說明", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        this.add(btn2);
        this.add(emptyLable);

        this.add(jl);
        this.add(jt);
        this.add(jl3); // -
        this.add(jl2);
        this.add(jt2);
        this.add(jl4); // 例如: ka - so
        // Panel 加進 Label & TextField
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("點下大按鈕!!");
                setAlphabetRange();
            }
        });
        this.add(btn);


    }

    private static void writeLogs(long timeSpentInMin) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());

//      if(minutes>0) { // 打開則滿一分鐘才寫log
        String record = date + " 程式啟動時間: " + timeSpentInMin + " 分鐘";

        Logger logger = Logger.getLogger("MyLog");
        Appender fh = null;
        try {
            fh = new FileAppender(new SimpleLayout(), "MyLogFile.log");
            logger.addAppender(fh);
            fh.setLayout(new SimpleLayout());
            logger.info(record);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//      }
    }

    private static long getTimeSpentInMin() {
        long programEndTime = System.nanoTime();
        long timeElapsed = programEndTime - programStartTime;

        long milliseconds = timeElapsed / 1000000; // 使用之毫秒
//                System.out.println("Execution time in milliseconds: " + milliseconds);
        long minutes = (milliseconds / 1000) / 60; // 使用之分鐘
//                System.out.println("Execution time in minutes: " + minutes);
//                long seconds = (milliseconds / 1000) % 60; // 使用之秒數
//                System.out.println("Execution time in seconds: " + seconds);
        return minutes;
    }

    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }
}
