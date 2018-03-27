package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Subseenit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubseenitRepository extends JpaRepository<Subseenit, String> {
    Subseenit findSubseenitByName(String name);
}