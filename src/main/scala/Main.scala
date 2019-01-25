import java.nio.file.Paths

import planner.Planner
import solver.Solver

object Main extends App {
  val recipesPath = Paths.get(".").toAbsolutePath + "/src/test/data/recipes"
  val recipes = Planner.loadRecipes(recipesPath)

  val result = Solver.solve(recipes)
  println(result)
}
