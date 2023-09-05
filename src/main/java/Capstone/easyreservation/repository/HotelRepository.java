package Capstone.easyreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Capstone.easyreservation.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
}