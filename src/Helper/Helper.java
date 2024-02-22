package Helper;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Sarychev Aleksey <freddis336@gmail.com>
 */
public class Helper implements KeyListener {

    static int mouseXBound = 1400 / 2;
    static int mouseYBound = 900 / 2;
    static int mouseBoundRadius = 100;
    static int delayPeriod = 60000;
    static int delayTime = 15000;
    static int period = 500;
    static int periodBound1 = 300;
    static int periodBound2 = 1000;
    static int switchTabPeriod = 45000;
    static int count = 0;
    static int killCount = 0;
    static int tabCount = 0;
    static int killTimeout = 60000*190;

    static Robot robot;
    static boolean stop = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AWTException, Exception {

       

        robot = new Robot();

        robot.delay(10000);
        //robot.mouseMove(40, 130);
//        robot.keyPress(KeyEvent.VK_META);
//        robot.keyPress(KeyEvent.VK_TAB);
//        robot.delay(50);
//        robot.keyRelease(KeyEvent.VK_TAB);
//        robot.keyRelease((KeyEvent.VK_META));

        while (true) {

            moveMouseRandomly();
            //moveMouseRandomly();
            moveKeyBoardRandomly();
            moveKeyBoardRandomly();
            robot.delay(period);
            count += period;
//            tryKillUpwork();
            trySwitchTab();
            if (count > delayPeriod) {
                System.out.print("Delay started for " + delayTime);
                Toolkit.getDefaultToolkit().beep();
                count = 0;
                robot.delay(delayTime);
                period = ThreadLocalRandom.current().nextInt(periodBound1, periodBound2);
            }
            if (stop) {
                break;
            }
        }
        System.out.println("Done");
    }

    public static void moveMouseRandomly() {
        int x = ThreadLocalRandom.current().nextInt(mouseXBound - mouseBoundRadius, mouseXBound + mouseBoundRadius);
        int y = ThreadLocalRandom.current().nextInt(mouseYBound - mouseBoundRadius, mouseYBound + mouseBoundRadius);
        robot.mouseMove(x, y);
        int[] keys = {KeyEvent.BUTTON1_MASK};
        int rand = ThreadLocalRandom.current().nextInt(0, keys.length);
        int keyToPress = keys[rand];
        System.out.println(rand);
        robot.mousePress(keyToPress);
        robot.delay(50);
        robot.mouseRelease(keyToPress);
        robot.delay(300);
    }

    private static void moveKeyBoardRandomly() {
        int[] keys = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN};
        int rand = ThreadLocalRandom.current().nextInt(0, keys.length);
        int keyToPress = keys[rand];
        System.out.println(rand);
        robot.keyPress(keyToPress);
        robot.delay(200);
        robot.keyRelease(keyToPress);
        robot.keyRelease(KeyEvent.VK_META);
    }

    private static void trySwitchTab() {
        tabCount += period;
        if (tabCount > switchTabPeriod) {
            System.out.println("Switching tab");
            tabCount = 0;
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.delay(30);
            robot.keyPress(KeyEvent.VK_META);
            robot.delay(50);
            robot.keyPress(KeyEvent.VK_OPEN_BRACKET);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_OPEN_BRACKET);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_META);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.delay(80);
        }
    }

    private static void tryKillUpwork() throws IOException, Exception {
        
        killCount+= period;
        if(killCount < killTimeout)
        {
            return;
        }
        
        String applicationToCheck = "Upwork Helper";
        int pid = 0;
//Running command that will get all the working processes.
        Process proc = Runtime.getRuntime().exec("ps -ax");
        InputStream stream = proc.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
//Parsing the input stream.
        while ((line = reader.readLine()) != null) {
            Pattern pattern = Pattern.compile(applicationToCheck);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                
                System.out.println(line);
                String[] parts = line.split("\\s+");
                String pidPart = parts[0];
                //System.out.println(pidPart);
                pid = Integer.parseInt(pidPart);
                break;
            }
        }
        if(pid != 0)
        {
            String cmd = "kill "+pid;
            System.out.println(cmd);
            Runtime.getRuntime().exec(cmd);
        }
        throw new Exception("Work is done");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
