package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.karhacter.movies_webapp.entity.Ad;
import com.karhacter.movies_webapp.entity.AdStatus;
import com.karhacter.movies_webapp.entity.AdType;
import com.karhacter.movies_webapp.entity.User;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.payloads.AdDTO;
import com.karhacter.movies_webapp.repository.AdRepo;
import com.karhacter.movies_webapp.repository.UserRepo;
import com.karhacter.movies_webapp.service.AdService;

@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdRepo adRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public AdDTO createAd(AdDTO adDTO) {
        User advertiser = userRepo.findById(adDTO.getAdvertiserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", adDTO.getAdvertiserId()));

        Ad ad = modelMapper.map(adDTO, Ad.class);
        ad.setAdvertiser(advertiser);
        ad.setStatus(AdStatus.PENDING);
        ad.setSpent(0.0);
        ad.setImpressions(0);
        ad.setClicks(0);

        Ad savedAd = adRepo.save(ad);
        return modelMapper.map(savedAd, AdDTO.class);
    }

    @Override
    public AdDTO getAdById(Long id) {
        Ad ad = adRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad", "id", id));
        return modelMapper.map(ad, AdDTO.class);
    }

    @Override
    @Transactional
    public AdDTO updateAd(Long id, AdDTO adDTO) {
        Ad ad = adRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad", "id", id));

        // Update fields
        ad.setTitle(adDTO.getTitle());
        ad.setContent(adDTO.getContent());
        ad.setImageUrl(adDTO.getImageUrl());
        ad.setTargetUrl(adDTO.getTargetUrl());
        ad.setType(adDTO.getType());
        ad.setBudget(adDTO.getBudget());
        ad.setEndDate(adDTO.getEndDate());

        Ad updatedAd = adRepo.save(ad);
        return modelMapper.map(updatedAd, AdDTO.class);
    }

    @Override
    @Transactional
    public void deleteAd(Long id) {
        Ad ad = adRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad", "id", id));
        adRepo.delete(ad);
    }

    @Override
    public List<AdDTO> getAllActiveAds() {
        List<Ad> activeAds = adRepo.findByStatus(AdStatus.ACTIVE);
        return activeAds.stream()
                .map(ad -> modelMapper.map(ad, AdDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdDTO> getAdsByType(AdType type) {
        List<Ad> ads = adRepo.findByStatusAndType(AdStatus.ACTIVE, type.name());
        return ads.stream()
                .map(ad -> modelMapper.map(ad, AdDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdDTO> getAdsByAdvertiser(Long advertiserId) {
        User advertiser = userRepo.findById(advertiserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", advertiserId));

        List<Ad> ads = adRepo.findByAdvertiser(advertiser);
        return ads.stream()
                .map(ad -> modelMapper.map(ad, AdDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdDTO updateAdStatus(Long id, AdStatus status) {
        Ad ad = adRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad", "id", id));

        ad.setStatus(status);
        Ad updatedAd = adRepo.save(ad);
        return modelMapper.map(updatedAd, AdDTO.class);
    }

    @Override
    @Transactional
    public void trackImpression(Long adId) {
        Ad ad = adRepo.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Ad", "id", adId));

        ad.setImpressions(ad.getImpressions() + 1);
        adRepo.save(ad);
    }

    @Override
    @Transactional
    public void trackClick(Long adId) {
        Ad ad = adRepo.findById(adId)
                .orElseThrow(() -> new ResourceNotFoundException("Ad", "id", adId));

        ad.setClicks(ad.getClicks() + 1);
        adRepo.save(ad);
    }

    @Override
    public AdDTO getAdStatistics(Long id) {
        Ad ad = adRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ad", "id", id));

        return modelMapper.map(ad, AdDTO.class);
    }
}