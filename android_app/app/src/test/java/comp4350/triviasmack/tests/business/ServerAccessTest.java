package comp4350.triviasmack.tests.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import comp4350.triviasmack.application.Services;
import comp4350.triviasmack.business.ServerAccess;
import comp4350.triviasmack.objects.Question;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ServerAccessTest {

    private final static int bigNum = 500;
    private static int dbSize = 410;
    private final static String category = "all";

    public static void testGetRandomQuestionValidNum(int numQuestions, String category) {
        ServerAccess serverAccess = Services.getServerAccess();
        ArrayList<Question> questions = new ArrayList<>();

        System.out.println("Testing ServerAccess: getRandomQuestions (" + numQuestions + ")");

        serverAccess.getRandomQuestions(questions, numQuestions, category);

        assertEquals(questions.size(), numQuestions);
        assertNotNull(questions);

        for (int i = 0; i < questions.size(); i++) {
            assertTrue(questions.get(i) instanceof Question);
            assertNotNull(questions.get(i).getAnswer());
            assertNotNull(questions.get(i).getOptions());
        }
    }

    public static void testGetRandomQuestionNegative() {
        ServerAccess serverAccess = Services.getServerAccess();
        ArrayList<Question> questions;
        int numQuestions;

        questions = new ArrayList<>();
        numQuestions = -1;

        System.out.println("Testing ServerAccess: getRandomQuestions(-1)");

        try {
            serverAccess.getRandomQuestions(questions, numQuestions, category);

            fail("Failed to catch exception.");
        } catch (Exception e) {
            assertEquals("Didn't throw the right exception", IllegalArgumentException.class, e.getClass());

        }
    }


    public static void testGetRandomQuestionNull() {
        ServerAccess serverAccess = Services.getServerAccess();
        int numQuestions;

        numQuestions = 0;

        System.out.println("Testing ServerAccess: getRandomQuestions(questions=null)");

        try {
            serverAccess.getRandomQuestions(null, numQuestions, category);
            fail("Failed to catch exception.");
        } catch (Exception e) {
            assertEquals("Didn't throw the right exception", NullPointerException.class, e.getClass());

        }
    }

    public static void testGetRandomQuestionNullNegative() {
        ServerAccess serverAccess = Services.getServerAccess();
        int numQuestions;

        numQuestions = -1;

        System.out.println("Testing ServerAccess: getRandomQuestions(questions=null, -1)");

        try {
            serverAccess.getRandomQuestions(null, numQuestions, category);
            fail("Failed to catch exception.");
        } catch (Exception e) {
            assertEquals("Didn't throw the right exception", IllegalArgumentException.class, e.getClass());

        }
    }

    public static void testGetRandomQuestionBigNum() {
        ServerAccess serverAccess = Services.getServerAccess();
        ArrayList<Question> questions;

        questions = new ArrayList<>();

        System.out.println("Testing ServerAccess: getRandomQuestions(100)");
        serverAccess.getRandomQuestions(questions, bigNum, category);

        assertNotEquals(questions.size(), bigNum);
        assertEquals(questions.size(), dbSize);
    }

    public static void testGetRandomQuestionCategory(int numQuestions, String category) {
        ServerAccess serverAccess = Services.getServerAccess();
        ArrayList<Question> questions = new ArrayList<>();

        System.out.println("Testing ServerAccess: getRandomQuestions(category=" + category +")");

        serverAccess.getRandomQuestions(questions, numQuestions, category);

        assertEquals(questions.size(), numQuestions);
        assertNotNull(questions);

        for (int i = 0; i < questions.size(); i++) {
            System.out.println(questions.get(i).getQuestion());
            assertTrue(questions.get(i) instanceof Question);
            assertEquals(category, questions.get(i).getCategory());
        }
    }

    public static void testGetRandomQuestionInvalidCategory() {
        ServerAccess serverAccess = Services.getServerAccess();
        ArrayList<Question> questions = new ArrayList<>();

        System.out.println("Testing ServerAccess: getRandomQuestions() with invalid category");

        try{
            serverAccess.getRandomQuestions(questions, 1, "invalid");
            fail("Failed to catch exception.");
        }
        catch (Exception e){
        }
    }

    public static void serverAccessTest() {
        testGetRandomQuestionValidNum(0, category);
        testGetRandomQuestionValidNum(3, category);
        testGetRandomQuestionValidNum(9, category);
        testGetRandomQuestionNull();
        testGetRandomQuestionNegative();
        testGetRandomQuestionNullNegative();
        testGetRandomQuestionBigNum();
        testGetRandomQuestionCategory(4, "math and science");
        testGetRandomQuestionInvalidCategory();
    }

    @Test
    public void testServerAccess() {
        System.out.println("Testing ServerAccess (stub)");
        dbSize = 10;
        serverAccessTest();
    }

    @Before
    public void setUp() {
        Services.closeServerAccess();
        Services.createServerAccess(new ServerAccessStub());
    }

    @After
    public void tearDown() {
        Services.closeServerAccess();
    }
}
