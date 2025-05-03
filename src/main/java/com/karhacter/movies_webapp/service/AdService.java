package com.karhacter.movies_webapp.service;

import java.util.List;
import com.karhacter.movies_webapp.dto.AdDTO;
import com.karhacter.movies_webapp.entity.AdType;
import com.karhacter.movies_webapp.entity.AdStatus;

public interface AdService {
    // Create new ad
    AdDTO createAd(AdDTO adDTO);

    // Get ad by ID
    AdDTO getAdById(Long id);

    // Update ad
    AdDTO updateAd(Long id, AdDTO adDTO);

    // Delete ad
    void deleteAd(Long id);

    // Get all active ads
    List<AdDTO> getAllActiveAds();

    // Get ads by type
    List<AdDTO> getAdsByType(AdType type);

    // Get ads by advertiser
    List<AdDTO> getAdsByAdvertiser(Long advertiserId);

    // Update ad status
    AdDTO updateAdStatus(Long id, AdStatus status);

    // Track ad impression
    void trackImpression(Long adId);

    // Track ad click
    void trackClick(Long adId);

    // Get ad statistics
    AdDTO getAdStatistics(Long id);
}