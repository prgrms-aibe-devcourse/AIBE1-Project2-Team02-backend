package aibe1.proj2.mentoss.feature.report.service;

import aibe1.proj2.mentoss.feature.report.model.dto.*;
import aibe1.proj2.mentoss.feature.report.model.mapper.AdminMapper;
import aibe1.proj2.mentoss.feature.report.model.mapper.ReportMapper;
import aibe1.proj2.mentoss.feature.review.model.mapper.ReviewMapper;
import aibe1.proj2.mentoss.global.entity.AdminAction;
import aibe1.proj2.mentoss.global.entity.Report;
import aibe1.proj2.mentoss.global.entity.enums.ActionType;
import aibe1.proj2.mentoss.global.entity.enums.TargetType;
import aibe1.proj2.mentoss.global.exception.DatabaseException;
import aibe1.proj2.mentoss.global.exception.ResourceNotFoundException;
import aibe1.proj2.mentoss.global.exception.report.InvalidActionTypeException;
import aibe1.proj2.mentoss.global.exception.report.InvalidSuspendPeriodException;
import aibe1.proj2.mentoss.global.exception.report.InvalidTargetTypeException;
import aibe1.proj2.mentoss.global.exception.report.ReportListException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;
    private final ReportMapper reportMapper;

    @Override
    public List<ReportResponseDto> getReportsNotProcessed() {
        try {
            return adminMapper.findReportsNotProcessed();
        }
        catch(Exception e) {
            throw new ReportListException(e);
        }
    }

    @Override
    public List<ReportDoneResponseDto> getReportsProcessed() {
        try {
            return adminMapper.findReportsProcessed();
        }
        catch(Exception e) {
            throw new ReportListException(e);
        }
    }

    @Override
    public void updateStatus(StatusUpdateRequestDto req) {
        String type = req.targetType();
        Long id = req.targetId();
        String status = req.status();
        switch (type) {
            case "USER":
                if (reportMapper.countUserById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateUserStatus(id, status);
                break;
            case "LECTURE":
                if (reportMapper.countLectureById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateLectureStatus(id, status);
                break;
            case "REVIEW":
                if (reportMapper.countLectureById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateReviewStatus(id, status);
                break;
            default:
                throw new InvalidTargetTypeException();
        }

    }

    @Override
    public void softDelete(SoftDeleteRequestDto req) {
        String type = req.targetType();
        Long id = req.targetId();
        switch (type) {
            case "USER":
                if (reportMapper.countUserById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateUserSoftDelete(id, true);
                break;
            case "LECTURE":
                if (reportMapper.countLectureById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateLectureSoftDelete(id, true);
                break;
            case "REVIEW":
                if (reportMapper.countLectureById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateReviewSoftDelete(id, true);
                break;
            default:
                throw new InvalidTargetTypeException();
        }

    }

    @Override
    public void processReport(Long reportId) {
        try {
            adminMapper.markReportProcessed(reportId);
        }
        catch(Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    public void recover(RecoverRequestDto req) {
        String type = req.targetType();
        Long id = req.targetId();
        switch (type) {
            case "USER":
                if (reportMapper.countUserById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateUserSoftDelete(id, false);
                break;
            case "LECTURE":
                if (reportMapper.countLectureById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateLectureSoftDelete(id, false);
                break;
            case "REVIEW":
                if (reportMapper.countReviewById(req.targetId()) == 0) {
                    throw new ResourceNotFoundException(type, req.targetId());
                }
                adminMapper.updateReviewSoftDelete(id, false);
                break;
            default:
                throw new InvalidTargetTypeException();
        }
    }

    @Override
    public Long adminAction(ReportProcessRequestDto req, Long adminId) {
        if (!ActionType.contains(req.actionType())) {
            throw new InvalidActionTypeException();
        }
        if (req.actionType().equals("FREE") || req.actionType().equals("WARN")) {
            if (req.suspendPeriod() > 0){
                throw new InvalidSuspendPeriodException();
            }
        }
        try {
            ReportTargetDto target = adminMapper.findReportTarget(req.reportId());
            AdminAction action = AdminAction.builder()
                    .adminId(adminId)
                    .targetType(target.targetType())
                    .targetId(target.targetId())
                    .actionType(req.actionType())
                    .reason(req.reason())
                    .suspensionPeriodDays(req.suspendPeriod())
                    .build();

            adminMapper.insertAdminAction(action);

            return action.getActionId();
        }
        catch(Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    public void reportActionRelation(Long reportId, Long actionId) {
        try {
            adminMapper.insertReportAction(reportId, actionId);
        }
        catch(Exception e) {
            throw new DatabaseException();
        }
    }
}
