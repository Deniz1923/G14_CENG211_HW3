package models;

public class Position {
    private int x;
    private int y;
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position(Position otherPosition){
        if(otherPosition != null){
            this.x = otherPosition.x;
            this.y = otherPosition.y;
        }
    }
    public Position(){
        this(0,0);
    }

    public void setPositionX(int x){
        if(x >= 0){
            this.x = x;
        }
    }
    public void setPositionY(int y){
        if(y >= 0){
            this.y = y;
        }
    }

    public Position getPosition(){
        return new Position(this.x, this.y);
    }
    // Add these to Position.java
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public String displayPosition(){
        return "X: " + x + " Y: " + y;
    }
}
