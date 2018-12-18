package FEM.model;

import static java.lang.StrictMath.sqrt;

public class IntegralPointP {

    public Point[] P1 = new Point[4];
    public Point[] P2 = new Point[4];
    double tmp = 1 / sqrt(3);

    public IntegralPointP() {
        P1[0] = new Point(-tmp, -1);
        P1[1] = new Point(1, -tmp);
        P1[2] = new Point(tmp, 1);
        P1[3] = new Point(-1, tmp);

        P2[0] = new Point(tmp, -1);
        P2[1] = new Point(1, tmp);
        P2[2] = new Point(-tmp, 1);
        P2[3] = new Point(-1, -tmp);
    }


}
