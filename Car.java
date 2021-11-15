package Race;

import java.util.*;

public class Car {
    ArrayList<double[]> coords = new ArrayList<>();
    //velocity composed from speed and direction
    double vSpeed = 0;
    double hSpeed = 0;
    
    public Car(){ //not necessarily needed
        
    }
    
    public void createCar(double a, double b, double radius){  //(x - a)^2 + (y - b)^2 = r^2
        for(int i = 0; i < 1300; i++){
            for(int j = 0; j < 700; j++){
                double e = (Math.pow((i-a), 2) + Math.pow((j-b), 2));
                if(e < Math.pow(radius, 2)){
                    double[] coord = {i, j};
                    coords.add(coord);
                }
            }
        }
    }
}
