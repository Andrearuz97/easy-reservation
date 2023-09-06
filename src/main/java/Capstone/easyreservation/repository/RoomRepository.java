package Capstone.easyreservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Capstone.easyreservation.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	List<Room> findByHotel_Id(Long hotelId);
}
