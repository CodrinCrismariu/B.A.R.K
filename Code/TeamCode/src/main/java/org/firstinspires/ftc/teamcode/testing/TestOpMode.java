package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.teamcode.testing.MathFunctions.gobildaTicksToAngle;
import static org.firstinspires.ftc.teamcode.testing.MathFunctions.revTicksToAngle;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.LegKinematics;

@Config
class MotorConfig {
    public static String motorName = "frontLeft";
}

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TestOpMode")
public class TestOpMode extends LinearOpMode {

    public DcMotorEx kneeMotor = null;
    public DcMotorEx thighMotor = null;

    public Servo frElbow = null;
    public Servo flElbow = null;
    public Servo rrElbow = null;
    public Servo rlElbow = null;

    @Override
    public void runOpMode() {

        kneeMotor = hardwareMap.get(DcMotorEx.class, MotorConfig.motorName + "Knee");
        thighMotor = hardwareMap.get(DcMotorEx.class, MotorConfig.motorName + "Thigh");

        frElbow = hardwareMap.get(Servo.class, "frontRightElbow");
        flElbow = hardwareMap.get(Servo.class, "frontLeftElbow");
        rrElbow = hardwareMap.get(Servo.class, "rearRightElbow");
        rlElbow = hardwareMap.get(Servo.class, "rearLeftElbow");

        frElbow.setPosition(0.5);
        flElbow.setPosition(0.5);
        rrElbow.setPosition(0.5);
        rlElbow.setPosition(0.5);

        kneeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        thighMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while(opModeIsActive()) {

            telemetry.addData("knee:", kneeMotor.getCurrentPosition());
            telemetry.addData("thigh:", thighMotor.getCurrentPosition());
            telemetry.update();

        }

    }

}
