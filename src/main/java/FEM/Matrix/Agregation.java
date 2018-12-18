package FEM.Matrix;

import FEM.model.Grid;

public class Agregation {


    double[][] H_global;
    double[] P_global;

    public void function(int nh,int el_nr, Grid grid, double [][]H_current, double[]P_current) {
        H_global = new double[nh][nh];
        P_global = new double[nh];

        for (int i = 0; i < nh; i++)
        {
            for (int j = 0; j < nh; j++) {
                H_global[i][j] = 0;
            }
            P_global[i] = 0;
        }

        for (int i = 0;i < 4; i++)
        {
            for (int j = 0; j < 4; j++) {
              //  H_global[grid.elemEL[el_nr].globalNodeID[i]][grid.elemEL[el_nr].globalNodeID[j]] += H_current[i][j];
            }
           // P_global[grid.elemEL[el_nr].globalNodeID[i]] += P_current[i];

        }
    }
}
