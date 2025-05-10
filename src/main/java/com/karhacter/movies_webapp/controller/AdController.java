package com.karhacter.movies_webapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.karhacter.movies_webapp.dto.AdDTO;
import com.karhacter.movies_webapp.service.AdService;
import com.karhacter.movies_webapp.entity.AdType;
import com.karhacter.movies_webapp.entity.AdStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    @Autowired
    private AdService adService;

    @PostMapping("/create")
    public ResponseEntity<AdDTO> createAd(@Valid @RequestBody AdDTO adDTO) {
        AdDTO createdAd = adService.createAd(adDTO);
        return new ResponseEntity<>(createdAd, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdDTO> getAdById(@PathVariable Long id) {
        AdDTO adDTO = adService.getAdById(id);
        return new ResponseEntity<>(adDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdDTO> updateAd(
            @PathVariable Long id,
            @Valid @RequestBody AdDTO adDTO) {
        AdDTO updatedAd = adService.updateAd(id, adDTO);
        return new ResponseEntity<>(updatedAd, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AdDTO>> getAllActiveAds() {
        List<AdDTO> activeAds = adService.getAllActiveAds();
        return new ResponseEntity<>(activeAds, HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AdDTO>> getAdsByType(@PathVariable AdType type) {
        List<AdDTO> ads = adService.getAdsByType(type);
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @GetMapping("/advertiser/{advertiserId}")
    public ResponseEntity<List<AdDTO>> getAdsByAdvertiser(@PathVariable Long advertiserId) {
        List<AdDTO> ads = adService.getAdsByAdvertiser(advertiserId);
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdDTO> updateAdStatus(
            @PathVariable Long id,
            @RequestParam AdStatus status) {
        AdDTO updatedAd = adService.updateAdStatus(id, status);
        return new ResponseEntity<>(updatedAd, HttpStatus.OK);
    }

    @PostMapping("/{id}/impression")
    public ResponseEntity<Void> trackImpression(@PathVariable Long id) {
        adService.trackImpression(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/click")
    public ResponseEntity<Void> trackClick(@PathVariable Long id) {
        adService.trackClick(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<AdDTO> getAdStatistics(@PathVariable Long id) {
        AdDTO stats = adService.getAdStatistics(id);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AdDTO>> searchAds(
            @RequestParam(required = false) String adName,
            @RequestParam(required = false) String campaignOwner,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) String status) {

        List<AdDTO> ads = adService.searchAds(adName, campaignOwner, duration, status);
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }
}
