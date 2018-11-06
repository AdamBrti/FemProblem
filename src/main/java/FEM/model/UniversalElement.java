package FEM.model;

import static java.lang.StrictMath.sqrt;

public class UniversalElement {

    private Point[] integralPoints = {new Point((-1 / Math.sqrt(3)), (-1 / Math.sqrt(3))), new Point((1 / Math.sqrt(3)), (-1 / Math.sqrt(3))), new Point((1 / Math.sqrt(3)), (1 / Math.sqrt(3))), new Point((-1 / Math.sqrt(3)), (1 / Math.sqrt(3)))};
    private double ksiValueTable[] = {-0.5773502692, -0.5773502692, 0.5773502692, 0.5773502692};
    private double etaValueTable[] = {-0.5773502692, 0.5773502692, 0.5773502692, -0.5773502692};
    private double dNdKsiValues[][]=new double[4][4];
    private double dNdEtaValues[][]=new double[4][4];

    private double elementsN[];


    public void calculate_dN_dKsi_AND_dEta() {

        double tmp[][] = new double[4][4];
        double tmp2[][] = new double[4][4];

        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                switch (j) {
                    case 0:
                        tmp[i][j] = -0.25 * (1 - ksiValueTable[i]);
                        tmp2[i][j] = -0.25 * (1 - etaValueTable[i]);
                        break;
                    case 1:
                        tmp[i][j] = 0.25 * (1 - ksiValueTable[i]);
                        tmp2[i][j] = -0.25 * (1 + etaValueTable[i]);
                        break;
                    case 2:
                        tmp[i][j] = 0.25 * (1 + ksiValueTable[i]);
                        tmp2[i][j] = 0.25 * (1 + etaValueTable[i]);
                        break;
                    case 3:
                        tmp[i][j] = -0.25 * (1 + ksiValueTable[i]);
                        tmp2[i][j] = 0.25 * (1 - etaValueTable[i]);
                        break;
                }

            }
            dNdKsiValues=tmp;
            dNdEtaValues=tmp2;
        }

        /*for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {
                System.out.print(tmp2[i][j] + "  \n");
            }
            System.out.print("\n\n\n");
        }*/


    }

    public UniversalElement() {

    }

    public double[] getKsiValueTable() {
        return ksiValueTable;
    }

    public void setKsiValueTable(double[] ksiValueTable) {
        this.ksiValueTable = ksiValueTable;
    }

    public double[] getEtaValueTable() {
        return etaValueTable;
    }

    public void setEtaValueTable(double[] etaValueTable) {
        this.etaValueTable = etaValueTable;
    }

    public double[][] getdNdKsiValues() {
        return dNdKsiValues;
    }
    public double getdNdKsiValuesByID(int i, int j) {
        return this.dNdKsiValues[i][j];
    }

    public void setdNdKsiValues(double[][] dNdKsiValues) {
        this.dNdKsiValues = dNdKsiValues;
    }

    public double[][] getdNdEtaValues() {
        return dNdEtaValues;
    }

    public void setdNdEtaValues(double[][] dNdEtaValues) {
        this.dNdEtaValues = dNdEtaValues;
    }

    public double getdNdEtaValuesByID(int i, int j) {
        return this.dNdEtaValues[i][j];
    }
}
