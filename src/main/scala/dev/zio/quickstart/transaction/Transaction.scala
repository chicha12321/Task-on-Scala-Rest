package dev.zio.quickstart.transaction

import java.util.UUID
import zio.json.*

case class Transaction(src: Int, dst: Int, amount: Int)

object Transaction:
  given JsonEncoder[Transaction] =
    DeriveJsonEncoder.gen[Transaction]
  given JsonDecoder[Transaction] =
    DeriveJsonDecoder.gen[Transaction]

