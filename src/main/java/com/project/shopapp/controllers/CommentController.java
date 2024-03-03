package com.project.shopapp.controllers;

import com.project.shopapp.dtos.comment.CommentDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.ResponseObject;
import com.project.shopapp.responses.comment.CommentResponse;
import com.project.shopapp.services.comment.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseObject> updateComment(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentDTO commentDTO
    ) throws DataNotFoundException {
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!Objects.equals(loginUser.getId(), commentDTO.getUserId())) {
                return ResponseEntity.badRequest().body(
                        ResponseObject.builder()
                                .message("You cannot update another user's comment")
                                .status(HttpStatus.BAD_REQUEST)
                                .data(null)
                                .build()
                );
            }
            commentService.updateComment(commentId, commentDTO);
            return ResponseEntity.ok(
                    new ResponseObject(
                           "Comment updated successfully",
                            HttpStatus.OK,
                            null
                    )
            );

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
