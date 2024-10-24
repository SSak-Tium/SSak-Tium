package com.sparta.ssaktium.domain.friends.repository;


import com.sparta.ssaktium.domain.friends.entity.FriendStatus;
import com.sparta.ssaktium.domain.friends.entity.Friend;
import com.sparta.ssaktium.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f WHERE f.userId.id = :userId AND f.friendUserId.id = :friendUserId")
    Optional<Friend> findByUserIdAndFriendUserId(Long userId, Long friendUserId);


    @Query("SELECT f FROM Friend f WHERE (f.userId.id = :userId OR f.friendUserId.id = :friendId) AND f.friendStatus = :status")
    Page<Friend> findByUserIdOrFriendIdAndStatus(@Param("userId") Long userId,
                                                 @Param("friendId") Long friendId,
                                                 @Param("status") FriendStatus status,
                                                 Pageable pageable);

    @Query("SELECT f FROM Friend f WHERE (f.userId.id = :userId AND f.friendUserId.id = :friendId) OR (f.userId.id = :friendId AND f.friendUserId.id = :userId)")
    Optional<Friend> findByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

    @Query("SELECT f.friendUserId FROM Friend f WHERE f.userId.id = :userId AND f.friendStatus = :status")
    List<User> findFriendsByUserId(@Param("userId") Long userId, @Param("status") FriendStatus status);
}