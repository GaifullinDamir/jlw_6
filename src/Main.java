import javax.swing.*;
import java.util.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
class Test {
    static int count;
    public static void main(String[] args){
        count = 0;
        Balls balls = new Balls();
    }
}
class Balls extends Frame implements Observer, ActionListener, ItemListener {
    private LinkedList FigureList = new LinkedList();
    private Color color;
    private Frame controlWindow;
    private Button createBtn;
    private Button updateNumBtn;
    private Button updateSpeedBtn;
//    private Choice choiceColor; //вЫБОР ЦВЕТА

    private JColorChooser choiceColor;
    private Choice choiceFigure;
    private TextField tfFigure;
    private TextField tfStartSpeed;
    private TextField tfNumFigure;
    private TextField tfNewNumFigure;
    private TextField tfCurrSpeed;
    Balls(){
        this.addWindowListener(new WindowAdapter2());
        controlWindow = new Frame();
        controlWindow.setSize(new Dimension(1200,300));
        controlWindow.setTitle("Контроль");
        controlWindow.setLayout(new GridLayout());
        controlWindow.addWindowListener(new WindowAdapter2());

        choiceColor = new JColorChooser();
        controlWindow.add(choiceColor, new Point(60,20));


        tfFigure = new TextField();
        tfFigure.setText("форма фигуры");
        controlWindow.add(tfFigure);

        tfStartSpeed = new TextField();
        tfStartSpeed.setText("начальная скорость");
        controlWindow.add(tfStartSpeed);

        tfNumFigure = new TextField();
        tfNumFigure.setText("номер фигуры");
        controlWindow.add(tfNumFigure);

        createBtn = new Button("Создать");
        createBtn.setSize(new Dimension(10,40));
        createBtn.setActionCommand("OK");
        createBtn.addActionListener(this);
        controlWindow.add(createBtn, new Point(20,20));

        choiceFigure = new Choice();
        choiceFigure.addItemListener(this);
        controlWindow.add(choiceFigure, new Point(60,20));

        tfNewNumFigure = new TextField();
        tfNewNumFigure.setText("новый номер фигуры");
        controlWindow.add(tfNewNumFigure);

        updateNumBtn = new Button("Изменить номер фигуры");
        updateNumBtn.setSize(new Dimension(10,40));
        updateNumBtn.setActionCommand("UPDATENUM");
        updateNumBtn.addActionListener(this);
        controlWindow.add(updateNumBtn, new Point(20,20));

        tfCurrSpeed = new TextField();
        tfCurrSpeed.setText("новая скорость");
        controlWindow.add(tfCurrSpeed);

        updateSpeedBtn = new Button("Изменить номер фигуры");
        updateSpeedBtn.setSize(new Dimension(10,40));
        updateSpeedBtn.setActionCommand("UPDATESPEED");
        updateSpeedBtn.addActionListener(this);
        controlWindow.add(updateSpeedBtn, new Point(20,20));

        controlWindow.setVisible(true);

        this.setSize(500,200);
        this.setVisible(true);
        this.setLocation(100, 150);
    }
    public void update(Observable o, Object arg) {
//        Figure ball = (Figure)arg;
//        System.out.println ("x= " + ball.thr.getName() + ball.x);
        repaint();
    }
    public void paint (Graphics g) {
        if (!FigureList.isEmpty()){
            for (Object fig : FigureList) {
                Figure figure = (Figure) fig;
                g.setColor(figure.col);
                switch(figure.figure) {
                    case "круг":
                        g.fillOval(figure.x, figure.y, 20, 20);
                        break;
                    case "овал":
                        g.fillOval(figure.x, figure.y, 20, 40);
                        break;
                    case "треугольник":
                        int[] xPoints = {figure.x,figure.x-10,figure.x+10};
                        int[] yPoints = {figure.y,figure.y+20,figure.y + 20};
                        g.fillPolygon(xPoints,yPoints,3);
                        break;
                    case "квадрат":
                        g.fillRect(figure.x, figure.y, 20, 20);
                        break;
                    case "прямоугольник":
                        g.fillRect(figure.x, figure.y, 20, 40);
                        break;
                    default:
                        return;
                }
                g.drawString(Integer.toString(figure.num),figure.x - 10, figure.y + 10);
            }
        }
    }

    public void itemStateChanged (ItemEvent iE) {}
    public void actionPerformed (ActionEvent aE) {
        String str = aE.getActionCommand();
        if (str.equals ("OK")){
            color = choiceColor.getColor();
            int numFigure, startSpeed;
            try {
                numFigure = Integer.parseInt(this.tfNumFigure.getText());
                startSpeed = Integer.parseInt(tfStartSpeed.getText());
            } catch(Exception e) {
                System.out.println(e);
                return;
            }
            for (Object fig : FigureList) {
                Figure figure = (Figure)fig;
                if(figure.num == numFigure) {
                    System.out.println("Такой номер фигуры уже существует");
                    return;
                }
            }
            Figure figure = new Figure(color, numFigure, startSpeed, tfFigure.getText());
            FigureList.add(figure);
            figure.addObserver(this);
//            choiceFigure.addItem(Integer.toString(figure.num));
        }
        else if(str.equals("UPDATENUM")) {
            int newNum;
            try {
                newNum = Integer.parseInt(tfNewNumFigure.getText());
            } catch(Exception e) {
                System.out.println(e);
                return;
            }
            for (Object fig: FigureList) {
                Figure figure = (Figure)fig;
                if(figure.num == newNum) {
                    System.out.println("Такой номер уже существует");
                    return;
                }
            }
            for (Object fig: FigureList) {
                Figure figure = (Figure)fig;
                if(figure.num == Integer.parseInt(tfNumFigure.getText())) {
//                    choiceFigure.remove(Integer.toString(figure.num));
//                    choiceFigure.addItem(Integer.toString(newNum));
                    figure.num = newNum;
                }
            }
        }
        else if(str.equals("UPDATESPEED")) {
            int newSpeed;
            try {
                newSpeed = Integer.parseInt(tfCurrSpeed.getText());
            } catch(Exception e) {
                System.out.println(e);
                return;
            }
            for (Object fig: FigureList) {
                Figure figure = (Figure)fig;
                if(figure.num == Integer.parseInt(tfNumFigure.getText())) {
                    figure.speed = newSpeed;
                }
            }
        }
        repaint();
    }
}
class Figure extends Observable implements Runnable {
    Thread thr;
    private boolean xplus;
    private boolean yplus;
    int x;
    int y;
    int num;
    int speed;
    Color col;
    String figure;
    public Figure (Color col, int num, int speed, String figure) {
        this.figure = figure;
        this.speed = speed;
        this.num = num;
        xplus = true; yplus = true;
        x = 0; y = 30;
        this.col = col;
        Test.count++;
        thr = new Thread(this);
        thr.start();
    }
    public void run(){
        while (true){
            if(x>=475) xplus = false;
            if(x<=-1) xplus = true;
            if(y>=175) yplus = false;
            if(y<=29) yplus = true;
            if(xplus) x+=speed; else x-=speed;
            if(yplus) y+=speed; else y-=speed;
            setChanged();
            notifyObservers (this);
            try{Thread.sleep (1);}
            catch (InterruptedException e){}
        }
    }
}
class WindowAdapter2 extends WindowAdapter {
    public void windowClosing (WindowEvent wE) {System.exit (0);}
}