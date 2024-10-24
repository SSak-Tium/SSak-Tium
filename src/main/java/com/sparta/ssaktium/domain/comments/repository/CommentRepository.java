package com.sparta.ssaktium.domain.comments.repository;

import com.sparta.ssaktium.domain.comments.entity.Comment;
import org.hibernate.annotations.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);

    List<Comment> findAllByBoardId(Long id);

}
