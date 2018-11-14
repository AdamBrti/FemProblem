package FEM.model;


import java.util.ArrayList;
import java.util.List;

public class BuilderMatrixH {

    private static double c = 700;
    private static double ro = 7800;
    private double[][] jacobian;
    private double[] detJ;
    private double[][] jacobian2D;
    private double[][] dNdX;                                // macierz zawierajaca dNi/dx
    private double[][] dNdY;                                // macierz zawierajaca dNi/dy

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
        this.jacobian = fullofJacobian;

        show2D(jacobian);
        buildDetJ();

        calculate_dN_dX(universalElement);
        calculate_dN_dY(universalElement);
    }

    public void buildDetJ() {
        double detJ[] = new double[4];
        System.out.println();
        for (int i = 0; i < 4; i++) {
            detJ[i] = jacobian[0][i] * jacobian[3][i] - jacobian[1][i] * jacobian[2][i];
        }
        this.detJ = detJ;
        System.out.println("DetJ");
        show1D(detJ);
        buildJ_L_L();

    }

    public void buildJ_L_L() {

        double J_1_1_1[] = new double[4];
        double J_1_1_2[] = new double[4];
        double J_1_2_1[] = new double[4];
        double J_1_2_2[] = new double[4];

        double jacobian2D[][] = new double[4][4];
        for (int i = 0; i < 4; i++) {
            //TODO sprawdzic poprawnosc iteracji po jacobianie - excel ma zamienione dwie wartosci
            J_1_1_1[i] = jacobian[3][i] / detJ[i];
            J_1_2_1[i] = jacobian[2][i] / detJ[i];
            J_1_1_2[i] = jacobian[1][i] / detJ[i];
            J_1_2_2[i] = jacobian[0][i] / detJ[i];

        }
        jacobian2D[0] = J_1_1_1;
        jacobian2D[1] = J_1_1_2;
        jacobian2D[2] = J_1_2_1;
        jacobian2D[3] = J_1_2_2;

        this.jacobian2D = jacobian2D;
        System.out.println("Jacobian 2D");
        show2D(jacobian2D);
    }

    public void calculate_dN_dX(UniversalElement universalElement) {
        System.out.println();
        double[][] tmpdNdX = new double[4][4];
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                tmpdNdX[i][j] = jacobian2D[0][0] * universalElement.getdNdKsiValuesByID(i, j)
                        + jacobian2D[1][1] * universalElement.getdNdEtaValuesByID(i, j);

            }

        }
        this.dNdX = tmpdNdX;
    }

    public void calculate_dN_dY(UniversalElement universalElement) {

        double[][] tmpdNdY = new double[4][4];
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 4; j++) {

                tmpdNdY[i][j] = jacobian2D[2][2] * universalElement.getdNdKsiValuesByID(i, j)
                        + jacobian2D[3][3] * universalElement.getdNdEtaValuesByID(i, j);
            }

        }
        this.dNdY = tmpdNdY;
        show2D(tmpdNdY);
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

                }

            }
            dNdX2DMatrix.add(tmpdNdX2);
            dNdY2DMatrix.add(tmpdNdY2);
        }


        /*
        for (double[][] tablica : dNdX2DMatrix) {
            show2D(tablica);
            System.out.println();
            System.out.println();
        }*/

        calculate_dNdX2DMatrix__dNdY2DMatrix_T_AND_detJ();
    }

    //JACOBIAN PRZEKSZTALCENIA
    public void calculate_dNdX2DMatrix__dNdY2DMatrix_T_AND_detJ() {
        double[][] dNdX2D;
        double[][] dNdY2D;

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
                }
            }
            listOf_dNdX2_and_DetJ.add(tmp_dNdX2_and_DetJ);
            listOf_dNdY2_and_DetJ.add(tmp_dNdY2_and_DetJ);
        }
        calculate_K_and_Matrixs();
    }

    public void calculate_K_and_Matrixs() {
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

        show2D(matrixH);
    }

    //--------------------------MATRIX C
    public void buildMatrixC(UniversalElement universalElement) {
       // Node[] nodes = {grid.getNodeByID(0), grid.getNodeByID(6), grid.getNodeByID(7), grid.getNodeByID(1)};
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

    }

    public void calculateIntegralPoints() {
        listOfPointIntegrals = new ArrayList<>();

        for (int p = 0; p < 4; p++) {
            double integralPoionts[][] = new double[4][4];
            double[] tmpFunKsztaltu=fukcjaKsztaltu[p];

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    integralPoionts[i][j]=c*ro*tmpFunKsztaltu[i]*tmpFunKsztaltu[j]*detJ[p];

                }
            }

            listOfPointIntegrals.add(integralPoionts);
        }


    }

    public void calculateMatrixC(){

        matrixC = new double[4][4];
        for (double[][] partmatrix : listOfPointIntegrals) {

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrixC[i][j] += partmatrix[i][j];
                }
            }
        }

        show2D(matrixC);

    }

    public void show1D(double[] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.format("%.3f  ", tab[i]);
        }
        System.out.println("\n----------------------------------------------------------------");
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
