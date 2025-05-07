package aibe1.proj2.mentoss.feature.message.model.mapper;

import java.util.Map;

public class MessageSqlProvider {

    public String findSentMessagesSql(Map<String, Object> params) {
        return buildBaseQuery(params, true); // true = sent
    }

    public String findReceivedMessagesSql(Map<String, Object> params) {
        return buildBaseQuery(params, false); // false = received
    }

    private String buildBaseQuery(Map<String, Object> params, boolean isSent) {
        StringBuilder sql = new StringBuilder();

        // ✅ SELECT 절
        sql.append("SELECT m.*, u.nickname FROM message m ");

        // ✅ JOIN 대상: 받은 쪽지면 sender, 보낸 쪽지면 receiver 닉네임 확인
        if (isSent) {
            sql.append("JOIN app_user u ON m.receiver_id = u.user_id ");
        } else {
            sql.append("JOIN app_user u ON m.sender_id = u.user_id ");
        }

        // ✅ WHERE 조건
        String userCondition = isSent ? "m.sender_id = #{userId}" : "m.receiver_id = #{userId}";
        sql.append("WHERE ").append(userCondition).append(" AND m.is_deleted = 0 ");

        // ✅ 검색 조건
        String filterBy = (String) params.get("filterBy");
        String keyword = (String) params.get("keyword");

        if (filterBy != null && keyword != null && !keyword.isBlank()) {
            if (filterBy.equals("nickname")) {
                sql.append("AND u.nickname LIKE CONCAT('%', #{keyword}, '%') ");
            } else if (filterBy.equals("content")) {
                sql.append("AND m.content LIKE CONCAT('%', #{keyword}, '%') ");
            }
        }

        // ✅ 정렬 및 페이징
        sql.append("ORDER BY m.message_id DESC ")
                .append("LIMIT #{limit} OFFSET #{offset}");

        return sql.toString();
    }

    public String countSentMessagesSql(Map<String, Object> params) {
        return buildCountQuery(params, true); // true = sent
    }

    public String countReceivedMessagesSql(Map<String, Object> params) {
        return buildCountQuery(params, false); // false = received
    }

    private String buildCountQuery(Map<String, Object> params, boolean isSent) {
        StringBuilder sql = new StringBuilder();

        // ✅ count만 반환
        sql.append("SELECT COUNT(*) FROM message m ");

        if (isSent) {
            sql.append("JOIN app_user u ON m.receiver_id = u.user_id ");
        } else {
            sql.append("JOIN app_user u ON m.sender_id = u.user_id ");
        }

        String userCondition = isSent ? "m.sender_id = #{userId}" : "m.receiver_id = #{userId}";
        sql.append("WHERE ").append(userCondition).append(" AND m.is_deleted = 0 ");

        String filterBy = (String) params.get("filterBy");
        String keyword = (String) params.get("keyword");

        if (filterBy != null && keyword != null && !keyword.isBlank()) {
            if (filterBy.equals("nickname")) {
                sql.append("AND u.nickname LIKE CONCAT('%', #{keyword}, '%') ");
            } else if (filterBy.equals("content")) {
                sql.append("AND m.content LIKE CONCAT('%', #{keyword}, '%') ");
            }
        }

        return sql.toString();
    }
}
