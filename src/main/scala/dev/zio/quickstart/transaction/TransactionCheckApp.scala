package dev.zio.quickstart.transaction

import zio.*
import zio.json.*
import zhttp.http.*

import scala.io.Source

val blackList = Source.fromResource("blacklist.txt").getLines().toSet.map(_.toInt)

object TransactionCheckApp:

  def apply(): Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case req@Method.POST -> !! / "transaction-check" =>
        for
          extraction <- req.bodyAsString
            .map(_.fromJson[Transaction])
          transaction <- ZIO.fromEither(extraction).mapError(error => new Throwable(error))
          containsBadBoy <- ZIO.succeed(!blackList.exists(id => id == transaction.src || id == transaction.dst))
          four <- ZIO.from(if (containsBadBoy) {
            "success"
          } else {
            "cansel"
          })
          result <- ZIO.from(Response.json(four))
        yield result
    }
