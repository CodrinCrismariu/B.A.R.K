package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class RearLeftLeg {
    static String name = "rearLeft";
    double gobildaTicksPerRev = 2786.2;
    double revTicksPerRev = 1680;

    public int kneeHigh = -50;
    public int kneeLow = -770;

    public int thighHigh = -20;
    public int thighLow = -300;

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

        elbowServo.setPosition(0.5);
    }

    public void goToPos(double x, double y, double z) {
        double[] ans = LegKinematics.calc(x, y, z);

        int kneePos = gobildaRawAngleToTicks(ans[1]);
        int thighPos = revRawAngleToTicks(ans[0]);

        if(kneeLow <= kneePos && kneePos <= kneeHigh)
            kneeMotor.setTargetPosition(kneePos);

        if(thighLow <= thighPos && thighPos <= thighHigh)
            thighMotor.setTargetPosition(thighPos);

        elbowServo.setPosition(servoRawAngleToPos(ans[2]));
    }

    public double servoRawAngleToPos(double angle) {
        return angle / Math.PI / 2 * 360 / 300 + 0.5;
    }

    public int gobildaRawAngleToTicks(double angle) {
        double actualAngle = -(angle + 1.13866) - 0.6043;
        //                               ^         ^
        //            value from kinematics       encoder value for rl position x = 0, y = 25
        return (int)(actualAngle / 2 / Math.PI * gobildaTicksPerRev);
    }

    public int revRawAngleToTicks(double angle) {
        double actualAngle = (angle - 2.098171) - 0.4525;
        //                               ^         ^
        //            value from kinematics       encoder value for rl position x = 0, y = 25

        return (int)(actualAngle / 2 / Math.PI * revTicksPerRev);
    }

}
