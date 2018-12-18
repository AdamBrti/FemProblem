package FEM.Matrix;

import FEM.FileOperation.DataFromFile;
import FEM.model.Grid;

import java.util.ArrayList;
import java.util.List;

public class AreaGenerator {

    private List<int[]> listBoarderConditionForElement;

    public void areaStatusGenerator(Grid grid, DataFromFile fileData) {
        listBoarderConditionForElement=new ArrayList<>();
        for (int i = 0; i < fileData.getN_B() - 1; ++i) {
          //  System.out.println("--------");
            for (int j = 0; j < fileData.getN_H() - 1; ++j) {
                int[] areaStatus = new int[4];
                if (grid.getNodes().get(j + i * fileData.getN_H()).isBoarderContition() && grid.getNodes().get(j + (i + 1) * fileData.getN_H()).isBoarderContition()) {
                    areaStatus[0] = 1;
                }
                if (grid.getNodes().get(j + (i + 1) * fileData.getN_H()).isBoarderContition() && grid.getNodes().get(j + (i + 1) * fileData.getN_H() + 1).isBoarderContition()) {
                    areaStatus[1] = 1;
                }
                if (grid.getNodes().get(j + (i + 1) * fileData.getN_H() + 1).isBoarderContition() && grid.getNodes().get(j + 1 + i * fileData.getN_H()).isBoarderContition()) {
                    areaStatus[2] = 1;
                }
                if (grid.getNodes().get(j + 1 + i * fileData.getN_H()).isBoarderContition() && grid.getNodes().get(j + i * fileData.getN_H()).isBoarderContition()) {
                    areaStatus[3] = 1;
                }
                for (int l = 0; l < 4; l++) {
                //    System.out.print(areaStatus[l] + " ");
                }
                listBoarderConditionForElement.add(areaStatus);
                //System.out.println("\n");
            }
        }
    }

    public List<int[]> getListBoarderConditionForElement() {
        return listBoarderConditionForElement;
    }
}