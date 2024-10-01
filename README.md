# Atari Breakout

## How to Play

This game is a rendition of Atari's classic "Breakout". To start, the user enters their name and selects a difficulty (the harder the difficulty, the more lines to clear). After doing so, they are brought to the game. The user uses their paddle to clear the lines of blocks. The ball's momentum changes based on which third of the paddle the suer hits the ball with. If the ball falls below the paddle, the user loses a life. They have 3 lives. The user wins by clearing all of the blocks. After the game ends, either by the user running out of lives or clearing out all of the blocks, the user is taken to the game over screen. This screen tells the user their score and a leaderboard of the top scores of that device, as long as when they were earned. 

## Project

This was made in Android Studio. The starting screen, the game screen, and the ending screen were made with different activities. This project utilizes a lot of custom views: one for the ball, one for the paddle, and one for the blocks. To communicate between the views and the activity, the app uses a handler. The app can detect the user's touch by using an OnTouchListeener. The leaderboard is made using a text file stored locally on the device. The app uses multithreading to run as efficiently as possible. The ball moves on a separate thread and the program checks the location of the ball and the paddle on a separate thread. 
