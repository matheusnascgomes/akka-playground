package repository

import slick.dbio.DBIO
import slick.lifted.Tag
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._
import repository.DBConnection.db

case class Product(id: Long, descricao: String, preco: Double)
class ProductSchema(tag: Tag) extends Table[Product](tag, "product") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def description = column[String]("description")
  def price = column[Double]("price")
  def * = (id, description, price) <> (Product.tupled, Product.unapply)
}

object ProductRepository extends DBConnection {
  private val tabela = TableQuery[ProductSchema]
  val schema = tabela.schema

  db.run(
    DBIO.seq(
      schema.createIfNotExists
    )
  )
  def create(product: Product): Future[Product] = run {
    (tabela returning tabela) += product
  }
}
