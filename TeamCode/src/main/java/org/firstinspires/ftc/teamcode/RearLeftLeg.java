package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
class RearLeftConfig {
    public static double gobildaCoeff1 = 1;
    public static double gobildaCoeff2 = 1;
    public static double revCoeff1 = -1;
    public static double revCoeff2 = -1;
}

public class RearLeftLeg {
    static String name = "rearLeft";
    double gobildaTicksPerRev = 2786.2;
    double revTicksPerRev = 1680;

    public int kneeHigh = -50;
    public int kneeLow = -770;

    public int thighLow = 20;
    public int thighHigh = 300;

    public double posX = -1;
    public double posY = -1;
    public double posZ = -1;

    public DcMotorEx kneeMotor;
    public DcMotorEx thighMotor;
    public Servo elbowServo;

    public RearLeftLeg(HardwareMap hardwareMap) {
        kneeMotor = hardwareMap.get(DcMotorEx.class, name + "Knee");
        thighMotor = hardwareMap.get(DcMotorEx.class, name + "Thigh");
        elbowServo = hardwareMap.get(Servo.class, name + "Elbow");

        kneeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        thighMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        kneeMotor.setTargetPosition(kneeMotor.getCurrentPosition());
        kneeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        kneeMotor.setPower(0.8);

        thighMotor.setTargetPosition(thighMotor.getCurrentPosition());
        thighMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        thighMotor.setPower(0.8);

        elbowServo.setPosition(0.45);
    }

    public void goToPos(double x, double y, double z, double pitch, double roll) {
        double[] ans = LegKinematics.calc(x, y, z);

        if(ans.length == 0) return;

        int kneePos = gobildaRawAngleToTicks(ans[1]);
        int thighPos = revRawAngleToTicks(ans[0]);

        if(kneeLow <= kneePos && kneePos <= kneeHigh)
            kneeMotor.setTargetPosition(kneePos);

        if(thighLow <= thighPos && thighPos <= thighHigh)
            thighMotor.setTargetPosition(thighPos);

        double servoAngle = servoRawAngleToPos(ans[2]) + roll / 300 * 3.8888 - 0.05;
            elbowServo.setPosition(servoAngle);
    }

    public double servoRawAngleToPos(double angle) {
        return angle / Math.PI / 2 * 360 / 300 * 3.88888 + 0.5;
    }

    public int gobildaRawAngleToTicks(double angle) {
        double actualAngle = -(angle + 1.13866) * RearLeftConfig.gobildaCoeff1- 0.6043 * RearLeftConfig.gobildaCoeff2;
        //                               ^         ^
        //            value from kinematics       encoder value for rl position x = 0, y = 25
        return (int)(actualAngle / 2 / Math.PI * gobildaTicksPerRev);
    }

    public int revRawAngleToTicks(double angle) {
        double actualAngle = (angle - 2.098171) * RearLeftConfig.revCoeff1 - 0.4525 * RearLeftConfig.revCoeff2;
        //                               ^         ^
        //            value from kinematics       encoder value for rl position x = 0, y = 25

        return (int)(actualAngle / 2 / Math.PI * revTicksPerRev);
    }

}
