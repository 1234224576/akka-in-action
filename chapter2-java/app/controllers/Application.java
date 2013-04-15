package controllers;

import static akka.pattern.Patterns.ask;
import play.libs.Akka;
import play.libs.F.Function;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import models.Event;
import java.util.List;
import java.util.ArrayList;
import actors.TicketingAgent;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Json;
import org.codehaus.jackson.JsonNode;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result newEvent() {
		return ok(index.render("created new event"));
	}

	public static Result currentEvents() {
		List events = new ArrayList<Event>();
		Event event = new Event();
		event.setName("test event");
		event.setNumberOfTickets(5);
		events.add(event);
		event = new Event();
		event.setName("test event 2");
		event.setNumberOfTickets(100);
		events.add(event);
		JsonNode eventsJson = Json.toJson(events);
		return ok(eventsJson.toString()).as("application/json");
	}

	public static Result ping() {
	
		ActorRef myActor = Akka.system().actorOf(
				new Props(TicketingAgent.class));

		return async(Akka.asPromise(ask(myActor, "hello", 1000)).map(
				new Function<Object, Result>() {
					public Result apply(Object response) {
						return ok(response.toString());
					}
				}));
	}

}
