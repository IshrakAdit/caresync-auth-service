package com.caresync.service.auth.repositories;

import com.caresync.service.auth.entities.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, String> {
}