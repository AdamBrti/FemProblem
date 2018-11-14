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
    private double cos;
    // TODO k powinno byc prawdopodobnie jako double
    private Integer k;
    private Integer t;


    public DataFromFile setData() {

        File jsonInputFile = new File("src\\main\\resources\\plik.json");
        InputStream is;
        DataFromFile dataFromFile = new DataFromFile();


        try {
            is = new FileInputStream(jsonInputFile);
            JsonReader reader = Json.createReader(is);

            JsonObject empObj = reader.readObject();

            reader.close();


            dataFromFile.setH(empObj.getInt("H"));
            dataFromFile.setL(empObj.getInt("L"));
            dataFromFile.setnH(empObj.getInt("nH"));
            dataFromFile.setnL(empObj.getInt("nL"));
            dataFromFile.setK(empObj.getInt("K"));
            dataFromFile.setT(empObj.getInt("t"));



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
}
