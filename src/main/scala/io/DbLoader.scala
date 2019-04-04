package io

import io.getquill._
import solver.{Recipe, Resource, ResourceProduction}

object DbLoader {

  case class Recipes(id: Int, recipeName: String, utility: Double)
  case class Resources(id: Int, resourceName: String, naturalProduction: Double, measurementUnit: String)
  case class Production(id: Long, recipeId: Int, resourceId: Int, production: Double)
  case class ResultRow(productionId: Long, recipeId: Int, resourceId: Int, production: Double,
                       recipeName: String, utility: Double, resourceName: String,
                       naturalProduction: Double, measurementUnit: String)

  private val ctx = new MysqlJdbcContext(SnakeCase, "db")

  import ctx._

  def loadRecipes: Seq[solver.Recipe] = {
    val select = quote {
      query[Production]
        .join(query[Recipes])
        .on((production, recipe) => production.recipeId == recipe.id)
        .join(query[Resources])
        .on((production, resource) => production._1.resourceId == resource.id)
        .map { case ((productionRow, recipeRow), resourceRow) =>
          ResultRow(productionRow.id, recipeRow.id, resourceRow.id, productionRow.production,
            recipeRow.recipeName, recipeRow.utility, resourceRow.resourceName,
            resourceRow.naturalProduction, resourceRow.measurementUnit
          )
        }
    }

    val results = ctx.run(select)
    val mapped = results.foldLeft(Map[Recipes, List[ResourceProduction]]()){
      case (acc, l) =>
        val recipeRow = Recipes(l.recipeId, l.recipeName, l.utility)
        val resource = Resource(l.resourceId, l.resourceName, l.measurementUnit, l.naturalProduction)
        val resourceProduction = ResourceProduction(resource, l.production)
        acc + (recipeRow -> (resourceProduction::acc.getOrElse(recipeRow, List())))
    }
    val recipes = mapped.map { x =>
      Recipe(x._1.id, x._1.recipeName, x._2, x._1.utility)
    }.toSeq

    println(s"Retrieved these recipes from DB: $recipes")
    recipes
  }

}
