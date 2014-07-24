package controllers

import play.api._
import play.api.mvc._

/**
 * Created by Dmitry Meshkov on 24.07.2014.
 */
object Search extends Controller{
  def search(query: List[String]) = Action {
    Ok(" query=" + query.toVector)
  }
}
