package com.winsontan520.myopencv;

/**
 * Created by Winson Tan on 17/9/17.
 */

public class MyNativeUtil {
    public static native long fastDetection(long addrRgba, long addrGray, int threshold);
}
