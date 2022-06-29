package org.firstinspires.ftc.teamcode.testing;
import static org.firstinspires.ftc.teamcode.testing.Time.millisCoeff;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FrontLeftLeg;
import org.firstinspires.ftc.teamcode.LegKinematics;

@Config
class Time {
    public static double millisCoeff = 0.5;
}

@Config
class Power {
    public static double motorPower = 0.8;
}

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="GoToPos")
public class GoToPosition extends LinearOpMode {

    public FrontLeftLeg leg = null;
    double lastX = 69, lastY = 69;
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        leg = new FrontLeftLeg(hardwareMap);

        waitForStart();

        lastX = 40;  lastY = 180;

        leg.goToPos(40, 180, 0);
        sleep(500);

        while(opModeIsActive()) {

            interpolateTo(70, 220, 1000);
            interpolateTo(90, 290, 1000);
            interpolateTo(-70, 290, 2000);

        }

    }

    void interpolateTo(double x, double y, double millis) {
        millis *= millisCoeff;

        runtime.reset();
        while(runtime.milliseconds() < millis && opModeIsActive()) {

            double ratio = runtime.milliseconds() / millis;
            double X = lastX * (1 - ratio) + x * ratio;
            double Y = lastY * (1 - ratio) + y * ratio;

            leg.goToPos(X, Y, 0);

        }

        leg.goToPos(x, y, 0);

        lastX = x;
        lastY = y;
    }

}
