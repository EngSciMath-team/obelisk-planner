import java.nio.file.{Files, Paths}

import com.typesafe.config.{Config, ConfigException, ConfigFactory}
import io.Loader
import solver.Solver

object Planner {

  def main(args: Array[String]): Unit = {
    args.toList.headOption match {
      case Some(recipesPath) => plan(recipesPath)
      case None =>
        val config = ConfigFactory.load
        try {
          val recipesPath = config.getString("recipes.path")
          plan(recipesPath)
        } catch {
          case e: ConfigException =>
            println("Error deriving recipes directory from command line arguments or configuration! Details: " + e.getMessage)
        }
    }
  }

  def plan(pathArg: String) = {
    val path = Paths.get(pathArg)
    if (Files.exists(path)) {
      val recipes = Loader.loadRecipes(path)
      val result = Solver.solve(recipes)
      println(result)
    } else {
      println("The supplied directory is not valid, please try again.")
    }
  }
}
