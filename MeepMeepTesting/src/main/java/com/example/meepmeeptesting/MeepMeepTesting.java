package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.geometry.*;
import com.acmerobotics.roadrunner.path.heading.SplineInterpolator;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Vector;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // Declare a MeepMeep instance
        // With a field size of 800 pixels
        MeepMeep meepMeep = new MeepMeep(800);

        double width = 13.46;
        double height = 17.55;
        final Pose2d startPosition = new Pose2d(-39.5669291, -70 + 17.55 / 2, Math.toRadians(90));
        double var = 61;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Required: Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 30, Math.toRadians(180), Math.toRadians(180), 12)
                // Option: Set theme. Default = ColorSchemeRedDark()
                .setColorScheme(new ColorSchemeRedDark())
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPosition)
                                .lineToLinearHeading(new Pose2d(-35, -20, Math.toRadians(0)))
                                .addTemporalMarker(() -> {
//                                    robot.openArm();
                                })
                                .addTemporalMarker(3, () -> {
//                                    robot.turretHeightServo.setPosition(turretStored);
//                                    robot.turretRotationServo.setPosition(turretMiddlePos);
                                })
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-60, -56, Math.toRadians(180)), Math.toRadians(180))
                                .setReversed(false)
                                .waitSeconds(0.2)
                                .addTemporalMarker(() -> {
//                                    robot.oneTurnCarousel();
//                                    robot.intakeOn = true;
                                })
                                .waitSeconds(1)
                                .lineToLinearHeading(new Pose2d(-56, -50, Math.toRadians(-90)))
                                .lineToLinearHeading(new Pose2d(-60, -60, Math.toRadians(-45)))
                                .strafeTo(new Vector2d(-26, -60))
                                .lineToLinearHeading(new Pose2d(-22, -66, Math.toRadians(-90)))
                                .waitSeconds(0.5)
                                .lineToLinearHeading(new Pose2d(-40, -55, Math.toRadians(-135)))
                                .waitSeconds(2)
                                .addTemporalMarker(() -> {
//                                    robot.intakeOn = false;
                                })
//                                .addTemporalMarker(() -> robot.liftAsync(3))
                                .waitSeconds(1)
//                                .addTemporalMarker(() -> robot.releaseAsync())
                                .waitSeconds(0.5)
//                                .addTemporalMarker(() -> robot.returnLiftAsync())
                                .splineToSplineHeading(new Pose2d(-65, -35, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );

        myBot.setDimensions(width, height);

        // Set field image
        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                // Background opacity from 0-1
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}