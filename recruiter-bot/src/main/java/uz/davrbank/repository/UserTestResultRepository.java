package uz.davrbank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.dto.DashboardDTO;
import uz.davrbank.entity.TestResult;
import uz.davrbank.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Email : user@example.com
 * Date :9/25/2024
 * Time :1:02 PM
 * Project name : recruiterbot
 */
@Repository
public interface UserTestResultRepository extends JpaRepository<TestResult, Long>, JpaSpecificationExecutor<TestResult> {
    //-------------------calculateMonthlyStatisticsForLastSixMonths---------------
    @Query("""
                SELECT 
                    EXTRACT(YEAR FROM tr.createdAt) AS year,
                    EXTRACT(MONTH FROM tr.createdAt) AS month,
                    SUM(CASE WHEN tr.totalPoints >= :points THEN 1 ELSE 0 END) AS passedCount,
                    SUM(CASE WHEN tr.totalPoints < :points THEN 1 ELSE 0 END) AS failedCount,
                    COUNT(DISTINCT tr.user.id) AS testTakersCount
                FROM TestResult tr
                WHERE tr.createdAt >= :startDateTime AND tr.createdAt <= :endDateTime
                GROUP BY EXTRACT(YEAR FROM tr.createdAt), EXTRACT(MONTH FROM tr.createdAt)
                ORDER BY EXTRACT(YEAR FROM tr.createdAt), EXTRACT(MONTH FROM tr.createdAt)
            """)
    List<Object[]> fetchStatisticsByDateRange(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("points") Integer points);
    //---------------------------------------------------------------------------------------------

    //--------------------------MY way-----------------------------------------
    @Query(value = """
            SELECT 
                (SELECT COUNT(DISTINCT tr.user_id) FROM Test_Result tr) AS total_distinct_users,
                (SELECT COUNT(DISTINCT tr.user_id) FROM Test_Result tr WHERE DATE(tr.created_at) = :date) AS daily_distinct_users,
                (SELECT COUNT(tr) FROM Test_Result tr WHERE DATE(tr.created_at) = :date AND tr.total_points >= :points) AS daily_passed_tests,
                (SELECT COUNT(tr) FROM Test_Result tr WHERE DATE(tr.created_at) = :date AND tr.total_points < :points) AS daily_failed_tests,
                (SELECT COUNT(tr) FROM Test_Result tr WHERE tr.total_points >= :points) AS total_passed_tests,
                (SELECT COUNT(tr) FROM Test_Result tr WHERE tr.total_points < :points) AS total_failed_tests,
                (SELECT COUNT(*) FROM department) AS department_count
            """, nativeQuery = true)
    DashboardDTO getUserDashboard(
            @Param("date") LocalDate date,
            @Param("points") Integer points
    );

    //-------------------------------------------
    @Query(value = """
            SELECT 
                u.id AS userId,
                u.name AS userName,
                u.phone AS userPhone,
                d.name AS departmentName,
                tr.created_at AS requestDate,
                tr.total_points AS totalScore,
                tr.total_questions AS totalQuestions,
                tr.percentage AS percentage,
                CASE WHEN tr.total_points >= 50 THEN 'Passed' ELSE 'Failed' END AS status,
                p.id AS fileId,  
                p.photo_name AS fileName, 
                (SELECT COUNT(*) FROM test_result tr2 WHERE tr2.user_id = u.id) AS totalTests
            FROM user_table u  
            LEFT JOIN department d ON u.department_id = d.id 
            LEFT JOIN test_result tr ON tr.user_id = u.id AND tr.id = (
                SELECT tr2.id FROM test_result tr2 WHERE tr2.user_id = u.id ORDER BY tr2.created_at DESC LIMIT 1
            )
            LEFT JOIN photo_upload p ON u.file_id = p.id  
            ORDER BY u.id DESC
            """,
            countQuery = "SELECT COUNT(*) FROM user_table u",
            nativeQuery = true)
    Page<Map<String, Object>> fetchUserTestDetailsWithPagination(Pageable pageable);

   /* @Query("SELECT COUNT(DISTINCT tr.user.id) FROM TestResult tr")
    Long countDistinctUsers();
    @Query("SELECT COUNT(tr) FROM TestResult tr WHERE tr.totalPoints >= :points")
    Long countByTotalPointsGreaterThanEqual(@Param("points") Integer points);
    @Query("SELECT COUNT(tr) FROM TestResult tr WHERE tr.totalPoints < :points")
    Long countByTotalPointsLessThan(@Param("points") Integer points);
    @Query("SELECT COUNT(DISTINCT tr.user.id) FROM TestResult tr WHERE DATE(tr.createdAt) = :date")
    Long countDistinctUsersByDate(@Param("date") LocalDate date);
    @Query("SELECT COUNT(tr) FROM TestResult tr WHERE DATE(tr.createdAt) = :date AND tr.totalPoints >= :points")
    Long countByDateAndTotalPointsGreaterThanEqual(@Param("date") LocalDate date, @Param("points") Integer points);
    @Query("SELECT COUNT(tr) FROM TestResult tr WHERE DATE(tr.createdAt) = :date AND tr.totalPoints < :points")
    Long countByDateAndTotalPointsLessThan(@Param("date") LocalDate date, @Param("points") Integer points);
    //----------------------6-month-----------------------------------------------------
    @Query("SELECT COUNT(tr) FROM TestResult tr WHERE tr.createdAt >= :startDateTime AND tr.createdAt < :endDateTime AND tr.totalPoints >= :points")
    Long countByDateRangeAndTotalPointsGreaterThanEqual(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("points") Integer points);
    @Query("SELECT COUNT(tr) FROM TestResult tr WHERE tr.createdAt >= :startDateTime AND tr.createdAt < :endDateTime AND tr.totalPoints < :points")
    Long countByDateAndTotalPointsLessThan(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("points") Integer points);
    @Query("SELECT COUNT(DISTINCT tr.user.id) FROM TestResult tr WHERE tr.createdAt >= :startDateTime AND tr.createdAt < :endDateTime")
    Long countDistinctUsersByDateRange(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    //------------------------------------------------------------------------------------*/

    Optional<TestResult> findFirstByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT COUNT(tr) FROM TestResult tr WHERE tr.user.id = :userId")
    Long countTotalTestsForUser(@Param("userId") Long userId);

}
