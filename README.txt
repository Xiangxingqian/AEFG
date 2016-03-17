0. Explanation of directory
Under the current directory, you can see some files except the project's files. They are: 
lib: all the libs needed.
output: output directory to store the generation result.
platforms: a directory provide android.jar used by soot, you'd better provide all the levels API. 
TestedApk: Apk to be analyzed. 
platforms.png: my platforms, you just need to copy the android-14 20 times, and name them in different names

1. Execution
Step1: import AndroidAnalysisBySoot into eclipse
Step2: Edit buildpath, change the libs. All the libs are storaged under the lib directory.
Step3: run entry/Main.java. Edit the run configuration, as following:
-apk TestedApk/gmail.apk -android_platforms platforms -dot "C:/Program Files (x86)/Graphviz2.38/bin/dot.exe"

The meanings of each argument:
-apk TestedApk/gmail.apk //apk to be analyzed
-android_platforms platforms //android platforms location
-dot "C:/Program Files (x86)/Graphviz2.38/bin/dot.exe" // GraphViz location used to draw graph

After running successfully, and you run on gmail.apk,there should be three files under the directory output: gmail.txt, gmail.pdf, and gmailEdges.txt

2. Exception
2.1. Exception in thread "main" java.lang.RuntimeException: error: target android.jar (platforms\android-8\android.jar) does not exist.
The reason is Soot will search for the target android.jar declared in app's androidManifest.xml, if it does not exist, throw this exception.
Resolver: Change the platforms\android-20 to the the target android.jar, or copy platforms\android-20 20 times, and name them android-1, android-2...android-19, make sure you have every API levels.

2.2. Try to set the VM argument -Xms512m -Xmx2000m

3. Result
I've given some explanation of the edge's output information, and you can find it in output/gmailEdges.txt. Some apps' results are under the output directory. 
The result should have result for wechat, but it seems something is missing in wechat during analyzing the implicit intent. I'll try another version.  
