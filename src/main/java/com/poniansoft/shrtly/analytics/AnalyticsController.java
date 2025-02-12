package com.poniansoft.shrtly.analytics;

import com.poniansoft.shrtly.analytics.model.ClickAnalyticsDTO;
import com.poniansoft.shrtly.base.BaseController;
import com.poniansoft.shrtly.store.Store;
import com.poniansoft.shrtly.store.StoreService;
import com.poniansoft.shrtly.user.User;
import com.poniansoft.shrtly.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://shrtlnk.shop"})
@RequestMapping("/api/store/{storeId}/analytics")
public class AnalyticsController extends BaseController {
    private final AnalyticsService clickAnalyticsService;
    private final StoreService storeService;

    public AnalyticsController(AnalyticsService clickAnalyticsService, UserService userService, StoreService storeService) {
        super(userService);
        this.clickAnalyticsService = clickAnalyticsService;
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<ClickAnalyticsDTO> getAnalytics(@PathVariable Long storeId, HttpServletRequest request) {
        User currentUser = getCurrentUser(request);
        Store store = storeService.getStoreById(storeId);
        if (!store.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(clickAnalyticsService.getAnalytics(storeId));
    }
}
