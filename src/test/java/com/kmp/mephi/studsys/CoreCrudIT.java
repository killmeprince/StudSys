package com.kmp.mephi.studsys;

import com.kmp.mephi.studsys.entity.*;
import com.kmp.mephi.studsys.repository.*;
import jakarta.persistence.EntityManager;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CoreCrudIT {

    @Autowired UserRepository userRepo;
    @Autowired CategoryRepository categoryRepo;
    @Autowired CourseRepository courseRepo;
    @Autowired ModuleRepository moduleRepo;
    @Autowired LessonRepository lessonRepo;
    @Autowired AssignmentRepository assignmentRepo;
    @Autowired SubmissionRepository submissionRepo;
    @Autowired EnrollmentRepository enrollmentRepo;
    @Autowired QuizRepository quizRepo;
    @Autowired QuestionRepository questionRepo;
    @Autowired EntityManager em;

    @Test
    @Transactional
    void schema_is_applied_and_basic_crud_works() {
        Category category = categoryRepo.save(
                Category.builder()
                        .name("Programming-" + UUID.randomUUID())
                        .build()
        );
        assertThat(category.getId()).isNotNull();

        User teacher = userRepo.save(User.builder()
                .name("Teacher")
                .email("teacher_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.TEACHER)
                .build());

        User student = userRepo.save(User.builder()
                .name("Student")
                .email("student_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.STUDENT)
                .build());

        Course course = courseRepo.save(Course.builder()
                .title("Hibernate 101 " + UUID.randomUUID())
                .description("Intro to Hibernate")
                .category(category)
                .teacher(teacher)
                .build());

        ModuleEntity module = moduleRepo.save(ModuleEntity.builder()
                .course(course)
                .title("Module 1")
                .orderIndex(1)
                .build());

        Lesson lesson = lessonRepo.save(Lesson.builder()
                .module(module)
                .title("Session & Transactions")
                .content("content")
                .build());

        Assignment assignment = assignmentRepo.save(Assignment.builder()
                .lesson(lesson)
                .title("HW1")
                .description("Do something")
                .maxScore(100)
                .build());

        Submission submission = submissionRepo.save(Submission.builder()
                .assignment(assignment)
                .student(student)
                .content("solution")
                .score(95)
                .build());

        em.flush();
        em.clear();

        Course reloadedCourse = courseRepo.findById(course.getId()).orElseThrow();
        assertThat(reloadedCourse.getCategory().getId()).isEqualTo(category.getId());
        assertThat(reloadedCourse.getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(reloadedCourse.getModules()).hasSize(1);
        assertThat(reloadedCourse.getModules().get(0).getLessons()).hasSize(1);

        Assignment reloadedAssignment =
                assignmentRepo.findById(assignment.getId()).orElseThrow();
        assertThat(reloadedAssignment.getSubmissions()).hasSize(1);

        Submission reloadedSubmission =
                submissionRepo.findById(submission.getId()).orElseThrow();
        assertThat(reloadedSubmission.getStudent().getId()).isEqualTo(student.getId());
    }

    @Test
    void enrollment_unique_constraint_works() {
        User student = userRepo.save(User.builder()
                .name("S")
                .email("enr_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.STUDENT)
                .build());

        Category category = categoryRepo.save(Category.builder()
                .name("DB-" + UUID.randomUUID())
                .build());

        User teacher = userRepo.save(User.builder()
                .name("T")
                .email("enr_t_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.TEACHER)
                .build());

        Course course = courseRepo.save(Course.builder()
                .title("Databases " + UUID.randomUUID())
                .category(category)
                .teacher(teacher)
                .build());

        Enrollment e1 = enrollmentRepo.save(Enrollment.builder()
                .student(student)
                .course(course)
                .build());

        assertThat(e1.getId()).isNotNull();

        Enrollment e2 = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        assertThatThrownBy(() -> enrollmentRepo.saveAndFlush(e2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void submission_unique_per_assignment_and_student() {
        User student = userRepo.save(User.builder()
                .name("S2")
                .email("sub_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.STUDENT)
                .build());

        Category category = categoryRepo.save(Category.builder()
                .name("Algo-" + UUID.randomUUID())
                .build());

        User teacher = userRepo.save(User.builder()
                .name("T2")
                .email("sub_t_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.TEACHER)
                .build());

        Course course = courseRepo.save(Course.builder()
                .title("Algorithms " + UUID.randomUUID())
                .category(category)
                .teacher(teacher)
                .build());

        ModuleEntity module = moduleRepo.save(ModuleEntity.builder()
                .course(course)
                .title("Module A")
                .orderIndex(1)
                .build());

        Lesson lesson = lessonRepo.save(Lesson.builder()
                .module(module)
                .title("Lesson A")
                .content("...")
                .build());

        Assignment assignment = assignmentRepo.save(Assignment.builder()
                .lesson(lesson)
                .title("HW-A")
                .description("desc")
                .maxScore(100)
                .build());

        Submission s1 = submissionRepo.saveAndFlush(Submission.builder()
                .assignment(assignment)
                .student(student)
                .content("solution-1")
                .build());

        assertThat(s1.getId()).isNotNull();

        Submission s2 = Submission.builder()
                .assignment(assignment)
                .student(student)
                .content("solution-2")
                .build();

        assertThatThrownBy(() -> submissionRepo.saveAndFlush(s2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void lazy_loading_throws_outside_transaction() {
        Category category = categoryRepo.save(Category.builder()
                .name("JPA-" + UUID.randomUUID())
                .build());

        User teacher = userRepo.save(User.builder()
                .name("LazyTeacher")
                .email("lazy_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.TEACHER)
                .build());

        Course course = courseRepo.save(Course.builder()
                .title("JPA Mastery " + UUID.randomUUID())
                .category(category)
                .teacher(teacher)
                .build());

        moduleRepo.save(ModuleEntity.builder()
                .course(course)
                .title("M1")
                .orderIndex(1)
                .build());

        Course detached = courseRepo.findById(course.getId()).orElseThrow();

        assertThatThrownBy(() -> detached.getModules().size())
                .isInstanceOf(LazyInitializationException.class);
    }

    @Test
    @Transactional
    void lazy_loading_inside_transaction_is_ok() {
        Category category = categoryRepo.save(Category.builder()
                .name("CQRS-" + UUID.randomUUID())
                .build());

        User teacher = userRepo.save(User.builder()
                .name("LazyTeacher2")
                .email("lazy2_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.TEACHER)
                .build());

        Course course = courseRepo.save(Course.builder()
                .title("CQRS 101 " + UUID.randomUUID())
                .category(category)
                .teacher(teacher)
                .build());

        moduleRepo.save(ModuleEntity.builder()
                .course(course)
                .title("M1")
                .orderIndex(1)
                .build());

        moduleRepo.save(ModuleEntity.builder()
                .course(course)
                .title("M2")
                .orderIndex(2)
                .build());

        Long count = em.createQuery(
                        "select count(m) from ModuleEntity m where m.course.id = :cid",
                        Long.class)
                .setParameter("cid", course.getId())
                .getSingleResult();

        assertThat(count).isEqualTo(2L);

        Course managed = courseRepo.findById(course.getId()).orElseThrow();
        assertThatCode(() -> managed.getModules().size())
                .doesNotThrowAnyException();
    }

    @Test
    @Transactional
    void quiz_structure_and_results_persisted() {
        Category category = categoryRepo.save(Category.builder()
                .name("Testing-" + UUID.randomUUID())
                .build());

        User teacher = userRepo.save(User.builder()
                .name("QuizTeacher")
                .email("quiz_t_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.TEACHER)
                .build());

        User student = userRepo.save(User.builder()
                .name("QuizStudent")
                .email("quiz_s_" + UUID.randomUUID() + "@example.com")
                .passwordHash("x")
                .role(Role.STUDENT)
                .build());

        Course course = courseRepo.save(Course.builder()
                .title("Testing course " + UUID.randomUUID())
                .category(category)
                .teacher(teacher)
                .build());

        ModuleEntity module = moduleRepo.save(ModuleEntity.builder()
                .course(course)
                .title("Test module")
                .orderIndex(1)
                .build());

        Quiz quiz = quizRepo.save(Quiz.builder()
                .module(module)
                .title("Module quiz")
                .timeLimitSeconds(600)
                .build());

        Question q1 = questionRepo.save(Question.builder()
                .quiz(quiz)
                .text("What is JPA?")
                .build());

        assertThat(q1.getId()).isNotNull();

        QuizSubmission qs = QuizSubmission.builder()
                .quiz(quiz)
                .student(student)
                .score(90)
                .build();

        em.persist(qs);
        em.flush();
        em.clear();

        Long quizId = quiz.getId();

        Quiz reloadedQuiz = quizRepo.findById(quizId).orElseThrow();

        assertThatCode(() -> {
            reloadedQuiz.getQuestions().size();
            reloadedQuiz.getQuizSubmissions().size();
        }).doesNotThrowAnyException();

        Long questionsCount = em.createQuery(
                        "select count(q) from Question q where q.quiz.id = :qid",
                        Long.class)
                .setParameter("qid", quizId)
                .getSingleResult();

        Long quizSubmissionsCount = em.createQuery(
                        "select count(qs) from QuizSubmission qs where qs.quiz.id = :qid",
                        Long.class)
                .setParameter("qid", quizId)
                .getSingleResult();

        assertThat(questionsCount).isEqualTo(1L);
        assertThat(quizSubmissionsCount).isEqualTo(1L);
    }
}
