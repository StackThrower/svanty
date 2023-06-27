package com.svanty.advice;

import com.svanty.model.AdminSettingsItemModel;
import com.svanty.service.AdminSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AdminSettingsControllerAdvice {

    private AdminSettingsService adminSettingsService;

    @Autowired
    AdminSettingsControllerAdvice(AdminSettingsService adminSettingsService) {
        this.adminSettingsService = adminSettingsService;
    }

    @ModelAttribute("settings")
    public AdminSettingsItemModel getSettings() {
            return adminSettingsService.getSettings();
    }
}
