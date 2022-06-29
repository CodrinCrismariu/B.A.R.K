package org.firstinspires.ftc.teamcode.testing;

public class MathFunctions {
    static double gobildaTicksPerRev = 2786.2;
    static double revTicksPerRev = 1680;

    public static double gobildaTicksToAngle(int encoderTicks) {
        return ((double)encoderTicks) / gobildaTicksPerRev * 2 * Math.PI;
    }
    public static double revTicksToAngle(int encoderTicks) {
        return ((double)encoderTicks) / revTicksPerRev * 2 * Math.PI;
    }
}
