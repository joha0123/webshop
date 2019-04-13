package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ItemRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ItemTable(tag: Tag) extends Table[Item](tag, "items") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]( "name" )
    def nettoPrice = column[Double]("netto_price")

    def * = (id, name, nettoPrice) <> ((Item.apply _).tupled, Item.unapply)

  }

  private val items = TableQuery[ItemTable]

  def create(name: String, nettoPrice: Double): Future[Item] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (items.map(i => (i.name, i.nettoPrice))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning items.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((namePrice, id) => Item(id, namePrice._1, namePrice._2))
      // And finally, insert the person into the database
      ) += (name, nettoPrice)
  }

  def list(): Future[Seq[Item]] = db.run {
    items.result
  }

}
