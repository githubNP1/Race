package Race;

import java.awt.*;
import java.awt.*; 
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;            
import javax.swing.*;

public class Main extends JPanel implements KeyListener{
    Car car = new Car();
    double gravity = 200;  //acceleration due to gravity
    double terminalVerticalVelocity = 300;
    ArrayList<double[]> floor = new ArrayList<>();
   
    public Main(){
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setSize(1320, 750); 
        this.setBackground(Color.white); 
        frame.addKeyListener(this); 
        createFloor();
        car.createCar(100, 100, 5);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        while(true){
            //first check if car is airbourne
            //if not airbourne...
            //apply acceleration to velocity
            //move car
            //cycle every 0.1s
            if(checkIfAirbourne()){ 
                freeFall();
            }
            else{
                //calculate bounce
                //calculate point of contact between car and floor - find gradient of floor
                //calculate downward acceleration
                getNewSpeed();
            }
            displacement();
            repaint();
            try {Thread.sleep(10);} catch (InterruptedException ex) {}
        }    
    }
    
    public void freeFall(){
        car.vSpeed += 0.01 * gravity;
        car.vSpeed = car.vSpeed > terminalVerticalVelocity ? terminalVerticalVelocity : car.vSpeed; 
    }
    
    public void displacement(){ 
        for(double[] coord : car.coords){
            coord[0] += 0.01 * car.hSpeed;
            coord[1] += 0.01 * car.vSpeed;
        }
    }
    
    public Boolean checkIfAirbourne(){ 
        for(double[] coordA : floor){
            for(double[] coordB : car.coords){
                if((int) coordA[0] == (int) coordB[0] && (int) coordA[1] == (int) coordB[1]){ 
                    return false;
                }
            }
        }
        return true;
    }
    
    public double getFloorGradient(){
        double[] a = new double[2];
        for(double[] coordA : floor){
            for(double[] coordB : car.coords){
                if((int) coordA[0] == (int) coordB[0] && (int) coordA[1] == (int) coordB[1]){ 
                    a = coordA;
                }
            }
        }
        ArrayList<double[]> line = new ArrayList<>();
        int index = floor.indexOf(a);
        for(int i = index - 20; i < index + 20; i += 10){
            double[] b = new double[2];
            b[0] = floor.get(i)[0];
            b[1] = floor.get(i)[1];
            line.add(b);
        } 
        double g1 = (line.get(1)[1] - line.get(0)[1])/(line.get(1)[0] - line.get(0)[0]);
        double g2 = (line.get(2)[1] - line.get(1)[1])/(line.get(2)[0] - line.get(1)[0]);;
        return (g1 + g2)/2;     
    }
    
    public double getAcceleration(){
        return gravity * Math.sin(Math.atan(getFloorGradient()));  //need vert and hori components of acceleration
    }
    
    public void getNewSpeed(){ //needs to make new vSpeed and hSpeed and apply acceleration
        double m1 = car.vSpeed / car.hSpeed;
        double angle = Math.atan((m1 - getFloorGradient())/(1 + m1 * getFloorGradient()));
        double carSpeed = Math.sqrt(Math.pow(car.vSpeed, 2) + Math.pow(car.hSpeed, 2)); 
        double nextSpeed = carSpeed * Math.cos(angle); 
        nextSpeed += 0.01 * getAcceleration();
        car.vSpeed = nextSpeed * Math.sin(Math.atan(getFloorGradient()));
        car.hSpeed = nextSpeed * Math.cos(Math.atan(getFloorGradient()));
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g); 
        g.setColor(Color.black); 
        for(double[] coord : floor){ 
            g.drawRect((int) coord[0], (int) coord[1], 1, 1); 
        }
        g.setColor(Color.gray); 
        for(double[] coord : car.coords){
             g.drawRect((int) coord[0], (int) coord[1], 1, 1); 
        }
    }
    
    public void createFloor(){ // y = 0.3x + 200
        for(int i = 0; i < 1300; i++){
            for(int j = 0; j < 700; j++){
                double e = 0.3 * i - j;
                if(e <= -199 && e >= -201){
                    double[] coord = {i, j};
                    floor.add(coord); 
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args){
        new Main();
    }
}
