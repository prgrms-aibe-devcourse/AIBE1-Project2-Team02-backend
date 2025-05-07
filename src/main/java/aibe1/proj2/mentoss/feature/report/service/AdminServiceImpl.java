package aibe1.proj2.mentoss.feature.report.service;

import aibe1.proj2.mentoss.feature.report.model.dto.*;
import aibe1.proj2.mentoss.feature.report.model.mapper.AdminMapper;
import aibe1.proj2.mentoss.global.entity.AdminAction;
import aibe1.proj2.mentoss.global.entity.Report;
import aibe1.proj2.mentoss.global.exception.report.InvalidTargetTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;


    @Override
    public List<ReportResponseDto> getReportsNotProcessed() {

        return adminMapper.findReportsNotProcessed();
    }

    @Override
    public List<ReportDoneResponseDto> getReportsProcessed() {

        return adminMapper.findReportsProcessed();
    }

    @Override
    public void updateStatus(StatusUpdateRequestDto request) {
        String type = request.targetType();
        Long id = request.targetId();
        String status = request.status();
        switch (type) {
            case "USER":
                adminMapper.updateUserStatus(id, status);
                break;
            case "LECTURE":
                adminMapper.updateLectureStatus(id, status);
                break;
            case "REVIEW":
                adminMapper.updateReviewStatus(id, status);
                break;
            default:
                throw new InvalidTargetTypeException();
        }

    }

    @Override
    public void softDelete(SoftDeleteRequestDto request) {
        String type = request.targetType();
        Long id = request.targetId();
        switch (type) {
            case "USER":
                adminMapper.updateUserSoftDelete(id, true);
                break;
            case "LECTURE":
                adminMapper.updateLectureSoftDelete(id, true);
                break;
            case "REVIEW":
                adminMapper.updateReviewSoftDelete(id, true);
                break;
            default:
                throw new InvalidTargetTypeException();
        }

    }

    @Override
    public void processReport(Long reportId) {
        adminMapper.markReportProcessed(reportId);
    }

    @Override
    public void recover(RecoverRequestDto req) {
        String type = req.targetType();
        Long id = req.targetId();
        switch (type) {
            case "USER":
                adminMapper.updateUserSoftDelete(id, false);
                break;
            case "LECTURE":
                adminMapper.updateLectureSoftDelete(id, false);
                break;
            case "REVIEW":
                adminMapper.updateReviewSoftDelete(id, false);
                break;
            default:
                throw new InvalidTargetTypeException();
        }
    }

    @Override
    public Long adminAction(ReportProcessRequestDto req) {
        ReportTargetDto target = adminMapper.findReportTarget(req.reportId());

        AdminAction action = AdminAction.builder()
                .adminId(req.adminId())
                .targetType(target.targetType())
                .targetId(target.targetId())
                .actionType(req.actionType())
                .reason(req.reason())
                .suspensionPeriodDays(req.suspendPeriod())
                .build();

        adminMapper.insertAdminAction(action);

        return action.getActionId();
    }

    @Override
    public void reportActionRelation(Long reportId, Long actionId) {
        adminMapper.insertReportAction(reportId, actionId);
    }
}
