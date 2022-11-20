package com.daangn.clone.town.repository;

import com.daangn.clone.town.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Pattern;
import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

    boolean existsByName(String name);

    @Query("select t.id from Town t where t.name=:name")
    Long findByName(@Param("name")String name);
}
