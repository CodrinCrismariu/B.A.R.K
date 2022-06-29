package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
class LegConfig {
    public static double kneePower = 0.05;
    public static double thighPower = 0.05;
};

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MotorHoldPosition")
public class HoldPosition extends LinearOpMode {

    public DcMotorEx kneeMotor = null;
    public DcMotorEx thighMotor = null;

    @Override
    public void runOpMode() {

        kneeMotor = hardwareMap.get(DcMotorEx.class, "knee");
        thighMotor = hardwareMap.get(DcMotorEx.class, "thigh");

        kneeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        kneeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        kneeMotor.setTargetPosition(0);
        kneeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        thighMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        thighMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        thighMotor.setTargetPosition(0);
        thighMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        while(opModeIsActive()) {

            telemetry.addData("knee:", kneeMotor.getCurrentPosition());
            telemetry.addData("thigh:", thighMotor.getCurrentPosition());
            telemetry.update();

            kneeMotor.setPower(LegConfig.kneePower * Math.signum(kneeMotor.getCurrentPosition()));
            thighMotor.setPower(LegConfig.thighPower * Math.signum(thighMotor.getCurrentPosition()));

        }

    }

}
