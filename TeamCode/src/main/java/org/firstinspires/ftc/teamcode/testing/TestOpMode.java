package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.teamcode.testing.MathFunctions.gobildaTicksToAngle;
import static org.firstinspires.ftc.teamcode.testing.MathFunctions.revTicksToAngle;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.LegKinematics;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TestOpMode")
public class TestOpMode extends LinearOpMode {

    public DcMotorEx kneeMotor = null;
    public DcMotorEx thighMotor = null;

    @Override
    public void runOpMode() {

        kneeMotor = hardwareMap.get(DcMotorEx.class, "knee");
        thighMotor = hardwareMap.get(DcMotorEx.class, "thigh");

        kneeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        thighMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while(opModeIsActive()) {

            telemetry.addData("knee:", gobildaTicksToAngle(kneeMotor.getCurrentPosition()));
            telemetry.addData("thigh:", revTicksToAngle(thighMotor.getCurrentPosition()));

            double ans[] = LegKinematics.calc(0.1, 225, 0);
            telemetry.addData("knee angle", ans[1]);
            telemetry.addData("thigh angle", ans[0]);
            telemetry.update();

        }

    }

}
