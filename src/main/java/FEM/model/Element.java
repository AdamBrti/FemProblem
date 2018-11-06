package model;

public class Element {


    private Integer[] id;
    private Double k;

    public Integer[] getId() {
        for(int i=0;i<4;i++)System.out.print(this.id[i]+ " ");
        return id;
    }

    public void setId(Integer[] id) {
        this.id = id;
    }

    public Double getK() {
        return k;
    }

    public void setK(Double k) {
        this.k = k;
    }
}
