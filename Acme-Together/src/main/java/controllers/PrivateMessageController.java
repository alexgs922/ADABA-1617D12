/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.PrivateMessageService;
import domain.Actor;
import domain.PrivateMessage;

@Controller
@RequestMapping("/privateMessage")
public class PrivateMessageController extends AbstractController {

	@Autowired
	private PrivateMessageService	privateMessageService;

	@Autowired
	private ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public PrivateMessageController() {
		super();
	}

	@RequestMapping(value = "/listReceivedMessages", method = RequestMethod.GET)
	public ModelAndView listReceivedMessages() {
		final ModelAndView result;

		final Actor c = this.actorService.findByPrincipal();

		final Collection<PrivateMessage> receivedMessages = this.privateMessageService.myRecivedMessages(c.getId());

		result = new ModelAndView("privateMessage/listReceivedMessages");
		result.addObject("privateMessages", receivedMessages);
		result.addObject("requestURI", "privateMessage/listReceivedMessages.do");

		return result;
	}
	@RequestMapping(value = "/listSentMessages", method = RequestMethod.GET)
	public ModelAndView listSentMessages() {
		ModelAndView result;

		final Actor c = this.actorService.findByPrincipal();

		final Collection<PrivateMessage> sendMessages = this.privateMessageService.mySendedMessages(c.getId());

		result = new ModelAndView("privateMessage/listSentMessages");
		result.addObject("privateMessages", sendMessages);
		result.addObject("requestURI", "privateMessage/listSentMessages.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PrivateMessage privateMessage;

		privateMessage = this.privateMessageService.create();

		result = this.createEditModelAndView(privateMessage);

		return result;
	}

	@RequestMapping(value = "/response/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int actorId) {
		ModelAndView result;
		PrivateMessage privateMessage;

		final Actor c = this.actorService.findOneToSent(actorId);
		privateMessage = this.privateMessageService.create(c);

		result = this.createEditModelAndView2(privateMessage);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(PrivateMessage privateMessageToEdit, final BindingResult binding) {

		ModelAndView result;

		privateMessageToEdit = this.privateMessageService.reconstruct(privateMessageToEdit, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(privateMessageToEdit);
		else
			try {
				this.privateMessageService.save(privateMessageToEdit);
				result = new ModelAndView("redirect:/privateMessage/listSentMessages.do");

			} catch (final Throwable th) {
				result = this.createEditModelAndView(privateMessageToEdit, "chirp.commit.error");

			}

		return result;
	}

	@RequestMapping(value = "/response/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(PrivateMessage privateMessageToEdit, final BindingResult binding) {

		ModelAndView result;

		privateMessageToEdit = this.privateMessageService.reconstruct(privateMessageToEdit, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView2(privateMessageToEdit);

			result.addObject("privateMessage", privateMessageToEdit);

		} else {
			try {

				this.privateMessageService.save(privateMessageToEdit);

			} catch (final Throwable th) {
				result = this.createEditModelAndView2(privateMessageToEdit, "chirp.commit.error");

				result.addObject("privateMessage", privateMessageToEdit);

				return result;
			}

			result = new ModelAndView("redirect:/privateMessage/listReceivedMessages.do");

		}

		return result;
	}

	// DELETE ---------------------------------------------------

	@RequestMapping(value = "/deleteReceived", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int privateMessageId) {

		ModelAndView result;
		PrivateMessage privateMessage;

		privateMessage = this.privateMessageService.findOne(privateMessageId);
		try {
			this.privateMessageService.deleteReceived(privateMessage);
		} catch (final Throwable th) {

		}

		result = new ModelAndView("redirect:/privateMessage/listReceivedMessages.do");

		return result;

	}

	@RequestMapping(value = "/deleteSent", method = RequestMethod.GET)
	public ModelAndView delete2(@RequestParam final int privateMessageId) {

		ModelAndView result;
		PrivateMessage privateMessage;

		privateMessage = this.privateMessageService.findOne(privateMessageId);
		try {
			this.privateMessageService.deleteSent(privateMessage);
		} catch (final Throwable th) {

		}

		result = new ModelAndView("redirect:/privateMessage/listSentMessages.do");

		return result;

	}

	//CREATE EDIT MODEL AND VIEW

	protected ModelAndView createEditModelAndView(final PrivateMessage privateMessageToEdit) {
		ModelAndView result;

		result = this.createEditModelAndView(privateMessageToEdit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PrivateMessage privateMessageToEdit, final String message) {
		ModelAndView result;

		Collection<Actor> actors;
		final Actor c = this.actorService.findByPrincipal();

		actors = this.actorService.findAll();
		actors.remove(c);

		result = new ModelAndView("privateMessage/create");
		result.addObject("privateMessage", privateMessageToEdit);
		result.addObject("actors", actors);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final PrivateMessage privateMessageToEdit) {
		ModelAndView result;

		result = this.createEditModelAndView2(privateMessageToEdit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final PrivateMessage privateMessageToEdit, final String message) {
		ModelAndView result;

		result = new ModelAndView("privateMessage/response/create");
		result.addObject("privateMessage", privateMessageToEdit);
		result.addObject("message", message);

		return result;
	}

}
