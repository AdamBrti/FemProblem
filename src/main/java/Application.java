import FEM.FileOperation.DataFromFile;
import FEM.model.BuilderMatrixH;
import FEM.model.Grid;
import FEM.model.UniversalElement;

public class Application {

    public static void main(String[] args) {

        Grid grid = new Grid();
        DataFromFile dataFromFile = new DataFromFile();
        dataFromFile = dataFromFile.setData();
        grid.generateNodes(dataFromFile);
       grid.showNodes(dataFromFile);
        System.out.print("\n\n\n\n");
        UniversalElement universalElement = new UniversalElement();
        universalElement.calculate_dN_dKsi_AND_dEta();
        BuilderMatrixH builderMatrix = new BuilderMatrixH();
        builderMatrix.buildJacobian(universalElement, grid);
        builderMatrix.buildMatrixC(universalElement);
        System.out.print("\n\n\n\n");

    }
}
