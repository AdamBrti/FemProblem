import FEM.FileOperation.DataFromFile;
import FEM.Matrix.*;
import FEM.model.Grid;
import FEM.model.Node;
import FEM.model.UniversalElement;

public class Application {

    public static void main(String[] args) {
        int[] localId = {1, 2, 3, 4};
        double[][] globalMatrixH = new double[16][16];
        double[][] globalMatrixC = new double[16][16];
        double[][] globalmatrixHBC = new double[16][16];
        double[] globalVectorP = new double[16];

        MetodaEliminacjiGaussa metodaEliminacjiGaussa = new MetodaEliminacjiGaussa();

        DataFromFile dataFromFile = new DataFromFile();
        UniversalElement universalElement = new UniversalElement();
        Grid grid = new Grid();
        grid.generateNodes2(dataFromFile);
        AreaGenerator areaArray = new AreaGenerator();
        areaArray.areaStatusGenerator(grid, dataFromFile);
        double[] endTemp = new double[16];
        for (int k = 0; k < 16; k++) {
            endTemp[k] = 100.0;
        }
        for (int l = 0; l < 10; l++) {
            System.out.println(l + " -=-=-=-=-=-ITERACJA --------**************----------------");
            for (int elementNumber = 0; elementNumber < 9; elementNumber++) {
                //pobieranie odpowiednich ID Elelmentu
                int[] globalId = new int[4];
                for (int i = 0; i < 4; i++) {
                    globalId[i] = grid.getElements().get(elementNumber).getId()[i];
                }

                //liczy macierz H dla kazdego elementu lokalnie
                MatrixH matrixH = new MatrixH(dataFromFile);
                matrixH.buildMatrixH(universalElement, grid, elementNumber);

                //oblicza matrix C
                MatrixC matrixC = new MatrixC(dataFromFile, universalElement, matrixH);
                matrixC.buildMatrixC(universalElement);

                //liczy druga czesc macierzy H z warunkiem brzegowym lokalnie
                //wylicza wartosc wektora P - jako atrybut lokalnie
                MatrixHBC matrixHBC = new MatrixHBC();
                matrixHBC.buildMatrixHBC(dataFromFile, universalElement, grid, elementNumber, areaArray.getListBoarderConditionForElement().get(elementNumber),
                        localId, globalId, globalVectorP);

                //przerzut wyliczonych czesci do globala
                arrayToGlobal(localId, globalId, matrixC.matrixC, globalMatrixC);
                arrayToGlobal(localId, globalId, matrixH.matrixH, globalMatrixH);
                arrayToGlobal(localId, globalId, matrixHBC.getMatrixHBC(), globalmatrixHBC);

            }
            // System.out.println();
            // showGlobalArray(globalMatrixC);
            // System.out.println("\n");
            // showGlobalArray(globalMatrixH);
            // System.out.println("\n");

            globalVectorPOperation(globalVectorP, globalMatrixC, dataFromFile.getDtau(), endTemp);
            globalMatrixH = globalMatrixHCalculation(globalMatrixH, globalMatrixC, dataFromFile.getDtau(), globalmatrixHBC);

            //showGlobalArray(globalMatrixH);
            // System.out.println("\n");
            // showGlobalArrayVectorP(globalVectorP);
            //System.out.println();

            endTemp = metodaEliminacjiGaussa.gaussElimination(grid.getNodes().size(), globalMatrixH, globalVectorP);
            for (int i = 0; i < 16; i++) {
                grid.getNodes().get(i).setT(endTemp[i]);
                System.out.println(endTemp[i]);
                for (int j = 0; j < 16; j++) {
                    globalMatrixH[i][j] = 0;
                    globalMatrixC[i][j] = 0;
                    globalmatrixHBC[i][j] = 0;

                }
                globalVectorP[i] = 0;
            }

        }


    }

    private static void showGlobalArray(double[][] globalArray) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.format("%.4f  ", globalArray[i][j]);
            }
            System.out.println("");
        }
    }

    private static double[][] arrayToGlobal(int[] localId, int[] globalId, double[][] localArray, double[][] globalArray) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int localx = localId[j];
                int localy = localId[i];

                int globalx = globalId[localx - 1];
                int globaly = globalId[localy - 1];

                globalArray[globalx - 1][globaly - 1] += localArray[i][j];
            }
        }
        return globalArray;
    }

    private static void showGlobalArrayVectorP(double[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                System.out.format("%.0f  ", array[i]);
            } else {
                System.out.format("%.4f  ", array[i]);
            }

        }
    }

    private static double[] globalVectorPOperation(double[] globalVectorP, double[][] globalMatrixC, double dt, double[] temperatur) {
        double[][] tmpglobalMatrixC = new double[globalMatrixC.length][globalMatrixC.length];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                tmpglobalMatrixC[i][j] = globalMatrixC[i][j];
            }
        }

        for (int i = 0; i < 16; i++) {

            for (int j = 0; j < 16; j++) {
                tmpglobalMatrixC[i][j] /= dt;
                tmpglobalMatrixC[j][i] *= temperatur[i];

            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                globalVectorP[i] += tmpglobalMatrixC[i][j];
            }
        }

        return globalVectorP;
    }

    private static double[][] globalMatrixHCalculation(double[][] matrixH, double[][] matrixC, double dt, double[][] matrixHBC2D) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                matrixH[i][j] += matrixC[i][j] / dt + matrixHBC2D[i][j];
            }
        }
        return matrixH;
    }
}


