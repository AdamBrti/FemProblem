import FEM.FileOperation.DataFromFile;
import FEM.model.*;

public class Application {

    public static void main(String[] args) {
        DataFromFile dataFromFile = new DataFromFile();
        UniversalElement universalElement = new UniversalElement();


        Grid grid = new Grid();
        grid.generateNodes2(dataFromFile);
        grid.showNodes(dataFromFile);
        System.out.println();

        AreaGenerator areaGenerator =new AreaGenerator();
        areaGenerator.areaStatusGenerator(grid,dataFromFile);


        BuilderMatrixH builderMatrix = new BuilderMatrixH(dataFromFile);
       // MatrixHBC matrixHBC = new MatrixHBC();
       // matrixHBC.buildMatrixHBC(universalElement, grid, 0);

        for (int elementNumber = 0; elementNumber < grid.getElements().size() ; elementNumber++) {

            MatrixHBC matrixHBC = new MatrixHBC();
            matrixHBC.buildMatrixHBC(universalElement, grid, elementNumber);
            builderMatrix.buildJacobian(universalElement, grid, elementNumber);
            builderMatrix.buildMatrixC(universalElement);
        }

        VectorP vectorP=new VectorP(dataFromFile);
        vectorP.buildVectorP();
    }
}
