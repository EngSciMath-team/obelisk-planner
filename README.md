# obelisk-planner

## About Obelisk

Obelisk is an attempt to create an alternative to the market for operating an economy. It works by modeling the economy as a set of recipes (eg, to make 3 books you need 5 papers and 0.5 electricity), each connecting to pools of resources, and attempts to figure out how these resources should be allocated to maximize utility (goodness, as numarically defined). Mathematically, this falls under operations research — usually either linear programming or, more generally, convex optimization, though there's also the more pragmatic problems of collecting and dispatching data, which this project also tackles.

Right now, the architecture consists of Obelisk-Planner, which does the math, and Obelisk-API, which will communicate with planned future user intefaces. Both will draw from and write to a central SQL database.

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

## Database Setup

- Install MySQL locally
- Run the following:

```
DELETE FROM mysql.user WHERE user = '';

create database obelisk;

create user 'obelisk_user'@'%'  identified by '';

grant all on obelisk.* to 'obelisk_user'@'%';
```

Then run the following (these should be added proper migrations via Flyway at some point):

```
CREATE TABLE IF NOT EXISTS resources (
    id BIGINT NOT NULL AUTO_INCREMENT,
    resource_name VARCHAR(256) NOT NULL,
    natural_production DECIMAL(40,20) NOT NULL,
    measurement_unit ENUM('Milliliter', 'Centimeter', 'Gram', 'Cup', 'Item', 'Cube', 'Pot Month'),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS recipes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    recipe_name VARCHAR(256) NOT NULL,
    utility DECIMAL(40,20) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS production (
    id BIGINT NOT NULL AUTO_INCREMENT,
    recipe_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    production DECIMAL(40,20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY `fk__recipe_id` (recipe_id) REFERENCES recipes(id),
    FOREIGN KEY `fk__resource_id` (resource_id) REFERENCES resources(id),
    UNIQUE KEY `uk__recipe_id_resource_id` (recipe_id, resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

Then populate with test data:

```
INSERT INTO `resources` (`id`,`resource_name`,`natural_production`,`measurement_unit`) VALUES (1,'Water',1.00000000000000000000,'Cup');
INSERT INTO `resources` (`id`,`resource_name`,`natural_production`,`measurement_unit`) VALUES (2,'Pot Time',1.00000000000000000000,'Pot Month');
INSERT INTO `resources` (`id`,`resource_name`,`natural_production`,`measurement_unit`) VALUES (3,'Flower',0.00000000000000000000,'Item');
INSERT INTO `resources` (`id`,`resource_name`,`natural_production`,`measurement_unit`) VALUES (4,'Ice',0.00000000000000000000,'Cube');

INSERT INTO `recipes` (`id`,`recipe_name`,`utility`) VALUES (1,'Freezing',0.00000000000000000000);
INSERT INTO `recipes` (`id`,`recipe_name`,`utility`) VALUES (2,'Ice Consumption',1.00000000000000000000);
INSERT INTO `recipes` (`id`,`recipe_name`,`utility`) VALUES (3,'Flower Growing',0.00000000000000000000);
INSERT INTO `recipes` (`id`,`recipe_name`,`utility`) VALUES (4,'Flower Consumption',2.00000000000000000000);

INSERT INTO `production` (`id`,`recipe_id`,`resource_id`,`production`) VALUES (1,1,1,-2.00000000000000000000);
INSERT INTO `production` (`id`,`recipe_id`,`resource_id`,`production`) VALUES (2,1,4,3.00000000000000000000);
INSERT INTO `production` (`id`,`recipe_id`,`resource_id`,`production`) VALUES (3,2,4,-1.00000000000000000000);
INSERT INTO `production` (`id`,`recipe_id`,`resource_id`,`production`) VALUES (4,3,1,-1.00000000000000000000);
INSERT INTO `production` (`id`,`recipe_id`,`resource_id`,`production`) VALUES (5,3,2,-3.00000000000000000000);
INSERT INTO `production` (`id`,`recipe_id`,`resource_id`,`production`) VALUES (6,3,3,1.00000000000000000000);
INSERT INTO `production` (`id`,`recipe_id`,`resource_id`,`production`) VALUES (7,4,3,-1.00000000000000000000);

```