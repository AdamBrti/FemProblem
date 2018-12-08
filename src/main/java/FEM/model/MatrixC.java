package FEM.model;

import FEM.FileOperation.DataFromFile;

import java.util.ArrayList;
import java.util.List;

public class MatrixC {
    private DataFromFile dataFromFile;
                         // macierz zawierajaca dNi/dy

    private List<double[][]> dNdX2DMatrix;                  // lista zawierajaca  ?4? macierze {dN/dx}{dN/dx}T
    private List<double[][]> dNdY2DMatrix;                  // lista zawierajaca  ?4? macierze {dN/dy}{dN/dy}T
    private List<double[][]> listOf_dNdX2_and_DetJ;         //lista 4 macierzy {dN/dx}{dN/dx}T*DetJ
    private List<double[][]> listOf_dNdY2_and_DetJ;         //lista 4 macierzy {dN/dy}{dN/dy}T*DetJ
    private List<double[][]> partsOf_H;                     //lista 4 macierzy "K*(     {dN/dx}{dN/dx}T  +  {dN/dy}{dN/dy}T)*DetJ
    private double[][] matrixH;
    //Funkcja ksztaltu
    private double[][] fukcjaKsztaltu;
    private double[][] matrixC;
    List<double[][]> listOfPointIntegrals;

    public void buildMatrixC(UniversalElement universalElement) {

        double[] ksiValues = universalElement.getKsiValueTable();
        double[] etaValues = universalElement.getEtaValueTable();
        double tmpFunkcjaKsztaltu[][] = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 - ksiValues[j]) * (1 - etaValues[j]);
                }
                if (i == 1) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 + ksiValues[j]) * (1 - etaValues[j]);
                }
                if (i == 2) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 + ksiValues[j]) * (1 + etaValues[j]);
                }
                if (i == 3) {
                    tmpFunkcjaKsztaltu[j][i] = 0.25 * (1 - ksiValues[j]) * (1 + etaValues[j]);
                }
            }
        }
        this.fukcjaKsztaltu = tmpFunkcjaKsztaltu;
        calculateIntegralPoints();
        calculateMatrixC();


        System.out.println("-=-=-=-=--=-=-=-=-=-MATRIX C-=-=-=-=-=-=-=-=-=-=-");
        show2D(matrixC);

    }

    public void calculateIntegralPoints() {
        listOfPointIntegrals = new ArrayList<>();
        for (int p = 0; p < 4; p++) {
            double integralPoionts[][] = new double[4][4];
            double[] tmpFunKsztaltu = fukcjaKsztaltu[p];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    integralPoionts[i][j] = dataFromFile.getSpecHeat() * dataFromFile.getDensity() * tmpFunKsztaltu[i] * tmpFunKsztaltu[j] * detJ[p];

                }
            }
            listOfPointIntegrals.add(integralPoionts);
        }


    }

    public void calculateMatrixC() {
        matrixC = new double[4][4];
        for (double[][] partMatrix : listOfPointIntegrals) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrixC[i][j] += partMatrix[i][j];
                }
            }
        }
    }

    public void show2D(double[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab.length; j++) {
                System.out.format("%.3f  ", tab[i][j]);
            }
            System.out.println();
        }
        System.out.println("\n----------------------------------------------------------------");
    }


    public List<double[][]> getdNdX2DMatrix() {
        return dNdX2DMatrix;
    }

    public void setdNdX2DMatrix(List<double[][]> dNdX2DMatrix) {
        this.dNdX2DMatrix = dNdX2DMatrix;
    }

    public List<double[][]> getdNdY2DMatrix() {
        return dNdY2DMatrix;
    }

    public void setdNdY2DMatrix(List<double[][]> dNdY2DMatrix) {
        this.dNdY2DMatrix = dNdY2DMatrix;
    }

    public List<double[][]> getListOf_dNdX2_and_DetJ() {
        return listOf_dNdX2_and_DetJ;
    }

    public void setListOf_dNdX2_and_DetJ(List<double[][]> listOf_dNdX2_and_DetJ) {
        this.listOf_dNdX2_and_DetJ = listOf_dNdX2_and_DetJ;
    }

    public List<double[][]> getListOf_dNdY2_and_DetJ() {
        return listOf_dNdY2_and_DetJ;
    }

    public void setListOf_dNdY2_and_DetJ(List<double[][]> listOf_dNdY2_and_DetJ) {
        this.listOf_dNdY2_and_DetJ = listOf_dNdY2_and_DetJ;
    }

    public List<double[][]> getPartsOf_H() {
        return partsOf_H;
    }

    public void setPartsOf_H(List<double[][]> partsOf_H) {
        this.partsOf_H = partsOf_H;
    }

    public double[][] getMatrixH() {
        return matrixH;
    }

    public void setMatrixH(double[][] matrixH) {
        this.matrixH = matrixH;
    }
}
