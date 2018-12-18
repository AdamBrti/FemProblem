package FEM.Matrix;

import FEM.model.Point;

public class VectorP {

    public double[] buildVectorP(Point P1, double detJ, double ambT, double alfa) {

        detJ=detJ/2.0;

        double vectorP1[] = new double[4];
        vectorP1[0] = alfa * ambT * 0.25 * (1 - P1.getX()) * (1 - P1.getY()) * detJ;
        vectorP1[1] = alfa * ambT * 0.25 * (1 + P1.getX()) * (1 - P1.getY()) * detJ;
        vectorP1[2] = alfa * ambT * 0.25 * (1 + P1.getX()) * (1 + P1.getY()) * detJ;
        vectorP1[3] = alfa * ambT * 0.25 * (1 - P1.getX()) * (1 + P1.getY()) * detJ;
        return vectorP1;

    }
}



