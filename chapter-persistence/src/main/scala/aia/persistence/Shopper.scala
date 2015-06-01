package aia.persistence

import akka.actor._

object Shopper {
  def props(shopperId: Long) = Props(new Shopper)
  def name(shopperId: Long) = shopperId.toString

  trait Command {
    def shopperId: Long
  }

  case class PayBasket(shopperId: Long) extends Command
}

class Shopper extends Actor {
  import Shopper._

  def shopperId = self.path.name.toLong

  val basket = context.actorOf(Basket.props,
    Basket.name(shopperId))

  val wallet = context.actorOf(Wallet.props(shopperId),
    Wallet.name(shopperId))

  def receive = {
    case cmd: Basket.Command => basket forward cmd
    case cmd: Wallet.Command => wallet forward cmd

    case PayBasket(shopperId) => basket ! Basket.GetItems(shopperId)
    case Basket.Items(list) => wallet ! Wallet.Pay(list, shopperId)
    case Wallet.Paid(_, shopperId) => basket ! Basket.Clear(shopperId)

    // alternative:
    // issue: ask timeout
    // benefit: report back to sender of final result.
    //
    // case PayBasket(shopperId) =>
    //   import scala.concurrent.duration._
    //   import context.dispatcher
    //   import akka.pattern.ask
    //   implicit val timeout = akka.util.Timeout(10 seconds)
    //   for {
    //     items <- basket.ask(Basket.GetItems(shopperId)).mapTo[Basket.Items]
    //     paid <- wallet.ask(Wallet.Pay(items.list, shopperId)).mapTo[Wallet.Paid]
    //   } yield {
    //     basket ! Basket.Clear(shopperId)
    //   }
  }
}
