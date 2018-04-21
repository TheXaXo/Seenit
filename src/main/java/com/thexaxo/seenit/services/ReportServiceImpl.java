package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Report;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SubmitReportBindingModel;
import com.thexaxo.seenit.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportServiceImpl implements ReportService {
    private ReportRepository repository;

    @Autowired
    public ReportServiceImpl(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public void submitReport(SubmitReportBindingModel bindingModel, Post reportedPost, User reportCreator) {
        Report report = new Report();

        report.setPost(reportedPost);
        report.setCreator(reportCreator);
        report.setReason(bindingModel.getReason());
        report.setCreationDate(LocalDateTime.now());

        this.repository.save(report);
    }

    @Override
    public Page<Report> getAllReportsByPage(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public long getAllReportsPagesCount(int size) {
        return (long) Math.ceil((double) this.repository.count() / size);
    }

    @Override
    public Report getReportById(String id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public void resolve(Report report) {
        this.repository.delete(report);
    }
}