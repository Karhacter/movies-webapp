package com.karhacter.movies_webapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.karhacter.movies_webapp.entity.Ad;
import com.karhacter.movies_webapp.entity.AdStatus;
import com.karhacter.movies_webapp.entity.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {
    List<Ad> findByStatus(AdStatus status);

    List<Ad> findByAdvertiser(User advertiser);

    List<Ad> findByStatusAndType(AdStatus status, String type);

    // Search ads by ad name, campaign owner, duration, status
    @Query("SELECT a FROM Ad a JOIN a.advertiser adv WHERE " +
            "(:adName IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :adName, '%'))) AND " +
            "(:campaignOwner IS NULL OR LOWER(adv.name) LIKE LOWER(CONCAT('%', :campaignOwner, '%'))) AND " +
            "(:duration IS NULL OR a.endDate = :duration) AND " +
            "(:status IS NULL OR a.status = :status)")
    List<Ad> searchAds(@Param("adName") String adName,
                       @Param("campaignOwner") String campaignOwner,
                       @Param("duration") String duration,
                       @Param("status") String status);
}
