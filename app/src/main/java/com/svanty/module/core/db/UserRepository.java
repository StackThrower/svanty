package com.svanty.module.core.db;

import com.svanty.module.core.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    @Query( "from User where email = :email")
    List<User> findByAuth(@Param("email") String email);

    @Query("from User where username = :username")
    User getByUsername(@Param("username") String username);
}
