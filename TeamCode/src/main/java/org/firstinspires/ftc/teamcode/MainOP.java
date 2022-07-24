package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="MainOP")
public class MainOP extends LinearOpMode {

    public BNO055IMU     imu         = null;
    public Orientation   lastAngles  = new Orientation();

    public FrontLeftLeg  frontLeft   = null;
    public FrontRightLeg frontRight  = null;
    public RearLeftLeg   rearLeft    = null;
    public RearRightLeg  rearRight   = null;
    public double pitch;
    public double roll;

    void _init() {

        frontLeft  = new FrontLeftLeg(hardwareMap);
        frontRight = new FrontRightLeg(hardwareMap);
        rearLeft   = new RearLeftLeg(hardwareMap);
        rearRight  = new RearRightLeg(hardwareMap);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calibration status", imu.getCalibrationStatus().toString());
        telemetry.update();

    }

    @Override
    public void runOpMode() {

        _init(); waitForStart();

        new Thread(() -> {
            while(opModeIsActive() && !isStopRequested()) {
                lastAngles = imu.getAngularOrientation();

                pitch = lastAngles.thirdAngle;
                roll = lastAngles.firstAngle;

                if(pitch > 90) pitch = 180 - pitch;
                if(pitch < -90) pitch = 180 + pitch;

                telemetry.addData("pitch: ", pitch);
                telemetry.addData("roll: ", roll);
                telemetry.update();

                sleep(50);
            }
        }).start();

        while(!isStopRequested()) {

            Thread[] threads = new Thread[4];

            threads[0] = new Thread(() -> {
                interpolateFrontLeft(0, 280, 0, 500);
            });

            threads[1] = new Thread(() -> {
                interpolateFrontRight(0, 280, 0, 500);
            });

            threads[2] = new Thread(() -> {
                interpolateRearLeft(0, 280, 0, 500);
            });

            threads[3] = new Thread(() -> {
                interpolateRearRight(0, 280, 0, 500);
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

            frontRight.goToPos(X, Y, Z, pitch, -roll + 4);
            frontRight.posX = X;
            frontRight.posY = Y;
            frontRight.posZ = Z;

        }

        frontRight.goToPos(x, y, z, pitch, -roll + 4);
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

            rearRight.goToPos(x, y, z, pitch, roll + 4);
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

            rearRight.goToPos(X, Y, Z, pitch, roll + 4);
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
