package Capstone.easyreservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.repository.RoomRepository;

@Service
public class RoomService {
	@Autowired
	private RoomRepository stanzaRepository;

}
