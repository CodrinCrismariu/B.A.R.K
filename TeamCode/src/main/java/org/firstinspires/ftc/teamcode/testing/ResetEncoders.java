package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="ResetEncoders")
public class ResetEncoders extends LinearOpMode {

    public DcMotorEx kneeMotor = null;
    public DcMotorEx thighMotor = null;

    @Override
    public void runOpMode() {

        kneeMotor = hardwareMap.get(DcMotorEx.class, "knee");
        thighMotor = hardwareMap.get(DcMotorEx.class, "thigh");

        waitForStart();

        kneeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        kneeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        thighMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        thighMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }

}
