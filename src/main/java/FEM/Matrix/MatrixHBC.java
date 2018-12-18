package FEM.Matrix;

import FEM.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.sqrt;

public class MatrixHBC {

    private double[][] matrixHBC;
    private double alfa = 300;
    private int warunekBrzegowy[];
    private List<double[][]> listOfPartsSum;
    protected double[] dx;
    public double[] vectorP1;
    public double[] vectorP2;

    public void buildMatrixHBC(UniversalElement universalElement, Grid grid, int number, int[] boarderCondition, int[] localId, int[] globalId, double[] globalArray) {
        Integer tabId[] = grid.getElements().get(number).getId();
        Node[] nodes = {grid.getNodeByID(tabId[0] - 1), grid.getNodeByID(tabId[1] - 1), grid.getNodeByID(tabId[2] - 1), grid.getNodeByID(tabId[3] - 1)};
        this.warunekBrzegowy = boarderCondition;
        double dx[] = pitagoras(nodes);
        this.dx = dx;
        calcWarunkiBrzegowe_2D(universalElement, dx);
        calcMatrixHBC();
        IntegralPointP integralPointP = new IntegralPointP();

        //oblicz wektor P
        VectorP vectorP = new VectorP();
        for (int i = 0; i < 4; i++) {
            if (boarderCondition[i] == 1) {
                this.vectorP1 = vectorP.buildVectorP(integralPointP.P1[i], dx[i], 1200, 300);
                vectorPToGlobal(localId, globalId,vectorP1, globalArray);
                this.vectorP2 = vectorP.buildVectorP(integralPointP.P2[i], dx[i], 1200, 300);
                vectorPToGlobal(localId, globalId,vectorP2, globalArray);
            }
        }

        //show2D(matrixHBC);
        //System.out.println();
    }
    private static double[] vectorPToGlobal(int[] localId, int[] globalId, double[] localArray, double[] globalArray) {
        for (int i = 0; i < 4; i++) {
            int localx = localId[i];
            int globalx = globalId[localx - 1];

            globalArray[globalx - 1] += localArray[i];

        }
        return globalArray;
    }

    public double[] addVector(double[] vectorA, double[] vectorB) {
        double tmp[] = new double[4];
        for (int i = 0; i < 4; i++) {
            tmp[i] += vectorA[i] + vectorB[i];
        }
        return tmp;
    }


    public void calcMatrixHBC() {
        matrixHBC = new double[4][4];
        for (int p = 0; p < 4; p++) {
            double[][] part = new double[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    part[i][j] = warunekBrzegowy[p] * listOfPartsSum.get(p)[i][j];
                    matrixHBC[i][j] += part[i][j];
                }
            }
        }

    }

    public void calcWarunkiBrzegowe_2D(UniversalElement universalElement, double[] dx) {
        IntegralPoint integralPoints = new IntegralPoint();
        double[] ksiValues = integralPoints.getKsiValueTable();
        double[] etaValues = {-1.0, 1.0, ksiValues[1], ksiValues[0]};
        List<double[][]> listdNdksideta = new ArrayList<>();
        int[] o;
        int[] u;
        for (int p = 0; p < 4; p++) {
            double[][] dNdKsidEta = new double[2][4];
            o = getIteratorForKsi(p);
            u = getIteratorForEta(p);
            double[] tmpKsiValue = {ksiValues[o[0]], ksiValues[o[1]]};
            double[] tmpEtaValue = {etaValues[u[0]], etaValues[u[1]]};
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2; j++) {
                    if (i == 0) {
                        dNdKsidEta[j][i] = 0.25 * (1 - tmpKsiValue[j]) * (1 - tmpEtaValue[j]);
                    }
                    if (i == 1) {
                        dNdKsidEta[j][i] = 0.25 * (1 + tmpKsiValue[j]) * (1 - tmpEtaValue[j]);
                    }
                    if (i == 2) {
                        dNdKsidEta[j][i] = 0.25 * (1 + tmpKsiValue[j]) * (1 + tmpEtaValue[j]);
                    }
                    if (i == 3) {
                        dNdKsidEta[j][i] = 0.25 * (1 - tmpKsiValue[j]) * (1 + tmpEtaValue[j]);
                    }
                }
            }
            listdNdksideta.add(dNdKsidEta);
        }
        this.listOfPartsSum = new ArrayList<>();
        for (int p = 0; p < 4; p++) {
            double[][] tmpSum = new double[4][4];
            double[] tmpdN1 = listdNdksideta.get(p)[0];
            double[] tmpdN2 = listdNdksideta.get(p)[1];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tmpSum[i][j] = ((tmpdN1[i] * tmpdN1[j] * alfa) + (tmpdN2[i] * tmpdN2[j] * alfa)) * (dx[p] / 2);
                }
            }
            listOfPartsSum.add(tmpSum);
            //show2D(tmpSum);

        }
    }

    public int[] getIteratorForKsi(int p) {
        int[] tmp = new int[2];
        if (p == 0) tmp = new int[]{0, 1};
        if (p == 1) tmp = new int[]{2, 2};
        if (p == 2) tmp = new int[]{1, 0};
        if (p == 3) tmp = new int[]{3, 3};

        return tmp;
    }

    public int[] getIteratorForEta(int p) {
        int[] tmp = new int[2];
        if (p == 0) tmp = new int[]{0, 0};
        if (p == 1) tmp = new int[]{2, 3};
        if (p == 2) tmp = new int[]{1, 1};
        if (p == 3) tmp = new int[]{2, 3};

        return tmp;
    }

    public double[] pitagoras(Node[] nodes) {
        double dx[] = new double[4];
        for (int i = 0; i < 4; i++) {
            if (i < 3) {
                dx[i] = sqrt((nodes[i].getX() - nodes[i + 1].getX()) * (nodes[i].getX() - nodes[i + 1].getX())
                        + (nodes[i].getY() - nodes[i + 1].getY()) * (nodes[i].getY() - nodes[i + 1].getY()));
            } else {
                dx[i] = sqrt((nodes[0].getX() - nodes[3].getX()) * (nodes[0].getX() - nodes[3].getX())
                        + (nodes[0].getY() - nodes[3].getY()) * (nodes[0].getY() - nodes[3].getY()));
            }
        }

        return dx;
    }


    private class IntegralPoint {
        private Point[] integralPoints = new Point[4];
        private double ksiValueTable[] = new double[4];
        private double etaValueTable[] = new double[4];
        double tmp = 1 / sqrt(3);

        public IntegralPoint() {
            integralPoints[0] = new Point(-tmp, -1);
            integralPoints[1] = new Point(tmp, -1);
            integralPoints[2] = new Point(1, tmp);
            integralPoints[3] = new Point(-1, tmp);
            for (int i = 0; i < 4; i++) {
                ksiValueTable[i] = integralPoints[i].getX();
                etaValueTable[i] = integralPoints[i].getY();
            }
        }

        public Point[] getIntegralPoints() {
            return integralPoints;
        }

        public double[] getKsiValueTable() {
            return ksiValueTable;
        }

        public double[] getEtaValueTable() {
            return etaValueTable;
        }
    }

    public double[] getDx() {
        return dx;
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

    public double[][] getMatrixHBC() {
        return matrixHBC;
    }

    public List<double[][]> getListOfPartsSum() {
        return listOfPartsSum;
    }
}
