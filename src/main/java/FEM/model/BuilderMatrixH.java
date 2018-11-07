package FEM.model;


import java.util.ArrayList;
import java.util.List;

public class BuilderMatrixH {

    private double[][] jacobian;
    private double[] detJ;
    private double[][] jacobian2D;
    private double[][] dNdX;   // macierz zawierajaca dNi/dx
    private double[][] dNdY;    // macierz zawierajaca dNi/dy
    private List<double[][]> dNdX2DMatrix; // lista zawierajaca  ?4? macierze {dN/dx}{dN/dx}T
    private List<double[][]> dNdY2DMatrix; // lista zawierajaca  ?4? macierze {dN/dy}{dN/dy}T
    private List<double[][]> listOf_dNdX2_and_DetJ;//lista 4 macierzy {dN/dx}{dN/dx}T*DetJ
    private List<double[][]> listOf_dNdY2_and_DetJ;//lista 4 macierzy {dN/dy}{dN/dy}T*DetJ
    private List<double[][]> partsOf_H;//lista 4 macierzy "K*(     {dN/dx}{dN/dx}T  +  {dN/dy}{dN/dy}T)*DetJ
    private double[][] matrixH;

    public void buildJacobian(UniversalElement universalElement, Grid grid) {

        // TODO - poprawic wybieranie nodebyID na jakies zmienne - poki co na sztywno
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
        this.dNdX = tmpdNdX;
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
        this.dNdY = tmpdNdY;
        calculate_dNdX2DMatrix_and_dNdY2DMatrix_T();
    }

    public void calculate_dNdX2DMatrix_and_dNdY2DMatrix_T() {
        //TODO sprawdz czy wartosci są na pewno dobre
        dNdX2DMatrix = new ArrayList<>();
        dNdY2DMatrix = new ArrayList<>();
        double tmpFor2X[];
        double tmpFor2Y[];

        for (int p = 0; p < 4; p++) {
            tmpFor2X = dNdX[p];
            tmpFor2Y = dNdY[p];
            double tmpdNdX2[][] = new double[4][4];
            double tmpdNdY2[][] = new double[4][4];
            for (int i = 0; i < 4; i++) {

                for (int j = 0; j < 4; j++) {

                    tmpdNdX2[i][j] = tmpFor2X[i] * tmpFor2X[j];
                    tmpdNdY2[i][j] = tmpFor2Y[i] * tmpFor2Y[j];
                    // System.out.print(tmpdNdY2[i][j] + " ");
                }
                // System.out.println();
            }
            dNdX2DMatrix.add(tmpdNdX2);
            dNdY2DMatrix.add(tmpdNdY2);
            System.out.println();
        }

/*

        for (double[][] tablica : dNdX2DMatrix) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.print(tablica[i][j] + "   ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
*/
        calculate_dNdX2DMatrix__dNdY2DMatrix_T_AND_detJ();
    }

    public void calculate_dNdX2DMatrix__dNdY2DMatrix_T_AND_detJ() {
        double[][] dNdX2D;
        double[][] dNdY2D;
        //System.out.println("PRZEDOSTATNIA: -----------------===========================");

        listOf_dNdX2_and_DetJ = new ArrayList<>();
        listOf_dNdY2_and_DetJ = new ArrayList<>();
        for (int p = 0; p < 4; p++) {
            double[][] tmp_dNdX2_and_DetJ = new double[4][4];
            double[][] tmp_dNdY2_and_DetJ = new double[4][4];

            dNdX2D = dNdX2DMatrix.get(p);
            dNdY2D = dNdY2DMatrix.get(p);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    tmp_dNdX2_and_DetJ[i][j] = dNdX2D[i][j] * detJ[i];
                    tmp_dNdY2_and_DetJ[i][j] = dNdY2D[i][j] * detJ[i];
                    //  System.out.print(tmp_dNdY2_and_DetJ[i][j]+" ");
                }
                // System.out.println();
            }
            // System.out.println();
            listOf_dNdX2_and_DetJ.add(tmp_dNdX2_and_DetJ);
            listOf_dNdY2_and_DetJ.add(tmp_dNdY2_and_DetJ);
        }
        calculate_K_and_Matrixs();
    }

    public void calculate_K_and_Matrixs() {

        // System.out.println("OSTATNIA: -----------------===========================");
        partsOf_H = new ArrayList<>();

        for (int p = 0; p < 4; p++) {
            double[][] partMatrix = new double[4][4];
            double[][] tmpX2D = listOf_dNdX2_and_DetJ.get(p);
            double[][] tmpY2D = listOf_dNdY2_and_DetJ.get(p);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    partMatrix[i][j] = 30.0 * (tmpX2D[i][j] + tmpY2D[i][j]);
                    //   System.out.print(partMatrix[i][j]+"   ");
                }
                // System.out.println();
            }
            partsOf_H.add(partMatrix);
            // System.out.println();
        }
        calculate_Matrix_H();
    }

    public void calculate_Matrix_H() {
        matrixH = new double[4][4];
        for (double[][] partmatrix : partsOf_H) {


            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrixH[i][j] += partmatrix[i][j];
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(matrixH[i][j]+" ");
            }
            System.out.println();
        }
    }
}
