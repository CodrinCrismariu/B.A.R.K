package org.firstinspires.ftc.teamcode.testing;

import static java.lang.Math.max;

import android.media.MediaPlayer;

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
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera2;
import org.openftc.easyopencv.OpenCvPipeline;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;

@Config
class Coeff {
    public static double pitchCoeff = 10;
    public static double rollCoeff = 10;
    public static double xCoeff = 20;
    public static double yCoeff = 20;
}

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "PitchRollTest")
public class PitchRollOPMODE extends LinearOpMode {

    public BNO055IMU imu = null;
    public Orientation lastAngles = new Orientation();

    public FrontLeftLeg frontLeft = null;
    public FrontRightLeg frontRight = null;
    public RearLeftLeg rearLeft = null;
    public RearRightLeg rearRight = null;
    public double pitch;
    public double roll = 0;
    public double actualPitch;
    public double actualRoll;
    public double X = 320;
    public double Y = 0;
    public double look = 0;
    public OpenCvCamera camera = null;
    public MediaPlayer player = null;

    // deschide camera si transmite datele catre laptop prin FTCDashBoard
    void openCamera() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier
                ("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera2(OpenCvInternalCamera2.CameraDirection.BACK, cameraMonitorViewId);
        camera.setPipeline(new PipeLine());

        // ------------------ OpenCv code
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_LEFT);
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

        frontLeft = new FrontLeftLeg(hardwareMap);
        frontRight = new FrontRightLeg(hardwareMap);
        rearLeft = new RearLeftLeg(hardwareMap);
        rearRight = new RearRightLeg(hardwareMap);

        try {
            player = new MediaPlayer();
            player.setVolume(1, 1);
            player.setDataSource("/storage/emulated/0/Music/dogbarking.mp3");
            player.prepareAsync();
        } catch (IOException e) {
            telemetry.addData("TEST", e);
            telemetry.update();
        }

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !imu.isGyroCalibrated()) {
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

        _init();
        waitForStart();

        new Thread(() -> {
            while (opModeIsActive() && !isStopRequested()) {
                lastAngles = imu.getAngularOrientation();

                actualPitch = lastAngles.thirdAngle;
                actualRoll = lastAngles.firstAngle;


                pitch = 0.9 * pitch + 0.1 * actualPitch;
                roll = 0.9 * roll + 0.1 * actualRoll;

//                telemetry.addData("pitch: ", pitch);
//                telemetry.addData("roll: ", roll);
//                telemetry.update();

                sleep(50);
            }
        }).start();

        while (!isStopRequested()) {

            if (!player.isPlaying() && gamepad1.a)
                player.start();

            if (X > 420) {
                look += 0.1;
            } else if (X < 210) {
                look -= 0.1;
            }

            if (look > 1) look = 1;
            if (look < -1) look = -1;

            frontLeft.goToPos(gamepad1.left_stick_x * Coeff.xCoeff,
                    300 + gamepad1.right_stick_y * Coeff.pitchCoeff + gamepad1.right_stick_x * Coeff.rollCoeff,
                    gamepad1.left_stick_y * Coeff.yCoeff,
                    pitch,
                    -roll + look * Coeff.rollCoeff);
            frontRight.goToPos(gamepad1.left_stick_x * Coeff.xCoeff,
                    300 + gamepad1.right_stick_y * Coeff.pitchCoeff - gamepad1.right_stick_x * Coeff.rollCoeff,
                    gamepad1.left_stick_y * Coeff.yCoeff,
                    pitch,
                    -roll + look * Coeff.rollCoeff);
            rearLeft.goToPos(-gamepad1.left_stick_x * Coeff.xCoeff,
                    300 - gamepad1.right_stick_y * Coeff.pitchCoeff + gamepad1.right_stick_x * Coeff.rollCoeff,
                    -gamepad1.left_stick_y * Coeff.yCoeff,
                    pitch,
                    roll + look * Coeff.rollCoeff);
            rearRight.goToPos(-gamepad1.left_stick_x * Coeff.xCoeff,
                    300 - gamepad1.right_stick_y * Coeff.pitchCoeff - gamepad1.right_stick_x * Coeff.rollCoeff,
                    -gamepad1.left_stick_y * Coeff.yCoeff,
                    pitch,
                    roll + look * Coeff.rollCoeff);

        }
    }

    public Mat mat = new Mat();

    class PipeLine extends OpenCvPipeline {
        boolean viewportPaused = false;

        @Override
        public Mat processFrame(Mat input) {

            Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

            // define a lower_bound and upper_bound in HSV for color red
            Scalar lowHSV = new Scalar(160, 150, 100);
            Scalar highHSV = new Scalar(180, 255, 255);

            Core.inRange(mat, lowHSV, highHSV, mat);

            final List<MatOfPoint> points = new ArrayList<>();
            final Mat hierarchy = new Mat();
            Imgproc.findContours(mat, points, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

            int mx = 0;
            for (int i = 0; i < points.size(); i++) {
                Rect boundRect = Imgproc.boundingRect(points.get(i));
                mx = max(mx, (int) boundRect.area());
            }

            int x = 0, y = 0;
            if (mx > 50) {
                for (int i = 0; i < points.size(); i++) {
                    Rect boundRect = Imgproc.boundingRect(points.get(i));
                    if ((int) boundRect.area() == mx) {
                        Imgproc.rectangle(input, boundRect, new Scalar(0, 255, 0));
                        x = boundRect.x + boundRect.height / 2;
                        y = boundRect.y + boundRect.width / 2;

                        X = x;
                        Y = y;
                    }
                }

            } else {
                X = 320;
                Y = 0;
            }

            Core.flip(input, input, -1);
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