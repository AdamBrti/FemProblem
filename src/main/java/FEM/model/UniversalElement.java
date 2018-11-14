package FEM.model;

import static java.lang.StrictMath.sqrt;

public class UniversalElement {


    private Point[] integralPoints = new Point[4];
    private double ksiValueTable[] = new double[4];
    private double etaValueTable[] = new double[4];
    private double dNdKsiValues[][] = new double[4][4];
    private double dNdEtaValues[][] = new double[4][4];

    public UniversalElement() {

        double tmp = 1 / sqrt(3);
        integralPoints[0] = new Point(-tmp, -tmp);
        integralPoints[1] = new Point(tmp, -tmp);
        integralPoints[2] = new Point(tmp, tmp);
        integralPoints[3] = new Point(-tmp, tmp);
        for (int i = 0; i < 4; i++) {
            ksiValueTable[i] = integralPoints[i].getX();
            etaValueTable[i] = integralPoints[i].getY();
        }

    }

    public void calculate_dN_dKsi_AND_dEta() {

        double tmp[][] = new double[4][4];
        double tmp2[][] = new double[4][4];

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                if (i == 0) {
                    tmp[j][i] = -0.25 * (1 - etaValueTable[j]);
                    tmp2[j][i] = -0.25 * (1 - ksiValueTable[j]);
                }
                if (i == 1) {
                    tmp[j][i] = 0.25 * (1 - etaValueTable[j]);
                    tmp2[j][i] = -0.25 * (1 + ksiValueTable[j]);
                }
                if (i == 2) {
                    tmp[j][i] = 0.25 * (1 + etaValueTable[j]);
                    tmp2[j][i] = 0.25 * (1 + ksiValueTable[j]);
                }
                if (i == 3) {
                    tmp[j][i] = -0.25 * (1 + etaValueTable[j]);
                    tmp2[j][i] = 0.25 * (1 - ksiValueTable[j]);
                }
            }
            this.dNdKsiValues = tmp;
            this.dNdEtaValues = tmp2;
        }
        int l = 0;

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                System.out.format("%.7f ", tmp2[i][j]);
            }
            System.out.println();
        }
        System.out.println();

    }

    public double[] getKsiValueTable() {
        return ksiValueTable;
    }

    public double[] getEtaValueTable() {
        return etaValueTable;
    }

    public double getdNdKsiValuesByID(int i, int j) {
        return this.dNdKsiValues[i][j];
    }

    public double getdNdEtaValuesByID(int i, int j) {
        return this.dNdEtaValues[i][j];
    }
}
