package Capstone.easyreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.services.RoomService;

@RestController
@RequestMapping("/stanze")
public class RoomController {
	@Autowired
	private RoomService stanzaService;

}