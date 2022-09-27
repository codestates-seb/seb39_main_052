package com.seb39.myfridge.heart.controller;

import com.seb39.myfridge.auth.annotation.AuthMemberId;
import com.seb39.myfridge.heart.dto.HeartResponse;
import com.seb39.myfridge.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/api/recipes/{recipeId}/heart")
    @Secured("ROLE_USER")
    public ResponseEntity<HeartResponse> postHeart(@PathVariable Long recipeId, @AuthMemberId Long memberId){
        heartService.addHeartToRecipe(memberId, recipeId);
        int heartCounts = heartService.findHeartCounts(recipeId);
        HeartResponse response = HeartResponse.of(recipeId, heartCounts);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/recipes/{recipeId}/heart")
    @Secured("ROLE_USER")
    public ResponseEntity<HeartResponse> deleteHeart(@PathVariable Long recipeId, @AuthMemberId Long memberId){
        heartService.removeHeartToRecipe(memberId,recipeId);
        int heartCounts = heartService.findHeartCounts(recipeId);
        HeartResponse response = HeartResponse.of(recipeId, heartCounts);
        return ResponseEntity.ok(response);
    }
}
