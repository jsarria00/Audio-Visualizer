Title:Log clears some entries when moving the whole application folder

ID:1

Platform: Windows 10; April 2018 Update

Discovered by: Arilda Lau

Discovered on: November 2nd 2018

File Version: 1.00

Description: When moving the folder containing the application, starting the application will clear previous log entries.


Steps to Reproduce: Open the application, and load a media file. 

Close the application and move the folder holding the application to another area in the computer. 

Open the application, media files from the application folder will disappear.


Status: Fixed

Comment: The program would use a parent directory. When the jar application directory was moved, then the parent directory would be invalid. 
This was corrected by extracting a child directory, if the directory contained "src/main/media".
(commit:ad69ea679a075f746d25b9e380f3dc9b84844e54)





Title: "Media Already Loaded, Aborting request" error displays with different sizes
 
ID:2

Platform: Windows 10; April 2018 Update

Discovered by: Arilda Lau

Discovered on: November 6th 2018

File Version: 1.00

Description: When loading the same song through the file loader and the song log, the error shows up with different sizes.

Steps to Reproduce: Load a song into the visualizer, load the same song through file loader, the error text should show at the top right. 

Load the same song through the song log, the text will show, but smaller.


Status: Fixed

Comment: setFont() of graphics2D depended on the songlogger's call. 
This was relocated to the start of the draw method of Song Logger, which now makes both error messages large.
(commit:fea040a7fac83df9c33f93329db678dfeaef336a) 






Title: Queue will empty itself if attempting to seek at another point of the current song.
 
ID:3

Platform: Windows 10; April 2018 Update

Discovered by: Arilda Lau

Discovered on: November 13th 2018

File Version: 1.01

Description: When setting up a song queue, and then starting it. Trying to seek through the song will empty the song queue.

Steps to Reproduce: Load multiple songs into the queue. 

Start the queue.

Click and hold the timeline

Queue should be empty, last queued song is loaded instead. 


Status: Fixed

Comment: The media queue ensures that a song isn't loaded if the VisualizerMediaplayerHolder is loading, is in use or if a song is playing. 
The queue didn't take into account a condition where the song can pause due to user interaction with the slider. 
Therefore a debounding state was added for the slider to prevent skipping the song when the user attempts to "seek"








Title: Queue will empty itself if attempting to seek at another point of the current song.
 
ID:4

Platform: Windows 10; April 2018 Update

Discovered by: Javier Sarria

Discovered on: November 16th 2018

File Version: 1.10

Description: Transistion will occur if MediaQueue is started while song is paused at less than 7 seconds but more than 3 seconds remaining. This will skip the first queued song, and jump to the following song

Steps to Reproduce: Load any song standardly.

Move to the the appoximate transitioning interval and keep song unpaused.

Start the queue, song will start and the media player will remain in paused state.

Once transition ends, the currently loaded song will be skipped for next in queue, if the queue is on.


Status: Fixed

Comment: The transition state did not enforce that it should only function if the mediaplayer is "playing". 
The transition will now occur only while a song from the queue is playing.







Title: Weird behaviour when accepting load window with any text
 
ID:5

Platform: Windows 10; April 2018 Update

Discovered by: Arilda Lau

Discovered on: November 16th 2018

File Version: 1.10

Description: When using the Queue, if you type anything into the File Name box it will accept it.
When it reaches that point in the queue, the media player will try to load it, and stay as playing.

Steps to reproduce:

Load a song into the queue.

Open the queue load, and type words into the box.

Queue will accept it.

Let the song transistion.



Status: Fixed

Comment: There was no condition to check the files using the load dialog, this would fail to load a new mediaplayer, and the end transition would swap the "main" mediaplayer to a null value. 
If the user attempted to continue to use the "pause" option, a null pointer exception would be thrown. 
The transistion state locks the system so that it can keep the visualizer in an "initialized" state as two MediaPlayer objects exist within the 7.5 second transistion state. 
This causes a null mediaplayer to exist while the program is "initialized". A dummy Media object is now put in place to throw an exception in case the file does not exist, and therefore preventing the load attempt from happening.









Title: OpenGL restricting GPU parallel processing to a single display. 
 
ID:6

Platform: Windows 10; April 2018 Update

Discovered by: Javier Sarria

Discovered on: November 18th 2018

File Version: 1.10

Description: When launching the program, it will function and work on the System's default display. With added functionality to minimize and move the frame that draws the visualizer. 
It was noticed that openGL restricts GPU processing on "Display 0". Ignoring the openGL option, the parallel processing will work on all displays.

Steps to reproduce:

Start the program.

Minimize the program, and drag frame to a second display.

Preformance will degrade until it's brought back to main display.

Status:Not Fixed

Comment: So far the only fix is to create two different builds of the Jar file, one that includes OpenGL and one that does not. 
This does not seem viabale as it creates versioning on types of Hardware configuration, rather than on OS types.
Completely disabling the option will cause hardware that has a processor with integrated graphics to use that rather than the best dedicated GPU.






Title: Holding Timeline during transition and releasing after transistion will cause a queue skip. 
 
ID:6

Platform: Windows 10; April 2018 Update

Discovered by: Javier Sarria

Discovered on: November 18th 2018

File Version: 1.10

Description: Holding the timeline during transition, and then releasing after it ends will cause queue skip.

Steps to Reproduce:Load 3 songs into the queue system.

Start queue, and then drag timeline to near end of song.

When song starts to transition, click and hold the slider. 

When transistion ends release the slider, song will "pause" and skip the the second song, and start playing the third.


Status:Fixed

Comment: Added a "wasTransitioning" state to VisualizerComponent to enforce a "two-Step" interaction restriction on the slider.


Title: UNIX based file loading no functioning anymore

ID:7

Platform: Ubuntu 18.10

Discovered by: Javier Sarria

Discovered on: February 3rd 2019

File Version: 1.2x

Description: This bug had risen through halted testing on UNIX OS. When loading an MP3 file, the file is found and an exception is thrown immediately
(where if no file is found, the exception will delay itself until it is certain the file does not exist). Anticipated fix, migrate project from java environment 8 to environment 11.
This bug will be corrected before continued development of other projects that modularize of this one.

Steps to Reproduce: Load the Jar by Calling java -jar AudioVisualizer.jar in linux. Attempting to load the file will certainly fail.


Status:Not Fixed

Comment: