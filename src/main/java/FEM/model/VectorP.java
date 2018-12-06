package FEM.model;

import FEM.FileOperation.DataFromFile;

public class VectorP extends MatrixHBC {

    private double[] warunekbrzegowy = {1, 1, 0, 0};
    private DataFromFile dataFromFile;


    public VectorP(DataFromFile dataFromFile) {
        this.dataFromFile = dataFromFile;
    }

    public void buildVectorP() {
        double alfa = dataFromFile.getAlfa();
        double ambT = dataFromFile.getAmbT();
        //TODO - chwilowo na sztywno
        double vector[]=new double[4];

        for (int i = 0; i < 4; i++) {
            vector[i]=alfa*warunekbrzegowy[i]*ambT*(0.03333333/2.0);
            System.out.print(vector[i]+"  ");
        }

    }


}
