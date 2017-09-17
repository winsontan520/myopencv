# myopencv
My OpenCV playground contains
1. Feature detection with FAST using OpenCV Java SDK
2. Feature detection with FAST for post captured image using Android camera2 using OpenCV Java SDK
3. Feature detection with FAST using native class to process with OpenCV native libs

# How to use
1. Checkout the source codes open in Android Studio
2. Open file CMakelists.txt, update your OpenCV sdk path at line "include_directories(YOUR_OPENCV_SDK_PATH/sdk/native/jni/include)"
3. Find all "OpenCVLoader.OPENCV_VERSION_3_3_0" in projects and replace with your OpenCV version
4. Copy all native libs from your OpencvSdkFolder\sdk\native\libs to myopencv\app\src\main\jniLibs
5. Run and install
