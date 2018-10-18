import FileOperation.DataFromFile;
import model.Grid;

public class Application {

    public static void main(String[] args) {

        Grid grid = new Grid();
        DataFromFile dataFromFile = new DataFromFile();
        dataFromFile = dataFromFile.setData();
        grid.generateNodes(dataFromFile);
        grid.showNodes(dataFromFile);
      // grid.showList();
    }
}
