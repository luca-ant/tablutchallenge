# AI client tablut game

## How to start player
Clone the repository
```
git clone https://github.com/luca-ant/tablutchallenge.git
```

Run JAR file. The first argument must be your color (BLACK or WHITE)

`
java -jar teampallo.jar BLACK
`

or

`
java -jar teampallo.jar WHITE
`



Optionally you can add some arguments to change the execution

**-t [timeout]** -> to change timeout for each move (*default 55 sec*)

**-p [core]** -> to set threading option (*default 8*)

**-d [max_depth]** -> to change max depth which min-max alghoritm try to reach (*default 8*)

**Example of used option parameters:**

`
java -jar teampallo.jar BLACK -t 30
`
