package aibe1.proj2.mentoss.global.moderation.service;

import aibe1.proj2.mentoss.global.moderation.model.ModerationResult;

/**
 * 콘텐츠 유해성 검사를 위한 서비스 인터페이스입니다.
 * 해당 인터페이스를 구현하는 클래스는 주어진 텍스트가 부적절한 내용을 포함하고 있는지를 검사하는 기능을 제공합니다.
 */
public interface ContentModerationService {


    /**
     * 주어진 콘텐츠에 유해하거나 부적절한 내용이 포함되어 있는지 검사합니다.
     *
     * @param content 검사할 텍스트 콘텐츠
     * @return 유해 여부 및 차단 사유를 포함한 ModerationResult 객체
     */
    ModerationResult moderateContent(String content);
}