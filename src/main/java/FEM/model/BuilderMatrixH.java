package FEM.model;

public class BuilderMatrixH {

    private double[][] jacobian;
    private double[] detJ;
    private double[][] jacobian2D;
    private double[][] dNdX;
    private double[][] dNdY;


    public void buildJacobian(UniversalElement universalElement, Grid grid) {

        Node[] nodes = {grid.getNodeByID(0), grid.getNodeByID(6), grid.getNodeByID(7), grid.getNodeByID(1)};
        double J_1_1[] = new double[4];
        double J_1_2[] = new double[4];
        double J_2_1[] = new double[4];
        double J_2_2[] = new double[4];
        double fullofJacobian[][] = new double[4][4];
        for (int i = 0; i < 4; i++) {


            for (int j = 0; j < 4; j++) {

                J_1_1[i] += universalElement.getdNdKsiValuesByID(i, j) * nodes[j].getX();
                J_1_2[i] += universalElement.getdNdKsiValuesByID(i, j) * nodes[j].getY();
                J_2_1[i] += universalElement.getdNdEtaValuesByID(i, j) * nodes[j].getX();
                J_2_2[i] += universalElement.getdNdEtaValuesByID(i, j) * nodes[j].getY();

            }
        }
        fullofJacobian[0] = J_1_1;
        fullofJacobian[1] = J_1_2;
        fullofJacobian[2] = J_2_1;
        fullofJacobian[3] = J_2_2;


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(fullofJacobian[i][j] + " ");
            }
            System.out.println();
        }
        this.jacobian = fullofJacobian;
        buildDetJ();

        calculate_dN_dX(universalElement);
        calculate_dN_dY(universalElement);
    }

    public void buildDetJ() {
        double detJ[] = new double[4];
        System.out.println();
        for (int i = 0; i < 4; i++) {

            detJ[i] = jacobian[0][i] * jacobian[3][i] - jacobian[1][i] * jacobian[2][i];
            System.out.println(detJ[i] + " ");

        }
        this.detJ = detJ;
        buildJ_L_L();

    }

    public void buildJ_L_L() {

        double J_1_1_1[] = new double[4];
        double J_1_1_2[] = new double[4];
        double J_1_2_1[] = new double[4];
        double J_1_2_2[] = new double[4];

        double jacobian2D[][] = new double[4][4];
        for (int i = 0; i < 4; i++) {

            J_1_1_1[i] = jacobian[3][i] / detJ[i];
            J_1_1_2[i] = jacobian[2][i] / detJ[i];
            J_1_2_1[i] = jacobian[1][i] / detJ[i];
            J_1_2_2[i] = jacobian[0][i] / detJ[i];

        }
        jacobian2D[0] = J_1_1_1;
        jacobian2D[1] = J_1_1_2;
        jacobian2D[2] = J_1_2_1;
        jacobian2D[3] = J_1_2_2;

        this.jacobian2D = jacobian2D;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(jacobian2D[i][j] + " ");
            }
            System.out.println();
        }


    }


    public void calculate_dN_dX(UniversalElement universalElement) {
        System.out.println();
        double[][] tmpdNdX = new double[4][4];
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {


                tmpdNdX[i][j] = jacobian2D[0][0] * universalElement.getdNdKsiValuesByID(i, j) + jacobian2D[1][1] * universalElement.getdNdEtaValuesByID(i, j);

                System.out.print(tmpdNdX[i][j] + " ");
            }
            System.out.println();
        }
    this.dNdX=tmpdNdX;
    }

    public void calculate_dN_dY(UniversalElement universalElement) {
        System.out.println();
        double[][] tmpdNdY = new double[4][4];
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {


                tmpdNdY[i][j] = jacobian2D[2][2] * universalElement.getdNdKsiValuesByID(i, j) + jacobian2D[3][3] * universalElement.getdNdEtaValuesByID(i, j);

                System.out.print(tmpdNdY[i][j] + " ");
            }
            System.out.println();
        }
        this.dNdY=tmpdNdY;
    }
}
