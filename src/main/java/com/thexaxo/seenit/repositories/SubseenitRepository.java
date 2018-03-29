package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Subseenit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubseenitRepository extends JpaRepository<Subseenit, String> {
    Subseenit findSubseenitByName(String name);
}