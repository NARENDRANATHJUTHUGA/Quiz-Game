import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

class Question {
    String question;
    String[] options;
    int correctAnswer;
    int userAnswer = -1;

    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

public class QuizGameGUI extends JFrame {
    private Question[] questions;
    private int currentIndex = 0;
    private int score = 0;
    private Timer timer;
    private int timeLeft = 15;

    // UI components
    JLabel questionLabel, timerLabel, feedbackLabel;
    JButton[] optionButtons;
    JButton nextButton, exitButton;

    public QuizGameGUI() {
        questions = new Question[] {
            new Question("What is the capital of France?", new String[]{"Berlin", "London", "Paris", "Madrid"}, 2),
            new Question("Which planet is known as the Red Planet?", new String[]{"Earth", "Mars", "Jupiter", "Venus"}, 1),
            new Question("What is 5 x 6?", new String[]{"11", "30", "56", "25"}, 1),
            new Question("Who wrote 'Romeo and Juliet'?", new String[]{"Mark Twain", "William Shakespeare", "Charles Dickens", "Jane Austen"}, 1)
        };

        setTitle("QUIZ GAME");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title Panel
        JLabel title = new JLabel("QUIZ GAME", JLabel.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 24));
        title.setForeground(Color.BLUE);
        add(title, BorderLayout.NORTH);

        // Question panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(6, 1));
        questionLabel = new JLabel("Question", JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(questionLabel);

        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            int index = i;
            optionButtons[i].addActionListener(e -> checkAnswer(index));
            centerPanel.add(optionButtons[i]);
        }

        feedbackLabel = new JLabel(" ", JLabel.CENTER);
        centerPanel.add(feedbackLabel);

        add(centerPanel, BorderLayout.CENTER);

        // Timer and Controls
        JPanel bottomPanel = new JPanel();
        timerLabel = new JLabel("Time Left: 15 sec");
        bottomPanel.add(timerLabel);

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> loadNextQuestion());
        nextButton.setEnabled(false);
        bottomPanel.add(nextButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> showResult());
        bottomPanel.add(exitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        loadQuestion(currentIndex);
        startTimer();

        setVisible(true);
    }

    void loadQuestion(int index) {
        Question q = questions[index];
        questionLabel.setText((index + 1) + ". " + q.question);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options[i]);
            optionButtons[i].setEnabled(true);
        }
        feedbackLabel.setText(" ");
        nextButton.setEnabled(false);
        timeLeft = 15;
        timerLabel.setText("Time Left: 15 sec");
    }

    void checkAnswer(int answerIndex) {
        stopTimer();
        Question q = questions[currentIndex];
        q.userAnswer = answerIndex;

        for (JButton btn : optionButtons) btn.setEnabled(false);

        if (answerIndex == q.correctAnswer) {
            score++;
            feedbackLabel.setText("✅ Answer is Correct!");
            feedbackLabel.setForeground(Color.GREEN.darker());
        } else {
            feedbackLabel.setText("❌ Answer is Wrong! Correct: " + q.options[q.correctAnswer]);
            feedbackLabel.setForeground(Color.RED);
        }

        nextButton.setEnabled(true);
    }

    void loadNextQuestion() {
        currentIndex++;
        if (currentIndex < questions.length) {
            loadQuestion(currentIndex);
            startTimer();
        } else {
            showResult();
        }
    }

    void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timeLeft--;
                    timerLabel.setText("Time Left: " + timeLeft + " sec");
                    if (timeLeft <= 0) {
                        stopTimer();
                        feedbackLabel.setText("⏰ Time is over!");
                        feedbackLabel.setForeground(Color.ORANGE);
                        for (JButton btn : optionButtons) btn.setEnabled(false);
                        nextButton.setEnabled(true);
                    }
                });
            }
        }, 1000, 1000);
    }

    void stopTimer() {
        if (timer != null) timer.cancel();
    }

    void showResult() {
        stopTimer();
        StringBuilder result = new StringBuilder("<html><h2>Your Score: " + score + "/" + questions.length + "</h2><br><ul>");
        for (int i = 0; i < questions.length; i++) {
            Question q = questions[i];
            result.append("<li>").append(q.question).append("<br>");
            result.append("Correct: ").append(q.options[q.correctAnswer]).append("<br>");
            if (q.userAnswer == -1)
                result.append("Your Answer: Not answered</li><br>");
            else if (q.userAnswer == q.correctAnswer)
                result.append("<span style='color:green'>Your Answer: ").append(q.options[q.userAnswer]).append(" ✔</span></li><br>");
            else
                result.append("<span style='color:red'>Your Answer: ").append(q.options[q.userAnswer]).append(" ✘</span></li><br>");
        }
        result.append("</ul></html>");
        JOptionPane.showMessageDialog(this, result.toString(), "Quiz Review", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuizGameGUI::new);
    }
}
