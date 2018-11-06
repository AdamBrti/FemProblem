package FEM.model;


public class Node {

    private Integer x;
    private Integer y;
    private double t;


    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setT(Integer t) {
        this.t = t;
    }
}
