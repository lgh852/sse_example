package com.ghl.sse.repository;

import com.ghl.sse.entity.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chats, Long> {

    Chats findByRoomId(String roomId);
}