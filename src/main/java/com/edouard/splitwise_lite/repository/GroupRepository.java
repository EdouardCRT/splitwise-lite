package com.edouard.splitwise_lite.repository;

import com.edouard.splitwise_lite.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMembersId(Long userId);
}