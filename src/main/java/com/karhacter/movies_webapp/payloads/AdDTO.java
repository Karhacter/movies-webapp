package com.karhacter.movies_webapp.payloads;

import java.util.Date;
import com.karhacter.movies_webapp.entity.AdType;
import com.karhacter.movies_webapp.entity.AdStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdDTO {
    private long id;
    private String title;
    private String content;
    private String imageUrl;
    private String targetUrl;
    private AdType type;
    private AdStatus status;
    private double budget;
    private double spent;
    private int impressions;
    private int clicks;
    private Date startDate;
    private Date endDate;
    private long advertiserId;
    private String advertiserName;
}