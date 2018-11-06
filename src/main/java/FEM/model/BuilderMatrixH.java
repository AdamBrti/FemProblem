package FEM.model;

import java.util.ArrayList;
import java.util.List;

public class BuilderMatrix {

    private double [][]jacobian;
    private double [] detJ;
    private double[][] jacobian2D;


    public void buildJacobian(UniversalElement universalElement, Grid grid) {

        Node[] nodes = {grid.getNodeByID(0), grid.getNodeByID(6), grid.getNodeByID(7), grid.getNodeByID(1)};
        double[][] integralPoints = new double[4][4];
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
            // System.out.println("i: " + i + " V: " + J_1_1[i] + "       " + J_1_2[i] + "     " + "       " + J_2_1[i] + "       " + J_2_2[i]);

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
        //wyznacznik detJ
        this.jacobian = fullofJacobian;
        buildDetJ();


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
           // System.out.println(J_1_1_1[i] + " " + J_1_1_2[i] + " " + J_1_2_1[i] + " " + J_1_2_2[i] + " ");

        }
        jacobian2D[0] = J_1_1_1;
        jacobian2D[1] = J_1_1_2;
        jacobian2D[2] = J_1_2_1;
        jacobian2D[3] = J_1_2_2;

        this.jacobian2D = jacobian2D;

        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(jacobian2D[i][j]+" ");
            }
            System.out.println();
        }
    }
}
