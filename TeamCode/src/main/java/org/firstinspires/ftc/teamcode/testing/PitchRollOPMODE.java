package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.FrontLeftLeg;
import org.firstinspires.ftc.teamcode.FrontRightLeg;
import org.firstinspires.ftc.teamcode.RearLeftLeg;
import org.firstinspires.ftc.teamcode.RearRightLeg;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera2;
import org.openftc.easyopencv.OpenCvPipeline;

import java.nio.file.OpenOption;

@Config
class Coeff {
    public static double pitchCoeff = 10;
    public static double rollCoeff = 10;
    public static double xCoeff = 20;
    public static double yCoeff = 20;
}

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="PitchRollTest")
public class PitchRollOPMODE extends LinearOpMode {

    public BNO055IMU     imu         = null;
    public Orientation   lastAngles  = new Orientation();

    public FrontLeftLeg frontLeft   = null;
    public FrontRightLeg frontRight  = null;
    public RearLeftLeg rearLeft    = null;
    public RearRightLeg rearRight   = null;
    public double pitch;
    public double roll = 0;
    public double actualPitch;
    public double actualRoll;
    public OpenCvCamera camera = null;

    // deschide camera si transmite datele catre laptop prin FTCDashBoard
    void openCamera() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier
                ("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera2(OpenCvInternalCamera2.CameraDirection.FRONT, cameraMonitorViewId);
        camera.setPipeline(new PipeLine());

        // ------------------ OpenCv code
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                camera.startStreaming(1280, 720, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode) {
                // ------------------ Tzeapa frate
            }

        });

        // transmit camera image to laptop
        FtcDashboard.getInstance().startCameraStream(camera, 0);
    }

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

        openCamera();

    }

    @Override
    public void runOpMode() {

        _init(); waitForStart();

        new Thread(() -> {
            while(opModeIsActive() && !isStopRequested()) {
                lastAngles = imu.getAngularOrientation();

                actualPitch = lastAngles.thirdAngle;
                actualRoll = lastAngles.firstAngle;

                roll = 0.9 * roll + 0.1 * actualRoll;

                telemetry.addData("pitch: ", pitch);
                telemetry.addData("roll: ", roll);
                telemetry.update();

                sleep(50);
            }
        }).start();

        while(!isStopRequested()) {

            frontLeft.goToPos(gamepad1.left_stick_x * Coeff.xCoeff, 300 - gamepad1.right_stick_y * Coeff.pitchCoeff + gamepad1.right_stick_x * Coeff.rollCoeff, gamepad1.left_stick_y * Coeff.yCoeff, 0, 0);
            frontRight.goToPos(gamepad1.left_stick_x * Coeff.xCoeff, 300 - gamepad1.right_stick_y * Coeff.pitchCoeff - gamepad1.right_stick_x * Coeff.rollCoeff, gamepad1.left_stick_y * Coeff.yCoeff, 0, 0);
            rearLeft.goToPos(-gamepad1.left_stick_x * Coeff.xCoeff, 300 + gamepad1.right_stick_y * Coeff.pitchCoeff + gamepad1.right_stick_x * Coeff.rollCoeff, -gamepad1.left_stick_y * Coeff.yCoeff, 0, 0);
            rearRight.goToPos(-gamepad1.left_stick_x * Coeff.xCoeff, 300 + gamepad1.right_stick_y * Coeff.pitchCoeff - gamepad1.right_stick_x * Coeff.rollCoeff, -gamepad1.left_stick_y * Coeff.yCoeff, 0, 0);

        }
    }

    class PipeLine extends OpenCvPipeline {
        boolean viewportPaused = false;

        @Override
        public Mat processFrame(Mat input) {
            // we can process the frame for image recognition
            return input;
        }

        @Override
        public void onViewportTapped() {
            viewportPaused = !viewportPaused;

            if (viewportPaused) {
                camera.pauseViewport();
            } else {
                camera.resumeViewport();
            }
        }
    }

}
