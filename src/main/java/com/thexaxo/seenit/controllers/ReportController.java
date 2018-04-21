package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Report;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.PostNotFoundException;
import com.thexaxo.seenit.exceptions.ReportNotFoundException;
import com.thexaxo.seenit.models.SubmitReportBindingModel;
import com.thexaxo.seenit.services.PostService;
import com.thexaxo.seenit.services.ReportService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class ReportController {
    private ReportService reportService;
    private PostService postService;
    private UserService userService;

    @Autowired
    public ReportController(ReportService reportService, PostService postService, UserService userService) {
        this.reportService = reportService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/report/{postId}")
    public ModelAndView report(ModelAndView modelAndView, @PathVariable String postId) {
        modelAndView.addObject("view", "report/report-post");
        modelAndView.addObject("postId", postId);
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @PostMapping("/report/{postId}")
    public ModelAndView reportConfirm(ModelAndView modelAndView, @ModelAttribute SubmitReportBindingModel bindingModel, @PathVariable String postId, Principal principal) {
        Post reportedPost = this.postService.findById(postId);
        User reportCreator = this.userService.getUserByUsername(principal.getName());

        if (reportedPost == null) {
            throw new PostNotFoundException();
        }

        this.reportService.submitReport(bindingModel, reportedPost, reportCreator);
        modelAndView.setViewName("redirect:/s/" + reportedPost.getSubseenit().getName() + "/comments/" + reportedPost.getId());

        return modelAndView;
    }

    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('GOD', 'ADMIN', 'MODERATOR')")
    public ModelAndView reports(ModelAndView modelAndView) {
        modelAndView.addObject("view", "report/reports-panel :: reports-panel");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @GetMapping("/reports/all")
    @PreAuthorize("hasAnyRole('GOD', 'ADMIN', 'MODERATOR')")
    public ModelAndView reports(ModelAndView modelAndView, @PageableDefault(10) Pageable pageable) {
        Page<Report> reports = this.reportService.getAllReportsByPage(pageable);

        modelAndView.addObject("reports", reports);
        modelAndView.addObject("totalPages", this.reportService.getAllReportsPagesCount(pageable.getPageSize()));
        modelAndView.setViewName("report/reports :: reports");

        return modelAndView;
    }

    @GetMapping("/report/reason/{id}")
    @PreAuthorize("hasAnyRole('GOD', 'ADMIN', 'MODERATOR')")
    public ModelAndView reportReason(ModelAndView modelAndView, @PathVariable String id) {
        Report report = this.reportService.getReportById(id);

        if (report == null) {
            throw new ReportNotFoundException();
        }

        modelAndView.addObject("view", "report/reason :: reason");
        modelAndView.addObject("reason", report.getReason());
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @GetMapping("/report/resolve/{id}")
    @PreAuthorize("hasAnyRole('GOD', 'ADMIN', 'MODERATOR')")
    public ResponseEntity reportReason(@PathVariable String id) {
        Report report = this.reportService.getReportById(id);

        if (report == null) {
            throw new ReportNotFoundException();
        }

        this.reportService.resolve(report);
        return new ResponseEntity(HttpStatus.OK);
    }
}