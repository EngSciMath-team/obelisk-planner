package planner

import java.nio.file.Paths

import org.scalatest.FlatSpec
import solver.Solver

class PlannerSpec extends FlatSpec {


  "Planner" should "deserialize nicely" in {
    val recipesPath = Paths.get(".").toAbsolutePath + "/src/test/data/recipes"

    val recipes = Planner.loadRecipes(recipesPath)

    val result = Solver.solve(recipes)

    // Test
    assert(result.objectiveValue == 1.6666666666666665)
    assert(result.recipeSolutions(0).solution == 0.33333333333333337)
    assert(result.recipeSolutions(1).solution == 1.0)
    assert(result.recipeSolutions(2).solution == 0.3333333333333333)
    assert(result.recipeSolutions(3).solution == 0.3333333333333333)
  }

}
