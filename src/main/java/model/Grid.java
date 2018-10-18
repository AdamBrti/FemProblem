package model;

import FileOperation.DataFromFile;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private List<Node> nodes;
    private List<Element> elements = new ArrayList<>();

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public void showList() {

        for (Node n : this.nodes) {
            System.out.print("(" + n.getX() + ", " + n.getY() + ")");
        }
    }

    public void createElements(List<Node> nodes, Integer nH, Double k, int up) {

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

    public void generateNodes(DataFromFile dataFromFile) {
        List<Node> tmpNodes = new ArrayList<>();
        Integer deltaX = dataFromFile.getL() / (dataFromFile.getnL() - 1);
        Integer deltaY = dataFromFile.getH() / (dataFromFile.getnH() - 1);

        for (int i = 0; i <= dataFromFile.getL(); i = i + deltaX) {

            for (int j = 0; j <= dataFromFile.getH(); j = j + deltaY) {

                Node tmpNode = new Node();
                tmpNode.setX(i);
                tmpNode.setY(j);
                tmpNode.setT(dataFromFile.getT());
                tmpNodes.add(tmpNode);

            }

        }

        this.nodes = tmpNodes;
        createElements(this.nodes, dataFromFile.getnH(), 5.0, 1);
        int ps = 1;
        for (Element e : this.elements) {
            System.out.print(ps + "[ ");
            e.getId();
            System.out.print("]\n");
            ps++;
        }
    }

    public void showNodes(DataFromFile dataFromFile) {
        int i = 0;
        int nodeNumber = 1;
        int line = 1;
        for (Node n : this.nodes) {

            if (i < dataFromFile.getnH()) {
                System.out.print(nodeNumber + "(" + n.getX() + ", " + n.getY() + ")   ");
                nodeNumber++;
                i++;
            } else {
                System.out.print("\n\n\n");
                line++;
                System.out.print(nodeNumber + "(" + n.getX() + ", " + n.getY() + ")   ");
                nodeNumber++;
                i = 1;
            }

        }
    }


}
