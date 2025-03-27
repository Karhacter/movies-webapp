package com.karhacter.movies_webapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.karhacter.movies_webapp.entity.Ad;
import com.karhacter.movies_webapp.entity.AdStatus;
import com.karhacter.movies_webapp.entity.User;

@Repository
public interface AdRepo extends JpaRepository<Ad, Long> {
    List<Ad> findByStatus(AdStatus status);

    List<Ad> findByAdvertiser(User advertiser);

    List<Ad> findByStatusAndType(AdStatus status, String type);
}