# Personal Project - Audio visualizer

File version: 1.00

The audio visualizer is a user interactive program. On start the user has the choice to load a .mp3 file into the program, and their audio file will start to visualize once loaded.
When loading a file, a history log will keep track of unique song names, and displayed them in the order they were recently loaded in where a max of 10 will display in the log. 
The user also has a choice to load a song from the history log. 
The user will be unable to reload a song that is already loaded.
The history log will validate itself and clean the history of files that no longer exist in the directory they were loaded in. 
On the following application start up, if there is a history, the most recently existing file will load.

On visualization the user has a choice to Play and pause the song that is loaded. The user can also seek a particular time in the song. 
Once the song ends, the visualization will end, and the song will be set to the start.

It is recommended to have a dedicated GPU to take advantage of the openGL, where GPU threads will render the image and CPU will be free to process the application. 
A 1080p resolution is preferable, but a 720p resolution is also viable.
