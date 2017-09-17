#include <jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>

using namespace std;
using namespace cv;

extern "C"
{

JNIEXPORT jstring JNICALL
Java_com_winsontan520_myopencv_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jlong JNICALL
Java_com_winsontan520_myopencv_MyNativeUtil_fastDetection(JNIEnv *env, jclass type, jlong addrRgba,
                                                          jlong addrGray, jint threshold) {

    Mat& mGr  = *(Mat*)addrGray;
    Mat& mRgb = *(Mat*)addrRgba;
    vector<KeyPoint> v;

    // pass the pointer of gray Mat to process, no idea why rgba Mat not working because dint know magic behind yet.
    Ptr<FeatureDetector> detector = FastFeatureDetector::create();
    detector->detect(mGr, v);

    if(v.size() > threshold){
        // show green circle on all detected points if number of detection more than threshold
        for( unsigned int i = 0; i < v.size(); i++ )
        {
            const KeyPoint& kp = v[i];
            circle(mRgb, Point((int) kp.pt.x, (int) kp.pt.y), 5, Scalar(0, 255, 0, 255));
        }
    }

    return v.size();
}


// Ignore this, this is my playground
JNIEXPORT void JNICALL
Java_com_winsontan520_myopencv_FASTActivity_salt(JNIEnv *env, jobject instance,
                                                 jlong matAddrGray,
                                                 jint nbrElem) {
    Mat &mGr = *(Mat *) matAddrGray;
    for (int k = 0; k < nbrElem; k++) {
        int i = rand() % mGr.cols;
        int j = rand() % mGr.rows;
        mGr.at<uchar>(j, i) = 255;
    }
}

}
