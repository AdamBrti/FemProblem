package FEM.FileOperation;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataFromFile {
    private Integer h;
    private Integer l;
    private Integer nH;
    private Integer nL;
    // TODO k powinno byc prawdopodobnie jako double
    private Integer k;
    private Integer t;

    private double temperatur;
    private double tau;
    private double dtau;
    private double ambT;
    private double alfa;
    private double _H;
    private double _B;
    private Integer N_H;
    private Integer N_B;
    private double specHeat;
    private double conductivity;
    private double density;

    public DataFromFile() {
        temperatur = 100;
        tau = 500;
        dtau = 50;
        ambT = 1200;
        alfa = 300;
        _H = 0.100;
        _B = 0.100;
        N_H = 6;
        N_B = 4;
        specHeat = 700;
        conductivity = 25;
        density = 7800;
    }

    public DataFromFile setData() {

        File jsonInputFile = new File("src\\main\\resources\\plik.json");
        InputStream is;
        DataFromFile dataFromFile = new DataFromFile();


        try {
            is = new FileInputStream(jsonInputFile);
            JsonReader reader = Json.createReader(is);

            JsonObject empObj = reader.readObject();

            reader.close();

            //vol 1
           /*//* dataFromFile.setH(empObj.getInt("H"));
            dataFromFile.setL(empObj.getInt("L"));
            dataFromFile.setnH(empObj.getInt("nH"));
            dataFromFile.setnL(empObj.getInt("nL"));
            dataFromFile.setK(empObj.getInt("K"));
            dataFromFile.setT(empObj.getInt("t"));*//*

            //vol 2
            *//*dataFromFile.setH(empObj.getInt("t"));
            dataFromFile.setL(empObj.getInt("tau"));
            dataFromFile.setnH(empObj.getInt("dtau"));
            dataFromFile.setnL(empObj.getInt("ambT"));
            dataFromFile.setK(empObj.getInt("alfa"));
            dataFromFile.setT(empObj.getInt("H_"));
            dataFromFile.setT(empObj.getInt("B"));
            dataFromFile.setT(empObj.getInt("N_H"));
            dataFromFile.setT(empObj.getInt("N_B"));
            dataFromFile.setT(empObj.getInt("specHeat"));
            dataFromFile.setT(empObj.getInt("conductivity"));
            dataFromFile.setT(empObj.getInt("density"));*/


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataFromFile;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getL() {
        return l;
    }

    public void setL(Integer l) {
        this.l = l;
    }

    public Integer getnH() {
        return nH;
    }

    public void setnH(Integer nH) {
        this.nH = nH;
    }

    public Integer getnL() {
        return nL;
    }

    public void setnL(Integer nL) {
        this.nL = nL;
    }

    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public double getTemperatur() {
        return temperatur;
    }

    public void setTemperatur(double temperatur) {
        this.temperatur = temperatur;
    }

    public void setT(double t) {
        temperatur = t;
    }

    public double getTau() {
        return tau;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public double getDtau() {
        return dtau;
    }

    public void setDtau(double dtau) {
        this.dtau = dtau;
    }

    public double getAmbT() {
        return ambT;
    }

    public void setAmbT(double ambT) {
        this.ambT = ambT;
    }

    public double getAlfa() {
        return alfa;
    }

    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }

    public double get_H() {
        return _H;
    }

    public void set_H(double _H) {
        this._H = _H;
    }

    public double get_B() {
        return _B;
    }

    public void set_B(Integer _B) {
        this._B = _B;
    }

    public Integer getN_H() {
        return N_H;
    }

    public void setN_H(Integer n_H) {
        N_H = n_H;
    }

    public Integer getN_B() {
        return N_B;
    }

    public void setN_B(Integer n_B) {
        N_B = n_B;
    }

    public double getSpecHeat() {
        return specHeat;
    }

    public void setSpecHeat(double specHeat) {
        this.specHeat = specHeat;
    }

    public double getConductivity() {
        return conductivity;
    }

    public void setConductivity(double conductivity) {
        this.conductivity = conductivity;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }
}
