import FEM.FileOperation.DataFromFile;
import FEM.model.BuilderMatrix;
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
        BuilderMatrix builderMatrix = new BuilderMatrix();
        builderMatrix.buildJacobian(universalElement, grid);
        System.out.print("\n\n\n\n");

    }
}
