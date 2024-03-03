package com.project.shopapp.controllers;

import com.project.shopapp.dtos.comment.CommentDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.comment.CommentResponse;
import com.project.shopapp.services.comment.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService commentService;

    @GetMapping("")
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam("product_id") Long productId
    ) {
        List<CommentResponse> comments;
        if (userId == null) {
            comments = commentService.getCommentsByProduct(productId);
        } else {
            comments = commentService.getCommentsByUserAndProduct(userId, productId);
        }
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentDTO commentDTO
    ) {
        try {
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!Objects.equals(loginUser.getId(), commentDTO.getUserId())) {
                return ResponseEntity.badRequest().body("You cannot comment as another user");
            }
            commentService.updateComment(commentId, commentDTO);
            return ResponseEntity.ok("Comment updated successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> insertComment(
            @Valid @RequestBody CommentDTO comment) {
        try {
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loginUser.getId() != comment.getUserId()) {
                return ResponseEntity.badRequest().body("You cannot comment as another user");
            }
            commentService.insertComment(comment);
            return ResponseEntity.ok("Comment inserted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
