package org.danielegiulianini.puzzled.commons;

public class Position { 

    private double x, y;

    public Position() {}
    
    public Position(double x,double y){
        this.x = x;
        this.y = y;
    }

    public void change(double x, double y){
    	this.x = x;
    	this.y = y;
    }
    
    public double getX() {
    	return x;
    }

    public double getY() {
    	return y;
    }
    
    @Override public String toString() {
    	return "[" + getX() +", "+getY() + "]";
    }
}
