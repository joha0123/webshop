package controllers

import javax.inject._
import models.ItemRepository
import play.api.mvc._
import play.api.libs.json.Json

import javax.inject._

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._


import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(itemRepo: ItemRepository, cc: ControllerComponents)
                              (implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() = Action { implicit request: Request[AnyContent] =>

    itemRepo.create("testItem", 12.50)
    Ok(views.html.index())
  }

  def createDb() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def items = Action.async { implicit request =>
    itemRepo.list().map { items =>
      Ok(Json.toJson(items))
    }
  }

}
