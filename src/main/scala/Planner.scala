import java.nio.file.{Files, Paths}

import com.typesafe.config.{Config, ConfigException, ConfigFactory}
import io.{DbLoader, FileLoader}
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

  private def plan(pathArg: String) = {
    val path = Paths.get(pathArg)
    if (Files.exists(path)) {
      // val recipes = FileLoader.loadRecipes(path) <-  if you want to load from files, commented out for now
      val recipes = DbLoader.loadRecipes
      val result = Solver.solve(recipes)
      println(result)
    } else {
      println("The supplied directory is not valid, please try again.")
    }
  }
}
