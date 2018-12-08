package FEM.model;

import FEM.FileOperation.DataFromFile;

public class AreaGenerator {

    public void areaStatusGenerator(Grid grid, DataFromFile fileData) {
        for (int i = 0; i < fileData.getN_B() - 1; ++i) {
            System.out.println("--------");
            for (int j = 0; j < fileData.getN_H() - 1; ++j) {
                double[] areaStatus = new double[4];
                if (grid.getNodes().get(j + i * fileData.getN_H()).isBoarderContition() == true && grid.getNodes().get(j + (i + 1) * fileData.getN_H()).isBoarderContition() == true) {
                    areaStatus[0] = 1;
                }
               if (grid.getNodes().get(j + (i + 1) * fileData.getN_H()).isBoarderContition() == true && grid.getNodes().get(j + (i + 1) * fileData.getN_H() + 1).isBoarderContition() == true) {
                    areaStatus[1] = 1;
                }
                if (grid.getNodes().get(j + (i + 1) * fileData.getN_H() + 1).isBoarderContition() == true && grid.getNodes().get(j + 1 + i * fileData.getN_H()).isBoarderContition() == true) {
                    areaStatus[2] = 1;
                }
                if (grid.getNodes().get(j + 1 + i * fileData.getN_H()).isBoarderContition() == true && grid.getNodes().get(j + i * fileData.getN_H()).isBoarderContition() == true) {
                    areaStatus[3] = 1;
                }
               for (int l = 0; l < 4; l++) {
                    System.out.print(areaStatus[l] + " ");
                }

                System.out.println("\n");
            }
        }
    }
}