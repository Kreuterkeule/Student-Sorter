package com.kreuterkeule.StudentSorter.repository;

import com.kreuterkeule.StudentSorter.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
