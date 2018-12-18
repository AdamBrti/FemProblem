import FEM.FileOperation.DataFromFile;
import FEM.Matrix.*;
import FEM.model.Grid;
import FEM.model.UniversalElement;

public class Application {

    public static void main(String[] args) {
        int[] localId = {1, 2, 3, 4};
        double[][] globalMatrixH = new double[16][16];
        double[][] globalMatrixC = new double[16][16];
        double[][] globalmatrixHBC = new double[16][16];
        double[] globalVectorP = new double[16];

        DataFromFile dataFromFile = new DataFromFile();
        UniversalElement universalElement = new UniversalElement();
        Grid grid = new Grid();
        grid.generateNodes2(dataFromFile);
        AreaGenerator areaArray = new AreaGenerator();
        areaArray.areaStatusGenerator(grid, dataFromFile);

        for (int elementNumber = 0; elementNumber < 9; elementNumber++) {
            int[] globalId = new int[4];
            globalId[0] = grid.getElements().get(elementNumber).getId()[0];
            globalId[1] = grid.getElements().get(elementNumber).getId()[1];
            globalId[2] = grid.getElements().get(elementNumber).getId()[2];
            globalId[3] = grid.getElements().get(elementNumber).getId()[3];

            System.out.println("Element:  " + elementNumber);
            //liczy macierz H dla kazdego elementu lokalnie
            MatrixH matrixH = new MatrixH(dataFromFile);
            matrixH.buildMatrixH(universalElement, grid, elementNumber);

            MatrixC matrixC = new MatrixC(dataFromFile, universalElement, matrixH);
            matrixC.buildMatrixC(universalElement);

            //liczy druga czesc macierzy H z warunkiem brzegowym lokalnie
            //wylicza wartosc wektora P - jako atrybut lokalnie
            MatrixHBC matrixHBC = new MatrixHBC();
            matrixHBC.buildMatrixHBC(universalElement, grid, elementNumber, areaArray.getListBoarderConditionForElement().get(elementNumber),
                    localId, globalId, globalVectorP);

            //przerzut wyliczonych czesci do globala
            arrayToGlobal(localId, globalId, matrixC.matrixC, globalMatrixC);
            arrayToGlobal(localId, globalId, matrixH.matrixH, globalMatrixH);
            arrayToGlobal(localId, globalId, matrixHBC.getMatrixHBC(), globalmatrixHBC);

            System.out.println();

        }
        System.out.println();
        showGlobalArray(globalMatrixC);
        System.out.println("\n");
        showGlobalArray(globalMatrixH);
        System.out.println("\n");
        showGlobalArray(globalmatrixHBC);
        System.out.println("\n");

        globalVectorPOperation(globalVectorP, globalMatrixC, dataFromFile.getDtau(), dataFromFile.getTemperatur());
        showGlobalArray(globalMatrixC);
        System.out.println("\n");
        globalMatrixH = globalMatrixHCalculation(globalMatrixH, globalMatrixC, 50, globalmatrixHBC);

        showGlobalArray(globalMatrixH);
        System.out.println();
    }

    private static void showGlobalArray(double[][] globalArray) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(globalArray[i][j] + " ");
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

    private static double[] globalVectorPOperation(double[] globalVectorP, double[][] globalMatrixC, double dt, double t0) {
        double[][] tmpglobalMatrixC = new double[globalMatrixC.length][globalMatrixC.length];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                tmpglobalMatrixC[i][j] = globalMatrixC[i][j];
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                tmpglobalMatrixC[i][j] /= dt;
                tmpglobalMatrixC[i][j] *= t0;
            }
        }
        double[] returnArray = new double[16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                returnArray[i] += tmpglobalMatrixC[i][j] * globalVectorP[i];
            }
        }

        return returnArray;
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


