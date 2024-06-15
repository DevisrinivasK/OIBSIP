import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}

class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;

    public Question(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

public class Online_Exam {
    private static User currentUser;
    private static List<Question> questions;
    private static List<Integer> selectedAnswers;
    private static Timer timer;
    private static int remainingTimeInSeconds = 1800; // 30 minutes

    public static void main(String[] args) {
        initializeQuestions();
        login();
    }

    private static void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("The battle of Kalinga was fought in the year?", List.of("540 BC", "320 BC", "440 BC", "261 BC"), 3));
        questions.add(new Question("What is the rarest blood type?", List.of("O-negative", "AB-negative", "AB-positive", "B-negative"), 1));
        questions.add(new Question("Which planet in our solar system is known as the 'Red Planet'?", List.of("Mars", "Mercury", "Venus", "Jupiter"), 0));
        questions.add(new Question("What is the name of the weak zone of the earth's crust?", List.of("Seismic", "Cosmic", "Formic", "Anaemic"), 0));
        questions.add(new Question("Which state is popular for 'Dandiya' dance?", List.of("Punjab", "Maharashtra", "Gujarat", "Tamil Nadu"), 2));
        questions.add(new Question("In which year was '@' chosen for use in email addresses?", List.of("1967", "1972", "1980", "1984"), 1));
        questions.add(new Question("Which vitamin is destroyed if the food is cooked?", List.of("Vitamin A", "Vitamin B", "Vitamin C", "Vitamin D"), 2));
        questions.add(new Question("Who was the inventor of the railway engine?", List.of("Charles Babbage", "George Stephenson", "James Watt"), 1));
        questions.add(new Question("Which was the first pico satellite of India?", List.of("INSAT", "STUDSAT", "GSAT-4", "ANUSAT"), 1));
        questions.add(new Question("Which instrument is used for detecting electric current?", List.of("Galvanometer", "Tube tester", "Altimeter", "Fathometer"), 0));
    }

    private static void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        currentUser = new User(username, password, "Devisrinivas");

        if (currentUser != null) {
            showMainMenu(scanner);
        } else {
            System.out.println("Login failed. Please try again.");
            login();
        }
    }

    private static void showMainMenu(Scanner scanner) {
        System.out.println("Welcome, " + currentUser.getFullName() + "!");
        System.out.println("1. Start Exam");
        System.out.println("2. Update Profile");
        System.out.println("3. Change Password");
        System.out.println("4. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                startExam(scanner);
                break;
            case 2:
                updateProfile(scanner);
                break;
            case 3:
                changePassword(scanner);
                break;
            case 4:
                logout(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                showMainMenu(scanner);
                break;
        }
    }

    private static void startExam(Scanner scanner) {
        selectedAnswers = new ArrayList<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                remainingTimeInSeconds--;
                if (remainingTimeInSeconds <= 0) {
                    autoSubmit();
                }
            }
        }, 1000, 1000);

        System.out.println("You have 30 minutes to complete the exam.");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("Question " + (i + 1) + ": " + question.getQuestionText());
            List<String> options = question.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ". " + options.get(j));
            }
            System.out.print("Select an answer (1-" + options.size() + "): ");
            int answer = scanner.nextInt();
            selectedAnswers.add(answer - 1);
        }
        autoSubmit();
    }

    private static void autoSubmit() {
        if (timer != null) {
            timer.cancel();
        }
        System.out.println("Time's up! Submitting your answers.");
        showResult();
    }

    private static void showResult() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int selectedAnswerIndex = selectedAnswers.get(i);
            if (selectedAnswerIndex == question.getCorrectOptionIndex()) {
                score++;
            }
        }

        System.out.println("You scored " + score + " out of " + questions.size() + " questions.");
        logout(new Scanner(System.in));
    }

    private static void updateProfile(Scanner scanner) {
        System.out.print("Enter your new full name: ");
        String newFullName = scanner.nextLine();
        currentUser = new User(currentUser.getUsername(), currentUser.getPassword(), newFullName);
        System.out.println("Profile updated successfully.");
        showMainMenu(scanner);
    }

    private static void changePassword(Scanner scanner) {
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();
        if (currentPassword.equals(currentUser.getPassword())) {
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
            currentUser = new User(currentUser.getUsername(), newPassword, currentUser.getFullName());
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Incorrect current password. Please try again.");
        }
        showMainMenu(scanner);
    }

    private static void logout(Scanner scanner) {
        System.out.println("Logging out. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}
