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

Status: Not Fixed

Comment:
