package comp4350.triviasmack.application;

public class Main {

    public static final int numQuestions = 3;
    public static final String[] categories = {"all", "geography", "history", "math and science", "pop culture", "other"};

    public static void main(String[] args) {
        startUp();

        shutDown();

        System.out.println("All done");
    }

    public static void startUp() { Services.createServerAccess(); }

    public static void shutDown() {
        Services.closeServerAccess();
    }
}
