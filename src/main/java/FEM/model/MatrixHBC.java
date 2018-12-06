package FEM.model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.sqrt;

public class MatrixHBC {

    private double[][] matrixHBC;
    private double alfa = 300;
    private int warunekBrzegowy[] = {1, 0, 0, 1};
    private List<double[][]> listOfPartsSum;
    protected double[] dx;

    public void buildMatrixHBC(UniversalElement universalElement, Grid grid, int number) {
        Node[] nodes = {grid.getNodeByID(number), grid.getNodeByID(number + 4), grid.getNodeByID(number + 5), grid.getNodeByID(number + 1)};
        double dx[] = pitagoras(nodes);
        this.dx = dx;
        calcWarunkiBrzegowe_2D(universalElement, dx);
        calcMatrixHBC();
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
        show2D(matrixHBC);
        // System.out.println();
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


    //Integral(alfa*{N}{N}T*L/2) etc.
    public void calcWarunkiBrzegowe_1D(UniversalElement universalElement, double[] dx) {
        double[] tmpksiValue = universalElement.getKsiValueTable();
        // wartosci ksi to zawsze dodatnia lub ujemna wartosc - dlatego pobieram tylko dwie (patrz uklad wspolrzednych ksi i eta)
        double[] ksiValue = {tmpksiValue[0], tmpksiValue[1]};
        double[][] dNdksi = new double[2][2];
        double[][] integralPoint1 = new double[2][2];
        double[][] integralPoint2 = new double[2][2];
        double[] tmpdNdksi1 = dNdksi[0];
        double[] tmpdNdksi2 = dNdksi[1];
        List<double[][]> listofSumPoints = new ArrayList<>();
        List<double[][]> listaSumzWarBrzegowym = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == 0) {
                    dNdksi[i][j] = 0.5 * (1 - ksiValue[j]);
                }
                if (i == 1) {
                    dNdksi[i][j] = 0.5 * (1 + ksiValue[j]);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                integralPoint1[i][j] = tmpdNdksi1[i] * tmpdNdksi1[j] * alfa;
                integralPoint2[i][j] = tmpdNdksi2[i] * tmpdNdksi2[j] * alfa;
            }
        }
        for (int p = 0; p < 4; p++) {
            double[][] tmpSum = new double[2][2];

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    tmpSum[i][j] = (integralPoint1[i][j] + integralPoint2[i][j]) * (dx[p] / 2.0);
                }
            }
            listofSumPoints.add(tmpSum);
        }
        for (int p = 0; p < 4; p++) {
            double[][] tmpSum = listofSumPoints.get(p);
            double[][] result = new double[2][2];

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    result[i][j] = tmpSum[i][j] * warunekBrzegowy[p];
                }
            }
            listaSumzWarBrzegowym.add(result);
        }
        System.out.println();
        matrixHBC = new double[4][4];
        for (int p = 0; p < 4; p++) {

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    matrixHBC[i][j] = listaSumzWarBrzegowym.get(p)[i][j];
                    System.out.print(matrixHBC[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }


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
}
