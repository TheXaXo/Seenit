package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Report;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends PagingAndSortingRepository<Report, String> {
}