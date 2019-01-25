# obelisk-planner

This repo contains just the Solver and a module that loads all json files from a provided directory and passes them into Solver.

All other functionality such as a REST or web interfaces for managing recipes will be in separate repos and probably written in Python.

To test the functionality, run PlannerSpec in test/scala/planner/PlannerSpec.scala

Test data for the spec is in test/data directory

Currently all files with a .json extension from a single directory are read. At some point we need to implement recursion into any existing subdirectories, pending decision on how file storage will be structured.