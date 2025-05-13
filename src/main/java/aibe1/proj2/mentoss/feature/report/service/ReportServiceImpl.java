package aibe1.proj2.mentoss.feature.report.service;


import aibe1.proj2.mentoss.feature.report.model.dto.request.CreateReportRequestDto;
import aibe1.proj2.mentoss.feature.report.model.mapper.ReportMapper;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import aibe1.proj2.mentoss.global.entity.Report;
import aibe1.proj2.mentoss.global.entity.enums.ReportReasonType;
import aibe1.proj2.mentoss.global.entity.enums.TargetType;
import aibe1.proj2.mentoss.global.exception.ResourceAccessDeniedException;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import aibe1.proj2.mentoss.global.exception.report.DuplicateReportException;
import aibe1.proj2.mentoss.global.exception.report.InvalidReasonTypeException;
import aibe1.proj2.mentoss.global.exception.report.InvalidTargetTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public void createReport(CreateReportRequestDto req, Long reporterId) {
        String type = req.targetType();
        int count = reportMapper.countByReporterAndTarget(
                reporterId, req.targetType(), req.targetId()
        );
        if (!TargetType.contains(type)) {
            throw new InvalidTargetTypeException();
        }
        switch (type) {
            case "USER":
                if (reportMapper.countUserById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }

                if (!reviewMapper.isUserAccessible(req.targetId())) {
                    throw new ResourceAccessDeniedException(type, req.targetId());
                }
                break;
            case "LECTURE":
                if (reportMapper.countLectureById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                if (!reviewMapper.isLectureAccessible(req.targetId())) {
                    throw new ResourceAccessDeniedException(type, req.targetId());
                }
                break;
            case "REVIEW":
                if (reportMapper.countReviewById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                if (!reviewMapper.isReviewAccessible(req.targetId())) {
                    throw new ResourceAccessDeniedException(type, req.targetId());
                }
                break;
        }
        if (!ReportReasonType.contains(req.reasonType())) {
            throw new InvalidReasonTypeException();
        }
        if (count > 0) {
            throw new DuplicateReportException();
        }
        Report report = Report.builder()
                .reporterId(reporterId)
                .targetType(type)
                .targetId(req.targetId())
                .reason(req.reason())
                .reasonType(req.reasonType())
                .isProcessed(false)
                .build();
        reportMapper.insertReport(report);
        Long id = req.targetId();
        switch (type) {
            case "USER" -> {
                reportMapper.incrementUserReportCount(id);
                if (reportMapper.getUserReportCount(id) >= 30) {
                    reportMapper.suspendUser(id);
                }
            }
            case "LECTURE" -> {
                reportMapper.incrementLectureReportCount(id);
                if (reportMapper.getLectureReportCount(id) >= 30) {
                    reportMapper.suspendLecture(id);
                }
            }
            case "REVIEW" -> {
                reportMapper.incrementReviewReportCount(id);
                if (reportMapper.getReviewReportCount(id) >= 30) {
                    reportMapper.suspendReview(id);
                }
            }
        }
    }

}
