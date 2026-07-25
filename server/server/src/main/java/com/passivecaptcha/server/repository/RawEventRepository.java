package com.passivecaptcha.server.repository;

import com.passivecaptcha.server.model.RawEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawEventRepository extends JpaRepository<RawEvent, Long> {

    /** Fetch all raw events for a session, ordered by timestamp ascending */
    List<RawEvent> findBySessionIdOrderByTimestampAsc(Long sessionId);
}
