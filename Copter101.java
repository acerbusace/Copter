/** The "Copter101" class.
* Purpose: Plays the Copter Game
* @author Alex Patel
* @version January, 14, 2012
*/
import java.applet.Applet; //allows the usuage of applets
import java.awt.*;
import java.awt.Canvas; //alows the creation of canvases
import java.awt.Color; //allows the usage of different colors
import java.awt.Font; //allows the creation of different fonts
import java.awt.Graphics; //allows the use of graphics
import java.awt.Image; //allows the creation of image objects
import java.awt.Toolkit; //allows the import of external images
import java.applet.AudioClip; //allows the use of audio
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; //used for mouse listening methods
import java.awt.image.BufferStrategy; //used for double buffering
import java.util.Random; //allows the usage of random generators


public class Copter101 extends Applet implements MouseListener, Runnable
{

    //http://www.youtube.com/watch?v=OEzVB51KBrw  -  Double Buffering
    private Graphics bufferGraphics = null;  //The graphics for the backbuffer
    private BufferStrategy bufferStrategy = null; //The strategy our applet uses
    private Canvas canvas; //creates a canvas
    private Thread thread; //creates a thread

    //variables for all the fonts used
    Font smallFont;
    Font mediumFont;
    Font largeFont;

    private boolean running = false;

    Random generator = new Random (); //creates a random generator

    boolean buttonPressed; //if the mouse button is pressed
    boolean buttonClicked; //if the mouse button is clicked
    boolean gameOver; //if the game is over
    boolean pause; //if the game is paused
    boolean startGame; //if the game has started

    boolean menu; //if the main menu is the main screen of the applet

    int menuOptions; //changes depending on what options from the main menu are clicked
    int difficulty; //the difficulty level of the game (1-4)

    boolean moveMiddleRect; //if the first middle rectangle should move
    boolean moveMiddleRectTwo; //if the second middle rectangle should move
    boolean setYPosOfMiddleRect; //if the y position of the first rectangle needs to be changed
    boolean setYPosOfMiddleRectTwo; //if the y position of the second rectangle needs to be changed

    int screenWidth = 795; //width of the applet
    int screenHeight = 484; //height of the applet

    int xPosCopter; //x coordinate of the helicopter
    int defaultYPosCopter; //initial y coordinate of the helicopter
    double yPosCopter; //y coordinate of the helicopter
    double moveCount; //initial speed of the change in copter's elevation
    double speedOfChange; //change in the increase of the speed of the change in copter's elevation
    double moveUpCount; //how much the helicopter moves up before moving down
    double moveDownCount; //how much the helicopter moves down before moving up
    int clickCount;
    double distance; //calculates how far the helicopter has traveled
    double distanceSpeed; //how fast distance increases
    int bestDistance; //farthest distance the copter has traveled
    int speed; //speed of bricks moving from right to left
    int speedOfCave;
    int upSpeedOfCave; //how likely the top rectangles will get bigger
    int downSpeedOfCave; //how likely the bottom rectangles will get bigger
    int elses;
    int defaultSpace; //the initial, aditional space between the top or bottom and the middle rectangle
    int space; //aditional space between the top or bottom and the middle rectangle
    int rateOfSpace; //the number by which space decreases by
    int i;
    int ix;


    //variables for all the images used
    Image background;
    Image backgroundSurrounding; //floor and ceiling image
    Image rect; //top and bottom rectangles image
    Image middleRect; //middle rectangles image
    //http://www.hawkee.com/snippet/6233/  -  helicopter pictures
    Image copterUp; //helicopter going up image
    Image copter; //helicopter going down image
    Image mainMenu; //image of the main menu
    Image credits; //image of credits
    Image settings; //image of settings
    //http://superdit.com/2011/04/23/jquery-detect-mouse-click-event/  -  mouse picture
    Image controls; //image of controls
    Image crashed; //image of game over

    //http://www.youtube.com/watch?v=T57aeumu-kA  -  tutorial on using sound
    AudioClip clip; //http://www.soundjay.com/button-sounds-1.html  -  sound
    AudioClip clipBack; //http://www.soundjay.com/button-sounds-1.html  -  sound

    //variables dealing with top and bottom rectangles
    int numOfRect; //number of top or bottom rectangle on the screen at the same time
    int heightChangeOfRect; //how much the top and bottom rectangles change height
    int[] xPosOfRect; //x coordinates of the top and bottom rectangles
    int[] yPosOfRect; //y coordinates of all the top rectangles
    int[] yPosOfDownRect; //y coordinates of all the bottom rectangles
    int rateOfRect; //the rate at which the top and bottom rectangles change height
    int[] xPosOfMiddleRect; //x coordinates of the middle rectangles
    int[] yPosOfMiddleRect; //y coordinates of the middle rectangles
    int widthOfRect; //width of the top and bottom rectangles
    int maxTotalSize; //the total size of the top and bottom rectangles with the same x coordinates combined

    //variables dealing with the middle rectangle
    int widthOfMiddleRect; //width of the middle rectangles
    int heightOfMiddleRect; //height of the middle rectangles
    int rateOfMaxTotalSize; //changes the height of the middle rectangle depending on the difficulty
    int defaultRateOfMiddleRect; //the inital value of how fast the middle rectangles spawn
    int rateOfMiddleRect; //how fast the middle rectangles spawn
    int incrementOfROMR; //how much the rateOfMiddleRect decreases by

    /** sets values for all the variables
    */

    public void init ()
    {
	setSize (screenWidth, screenHeight); //sets the size of the applet

	//sets the values for the variables
	buttonPressed = false;
	buttonClicked = false;
	gameOver = false;
	pause = false;
	startGame = false;

	menu = true;

	menuOptions = 0;
	difficulty = 2;

	moveMiddleRect = false;
	moveMiddleRectTwo = false;
	setYPosOfMiddleRect = false;
	setYPosOfMiddleRectTwo = false;

	//imports all the images
	background = getToolkit ().getImage ("images/background.png");
	backgroundSurrounding = getToolkit ().getImage ("images/backgroundSurrounding.png");
	rect = getToolkit ().getImage ("images/recs.png");
	middleRect = getToolkit ().getImage ("images/middlerecs.png");
	copter = getToolkit ().getImage ("images/helicopter.gif");
	copterUp = getToolkit ().getImage ("images/upCopter.gif");
	mainMenu = getToolkit ().getImage ("images/mainMenu.png");
	credits = getToolkit ().getImage ("images/credits.png");
	settings = getToolkit ().getImage ("images/settings.png");
	controls = getToolkit ().getImage ("images/controls.png");
	crashed = getToolkit ().getImage ("images/gameOver.png");

	clip = getAudioClip (getCodeBase (), "sounds/button.wav");
	clipBack = getAudioClip (getCodeBase (), "sounds/buttonBack.wav");

	xPosCopter = this.getSize ().width / 13 + 20;
	defaultYPosCopter = (this.getSize ().height / 2) - ((this.getSize ().height / 12) / 2);
	yPosCopter = defaultYPosCopter;
	moveCount = 0;
	speedOfChange = 0.05;
	moveUpCount = moveCount;
	moveDownCount = moveCount;
	clickCount = 0;
	distance = 0;
	distanceSpeed = 0.4;
	bestDistance = 0;
	speed = 4;
	speedOfCave = 6;
	upSpeedOfCave = 7;
	downSpeedOfCave = 5;
	elses = 1;
	defaultSpace = 60;
	space = defaultSpace;
	rateOfSpace = 2;
	i = 1;
	ix = 1;

	numOfRect = 25;
	heightChangeOfRect = 6;
	heightOfMiddleRect = this.getSize ().height / 5;
	widthOfRect = this.getSize ().width / numOfRect;
	widthOfMiddleRect = (int) (widthOfRect / 1.5);
	maxTotalSize = (int) ((this.getSize ().height / 15) + 30);
	rateOfMaxTotalSize += 8;
	xPosOfRect = new int [numOfRect + 2];
	yPosOfRect = new int [numOfRect + 2];
	yPosOfDownRect = new int [numOfRect + 2];
	xPosOfMiddleRect = new int [2];
	yPosOfMiddleRect = new int [2];
	rateOfRect = 300;
	defaultRateOfMiddleRect = 200;
	rateOfMiddleRect = defaultRateOfMiddleRect;
	incrementOfROMR = 4;

	//all the creation of different fonts
	smallFont = new Font ("Serif", 0, 20);
	mediumFont = new Font ("Serif", 0, 50);
	largeFont = new Font ("Serif", 0, 75);

	//http://www.java-gaming.org/index.php?topic=24356.0  -  Canvas
	canvas = new Canvas ();
	canvas.setSize (this.getSize ().width, this.getSize ().height);
	add (canvas); //adds the canvas to the Copter101 class
	canvas.createBufferStrategy (2); //creates a BufferStrategy inside canvas
	canvas.addMouseListener (this); //tells canvas we will be using mouse listener

	//prepares all the images to be loaded
	prepareImage (background, this);
	prepareImage (backgroundSurrounding, this);
	prepareImage (rect, this);
	prepareImage (middleRect, this);
	prepareImage (copterUp, this);
	prepareImage (copter, this);
	prepareImage (mainMenu, this);
	prepareImage (credits, this);
	prepareImage (settings, this);
	prepareImage (controls, this);
	prepareImage (crashed, this);


	MediaTracker tracker = new MediaTracker (this); //creates a media tracker
	//adds all the images to the list of images to be tracked
	tracker.addImage (background, 0);
	tracker.addImage (backgroundSurrounding, 0);
	tracker.addImage (rect, 0);
	tracker.addImage (middleRect, 0);
	tracker.addImage (copterUp, 0);
	tracker.addImage (copter, 0);
	tracker.addImage (mainMenu, 0);
	tracker.addImage (credits, 0);
	tracker.addImage (settings, 0);
	tracker.addImage (controls, 0);
	tracker.addImage (crashed, 0);

	/*wait untill all the images are loaded, this can throw an InterruptedException,
	*although it very unlikely, so we ignore the exception if it occurs
	*/
	try
	{
	    tracker.waitForAll ();
	}
	catch (InterruptedException e)
	{
	}

	this.thread = new Thread (this); //creates a new thread
	running = true;

    }


    /** starts the buffer strategy (double buffering)
    */

    public void paint (Graphics g)
    {
	//creates a bufferStrategy, if it has not been created
	if (bufferStrategy == null)
	{
	    bufferStrategy = canvas.getBufferStrategy ();
	    bufferGraphics = bufferStrategy.getDrawGraphics ();

	    this.thread.start (); //starts the thread
	}
    }


    /** Hooks all the other methods together
    */

    public void run ()
    {
	//thread refers to this method
	while (running)
	{
	    //program's logic
	    Start ();
	    Draw ();
	    DrawBackbuffertoScreen ();

	    try
	    {
		Thread.sleep (7); //thread sleeps for 17 milliseconds (60 fps)
	    }
	    //throws an InterruptedException
	    catch (InterruptedException e)
	    {
		e.printStackTrace ();
	    }
	}
    }


    /** Runs the methods needed, to play the game
    */

    public void Start ()
    {
	//Calculations of the program

	//if menuOptions is 1, the game is started
	if (menuOptions == 1)
	{

	    //calls upon all the game methods if the game has started
	    if (clickCount == 0)
		startGame = false;

	    moveCopter ();
	    updateRect ();
	    updateMiddleRect ();
	    copterCrashed ();
	    distance ();

	    //if the mouse exits the applet or if the mouse is not clicked after the game is started, the game pauses
	    while (pause || !startGame)
	    {
		Draw ();
		DrawBackbuffertoScreen ();
	    }
	}
    }


    /** moves the helicopter up or down
    */

    private void moveCopter ()
    {
	//moves the copter up, if the mouse button is pressed
	if (buttonPressed)
	{
	    moveDownCount = moveCount;
	    moveUpCount += speedOfChange;
	    yPosCopter -= moveUpCount;
	}
	//moves the copter down, if the mouse button is not pressed
	else
	{
	    moveUpCount = moveCount;
	    moveDownCount += speedOfChange;
	    yPosCopter += moveDownCount;
	}
    }


    /** moves the top and bottom rectangles
    */

    private void updateRect ()
    {
	if (distance == 0 && !gameOver)
	{
	    //sets the x coordinates of the top and bottom rectangles, if the game just started
	    for (int count = 0 ; count < xPosOfRect.length ; count++)
	    {
		xPosOfRect [count] = this.getSize ().width + (widthOfRect * count);
	    }

	    //generates a random height for the top and bottom rectangles, if the game just started
	    for (int count = 0 ; count < yPosOfRect.length ; count++)
	    {
		if (count == 0) //count 0 reffers to the first rectangle
		{
		    //generates a greater height for the top rectangle
		    if (generator.nextInt (11) < speedOfCave)
			yPosOfRect [count] = (this.getSize ().height / 15) + generator.nextInt (heightChangeOfRect);
		    //generates a smaller height for the top rectangle
		    else
			yPosOfRect [count] = (this.getSize ().height / 15) - generator.nextInt (heightChangeOfRect);
		}
		//generates a greater height for the top rectangle
		else if (generator.nextInt (11) < speedOfCave)
		    yPosOfRect [count] = (yPosOfRect [count - 1]) + generator.nextInt (heightChangeOfRect);
		//generates a smaller height for the top rectangle
		else
		    yPosOfRect [count] = (yPosOfRect [count - 1]) - generator.nextInt (heightChangeOfRect);

		//changes the height of the top rectangle is the rectangle is to big
		if (yPosOfRect [count] > (maxTotalSize - (this.getSize ().height / 15)))
		{
		    if (count == 0)
			yPosOfRect [count] = (this.getSize ().height / 15) - generator.nextInt (heightChangeOfRect);
		    else
			yPosOfRect [count] = (yPosOfRect [count - 1]) - generator.nextInt (heightChangeOfRect);
		}
		else if (yPosOfRect [count] < (this.getSize ().height / 15))
		{
		    if (count == 0)
			yPosOfRect [count] = (this.getSize ().height / 15) + generator.nextInt (heightChangeOfRect) + ((this.getSize ().height / 15) - yPosOfRect [count]);
		    else
			yPosOfRect [count] = (yPosOfRect [count - 1]) + generator.nextInt (heightChangeOfRect) + ((this.getSize ().height / 15) - yPosOfRect [count]);
		}

		yPosOfDownRect [count] = (maxTotalSize - yPosOfRect [count]); //sets the height for the bottom rectangles
	    }
	}

	else if (!gameOver && distance != 0)
	{
	    for (int count = 0 ; count < xPosOfRect.length ; count++)
	    {
		//generates a random height for the top and bottom rectangle, if the rectangle passed the helicopter and disappeared from the screen
		if ((xPosOfRect [count] + widthOfRect) < 0)
		{
		    xPosOfRect [count] = widthOfRect * (numOfRect + 1);

		    if (count == 0)
		    {
			//generates a greater height for the top rectangle
			if (generator.nextInt (11) < speedOfCave)
			    yPosOfRect [count] = (yPosOfRect [numOfRect + 1]) + generator.nextInt (heightChangeOfRect);
			//generates a smaller height for the top rectangle
			else
			    yPosOfRect [count] = (yPosOfRect [numOfRect + 1]) - generator.nextInt (heightChangeOfRect);
		    }
		    //generates a greater height for the top rectangle
		    else if (generator.nextInt (11) < speedOfCave)
			yPosOfRect [count] = (yPosOfRect [count - 1]) + generator.nextInt (heightChangeOfRect);
		    //generates a smaller height for the top rectangle
		    else
			yPosOfRect [count] = (yPosOfRect [count - 1]) - generator.nextInt (heightChangeOfRect);

		    //changes the height of the top rectangle is the rectangle is to big
		    if (yPosOfRect [count] > (maxTotalSize - (this.getSize ().height / 15)))
		    {
			if (count == 0)
			    yPosOfRect [count] = (yPosOfRect [numOfRect + 1]) - generator.nextInt (heightChangeOfRect);
			else
			    yPosOfRect [count] = (yPosOfRect [count - 1]) - generator.nextInt (heightChangeOfRect);
		    }
		    else if (yPosOfRect [count] < (this.getSize ().height / 15))
		    {
			if (count == 0)
			    yPosOfRect [count] = (yPosOfRect [numOfRect + 1]) + generator.nextInt (heightChangeOfRect) + ((this.getSize ().height / 15) - yPosOfRect [count]);
			else
			    yPosOfRect [count] = (yPosOfRect [count - 1]) + generator.nextInt (heightChangeOfRect) + ((this.getSize ().height / 15) - yPosOfRect [count]);
		    }

		    yPosOfDownRect [count] = (maxTotalSize - yPosOfRect [count]); //sets the height for the bottom rectangles
		}

		xPosOfRect [count] -= speed; //moves the top and bottom rectangles from left to right
	    }
	}
    }


    /** moves the middle rectangles
    */

    private void updateMiddleRect ()
    {
	//sets the x coordinate and generates a random height for the middle rectangle
	for (int countx = 0 ; countx < xPosOfMiddleRect.length ; countx++)
	{
	    if (distance == 0 && !gameOver || (xPosOfMiddleRect [countx] + widthOfMiddleRect) < 0)
	    {
		xPosOfMiddleRect [countx] = this.getSize ().width;
		yPosOfMiddleRect [countx] = generator.nextInt (this.getSize ().height - heightOfMiddleRect);

		if (countx == 0)
		    moveMiddleRect = false; //stops the first middle rectangle from moving
		else
		    moveMiddleRectTwo = false; //stops the second middle rectangle from moving
	    }

	    //sets the middle rectangles to move if the conditions are true
	    if (distance > (rateOfMiddleRect * ix))
	    {
		if (elses == 2 || moveMiddleRect)
		{
		    moveMiddleRectTwo = true;
		    setYPosOfMiddleRectTwo = true;
		    elses = 1;
		}
		else if (elses == 1 || moveMiddleRectTwo)
		{
		    moveMiddleRect = true;
		    setYPosOfMiddleRect = true;
		    elses = 2;
		}
		//makes sure the middle rectangles don't spawn to fast if there are two rectangles on screen
		if (rateOfMiddleRect > 60 && moveMiddleRect && moveMiddleRectTwo)
		    rateOfMiddleRect -= incrementOfROMR / 2;
		//makes the middle rectangles spawn faster
		else if (rateOfMiddleRect > 60)
		    rateOfMiddleRect -= incrementOfROMR;
		ix += 1;
	    }

	    if (moveMiddleRect && countx == 0)
	    {
		if (setYPosOfMiddleRect)
		{
		    for (int count = 0 ; count < xPosOfRect.length ; count++)
		    {
			if ((xPosOfRect [count] + widthOfRect) > xPosOfMiddleRect [countx] && (xPosOfRect [count] + widthOfRect) < (xPosOfMiddleRect [countx] + widthOfMiddleRect))
			{
			    /*checks to make sure, there is enough space for the helicopter to pass through
			    *if there isn't enough space, changes the y coordinate of the middle rectangle to allow enough space
			    */
			    if ((yPosOfMiddleRect [countx] - yPosOfRect [count]) < ((this.getSize ().height / 10) + space) && (yPosOfDownRect [count] - (yPosOfMiddleRect [countx] + heightOfMiddleRect)) < ((this.getSize ().height / 10) + space) && xPosOfMiddleRect [countx] == this.getSize ().width)
			    {
				if (generator.nextInt (2) == 0)
				    yPosOfMiddleRect [countx] += Math.abs (yPosOfMiddleRect [countx] - yPosOfRect [count]) + ((this.getSize ().height / 10) + space);
				else
				    yPosOfMiddleRect [countx] -= Math.abs (yPosOfDownRect [count] - (yPosOfMiddleRect [countx] + heightOfMiddleRect)) - ((this.getSize ().height / 10) + space);
			    }
			}
		    }
		    setYPosOfMiddleRect = false;
		}
		xPosOfMiddleRect [countx] -= speed; //moves the middle rectangle from right to left
	    }

	    if (moveMiddleRectTwo && countx == 1)
	    {
		if (setYPosOfMiddleRectTwo)
		{
		    for (int count = 0 ; count < xPosOfRect.length ; count++)
		    {
			if ((xPosOfRect [count] + widthOfRect) > xPosOfMiddleRect [countx] && (xPosOfRect [count] + widthOfRect) < (xPosOfMiddleRect [countx] + widthOfMiddleRect))
			{
			    /*checks to make sure, there is enough space for the helicopter to pass through
			    *if there isn't enough space, changes the y coordinate of the middle rectangle to allow enough space
			    */
			    if ((yPosOfMiddleRect [countx] - yPosOfRect [count]) < ((this.getSize ().height / 10) + space) && (yPosOfDownRect [count] - (yPosOfMiddleRect [countx] + heightOfMiddleRect)) < ((this.getSize ().height / 10) + space) && xPosOfMiddleRect [countx] == this.getSize ().width)
			    {
				if (generator.nextInt (2) == 0)
				    yPosOfMiddleRect [countx] += Math.abs (yPosOfMiddleRect [countx] - yPosOfRect [count]) + ((this.getSize ().height / 10) + space);
				else
				    yPosOfMiddleRect [countx] -= Math.abs (yPosOfDownRect [count] - (yPosOfMiddleRect [countx] + heightOfMiddleRect)) - ((this.getSize ().height / 10) + space);
			    }
			}
		    }
		    setYPosOfMiddleRectTwo = false;
		}
		xPosOfMiddleRect [countx] -= speed; //moves the middle rectangle from right to left
	    }
	}
    }


    /** checks if the helicopter crashes
    */


    private void copterCrashed ()
    {
	//checks if the helicopter hit the floor or celling
	if (yPosCopter < ((this.getSize ().height / 15) - 5) || yPosCopter > ((this.getSize ().height - ((this.getSize ().height / 15)) - this.getSize ().height / 12) + 5))
	{
	    gameOver = true;
	    yPosCopter = defaultYPosCopter; //resets the y coordinate of the helicopter
	    rateOfMiddleRect = defaultRateOfMiddleRect; //resets the spawn rate of the rectangles
	}

	for (int count = 0 ; count < xPosOfRect.length ; count++)
	{
	    //checks if the helicopter hit the top or bottom rectangles
	    if (xPosOfRect [count] > xPosCopter && xPosOfRect [count] < (xPosCopter + (this.getSize ().width / 10)) && yPosCopter < (yPosOfRect [count] - 5) || (xPosOfRect [count] + widthOfRect) > xPosCopter && (xPosOfRect [count] + widthOfRect) < (xPosCopter + (this.getSize ().width / 10)) && (yPosCopter + (this.getSize ().height / 12)) > (this.getSize ().height - yPosOfDownRect [count] + 5))
	    {
		gameOver = true;
		yPosCopter = defaultYPosCopter; //resets the y coordinate of the helicopter
		rateOfMiddleRect = defaultRateOfMiddleRect; //resets the spawn rate of the rectangles
	    }
	}

	//checks if the helicopter hit the middle rectangle
	for (int count = 0 ; count < xPosOfMiddleRect.length ; count++)
	{
	    if (xPosOfMiddleRect [count] > xPosCopter && xPosOfMiddleRect [count] < (xPosCopter + (this.getSize ().width / 10) - 2) && yPosCopter < (yPosOfMiddleRect [count] + heightOfMiddleRect - 3) && yPosCopter > (yPosOfMiddleRect [count]) || xPosOfMiddleRect [count] > xPosCopter && xPosOfMiddleRect [count] < (xPosCopter + (this.getSize ().width / 10) - 2) && (yPosCopter + (this.getSize ().height / 12)) < (yPosOfMiddleRect [count] + heightOfMiddleRect - 3) && (yPosCopter + (this.getSize ().height / 12)) > yPosOfMiddleRect [count])
	    {
		gameOver = true;
		yPosCopter = defaultYPosCopter; //resets the y coordinate of the helicopter
		rateOfMiddleRect = defaultRateOfMiddleRect; //resets the spawn rate of the rectangles
	    }
	}
    }


    /** calculates how far the helicopter has traveled
    */

    private void distance ()
    {
	if (!gameOver)
	{
	    //allows the top and bottom rectangles to have a higher height, if the conditions are true
	    if (distance > (rateOfRect * i))
	    {
		if (maxTotalSize < (this.getSize ().height - (this.getSize ().height / 2.5)))
		    maxTotalSize += rateOfMaxTotalSize;
		if (space > 18)
		    space -= rateOfSpace;
		//makes it so, that the bottom rectangles are more likely to increase in height
		if (speedOfCave == upSpeedOfCave)
		    speedOfCave = downSpeedOfCave;
		//makes it so, that the top rectangles are more likely to increase in height
		else
		    speedOfCave = upSpeedOfCave;
		i += 1;
	    }

	    distance += distanceSpeed; //increases the distance of the helicopter
	}

	//if the game is over, the distance is reset to 0 and bestDistance will equal to distance if distance is greater
	if (distance > bestDistance)
	{
	    bestDistance = (int) distance;
	}
	//if game is over, sets the variable to their default values
	if (gameOver)
	{
	    distance = 0;
	    i = 1;
	    ix = 1;
	    maxTotalSize = (int) ((this.getSize ().height / 15) + 30);
	    space = defaultSpace;
	}
    }


    /** draws all the graphics to the applet
    */

    public void Draw ()
    {
	//Clearing the secondary screen that we can't see
	bufferGraphics = bufferStrategy.getDrawGraphics ();

	try
	{
	    bufferGraphics.clearRect (0, 0, this.getSize ().width, this.getSize ().height);

	    //This is where everything will be drawn to the backbuffer
	    Graphics g = (Graphics) bufferGraphics; //sets bufferGprahics to Graphics g

	    //draws the main menu
	    if (menu == true)
		g.drawImage (mainMenu, 0, 0, null);
	    //draws the game
	    else if (menuOptions == 1)
	    {
		if (!gameOver)
		{
		    g.drawImage (background, 0, 0, this.getSize ().width, this.getSize ().height, null); //draws the background

		    for (int count = 0 ; count < xPosOfMiddleRect.length ; count++)
		    {
			g.drawImage (middleRect, xPosOfMiddleRect [count], yPosOfMiddleRect [count], widthOfMiddleRect, heightOfMiddleRect, null); //draws the middle rectangle
		    }
		    //draws the top and bottom rectangles
		    for (int count = 0 ; count < xPosOfRect.length ; count++)
		    {
			g.drawImage (rect, xPosOfRect [count], 0, widthOfRect, yPosOfRect [count], null);
			g.drawImage (rect, xPosOfRect [count], this.getSize ().height - yPosOfDownRect [count], widthOfRect, yPosOfDownRect [count], null);
		    }

		    //draws the celling and floor
		    g.drawImage (backgroundSurrounding, 0, 0, this.getSize ().width, this.getSize ().height / 15, null);  //celling
		    g.drawImage (backgroundSurrounding, 0, this.getSize ().height - (this.getSize ().height / 15), this.getSize ().width, this.getSize ().height / 12, null);  //floor

		    //draws the distance and best distant
		    g.setColor (Color.GREEN);

		    g.setFont (smallFont);
		    g.drawString ("Distance: " + (int) distance, this.getSize ().width / 13, this.getSize ().height - (backgroundSurrounding.getHeight (null) / 4));
		    g.drawString ("Best Distance: " + bestDistance, this.getSize ().width / 2, this.getSize ().height - (backgroundSurrounding.getHeight (null) / 4));

		    //draws the helicopter
		    if (buttonPressed)
			g.drawImage (copterUp, xPosCopter, (int) yPosCopter, this.getSize ().width / 10, this.getSize ().height / 12, null);
		    else
			g.drawImage (copter, xPosCopter, (int) yPosCopter, this.getSize ().width / 10, this.getSize ().height / 12, null);
		    //draws "Click Mouse to Start"
		    if (!startGame)
		    {
			g.setColor (Color.GREEN);

			g.setFont (mediumFont);
			g.drawString ("Click Mouse to Start", this.getSize ().width / 2 - 220, this.getSize ().height / 2 + 15); //draws "click mose to start" in the middle of the screen
		    }
		    //draws "Paused!"
		    else if (pause)
		    {
			g.setColor (Color.GREEN);

			g.setFont (largeFont);
			g.drawString ("Paused!", this.getSize ().width / 2 - 120, this.getSize ().height / 2 + 20); //draws "paused!" in the middle of the screen
		    }
		}
		else
		    g.drawImage (crashed, 0, 0, this.getSize ().width, this.getSize ().height, null); //draws the game over menu
	    }
	    else if (menuOptions == 2)
		//draws the credits
		g.drawImage (credits, 0, 0, this.getSize ().width, this.getSize ().height, null);
	    else if (menuOptions == 3)
	    {
		//draws the settings
		g.drawImage (settings, 0, 0, this.getSize ().width, this.getSize ().height, null);

		g.setColor (Color.GREEN);

		g.setFont (smallFont);

		g.drawString ("Difficulty: " + difficulty, this.getSize ().width / 3 + 13, this.getSize ().height / 2 - 16);
		g.drawString ("Speed: " + speed, this.getSize ().width / 3 + 23, this.getSize ().height / 2 + 29);
		if (moveCount == 0.25)
		    g.drawString ("Stable Helicopter: OFF", this.getSize ().width / 3 - 32, this.getSize ().height / 2 + 79);
		else
		    g.drawString ("Stable Helicopter: ON", this.getSize ().width / 3 - 32, this.getSize ().height / 2 + 79);
	    }
	    else if (menuOptions == 4)
		//draws the controls
		g.drawImage (controls, 0, 0, this.getSize ().width, this.getSize ().height, null);

	}
	//throws an Exception
	catch (Exception e)
	{
	    e.printStackTrace ();
	}
	finally
	{
	    bufferGraphics.dispose (); //clears the secondary screen
	}
    }


    public void DrawBackbuffertoScreen ()
    {
	bufferStrategy.show (); //shows the graphics from the secondary screen to the primary screen

	Toolkit.getDefaultToolkit ().sync (); //sync's the secondary screen to the first screen (applet/ visible window)
    }


    /** mainly used to check were the mouse was clicked (used to navigate through menus)
    */

    public void mouseClicked (MouseEvent me)
    {
	if (menu == true)
	{
	    if (me.getY () >= (this.getSize ().height / 2.35) && me.getY () < (this.getSize ().height / 1.94) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 2.50))
	    {
		menuOptions = 1; //play option is clicked in the main menu
		menu = false;
		clip.play ();
	    }
	    else if (me.getY () >= (this.getSize ().height / 1.94) && me.getY () < (this.getSize ().height / 1.65) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 2.00))
	    {
		menuOptions = 2; //credits option is clicked in the main menu
		menu = false;
		clip.play ();
	    }
	    else if (me.getY () >= (this.getSize ().height / 1.65) && me.getY () < (this.getSize ().height / 1.42) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 1.80))
	    {
		menuOptions = 3; //controls option is clicked in the main menu
		menu = false;
		clip.play ();
	    }
	    else if (me.getY () >= (this.getSize ().height / 1.42) && me.getY () < (this.getSize ().height / 1.25) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 1.76))
	    {
		menuOptions = 4; //settings option is clicked in the main menu
		menu = false;
		clip.play ();
	    }
	    else
	    {
		menuOptions = 0; //no options are clicked
		clipBack.play ();
	    }
	}

	else if (menuOptions == 1 && gameOver == true)
	{
	    //if the play again option is clicked after the game is over/helicopter crashes
	    if (me.getY () > (this.getSize ().height / 2.19) && me.getY () < (this.getSize ().height / 1.92) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 1.69))
	    {
		//restarts the game
		menuOptions = 1;
		menu = false;
		gameOver = false;
		clickCount = 0;
		clip.play ();
	    }
	    //if the mainmenu option is clicked after the game is over/helicopter craches
	    else if (me.getY () >= (this.getSize ().height / 1.92) && me.getY () < (this.getSize ().height / 1.72) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 1.88))
	    {
		//goes to the main menu
		menuOptions = 0;
		menu = true;
		gameOver = false;
		clickCount = 0;
		clipBack.play ();
	    }
	}

	else if (menuOptions == 1)
	{
	    clickCount = 1;
	    if (!startGame)
		startGame = true;
	}

	else if (menuOptions == 2 || menuOptions == 4)
	{
	    if (me.getY () > (this.getSize ().height / 1.37) && me.getY () < (this.getSize ().height / 1.27) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 2.76))
	    {
		//goes to the main menu
		menuOptions = 0;
		menu = true;
		clipBack.play ();
	    }
	}
	else if (menuOptions == 3)
	{
	    if (me.getY () > (this.getSize ().height / 1.37) && me.getY () < (this.getSize ().height / 1.27) && me.getX () > (this.getSize ().width / 4.57) && me.getX () < (this.getSize ().width / 2.76))
	    {
		//goes to the main menu
		menuOptions = 0;
		menu = true;
		clipBack.play ();
	    }
	    //button to the left of difficult is clicked
	    else if (me.getY () > (this.getSize ().height / 2.37) && me.getY () < (this.getSize ().height / 2.04) && me.getX () > (this.getSize ().width / 4.68) && me.getX () < (this.getSize ().width / 3.52))
	    {
		if (difficulty > 1)
		{
		    difficulty -= 1;
		    heightChangeOfRect -= 2;
		    heightOfMiddleRect -= 3;
		    defaultRateOfMiddleRect += 20;
		    rateOfMiddleRect = defaultRateOfMiddleRect;
		    rateOfRect += 30;
		    incrementOfROMR -= 2;
		    upSpeedOfCave -= 1;
		    downSpeedOfCave += 1;
		    defaultSpace += 4;
		    space = defaultSpace;
		    rateOfSpace -= 1;
		    rateOfMaxTotalSize -= 3;

		    xPosCopter -= 20;
		    speedOfChange -= 0.005;
		}
		clip.play ();
	    }
	    //button to the right of difficult is clicked
	    else if (me.getY () > (this.getSize ().height / 2.37) && me.getY () < (this.getSize ().height / 2.04) && me.getX () > (this.getSize ().width / 1.86) && me.getX () < (this.getSize ().width / 1.65))
	    {
		if (difficulty < 4)
		{
		    difficulty += 1;
		    heightChangeOfRect += 2;
		    heightOfMiddleRect += 3;
		    defaultRateOfMiddleRect -= 20;
		    rateOfMiddleRect = defaultRateOfMiddleRect;
		    rateOfRect -= 30;
		    incrementOfROMR += 2;
		    upSpeedOfCave += 1;
		    downSpeedOfCave -= 1;
		    defaultSpace -= 4;
		    space = defaultSpace;
		    rateOfSpace += 1;
		    rateOfMaxTotalSize += 3;

		    xPosCopter += 20;
		    speedOfChange += 0.005;
		}
		clip.play ();
	    }
	    //button to the left of speed is clicked
	    else if (me.getY () > (this.getSize ().height / 1.94) && me.getY () < (this.getSize ().height / 1.70) && me.getX () > (this.getSize ().width / 4.68) && me.getX () < (this.getSize ().width / 3.52))
	    {
		//decreases the speed of the bricks
		if (speed > 1)
		{
		    speed -= 1;
		    distanceSpeed -= 0.1;
		}
		clip.play ();
	    }
	    //button to the right of speed is clicked
	    else if (me.getY () > (this.getSize ().height / 1.94) && me.getY () < (this.getSize ().height / 1.70) && me.getX () > (this.getSize ().width / 1.86) && me.getX () < (this.getSize ().width / 1.65))
	    {
		//increases the speed of the bricks
		if (speed < 10)
		{
		    speed += 1;
		    distanceSpeed += 0.1;
		}
		clip.play ();
	    }
	    //"Stable Copter" is clicked
	    else if (me.getY () > (this.getSize ().height / 1.64) && me.getY () < (this.getSize ().height / 1.44) && me.getX () > (this.getSize ().width / 4.68) && me.getX () < (this.getSize ().width / 1.65))
	    {
		if (moveCount == 0)
		    moveCount = 0.25; //increases how fast the helicopter ascends or decends
		else
		    moveCount = 0; //decreases how fast the helicopter ascends or decends
		clip.play ();
	    }
	}
    }


    /** checks if the mouse entered the applet
    */

    public void mouseEntered (MouseEvent me)
    {
	if (menuOptions == 1)
	{
	    pause = false;
	}
    }


    /** checks if the mouse exited the applet
    */

    public void mouseExited (MouseEvent me)
    {
	if (menuOptions == 1)
	    pause = true;
    }


    /** checks if the mouse button is pressed
    */

    public void mousePressed (MouseEvent me)
    {
	buttonPressed = true;
    }


    /** checks if the mouse button is released
    */

    public void mouseReleased (MouseEvent me)
    {
	buttonPressed = false;
    }
}
