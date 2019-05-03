# term-project

### Project Idea
We want to make a project for the roleplaying game Dungeons and Dragons, with the intent to help both players and dungeon masters access information quickly during a session. The rules for D&D are very complex, and with hundreds of items, monsters, and spells to encounter, and each character is a unique mix of different races, classes, and backgrounds, each of which come with their own set of features unlocked at different levels. Most members of our group are longtime D&D players, and frequently find ourselves Googling various terms and rules during a session and opening tons of tabs in the process.

Our goal is to create a REPL-based system that allows players to look up information instantly without their fingertips ever leaving the keyboard. Frequently, we just want a snippet of information: does Heat Metal require concentration? What are all beasts below CR2 that my Druid can transform into? How much is a Bag of Holding? This project will first require building a database through data scraping the Player's Handbook and the D&D Wiki, and as we fill in the basic deliverables of looking up various attributes of the ruleset, we hope to add more complex helper mechanisms. One would be an automatic encounter generator based on the current party's composition and status, for DMs making things up on the fly. During an encounter, the DM could even adjust the health and status levels of these enemies on the fly, to avoid keeping another tab open with that information, which diverges focus away from narrating the campaign. Other modules we're considering include procedurally generating side-quests, simple NPCs, and dungeon maps.

With the tools learned in CS32, such as database querying and designing a flexible REPL, we want to combine this together with one of our favorite pasttime activities to create something we'll actually continue to use after the course is over. Other members of our parties have already expressed interest in beta-testing it, and if this project is successful, we hope to host it on a server for anyone in the world to use. (Of course, we hope to make the web-based version prettier and more user-friendly, perhaps with nodes that can stay around from past searches to visually help organize the workspace.)

### Team Strengths/Weaknesses
Two of our team members come from the 15/16 intro sequence, and the other two come from 17/18. Overall, we have a good balance of front-end/back-end/full-stack developers, and each of us are interested in different parts of this project. Andrew and Donnie have a great intuition for general code design, and are interested in implementing the algorithmic complexities of this project, drawing from their past experience in fields like machine learning. Julie and Giuse come from a more front-end perspective, and are interested in designing not just what the UI will look like, but how the user interacts with the commands to perform results in the least amount of keystrokes necessary. In addition, most of us have a lot of experience with D&D, and have a wealth of first-hand experience to draw from in regards to how we can best design this project towards our projected user base. However, none of us have extensive experience with Spark and building interactive web applications, so we're unsure of how to handle the Javascript vs Java backend balance once we start thinking about our front-end GUI.

## Known bugs
There are no known bugs that we are currently aware of.

## Design Details specific to code
We implemented the Command Manager pattern to handle REPL commands. We used 
the Command, Handler, CommandHandler, REPL, and command handlers to make a 
generic REPL and manager, with each individual command being a handler class.
Since most of our commands query the database, we created enums, Result, and 
ReturnType classes to generalize what the commands return, such as DUNGEON,
MONSTER, ENCOUNTER, etc. 

Much of our project is based on algorithms that focus on intelligent 
randomization that factors in user input and tries to make the end product as 
organic as possible. For generating the dungeon, we abstracted out the Dungeon
and Room/Path classes, overlaying them onto a 2d grid of coordinates. We also
created a Randomizer utility class to handle generating random numbers within
a range, as well as the NormalDistribution class to handle drawing numbers from
a normal distribution. We designed our dungeon algorithm to:

1. Generate random room top-left coordinates given the dungeon dimensions, and
terminate when either a specified percentage of the dungeon is full or if 
x-amount of failures to generate a valid room have happened, signaling that the
dungeon is becoming populated enough.

2. Toss out some of the rooms that do not meet a certain size threshold that
is calculated based on the mean room size.

3. Create a graph that connects every room to every other room

4. Generate an mst of this graph that will toss out some of the edges

5. Create Path rooms based on these edges. Path sizes are pulled from a uniform
distribution based on how two rooms can connect to each other. Paths can also be
two-segment, looping in an L-shape in one of both directions if two rooms 
cannot be directly vertically or horizontally connected. 

## How to build and run our program/tests from the command line
To build our program and run the unit tests, the user should first mvn package
the project. To run our system tests, the user should run the cs32-test
command, with the following possible paths:

python3 cs32-test tests/generate-dungeon/*
                  tests/generate-encounter/*
                  tests/generate-npc/*
                  tests/search/*
                  tests/roll/*
                  tests/*/*  
                  
Once the program is mvn packaged, the user can then enter ./run or ./run --gui,
depending on if they want to also run the GUI. Once the program is running, 
the user can enter any of the following commands:

Commands that can be run in the console: 

Search

search <term>: searches the entire database (e.g. search foresight, or search “danse macabre”)
search <table> <term>: searches a specific table (e.g. search monsters bear)
search <table> | <flags>: searches with a set of operators in a table (e.g. search spells | class: bard, level: 2, or search monsters | type: humanoid, ac: >= 18, int: >= 14)
Tip: you can also search with just “s” for short!
Searchable tables:
Spells
Name
School
Level
Ritual
Range
castingTime
Verbal
Somatic
Concentration
Materials
Duration
Class
Description
Monsters
Name
Size
Type
Alignment
AC
HP
hpDice
Speed
Str
Dex
Con
Int
Wis
Cha
CR
Traits
Actions
legendaryActions
Feats
Name
Description

Roll

roll <dice>: randomly rolls the result (e.g. roll 2d4 will roll two four-sided dice and sum the results together)
Tip: you can join dice amounts together, such as roll 2d4 + 10d6 - 5, for you Sneak Attack Rogues!

Generate NPC

generate-npc: generates an entirely random NPC
generate-npc <flags>: allows finer control over the randomization. Separate flags by a space (e.g. generate-npc size: medium, type: humanoid)

Generate Encounter

generate-encounter <partyLevel>: generates a random encounter for a combined party level (e.g. generate-encounter 24, if your party has 3 level 8 characters)
Generate Dungeon (simple)
generate-dungeon: <width> <height> <small, medium or large>

Help

/help: see list of all commands
/help <command>: see more details about a specific command

SuperREPL GUI Features:
Battle manager
When generate-encounter is run, the battle manager will automatically populate with the monsters given. The table is editable, so it’s quite easy to adjust their health and add notes mid-battle.
Sticky system
If you see a result you want to sticky for later, click its name and that will sticky it to the side.
Generate Dungeon (complex)
Opens up a new tab that displays a UI for generating dungeons. Pretty pictures are involved.

## Checkstyle appeals
In our Monster class, the checkstyle does not like that there are more than 7
parameters in the constructor. We thought about incorporating them all into 
a single hashmap, but concluded that it wouldn't be as clear, since the monsters
have a finite amount of known characteristics in DnD and can be represented as
such in the class. 

Similarly, when we extract a monster from a query result, checkstyle complains
about the magic numbers due to the high indices of the ResultSet. We thought it
would be even worse style to declare final int variables from 9-18, as 
declaring them is usually for constants used in our code, not array indices. 

Checkstyle also complains about importing using the .* method. However, 
IntelliJ automatically does this, despite us changing it and importing the
libraries manually.