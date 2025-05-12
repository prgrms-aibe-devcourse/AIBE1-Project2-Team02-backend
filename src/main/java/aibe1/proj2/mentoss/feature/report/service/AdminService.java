package aibe1.proj2.mentoss.feature.report.service;

import aibe1.proj2.mentoss.feature.report.model.dto.request.RecoverRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.request.ReportProcessRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.request.SoftDeleteRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.request.StatusUpdateRequestDto;
import aibe1.proj2.mentoss.feature.report.model.dto.response.*;

import java.util.List;

public interface AdminService {
    List<ReportResponseDto> getReportsNotProcessed();
    List<ReportDoneResponseDto> getReportsProcessed();

    UserDataResponseDto getUserDataById(Long id);
    LectureDataResponseDto getLectureDataById(Long id);
    MentorDataResponseDto getMentorDataById(Long id);
    MessageDataResponseDto getMessageDataById(Long id);
    ApplicationDataResponseDto getApplicationDataById(Long id);
    ReviewDataResponseDto getReviewDataById(Long id);


    void updateStatus(StatusUpdateRequestDto request);

    void softDelete(SoftDeleteRequestDto request);

    void processReport(Long reportId);

    void recover(RecoverRequestDto req);

    Long adminAction(ReportProcessRequestDto req, Long adminId);

    void reportActionRelation(Long reportId, Long actionId);
}
