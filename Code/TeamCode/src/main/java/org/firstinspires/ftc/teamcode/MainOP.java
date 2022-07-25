package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.io.IOException;

@Config
class SpeedCoeff {
    public static double speed = 4;
}

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MainOP")
public class MainOP extends LinearOpMode {

    public BNO055IMU     imu         = null;
    public Orientation   lastAngles  = new Orientation();

    public FrontLeftLeg  frontLeft   = null;
    public FrontRightLeg frontRight  = null;
    public RearLeftLeg   rearLeft    = null;
    public RearRightLeg  rearRight   = null;
    public double pitch;
    public double roll = 0;
    public double actualPitch;
    public double actualRoll;
    public MediaPlayer player=null;

    void _init() {

        frontLeft  = new FrontLeftLeg(hardwareMap);
        frontRight = new FrontRightLeg(hardwareMap);
        rearLeft   = new RearLeftLeg(hardwareMap);
        rearRight  = new RearRightLeg(hardwareMap);
        try {
            player = new MediaPlayer();
            player.setVolume(1, 1);
            player.setDataSource("/storage/emulated/0/Music/dogbarking.mp3");
            player.prepareAsync();
        }catch (IOException e){
            telemetry.addData("TEST",e);
            telemetry.update();
        }

//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//
//        parameters.mode                = BNO055IMU.SensorMode.IMU;
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.loggingEnabled      = false;
//
//        imu = hardwareMap.get(BNO055IMU.class, "imu");
//        imu.initialize(parameters);
//
//
//        telemetry.addData("Mode", "calibrating...");
//        telemetry.update();
//
//        // make sure the imu gyro is calibrated before continuing.
//        while (!isStopRequested() && !imu.isGyroCalibrated())
//        {
//            sleep(50);
//            idle();
//        }

//        telemetry.addData("Mode", "waiting for start");
//        telemetry.addData("imu calibration status", imu.getCalibrationStatus().toString());
//        telemetry.update();

    }

    @Override
    public void runOpMode() {

        _init(); waitForStart();

//        new Thread(() -> {
//            while(opModeIsActive() && !isStopRequested()) {
////                lastAngles = imu.getAngularOrientation();
////
////                actualPitch = lastAngles.thirdAngle;
////                actualRoll = lastAngles.firstAngle;
//
////                roll = 0.9 * roll + 0.1 * actualRoll;
////
////                telemetry.addData("pitch: ", pitch);
////                telemetry.addData("roll: ", roll);
////                telemetry.update();
//
//                sleep(50);
//            }
//        }).start();

        while(!isStopRequested()) {
            if(!player.isPlaying())
                player.start();

            Thread[] threads = new Thread[4];
//              DYNAMICALLY STABLE GATE NOT WORKING RN
//            threads[0] = new Thread(() -> {
//                interpolateFrontLeft(-20, 290, 0, 900);
//                interpolateFrontLeft(10, 220, 0, 100);
//                interpolateFrontLeft(20, 290, 0, 100);
//                interpolateFrontLeft(-10, 290, 0, 900);
//            });
//
//            threads[1] = new Thread(() -> {
//                interpolateFrontRight(10, 220, 0, 100);
//                interpolateFrontRight(20, 290, 0, 100);
//                interpolateFrontRight(-20, 290, 0, 1800);
//            });
//
//            threads[2] = new Thread(() -> {
//                interpolateRearLeft(-10, 220, 0, 100);
//                interpolateRearLeft(-20, 290, 0, 100);
//                interpolateRearLeft(20, 290, 0, 1800);
//            });
//
//            threads[3] = new Thread(() -> {
//                interpolateRearRight(20, 290, 0, 900);
//                interpolateRearRight(-10, 220, 0, 100);
//                interpolateRearRight(-20, 290, 0, 100);
//                interpolateRearRight(10, 290, 0, 900);
//            });

            threads[0] = new Thread(() -> {
                interpolateFrontLeft(10, 280, 0, 100 * SpeedCoeff.speed);
                interpolateFrontLeft(30, 300, 0, 100 * SpeedCoeff.speed);
                interpolateFrontLeft(20, 300, -20, 100 * SpeedCoeff.speed);
                interpolateFrontLeft(10, 300, 0, 100 * SpeedCoeff.speed);
                interpolateFrontLeft(0, 300, -20, 100 * SpeedCoeff.speed);
                interpolateFrontLeft(10, 300, 0, 100 * SpeedCoeff.speed);
                interpolateFrontLeft(-20, 300, 20, 100 * SpeedCoeff.speed);
                interpolateFrontLeft(-30, 300, 0, 100 * SpeedCoeff.speed);
            });

            threads[1] = new Thread(() -> {
                interpolateFrontRight(0, 300, 20, 100 * SpeedCoeff.speed);
                interpolateFrontRight(-10, 300, 0, 100 * SpeedCoeff.speed);
                interpolateFrontRight(-20, 300, -20, 100 * SpeedCoeff.speed);
                interpolateFrontRight(-30, 300, 0, 100 * SpeedCoeff.speed);
                interpolateFrontRight(10, 280, 0, 100 * SpeedCoeff.speed);
                interpolateFrontRight(30, 300, 0, 100 * SpeedCoeff.speed);
                interpolateFrontRight(20, 300, 20, 100 * SpeedCoeff.speed);
                interpolateFrontRight(10, 300, 0, 100 * SpeedCoeff.speed);
            });

            threads[2] = new Thread(() -> {
                interpolateRearLeft(-20, 300, -20, 100 * SpeedCoeff.speed);
                interpolateRearLeft(-10, 300, 0, 100 * SpeedCoeff.speed);
                interpolateRearLeft(0, 300, 20, 100 * SpeedCoeff.speed);
                interpolateRearLeft(10, 300, 0, 100 * SpeedCoeff.speed);
                interpolateRearLeft(20, 300, 20, 100 * SpeedCoeff.speed);
                interpolateRearLeft(30, 300, 0, 100 * SpeedCoeff.speed);
                interpolateRearLeft(-10, 280, 0, 100 * SpeedCoeff.speed);
                interpolateRearLeft(-30, 300, 0, 100 * SpeedCoeff.speed);
            });

            threads[3] = new Thread(() -> {
                interpolateRearRight(20, 300, -20, 100 * SpeedCoeff.speed);
                interpolateRearRight(30, 300, 0, 100 * SpeedCoeff.speed);
                interpolateRearRight(-10, 280, 0, 100 * SpeedCoeff.speed);
                interpolateRearRight(-30, 300, 0, 100 * SpeedCoeff.speed);
                interpolateRearRight(-20, 300, 20, 100 * SpeedCoeff.speed);
                interpolateRearRight(-10, 300, 0, 100 * SpeedCoeff.speed);
                interpolateRearRight(0, 300, -20, 100 * SpeedCoeff.speed);
                interpolateRearRight(10, 300, 0, 100 * SpeedCoeff.speed);
            });



            for(int i = 0; i < 4; i++)
                if(threads[i] != null)
                    threads[i].start();

            for(int i = 0; i < 4; i++)
                if(threads[i] != null) {
                    try {
                        threads[i].join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        }
    }



    // ------------------------ LEG INTERPOLATION CODE ------------------------

    public void interpolateFrontLeft(double x,
                                     double y,
                                     double z,    // position in mm
                                     double time) // time in milliseconds
    {

        if(frontLeft == null) return;

        double startX = frontLeft.posX;
        double startY = frontLeft.posY;
        double startZ = frontLeft.posZ;

        if(startY == -1) {

            frontLeft.goToPos(x, y, z, pitch, -roll);
            frontLeft.posX = x;
            frontLeft.posY = y;
            frontLeft.posZ = z;

            return;

        }

        ElapsedTime   runtime     = new ElapsedTime();
        runtime.reset();

        while(runtime.milliseconds() < time && !isStopRequested()) {

            double ratio = runtime.milliseconds() / time;
            double X = startX * (1 - ratio) + x * ratio;
            double Y = startY * (1 - ratio) + y * ratio;
            double Z = startZ * (1 - ratio) + z * ratio;

            frontLeft.goToPos(X, Y, Z, pitch, -roll);
            frontLeft.posX = X;
            frontLeft.posY = Y;
            frontLeft.posZ = Z;

        }

        frontLeft.goToPos(x, y, z, pitch, -roll);
        frontLeft.posX = x;
        frontLeft.posY = y;
        frontLeft.posZ = z;

    }

    public void interpolateFrontRight(double x,
                                      double y,
                                      double z,    // position in mm
                                      double time) // time in milliseconds
    {

        if(frontRight == null) return;

        double startX = frontRight.posX;
        double startY = frontRight.posY;
        double startZ = frontRight.posZ;

        if(startY == -1) {

            frontRight.goToPos(x, y, z, pitch, -roll + 4);
            frontRight.posX = x;
            frontRight.posY = y;
            frontRight.posZ = z;

            return;

        }

        ElapsedTime   runtime     = new ElapsedTime();
        runtime.reset();

        while(runtime.milliseconds() < time && !isStopRequested()) {

            double ratio = runtime.milliseconds() / time;
            double X = startX * (1 - ratio) + x * ratio;
            double Y = startY * (1 - ratio) + y * ratio;
            double Z = startZ * (1 - ratio) + z * ratio;

            frontRight.goToPos(X, Y, Z, pitch, -roll);
            frontRight.posX = X;
            frontRight.posY = Y;
            frontRight.posZ = Z;

        }

        frontRight.goToPos(x, y, z, pitch, -roll);
        frontRight.posX = x;
        frontRight.posY = y;
        frontRight.posZ = z;

    }

    public void interpolateRearLeft(double x,
                                    double y,
                                    double z,    // position in mm
                                    double time) // time in milliseconds
    {

        if(rearLeft == null) return;

        double startX = rearLeft.posX;
        double startY = rearLeft.posY;
        double startZ = rearLeft.posZ;

        if(startY == -1) {

            rearLeft.goToPos(x, y, z, pitch, roll);
            rearLeft.posX = x;
            rearLeft.posY = y;
            rearLeft.posZ = z;

            return;

        }

        ElapsedTime   runtime     = new ElapsedTime();
        runtime.reset();

        while(runtime.milliseconds() < time && !isStopRequested()) {

            double ratio = runtime.milliseconds() / time;
            double X = startX * (1 - ratio) + x * ratio;
            double Y = startY * (1 - ratio) + y * ratio;
            double Z = startZ * (1 - ratio) + z * ratio;

            rearLeft.goToPos(X, Y, Z, pitch, roll);
            rearLeft.posX = X;
            rearLeft.posY = Y;
            rearLeft.posZ = Z;

        }

        rearLeft.goToPos(x, y, z, pitch, roll);
        rearLeft.posX = x;
        rearLeft.posY = y;
        rearLeft.posZ = z;

    }

    public void interpolateRearRight(double x,
                                     double y,
                                     double z,    // position in mm
                                     double time) // time in milliseconds
    {

        if(rearRight == null) return;

        double startX = rearRight.posX;
        double startY = rearRight.posY;
        double startZ = rearRight.posZ;

        if(startY == -1) {

            rearRight.goToPos(x, y, z, pitch, roll);
            rearRight.posX = x;
            rearRight.posY = y;
            rearRight.posZ = z;

            return;

        }

        ElapsedTime   runtime     = new ElapsedTime();
        runtime.reset();

        while(runtime.milliseconds() < time && !isStopRequested()) {

            double ratio = runtime.milliseconds() / time;
            double X = startX * (1 - ratio) + x * ratio;
            double Y = startY * (1 - ratio) + y * ratio;
            double Z = startZ * (1 - ratio) + z * ratio;

            rearRight.goToPos(X, Y, Z, pitch, roll);
            rearRight.posX = X;
            rearRight.posY = Y;
            rearRight.posZ = Z;

        }

        rearRight.goToPos(x, y, z, pitch, roll + 4);
        rearRight.posX = x;
        rearRight.posY = y;
        rearRight.posZ = z;

    }

}
