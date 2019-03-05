package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubseenitRepository extends JpaRepository<Subseenit, String> {
    Subseenit findSubseenitByName(String name);

    Page<Subseenit> findAllBySubscribersContains(@Param("subscriber") User user, Pageable pageable);
}