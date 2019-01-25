# obelisk-planner

## Usage

NOTE: This assumes a Linux/Mac local machine. Windows commands will be slightly different.

Install sbt.

Clone this repo.

Run "sbt stage" at root of repo. This should create directory "target" with subdirectories. Navigate to:

> obelisk-planner/target/universal/stage/bin

Run: 

> ./obelisk-planner 

You should get something like this output:

> The supplied directory is not valid, please try again.

That's because obelisk-planner expects a directory of recipe files as its argument, like so:

> ./obelisk-planner /var/tmp/obelisk/recipes

In the example above, directory "/var/tmp/obelisk/recipes" should contain some JSON files which conform to Obelisk recipe format.

The project already contains two sample recipe files in "obelisk-planner/src/test/data/recipes/" directory. So we can supply that directory as an argument for now:

> ./obelisk-planner /wherever/you/cloned/this/to/obelisk-planner/src/test/data/recipes/

Make sure to replace "/wherever/you/cloned/this/to" with a full path where you cloned this repo to.

If everything is good, you should get this output:

```
4
Coin0506I Presolve 2 (-2) rows, 3 (-1) columns and 4 (-3) elements
Clp0006I 0  Obj -0 Dual inf 2.999998 (2)
Clp0006I 3  Obj 1.6666667
Clp0000I Optimal - objective value 1.6666667
Coin0511I After Postsolve, objective 1.6666667, infeasibilities - dual 0 (0), primal 0 (0)
Clp0032I Optimal objective 1.666666667 - 3 iterations time 0.002, Presolve 0.00
SolverResult(1.6666666666666665,List(RecipeSolution(Freezing,0.33333333333333337), RecipeSolution(Ice Consumption,1.0), RecipeSolution(Flower Growing,0.3333333333333333), RecipeSolution(Flower Consumption,0.3333333333333333)))
```

## Command line arguments or configuration

In addition to supplying recipes directory as a command line argument, we can also set it in file "src/main/resources/application.conf"

Then you don't need to supply it as a program argument every time.

## General Info

This repo contains just the Solver and a module that loads all json files from a provided directory and passes them into Solver.

All other functionality such as a REST or web interfaces for managing recipes will be in separate repos and probably written in Python.

To test the functionality, run PlannerSpec in test/scala/planner/PlannerSpec.scala

Test data for the spec is in test/data directory

Currently all files with a .json extension from a single directory are read. At some point we need to implement recursion into any existing subdirectories, pending decision on how file storage will be structured.