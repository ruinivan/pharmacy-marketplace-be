package pharmacymarketplace.review.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacymarketplace.review.domain.mongo.ReviewDocument.ReviewableType;
import pharmacymarketplace.review.dtos.CreateReviewRequest;
import pharmacymarketplace.review.dtos.ReviewDto;
import pharmacymarketplace.review.services.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> getReviews(
            @RequestParam ReviewableType reviewableType,
            @RequestParam Long reviewableId
    ) {
        return ResponseEntity.ok(reviewService.findByReviewable(reviewableType, reviewableId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<ReviewDto> createReview(
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}

