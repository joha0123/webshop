package models

import play.api.libs.json.Json

case class Item(id: Long, name: String, nettoPrice: Double)

object Item {
  implicit val itemFormat = Json.format[Item]
}
