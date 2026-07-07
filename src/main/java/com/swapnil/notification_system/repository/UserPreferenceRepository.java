package com.swapnil.notification_system.repository;

import com.swapnil.notification_system.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    //finds the preference using the user id's
    Optional<UserPreference> findByUserId(Long userId);

}
