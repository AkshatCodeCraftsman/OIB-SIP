import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class User {
    private String username;
    private String password;
    private String profileInfo;

    private static HashMap<String, User> users = new HashMap<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.profileInfo = "";
        users.put(username, this);
    }

    public static User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            return user;
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void updateProfile(String profileInfo) {
        this.profileInfo = profileInfo;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public String getProfileInfo() {
        return profileInfo;
    }
}

class ProfileManager {
    private User user;

    public ProfileManager(User user) {
        this.user = user;
    }

    public void updateProfile(String profileInfo) {
        user.updateProfile(profileInfo);
    }

    public void changePassword(String newPassword) {
        user.changePassword(newPassword);
    }
}

class Quiz {
    private HashMap<Integer, String> questions;
    private HashMap<Integer, String[]> options;
    private HashMap<Integer, String> correctAnswers;
    private HashMap<Integer, String> userAnswers;
    private Timer timer;
    private int currentQuestionIndex;
    private boolean isQuizActive;

    public Quiz() {
        questions = new HashMap<>();
        options = new HashMap<>();
        correctAnswers = new HashMap<>();
        userAnswers = new HashMap<>();
        loadQuestions();
        currentQuestionIndex = 1;
        isQuizActive = false;
    }

    private void loadQuestions() {
        questions.put(1, "What is the size of int in Java?");
        options.put(1, new String[]{"A. 2 bytes", "B. 4 bytes", "C. 8 bytes", "D. 16 bytes"});
        correctAnswers.put(1, "B");

        questions.put(2, "Which of the following is not a Java feature?");
        options.put(2, new String[]{"A. Object-oriented", "B. Use of pointers", "C. Portable", "D. Dynamic and Extensible"});
        correctAnswers.put(2, "B");

        questions.put(3, "What is the default value of a boolean variable?");
        options.put(3, new String[]{"A. true", "B. false", "C. null", "D. 0"});
        correctAnswers.put(3, "B");

        questions.put(4, "Which of the following is used to find and fix bugs in Java programs?");
        options.put(4, new String[]{"A. JVM", "B. JRE", "C. JDK", "D. JDB"});
        correctAnswers.put(4, "D");

        questions.put(5, "What is the return type of the hashCode() method in the Object class?");
        options.put(5, new String[]{"A. int", "B. Object", "C. long", "D. void"});
        correctAnswers.put(5, "A");
    }

    public void selectAnswer(int questionId, String answer) {
        userAnswers.put(questionId, answer);
    }

    public void startTimer(int seconds) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSubmit();
            }
        }, seconds * 1000);
        isQuizActive = true;
    }

    public void autoSubmit() {
        System.out.println("Auto submitting quiz...");
        submitQuiz();
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex <= questions.size();
    }

    public void displayNextQuestion() {
        if (hasMoreQuestions()) {
            int qid = currentQuestionIndex;
            System.out.println(qid + ": " + questions.get(qid));
            String[] opts = options.get(qid);
            for (String opt : opts) {
                System.out.println(opt);
            }
        }
    }

    public void submitQuiz() {
        System.out.println("Quiz submitted!");
        if (timer != null) {
            timer.cancel();
        }
        displayScore();
        isQuizActive = false;
    }

    private void displayScore() {
        int score = 0;
        for (int qid : userAnswers.keySet()) {
            if (userAnswers.get(qid).equals(correctAnswers.get(qid))) {
                score++;
            }
        }
        System.out.println("Your score: " + score + "/" + questions.size());
    }

    public void nextQuestion() {
        currentQuestionIndex++;
    }

    public boolean isQuizActive() {
        return isQuizActive;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
}

class SessionManager {
    private User currentUser;

    public void login(String username, String password) {
        currentUser = User.login(username, password);
        if (currentUser != null) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

public class OnlineExam {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SessionManager sessionManager = new SessionManager();
        Quiz quiz = new Quiz();

        // For demo purposes, adding a default user
        new User("student", "123456");

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Update Profile");
            System.out.println("3. Change Password");
            System.out.println("4. Start Quiz");
            System.out.println("5. Logout");
            System.out.println("6. Exit");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid option.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    sessionManager.login(username, password);
                    break;
                case 2:
                    if (sessionManager.isLoggedIn()) {
                        System.out.print("Enter new profile info: ");
                        String profileInfo = scanner.nextLine();
                        ProfileManager profileManager = new ProfileManager(sessionManager.getCurrentUser());
                        profileManager.updateProfile(profileInfo);
                        System.out.println("Profile updated!");
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 3:
                    if (sessionManager.isLoggedIn()) {
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        ProfileManager profileManager = new ProfileManager(sessionManager.getCurrentUser());
                        profileManager.changePassword(newPassword);
                        System.out.println("Password changed!");
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 4:
                    if (sessionManager.isLoggedIn()) {
                        quiz = new Quiz();
                        quiz.startTimer(60);  // 1 minute timer for demo purposes

                        while (quiz.hasMoreQuestions()) {
                            quiz.displayNextQuestion();
                            int qid = quiz.getCurrentQuestionIndex();
                            System.out.print("Your answer: ");
                            String answer = scanner.nextLine();
                            quiz.selectAnswer(qid, answer);
                            quiz.nextQuestion();
                        }
                        quiz.submitQuiz();
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 5:
                    sessionManager.logout();
                    break;
                case 6:
                    System.exit(0);
            }
        }
    }
}
