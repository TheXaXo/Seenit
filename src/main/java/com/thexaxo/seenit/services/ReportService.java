package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Report;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SubmitReportBindingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    void submitReport(SubmitReportBindingModel bindingModel, Post reportedPost, User reportCreator);

    Page<Report> getAllReportsByPage(Pageable pageable);

    long getAllReportsPagesCount(int size);

    Report getReportById(String id);

    void resolve(Report report);
}