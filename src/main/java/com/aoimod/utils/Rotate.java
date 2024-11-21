package com.aoimod.utils;

import org.joml.Vector3f;

public class Rotate {
    public static Vector3f rotate(float angle, Vector3f xyz, float px, float py, float pz, float ax, float ay, float az) {
        Vector3f rotated = rotate(angle,
                xyz.sub(px, py, pz),
                ax, ay, az);
//        System.out.printf("PreRotated Point: (%.5f, %.5f, %.5f)%n", x, y, z);
//        System.out.printf("Rotated Point: (%.5f, %.5f, %.5f)%n", rotated[0], rotated[1], rotated[2]);
        return rotated.add(px, py, pz);
    }
    public static Vector3f rotate(float angle, Vector3f xyz, float ax, float ay, float az) {
        float degree = (float) Math.toRadians(angle);
        float[] axis = normalize(ax, ay, az);
        ax = axis[0];
        ay = axis[1];
        az = axis[2];

        float cos = (float) Math.cos(degree);
        float sin = (float) Math.sin(degree);
        float ncos = 1f - cos;
        float rx = xyz.x * (cos + ax * ax * ncos) +
                xyz.y * (ax * ay * ncos - az * sin) +
                xyz.z * (ax * az * ncos + ay * sin);
        float ry = xyz.x * (ay * ax * ncos + az * sin) +
                xyz.y * (cos + ay * ay * ncos) +
                xyz.z * (ay * az * ncos - ax * sin);
        float rz = xyz.x * (az * ax * ncos - ay * sin) +
                xyz.y * (az * ay * ncos + ax * sin) +
                xyz.z * (cos + az * az * ncos);
        return new Vector3f(rx, ry, rz);
    }

    public static float[] normalize(float x, float y, float z) {
        float length = (float) Math.sqrt(x * x + y * y + z * z);
        return new float[] { x / length, y / length, z / length };
    }
}
