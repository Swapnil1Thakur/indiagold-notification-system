package com.swapnil.notification_system.repository;

import com.swapnil.notification_system.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    //start


}
