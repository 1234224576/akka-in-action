package com.manning.aa
package words

import java.net.URLEncoder

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor._

import org.scalatest._
import org.scalatest.matchers.MustMatchers

import JobReceptionist._
import akka.routing.BroadcastPool


trait LocalBroadcastWork extends BroadcastWork { this: Actor =>
  import JobWorker._


  override def createRouter: ActorRef = {
    context.actorOf(BroadcastPool(5).props(Props[JobWorker]), "worker-router")
  }

}

class TestJobMaster extends JobMaster with LocalBroadcastWork

class TestReceptionist extends JobReceptionist with CreateMaster {
  override def createMaster(name: String): ActorRef = context.actorOf(Props[TestJobMaster], name)
}

class LocalWordsSpec extends TestKit(ActorSystem("test"))
                        with WordSpec
                        with MustMatchers
                        with StopSystemAfterAll
                        with ImplicitSender {
  val receptionist = system.actorOf(Props[TestReceptionist], JobReceptionist.name)

  "the words" must {
    "finish a job" in {
      receptionist ! JobRequest("test1", List("this", "is", "a", "test"))
      expectMsg(JobSuccess("test1", Map("this" -> 1, "is"-> 1, "a" -> 1, "test" -> 1)))
      expectNoMsg
    }

    "finish another job" in {
      receptionist ! JobRequest("test2", List("this is a test ", "this is a test", "this is", "this"))
      expectMsg(JobSuccess("test2", Map("this" -> 4, "is"-> 3, "a" -> 2, "test" -> 2)))
      expectNoMsg
    }

    "finish another bigger job" in {
      val words = List("this is a test ", "this is a test", "this is", "this")
      receptionist ! JobRequest("test3", (1 to 100).map(i=> words ++ words).flatten.toList)
      import scala.concurrent.duration._
      expectMsg(10 seconds, JobSuccess("test3", Map("this" -> 800, "is"-> 600, "a" -> 400, "test" -> 400)))
      expectNoMsg
    }

    "retry a failing job" in {
      // the failure is simulated by a job worker throwing an exception on finding the word FAIL in the text.
      receptionist ! JobRequest("test4", List("this", "is", "a", "test", "FAIL!"))
      expectMsg(JobSuccess("test4", Map("this" -> 1, "is"-> 1, "a" -> 1, "test" -> 1)))
      expectNoMsg
    }
  }
}
