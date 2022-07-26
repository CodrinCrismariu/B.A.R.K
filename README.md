# B.A.R.K


Big Automated Robot Kanine or B.A.R.K. represents our project for InfoEducatie.

## CAD
https://a360.co/3bcF5bw
![Cad Screenshot](/assets/image1.jpeg)
![Cad Screenshot](/assets/image2.jpeg)


## Hardware

**2 Hubs:** https://www.revrobotics.com/rev-31-1153/

**4 goBILDA 60RPM motors:** https://www.gobilda.com/5203-series-yellow-jacket-planetary-gear-motor-99-5-1-ratio-24mm-length-8mm-rex-shaft-60-rpm-3-3-5v-encoder/

**2 REV motors:** https://www.revrobotics.com/rev-41-1600/

**2 goBILDA 117RPM motor:** https://www.gobilda.com/5203-series-yellow-jacket-planetary-gear-motor-50-9-1-ratio-24mm-length-8mm-rex-shaft-117-rpm-3-3-5v-encoder/

**3 REV Servos:** https://www.revrobotics.com/rev-41-1097/

**1 goBILDA Torque servo:** https://www.gobilda.com/2000-series-dual-mode-servo-25-2-torque/

**4 goBILDA ServoBlocks:** https://www.gobilda.com/servoblock-standard-size-25-tooth-spline-hub-shaft/

**2 goBILDA Matrix batteries:** https://www.gobilda.com/matrix-12v-3000mah-nimh-battery/

## Other Materials

**3D printer filament:** black/blue pla

**Tubes:** pvc/ppr tuber

## Deployment

First reset the encoders

```
ResetEncoders.java
```

Run the main sdk

```
PitchRollOPMODE.java
```

## Notable code

Switching from ticks to angle
```java
public class MathFunctions {
    static double gobildaTicksPerRev = 2786.2;
    static double revTicksPerRev = 1680;

    public static double gobildaTicksToAngle(int encoderTicks) {
        return ((double)encoderTicks) / gobildaTicksPerRev * 2 * Math.PI;
    }
    public static double revTicksToAngle(int encoderTicks) {
        return ((double)encoderTicks) / revTicksPerRev * 2 * Math.PI;
    }
}
```

Invers kinematics
```java
public class LegKinematics {
    final static double l1 = 142.115;
    final static double l2 = 124.793;
    final static double l3 = 57.4;

    public static double[] calc(double x, double y, double z) {

        try {
            double sq = sqrt(y * y + z * z);
            double alfa3 = asin(z / (sq));
            y = sq;
            y -= l3;
            double alfa2 = -acos((x * x + y * y - l1 * l1 - l2 * l2) / (2 * l1 * l2));
            double alfa1 = atan(y / x) - atan((l2 * sin(alfa2)) / (l1 + l2 * cos(alfa2)));

            if (alfa1 < 0) 
                alfa1 += Math.PI;
            
            return new double[]{alfa1, alfa2, alfa3};
        } catch (Exception e) {
            return new double[]{};
        }
    }
}
```

