package com.dstevens.event;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dstevens.character.PlayerCharacterRepository;
import com.dstevens.troupe.TroupeRepository;
import com.dstevens.web.config.ServerConfiguration;
import com.google.gson.Gson;

@Controller
public class EventController {

	private final PlayerCharacterRepository characterRepository;
	private final TroupeRepository troupeRepository;
	private final EventRepository eventRepository;
	private final ServerConfiguration serverConfiguration;

	@Autowired
	public EventController(EventRepository eventRepository, TroupeRepository troupeRepository, PlayerCharacterRepository characterRepository, ServerConfiguration serverConfiguration) {
		this.eventRepository = eventRepository;
		this.troupeRepository = troupeRepository;
		this.characterRepository = characterRepository;
		this.serverConfiguration = serverConfiguration;
	}
	
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public @ResponseBody String getEvents() {
		List<DisplayableEvent> collect = StreamSupport.stream(eventRepository.findAll().spliterator(), false).
				             map((Event t) -> DisplayableEvent.reference(t)).
				             sorted().
				             collect(Collectors.toList());
		return new Gson().toJson(collect);
	}
	
	@RequestMapping(value = "/events/{id}", method = RequestMethod.GET)
	public @ResponseBody String getEvent(@PathVariable Integer id) {
		Event event = eventRepository.findWithId(id);
		DisplayableEvent listable = DisplayableEvent.from(event);
		return new Gson().toJson(listable);
	}
	
	@ResponseStatus(value=HttpStatus.CREATED)
	@RequestMapping(value = "/events", method = RequestMethod.POST)
	public @ResponseBody String  addEvent(HttpServletRequest request, HttpServletResponse response, @RequestBody final DisplayableEvent event) {
		Event savedEvent = eventRepository.save(event.to(troupeRepository, characterRepository));
		return serverConfiguration.getHost() + "/events/" + savedEvent.getId(); 
	}
}