package io.github.samkelsey.wordzle.repository;

import io.github.samkelsey.wordzle.entity.UserData;
import io.github.samkelsey.wordzle.entity.UserDataPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends CrudRepository<UserData, UserDataPK> {

}
