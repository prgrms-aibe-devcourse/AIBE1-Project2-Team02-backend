package aibe1.proj2.mentoss.feature.lecture.model.mapper;

import aibe1.proj2.mentoss.feature.lecture.model.dto.request.LectureSearchRequest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class LectureSqlProvider {

    /**
     * 강의 목록 조회를 위한 동적 SQL 생성
     */
    public String findLectures(@Param("searchRequest") LectureSearchRequest searchRequest,
                               @Param("pageSize") int pageSize,
                               @Param("offset") int offset) {
        SQL sql = new SQL();
        sql.SELECT("l.lecture_id AS lectureId");
        sql.SELECT("l.lecture_title AS lectureTitle");
        sql.SELECT("l.price");
        sql.SELECT("u.nickname AS mentorNickname");
        sql.SELECT("u.profile_image AS profileImage");
        sql.SELECT("COALESCE(AVG(r.rating), 0) AS averageRating");
        sql.SELECT("COUNT(DISTINCT r.review_id) AS reviewCount");
        sql.SELECT("mp.is_certified AS isCertified");
        sql.SELECT("lc.parent_category AS parentCategory");
        sql.SELECT("lc.middle_category AS middleCategory");
        sql.SELECT("lc.subcategory AS subcategory");
        sql.SELECT("l.created_at AS createdAt");

        sql.FROM("lecture l");
        sql.JOIN("mentor_profile mp ON l.mentor_id = mp.mentor_id");
        sql.JOIN("app_user u ON mp.user_id = u.user_id");
        sql.JOIN("lecture_category lc ON l.category_id = lc.category_id");
        sql.LEFT_OUTER_JOIN("review r ON l.lecture_id = r.lecture_id AND r.is_deleted = FALSE AND r.status = 'AVAILABLE'");

        // 지역 관련 JOIN을 한 번만 추가
        if (searchRequest.regions() != null && !searchRequest.regions().isEmpty()) {
            sql.JOIN("lecture_region lr ON l.lecture_id = lr.lecture_id");
            sql.JOIN("region reg ON lr.region_code = reg.region_code");
        }

        sql.WHERE("l.is_deleted = FALSE");
        sql.WHERE("l.status = 'AVAILABLE'");

        if (searchRequest.keyword() != null) {
            sql.WHERE("(l.lecture_title LIKE CONCAT('%', #{searchRequest.keyword}, '%') " +
                    "OR l.description LIKE CONCAT('%', #{searchRequest.keyword}, '%') " +
                    "OR u.nickname LIKE CONCAT('%', #{searchRequest.keyword}, '%'))");
        }

        if (searchRequest.category() != null) {
            sql.WHERE("(lc.parent_category = #{searchRequest.category} " +
                    "OR lc.middle_category = #{searchRequest.category} " +
                    "OR lc.subcategory = #{searchRequest.category})");
        }

        if (searchRequest.minPrice() != null) {
            sql.WHERE("l.price >= #{searchRequest.minPrice}");
        }

        if (searchRequest.maxPrice() != null) {
            sql.WHERE("l.price <= #{searchRequest.maxPrice}");
        }

        // 지역 검색 조건만 한 번 추가
        if (searchRequest.regions() != null && !searchRequest.regions().isEmpty()) {
            StringBuilder regionsCondition = new StringBuilder();
            regionsCondition.append("(");

            for (int i = 0; i < searchRequest.regions().size(); i++) {
                if (i > 0) {
                    regionsCondition.append(" OR ");
                }

                regionsCondition.append("(reg.sido = #{searchRequest.regions[").append(i).append("]} ");
                regionsCondition.append("OR reg.sigungu = #{searchRequest.regions[").append(i).append("]} ");
                regionsCondition.append("OR reg.dong = #{searchRequest.regions[").append(i).append("]} ");
                regionsCondition.append("OR CONCAT(reg.sido, ' ', reg.sigungu) = #{searchRequest.regions[").append(i).append("]} ");
                regionsCondition.append("OR CONCAT(reg.sido, ' ', reg.sigungu, ' ', IFNULL(reg.dong, '')) = #{searchRequest.regions[").append(i).append("]})");
            }

            regionsCondition.append(")");
            sql.WHERE(regionsCondition.toString());
        }

        if (searchRequest.isCertified() != null) {
            sql.WHERE("mp.is_certified = #{searchRequest.isCertified}");
        }

        if (searchRequest.isOpen() != null) {
            sql.WHERE("l.is_closed = #{searchRequest.isOpen}");
        }

        sql.GROUP_BY("l.lecture_id");

        if (searchRequest.minRating() != null) {
            sql.HAVING("COALESCE(AVG(r.rating), 0) >= #{searchRequest.minRating}");
        }

        sql.ORDER_BY("l.created_at DESC");

        return sql.toString() + " LIMIT #{pageSize} OFFSET #{offset}";
    }

    /**
     * 강의 목록 총 개수 조회를 위한 동적 SQL 생성
     */
    public String countLectures(@Param("searchRequest") LectureSearchRequest searchRequest) {
        // 서브쿼리를 문자열로 직접 구성
        StringBuilder subQuery = new StringBuilder();
        subQuery.append("SELECT l.lecture_id ");
        subQuery.append("FROM lecture l ");
        subQuery.append("JOIN mentor_profile mp ON l.mentor_id = mp.mentor_id ");
        subQuery.append("JOIN app_user u ON mp.user_id = u.user_id ");
        subQuery.append("JOIN lecture_category lc ON l.category_id = lc.category_id ");
        subQuery.append("LEFT JOIN review r ON l.lecture_id = r.lecture_id AND r.is_deleted = FALSE AND r.status = 'AVAILABLE' ");

        // 지역 관련 JOIN을 한 번만 추가
        if (searchRequest.regions() != null && !searchRequest.regions().isEmpty()) {
            subQuery.append("JOIN lecture_region lr ON l.lecture_id = lr.lecture_id ");
            subQuery.append("JOIN region reg ON lr.region_code = reg.region_code ");
        }

        subQuery.append("WHERE l.is_deleted = FALSE AND l.status = 'AVAILABLE' ");

        if (searchRequest.keyword() != null) {
            subQuery.append("AND (l.lecture_title LIKE CONCAT('%', #{searchRequest.keyword}, '%') ");
            subQuery.append("OR l.description LIKE CONCAT('%', #{searchRequest.keyword}, '%') ");
            subQuery.append("OR u.nickname LIKE CONCAT('%', #{searchRequest.keyword}, '%')) ");
        }

        if (searchRequest.category() != null) {
            subQuery.append("AND (lc.parent_category = #{searchRequest.category} ");
            subQuery.append("OR lc.middle_category = #{searchRequest.category} ");
            subQuery.append("OR lc.subcategory = #{searchRequest.category}) ");
        }

        if (searchRequest.minPrice() != null) {
            subQuery.append("AND l.price >= #{searchRequest.minPrice} ");
        }

        if (searchRequest.maxPrice() != null) {
            subQuery.append("AND l.price <= #{searchRequest.maxPrice} ");
        }

        // 지역 조건도 한 번만 추가
        if (searchRequest.regions() != null && !searchRequest.regions().isEmpty()) {
            subQuery.append("AND (");
            for (int i = 0; i < searchRequest.regions().size(); i++) {
                if (i > 0) {
                    subQuery.append(" OR ");
                }

                subQuery.append("(reg.sido = #{searchRequest.regions[").append(i).append("]} ");
                subQuery.append("OR reg.sigungu = #{searchRequest.regions[").append(i).append("]} ");
                subQuery.append("OR reg.dong = #{searchRequest.regions[").append(i).append("]} ");
                subQuery.append("OR CONCAT(reg.sido, ' ', reg.sigungu) = #{searchRequest.regions[").append(i).append("]} ");
                subQuery.append("OR CONCAT(reg.sido, ' ', reg.sigungu, ' ', IFNULL(reg.dong, '')) = #{searchRequest.regions[").append(i).append("]})");
            }
            subQuery.append(") ");
        }

        if (searchRequest.isCertified() != null) {
            subQuery.append("AND mp.is_certified = #{searchRequest.isCertified} ");
        }

        if (searchRequest.isOpen() != null) {
            subQuery.append("AND l.is_closed = #{searchRequest.isOpen} ");
        }

        subQuery.append("GROUP BY l.lecture_id ");

        if (searchRequest.minRating() != null) {
            subQuery.append("HAVING COALESCE(AVG(r.rating), 0) >= #{searchRequest.minRating} ");
        }

        // 최종 쿼리 구성
        return "SELECT COUNT(*) FROM (" + subQuery.toString() + ") AS count_table";
    }
}