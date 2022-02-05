import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import repository.{Product, ProductRepository}

object Boot extends App {
  implicit val system: ActorSystem = ActorSystem("system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  var route = path("hello") {
    get {
      complete(
        HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Olá, Akka!</h1>")
      )
    }
  }

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(route)

  ProductRepository.create(Product(1, "abc", 1)).onComplete(p => println(p))

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
