# lwjgl-project
Rendering analytical surface using LWJGL library.
Include 4 practical assignments.
## Practical assignment 1
Render analytical surface as per individual variant using OpenGL API.
## Practical assignment 2
Add user interaction to the software from assignment 1 by using mouse manipulator:
- Scroller wheel has to zoom the model;
- Holding the right mouse button the model has to be rotate adequately.
## Practical assignment 3
Show the video stream captured from the webcam and show in at the distance of convergence plane. The video has to form a background for the stereo image of the surface rendered in practical assignment 2.
## Practical assignment 4
Add to the practical assignment 3 the code opening listening socket via (UDP) protocol. Receive packets having values from three accelerometers found in your smartphone. Parse the values and make a vector of it. Transform the model according to the inclination of your smartphone.

*_Preconditions_*:
1. _Download and install application for android: SensoreNode (Android) / SensorStreamer (iOS)_
2. _Turn on hotspot on your smartphone_
3. _Connect your Laptop via WI-FI to your smartphone_
4. _Enter this ip address and port into the application_
#### NOTE:
For launching this application, you need to add following VM Options:
```java
-Djava.library.path=libs/
```
