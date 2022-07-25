package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="ResetEncoders")
public class ResetEncoders extends LinearOpMode {

    public DcMotorEx[] kneeMotors = new DcMotorEx[4];
    public DcMotorEx[] thighMotors = new DcMotorEx[4];
    public String[] motorNames = { "frontLeft", "frontRight", "rearLeft", "rearRight" };

    @Override
    public void runOpMode() {

        for(int i = 0; i < 4; i++) {
            kneeMotors[i] = hardwareMap.get(DcMotorEx.class, motorNames[i] + "Knee");
            thighMotors[i] = hardwareMap.get(DcMotorEx.class, motorNames[i] + "Thigh");
        }

        waitForStart();

        for(int i = 0; i < 4; i++) {

            kneeMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            kneeMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            thighMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            thighMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }

    }

}
