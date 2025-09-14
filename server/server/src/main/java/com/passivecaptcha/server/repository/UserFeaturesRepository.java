package com.passivecaptcha.server.repository;

import com.passivecaptcha.server.model.UserFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeaturesRepository extends JpaRepository<UserFeatures, Long> {
}
