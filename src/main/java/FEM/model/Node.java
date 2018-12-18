package FEM.model;


public class Node {

    private double x;
    private double y;
    private double t;
    private boolean boarderContition=false;

    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getT() {
        return t;
    }
    public void setT(double t) {
        this.t = t;
    }
    public boolean isBoarderContition() {
        return boarderContition;
    }
    public void setBoarderContition(boolean boarderContition) {
        this.boarderContition = boarderContition;
    }
}
