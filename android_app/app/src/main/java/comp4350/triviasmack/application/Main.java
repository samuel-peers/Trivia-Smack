package comp4350.triviasmack.application;

public class Main {

    public static final int numQuestions = 5;
    public static final int numPracticeQuestions = 50;

    public static void main(String[] args) {
        startUp();

        shutDown();

        System.out.println("All done");
    }

    public static void startUp() {
        Services.createServerAccess();
    }

    public static void shutDown() {
        Services.closeServerAccess();
    }
}
