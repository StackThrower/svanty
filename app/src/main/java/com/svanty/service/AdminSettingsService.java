package com.svanty.service;

import com.svanty.db.AdminSettingsRepository;
import com.svanty.model.AdminSettingsItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminSettingsService {

    private AdminSettingsRepository adminSettingsRepository;

    @Autowired
    AdminSettingsService(AdminSettingsRepository adminSettingsRepository) {
        this.adminSettingsRepository = adminSettingsRepository;
    }


    public AdminSettingsItemModel getSettings() {
         return adminSettingsRepository.findAll().stream()
                 .map(e -> new AdminSettingsItemModel(
                         e.getId(),
                         e.getGoogleAnalytics(),
                         e.getGoogleAdsense()))
                 .findFirst().orElse(new AdminSettingsItemModel());
    }
}
