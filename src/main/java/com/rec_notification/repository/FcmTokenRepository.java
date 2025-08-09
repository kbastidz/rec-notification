package com.rec_notification.repository;

import com.rec_notification.entity.FcmToken;
import com.rec_notification.enums.DeviceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    //Optional<FcmToken> findByUserIdAndIsActiveTrue(String userId);
    List<FcmToken> findByUserIdAndIsActiveTrue(String userId);
    List<FcmToken> findByDeviceTypeAndIsActiveTrue(DeviceType deviceType);

    @Modifying
    @Query("UPDATE FcmToken f SET f.isActive = false WHERE f.userId = :userId")
    void deactivateTokensByUserId(@Param("userId") String userId);
}