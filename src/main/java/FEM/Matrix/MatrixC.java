package FEM.Matrix;

import FEM.FileOperation.DataFromFile;
import FEM.model.UniversalElement;

import java.util.ArrayList;
import java.util.List;

public class MatrixC {

    private DataFromFile dataFromFile;
    private double[][] jacobian;
    private double[][] jacobian2D;
    private double[] detJ;

    private double[][] fukcjaKsztaltu;
    public double[][] matrixC;
    private List<double[][]> listOfPointIntegrals;

    public MatrixC() {
    }

    public MatrixC(DataFromFile dataFromFile, UniversalElement universalElement, MatrixH matrixH) {
        this.dataFromFile = dataFromFile;
        this.jacobian = matrixH.getJacobian();
        this.jacobian2D = matrixH.getJacobian2D();
        this.detJ = matrixH.getDetJ();
    }


    public void buildMatrixC(UniversalElement universalElement) {

        calculateFunkcjaKsztaltu(universalElement);
        calculateIntegralPoints();
        calculateMatrixC();
        //show2D(matrixC);

    }
    public void calculateFunkcjaKsztaltu(UniversalElement universalElement) {
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


}
