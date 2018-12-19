package FEM.model;

import FEM.FileOperation.DataFromFile;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private List<Node> nodes;
    private List<Element> elements = new ArrayList<>();

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Element> getElements() {
        return elements;
    }


    public void showList() {

        for (Node n : this.nodes) {
            System.out.print("(" + n.getX() + ", " + n.getY() + ")");
        }
    }

    public void createElements(List<Node> nodes, Integer nH, double k, int up) {

        Element element = new Element();
        int down;
        down = up + nH;
        Integer tmpTab[] = {up, down, down + 1, up + 1};


        if (up % nH != 0) {
            element.setId(tmpTab);
            element.setK(k);
            this.elements.add(element);
        }
        up++;

        if (down < nodes.size() - 1) {
            createElements(nodes, nH, k, up);
        }


    }


    public void generateNodes2(DataFromFile dataFromFile) {
        List<Node> tmpNodes = new ArrayList<>();
        double deltaX = dataFromFile.get_B() / (dataFromFile.getN_B() - 1);
        double deltaY = dataFromFile.get_H() / (dataFromFile.getN_H() - 1);

        int p = 0;
        for (double i = 0; i <= dataFromFile.get_B(); i = i + deltaX) {
            int q = 0;
            for (double j = 0; j <= dataFromFile.get_H(); j = j + deltaY) {

                Node tmpNode = new Node();
                tmpNode.setX(i);
                tmpNode.setY(j);
                tmpNode.setT(dataFromFile.getTemperatur());
                tmpNodes.add(tmpNode);
                if (p == 0 || p == dataFromFile.getN_B() - 1 || q == 0 || q == dataFromFile.getN_H() - 1) {
                    tmpNode.setBoarderContition(true);
                }
                q++;
            }
            p++;
        }
        this.nodes = tmpNodes;
        createElements(this.nodes, dataFromFile.getN_H(), dataFromFile.getConductivity(), 1);
    }
    public void showNodes(DataFromFile dataFromFile) {
        int i = 0;
        int nodeNumber = 1;
        int line = 1;
        int iter = 1;
       /* for (Node n : this.nodes) {
            System.out.println(iter + " " + n.isBoarderContition());
            iter++;
        }*/
        for (Node n : this.nodes) {

            if (i < dataFromFile.getN_H()) {
                System.out.print(nodeNumber+" || "+n.getT());
                System.out.format("( %.3f ", n.getX());
                System.out.print(", ");
                System.out.format("%.3f ", n.getY());
                System.out.print(")   ");
                nodeNumber++;
                i++;
            } else {
                System.out.print("\n\n\n");
                line++;
                System.out.print(nodeNumber+" || "+n.getT());
                System.out.format("( %.3f ", n.getX());
                System.out.print(", ");
                System.out.format("%.3f ", n.getY());
                System.out.print(")   ");
                nodeNumber++;
                i = 1;
            }
        }
        System.out.println();
    }

    public Node getNodeByID(int id) {
        return nodes.get(id);
    }

}
