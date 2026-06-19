## Possible upgrades for the Snake Game.

## Easy additions

1. Pause key (P). ✅
> Added the pauseDisplay() method that is called if the "P" key is pressed.
2. Restart key after Game Over. ✅
> Made a restart button that appear after the game is finished.
3. High score that survives multiple games. ✅
> Added the highScoreDisplay() method, altered the checkApple() method with if cindition that checks if current score is more than high score and if so the new high score is set.
4. Difficulty selection (Easy/Medium/Hard). ✅
> Created a difficulty menu, that pops up before the game, in GamePanel class.
> Implemented a Strategy DP, by creating the DifficultyBehaviour interface, and implementing it across distinct classes including EasyDifficulty, MediumDifficulty, HardDifficulty.
5. Speed increases every few apples. ✅
> Added an if condition inside the checkApple() class, that increasses the speed by 10 (i.e. reduces the delay by 10)every 2 apples.
6. Different snake colors each game. ✅
> Implemented a random colour generator.


## Gameplay improvements

7. Golden apples worth 3 points. ✅
> Created a seperate apple package to organise apple-related behaviour. This package contains AppleSpawner, AppleType classes. AppleType is an enum that has REGULAR and GOLDEN apples. The Apple class stores the apple's position, type, colour, and point value, while AppleSpawner handles randomly creating regular or golden apples.
8. Poison apples that shorten the snake. ✅
> Created a POISENED apple type, added a checkPoisonApple() method inside the GamePanel class that checks if the poisoned apple has been eaten and shortens the snake by 1 body part and deducts 2 points from the score. Also i added the updatePoisonApple() method in GamePanel that ensures the duration of 7 seconds of poison apple being on a map.
9. Moving apples (xz , maybe do the visual improvement rather)
10. Timed apples that disappear after a few seconds
11. Walls/obstacles
12. Random maze generation
13. Teleport tunnels (go in left side, come out right)

## Visual improvements

14. Animated snake eyes
15. Gradient snake body
16. Particle effects when eating apples
17. Score popups (+1, +5)
18. Smooth movement instead of grid jumping. ✅
> Positions use previousX, previousY, x, and y with animationProgress so the snake moves smoothly between grid squares instead of jumping instantly. I'm gonna say that it works , because i don't really know how to make it even smoother, so i'll let it be like this for now.
19. Snake thickness decreases gradually from head towards tail. ✅
> For each segment, "double t" first calculates how far that segment is from the tail toward the head. t becomes a value between 0 and 1. Near the tail, t is close to 0. Near the head, t is closer to 1. Then the "partSize" uses t to calculate the thickness of that part of the snake.
20. Add visual image-based snake design. ✅
> Added PNG-based assets to improve the snake's appearance, this includes a snake head image and a snake skin texture.
> Updated the snake body rendering to use the skin image as a repeated texture instead of a plain colour.
> Implemented head rotation logic so the head image turns to match the snake's current direction.
21. Add background image. ✅
22. Add different apple's images. ✅
23. Bugs: the rotten apple countdown still goes,even when the game is paused. The head snake can point in different side after the game is restarted. ✅ (fixed).
24. Bugs: laggy, because of "Image appleImage = new ImageIcon(getClass().getResource(apple.getAppleType().getImagePath())).getImage();" in the drawApple() method. That was loading apple images every repaint, around 60 times per second.

## More challenging mechanics

19. Wrap-around map
20. Going off the left side appears on the right
21. Limited lives
22. Boss apples
23. Take multiple hits to eat
24. Enemy snakes
25. AI snake competing for food

## Power-ups

26. Speed boost
27. Slow motion
28. Invincibility
29. Double points
30. Shrink snake
31. Ghost mode (pass through yourself)

## These are great for learning:

32. Save high scores to a file
33. Main menu screen
34. Settings screen
35. Sound effects
36. Music
37. Multiple levels
38. Local 2-player mode
39. Online multiplayer (advanced)

## If I were picking the most educational, shortlist:

1. Speed increases over time
2. High score saving to a file + maybe a leaderboard based on the data in the file
3. Golden apples
4. Obstacles/mazes
5. Pause + restart menu
6. Power-ups
7. 2-player snake