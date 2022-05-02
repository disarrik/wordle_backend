package io.github.samkelsey.wordzle.repository;

import io.github.samkelsey.wordzle.entity.UserData;
import io.github.samkelsey.wordzle.entity.UserDataPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, UserDataPK> {

}
