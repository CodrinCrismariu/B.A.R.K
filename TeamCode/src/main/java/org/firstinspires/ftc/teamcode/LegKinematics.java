package org.firstinspires.ftc.teamcode;

import static java.lang.Math.*;

public class LegKinematics {
    final static double l1 = 142.115;
    final static double l2 = 124.793;
    final static double l3 = 57.4;

    public static double[] calc(double x, double y, double z) {

        try {
            double sq = sqrt(y * y + z * z);
            double alfa3 = acos(y / (sq));
            ///y=y'
            y = sq;
            y -= l3;
            double alfa2 = -acos((x * x + y * y - l1 * l1 - l2 * l2) / (2 * l1 * l2));
            double alfa1 = atan(y / x) - atan((l2 * sin(alfa2)) / (l1 + l2 * cos(alfa2)));

            if (alfa1 < 0) {
                alfa1 += Math.PI;
            }

            return new double[]{alfa1, alfa2, alfa3};
        } catch (Exception e) {

            return new double[]{};
        }
    }
}
