package aibe1.proj2.mentoss.feature.report.service;


import aibe1.proj2.mentoss.feature.report.model.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;

}
