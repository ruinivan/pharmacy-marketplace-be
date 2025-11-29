package pharmacymarketplace.review.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pharmacymarketplace.exceptions.ResourceNotFoundException;
import pharmacymarketplace.review.ReviewMapper;
import pharmacymarketplace.review.domain.mongo.ReviewDocument;
import pharmacymarketplace.review.dtos.CreateReviewRequest;
import pharmacymarketplace.review.dtos.ReviewDto;
import pharmacymarketplace.review.repository.mongo.ReviewRepository;
import pharmacymarketplace.user.domain.jpa.User;
import pharmacymarketplace.user.repository.jpa.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewDto> findByReviewable(ReviewDocument.ReviewableType type, Long reviewableId) {
        List<ReviewDocument> reviews = reviewRepository.findByReviewableTypeAndReviewableId(type, reviewableId);
        return reviews.stream()
                .map(review -> {
                    User reviewer = userRepository.findById(review.reviewerId())
                            .orElse(null);
                    String reviewerName = reviewer != null && reviewer.getCustomer() != null 
                            ? reviewer.getCustomer().getFullName() 
                            : "Usuário Anônimo";
                    return reviewMapper.toDtoWithReviewerName(review, reviewerName);
                })
                .toList();
    }

    public ReviewDto createReview(CreateReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        ReviewDocument review = new ReviewDocument(
                null,
                user.getId(),
                request.rating(),
                request.comment(),
                request.reviewableType(),
                request.reviewableId(),
                Instant.now()
        );

        ReviewDocument saved = reviewRepository.save(review);
        String reviewerName = user.getCustomer() != null 
                ? user.getCustomer().getFullName() 
                : "Usuário Anônimo";
        return reviewMapper.toDtoWithReviewerName(saved, reviewerName);
    }

    public void deleteReview(String id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação não encontrada");
        }
        reviewRepository.deleteById(id);
    }
}

