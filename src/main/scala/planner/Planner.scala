package planner

import java.io.File
import java.nio.charset.StandardCharsets._
import java.nio.file.{Files, Paths}

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import solver.Recipe

case class RecipesArray(recipes: Seq[Recipe])

object Planner {

  def loadRecipes(recipesPath: String): Seq[Recipe] = {
    val recipeFiles = getListOfFiles(Paths.get(recipesPath).toFile)

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)

    val recipes = recipeFiles.flatMap { recipeFile =>
      val fileData = new String(Files.readAllBytes(recipeFile.toPath), UTF_8)
      val fileRecipes = mapper.readValue(fileData, classOf[RecipesArray])
      fileRecipes.recipes
    }

    println(recipes.size)

    recipes
  }

  private def getListOfFiles(dir: File): List[File] = {
    val extensions = List("json", "JSON")
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }
}
