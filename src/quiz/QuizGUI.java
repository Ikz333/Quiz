/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package quiz;

// All imports
//import guisource.HomePage;
import quiz.QuizData;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**
 *
 * @author ikram
 */
public class QuizGUI extends javax.swing.JFrame {

    // ===== Core quiz fields (Maths / Biology / Physics) =====
    private Timer timer;                         // Timer for core quiz tab
    private int remainingSeconds = 30;           // Time left for core quiz (seconds)
    private QuizData.Question[] currentQuestions;// Array of questions for selected core subject
    private int currentQuestionIndex = 0;        // Index of current question in core quiz
    private int score = 0;                       // Player score for core quiz
    private int totalQuestions = 0;              // Number of questions in current core quiz

    // ===== Side quiz fields (CompSci / Chemistry / Mathematics) =====
    private Timer sideTimer;                     // Timer for side quiz tab
    private int sideRemainingSeconds = 30;       // Time left for side quiz (seconds)
    private QuizData.Question[] sideQuestions;   // Array of questions for selected side subject
    private int sideCurrentQuestionIndex = 0;    // Index of current question in side quiz
    private int sideScore = 0;                   // Player score for side quiz
    private int sideTotalQuestions = 0;          // Number of questions in current side quiz

    /**
     * Creates new form QuizGUI
     * Sets up timers, listeners, and initial UI state.
     */
    public QuizGUI() {
        initComponents();    // NetBeans-generated UI setup
        initTimer();         // Set up core quiz timer (countdown)
        initSideTimer();     // Set up side quiz timer (countdown)

        // Timer text areas: display-only, start at 30 seconds
        TimerTA.setEditable(false);
        TimerTA.setText("0:30");

        TimerTA1.setEditable(false);
        TimerTA1.setText("0:30");

        // Attach listeners for subject radio buttons and tab changes
        setupSubjectListeners();        // Core subjects (Maths/Bio/Physics)
        setupSideSubjectListeners();    // Side subjects (CS/Chem/Maths)
        setupTabChangeListener();       // Reset quizzes when changing tabs

        // Question display areas should not be editable by user
        DisplayTA.setEditable(false);
        DisplayTA1.setEditable(false);
    }

    // ===== Core timer (for main/core quiz tab) =====
    private void initTimer() {
        // Timer fires every 1000 ms (1 second)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingSeconds--;  // count down

                if (remainingSeconds >= 0) {
                    int minutes = remainingSeconds / 60;
                    int seconds = remainingSeconds % 60;

                    // Show remaining time in m:ss format, e.g. 0:25
                    TimerTA.setText(String.format("%d:%02d", minutes, seconds));
                }

                // When core quiz time runs out
                if (remainingSeconds <= 0) {
                    timer.stop();

                    String result = "Time's up!\n\n";
                    result = result + "Your score: " + score + " out of " + totalQuestions;

                    // Show final result and clear selected answers
                    DisplayTA.setText(result);
                    AnswersBG.clearSelection();
                }
            }
        });
    }

    // ===== Side timer (for side subjects tab) =====
    private void initSideTimer() {
        // Timer fires every 1000 ms (1 second)
        sideTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sideRemainingSeconds--;  // count down

                if (sideRemainingSeconds >= 0) {
                    int minutes = sideRemainingSeconds / 60;
                    int seconds = sideRemainingSeconds % 60;

                    // Show remaining time in m:ss format for side quiz
                    TimerTA1.setText(String.format("%d:%02d", minutes, seconds));
                }

                // When side quiz time runs out
                if (sideRemainingSeconds <= 0) {
                    sideTimer.stop();

                    String result = "Time's up!\n\n";
                    result = result + "Your score: " + sideScore + " out of " + sideTotalQuestions;

                    // Show final result and clear selected answers
                    DisplayTA1.setText(result);
                    AnswersBG.clearSelection();
                }
            }
        });
    }

    // ===== Attach listeners to core subject radio buttons =====
    private void setupSubjectListeners() {
        // When each subject is selected, load its questions and start timer
        MathsRB.addActionListener(e -> loadSubject("Maths"));
        BiologyRB.addActionListener(e -> loadSubject("Biology"));
        PhysicsRB.addActionListener(e -> loadSubject("Physics"));
    }

    // ===== Attach listeners to side subject radio buttons =====
    private void setupSideSubjectListeners() {
        CompSciRB.addActionListener(e -> loadSideSubject("CS"));
        ChemistryRB.addActionListener(e -> loadSideSubject("Chemistry"));
        MathematicsRB.addActionListener(e -> loadSideSubject("Maths"));
    }

    // ===== Tab change behavior =====
    private void setupTabChangeListener() {
        // Whenever the user switches between tabs, reset both quizzes to default state
        TabsPNL.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                resetCoreQuiz();
                resetSideQuiz();
            }
        });
    }

    // ===== Load core subject questions and start core quiz =====
    private void loadSubject(String subject) {
        // Pick the correct question set based on which subject was selected
        if (subject.equals("Maths")) {
            currentQuestions = QuizData.getMathsQuestions();
        } else if (subject.equals("Biology")) {
            currentQuestions = QuizData.getBiologyQuestions();
        } else if (subject.equals("Physics")) {
            currentQuestions = QuizData.getPhysicsQuestions();
        }

        // Reset progress for this quiz
        currentQuestionIndex = 0;
        score = 0;

        if (currentQuestions != null) {
            totalQuestions = currentQuestions.length;
        } else {
            totalQuestions = 0;
        }

        // Show the first question
        showCurrentQuestion();

        // Reset and start core timer
        if (timer != null) {
            remainingSeconds = 30;
            TimerTA.setText("0:30");
            timer.start();
        }
    }

    // ===== Load side subject questions and start side quiz =====
    private void loadSideSubject(String subject) {
        // Pick the correct question set for the side quiz based on subject
        if (subject.equals("CS")) {
            sideQuestions = QuizData.getComputerScienceQuestions();
        } else if (subject.equals("Chemistry")) {
            sideQuestions = QuizData.getChemistryQuestions();
        } else if (subject.equals("Maths")) {
            sideQuestions = QuizData.getSideMathsQuestions();
        }

        // Reset progress for side quiz
        sideCurrentQuestionIndex = 0;
        sideScore = 0;

        if (sideQuestions != null) {
            sideTotalQuestions = sideQuestions.length;
        } else {
            sideTotalQuestions = 0;
        }

        // Reset and start side timer
        sideRemainingSeconds = 30;
        TimerTA1.setText("0:30");

        if (sideTimer != null) {
            sideTimer.start();
        }

        // Show the first side question
        showCurrentSideQuestion();
    }

    // ===== Show current core question or final result =====
    private void showCurrentQuestion() {
        // No questions loaded (e.g. subject not chosen)
        if (currentQuestions == null || totalQuestions == 0) {
            DisplayTA.setText("No questions for this subject.");
            return;
        }

        // If there are still questions left, show the current one
        if (currentQuestionIndex < totalQuestions) {
            QuizData.Question q = currentQuestions[currentQuestionIndex];

            String text = "Question " + (currentQuestionIndex + 1) + " of " + totalQuestions + ":\n\n";
            text = text + q.getQuestion() + "\n\n";
            text = text + "A) " + q.getOptionA() + "\n";
            text = text + "B) " + q.getOptionB() + "\n";
            text = text + "C) " + q.getOptionC() + "\n";

            // Show question and options in core display area
            DisplayTA.setText(text);

            // Clear any previously selected answer
            AnswersBG.clearSelection();

        } else {
            // All questions answered: end core quiz
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }

            String result = "Quiz finished!\n\n";
            result = result + "Your score: " + score + " out of " + totalQuestions;

            DisplayTA.setText(result);
            AnswersBG.clearSelection();
        }
    }

    // ===== Show current side question or final result =====
    private void showCurrentSideQuestion() {
        // No questions loaded for side quiz
        if (sideQuestions == null || sideTotalQuestions == 0) {
            DisplayTA1.setText("No questions for this subject.");
            return;
        }

        // If there are still side questions left, show the current one
        if (sideCurrentQuestionIndex < sideTotalQuestions) {
            QuizData.Question q = sideQuestions[sideCurrentQuestionIndex];

            String text = "Question " + (sideCurrentQuestionIndex + 1) + " of " + sideTotalQuestions + ":\n\n";
            text = text + q.getQuestion() + "\n\n";
            text = text + "A) " + q.getOptionA() + "\n";
            text = text + "B) " + q.getOptionB() + "\n";
            text = text + "C) " + q.getOptionC() + "\n";

            // Show question and options in side display area
            DisplayTA1.setText(text);

            // Clear selected answer (same button group used)
            AnswersBG.clearSelection();

        } else {
            // All side questions answered: end side quiz
            if (sideTimer != null && sideTimer.isRunning()) {
                sideTimer.stop();
            }

            String result = "Side quiz finished!\n\n";
            result = result + "Your score: " + sideScore + " out of " + sideTotalQuestions;

            DisplayTA1.setText(result);
            AnswersBG.clearSelection();
        }
    }

    // ===== Reset core quiz when tab changes or exiting =====
    private void resetCoreQuiz() {
        // Stop core timer if running
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // Reset time display and counters
        remainingSeconds = 30;
        TimerTA.setText("0:30");

        currentQuestions = null;
        currentQuestionIndex = 0;
        score = 0;
        totalQuestions = 0;

        // Clear questions and selections
        DisplayTA.setText("");

        if (MainBG != null) {
            MainBG.clearSelection();    // Clear core subject radio buttons
        }

        if (AnswersBG != null) {
            AnswersBG.clearSelection(); // Clear A/B/C answers
        }
    }

    // ===== Reset side quiz when tab changes or exiting =====
    private void resetSideQuiz() {
        // Stop side timer if running
        if (sideTimer != null && sideTimer.isRunning()) {
            sideTimer.stop();
        }

        // Reset time display and side quiz counters
        sideRemainingSeconds = 30;
        TimerTA1.setText("0:30");

        sideQuestions = null;
        sideCurrentQuestionIndex = 0;
        sideScore = 0;
        sideTotalQuestions = 0;

        // Clear questions and selections
        DisplayTA1.setText("");

        if (SideBG != null) {
            SideBG.clearSelection();    // Clear side subject radio buttons (if grouped here)
        }

        if (AnswersBG != null) {
            AnswersBG.clearSelection(); // Clear A/B/C answers
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AnswersBG = new javax.swing.ButtonGroup();
        MainBG = new javax.swing.ButtonGroup();
        SideBG = new javax.swing.ButtonGroup();
        TabsPNL = new javax.swing.JTabbedPane();
        CorePNL = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        DisplayTA = new javax.swing.JTextArea();
        QuizSubjectsPNL = new javax.swing.JPanel();
        MathsRB = new javax.swing.JRadioButton();
        BiologyRB = new javax.swing.JRadioButton();
        PhysicsRB = new javax.swing.JRadioButton();
        MenuBTN = new javax.swing.JButton();
        SelectAnsLBL = new javax.swing.JLabel();
        AnswerARB = new javax.swing.JRadioButton();
        AnswerBRB = new javax.swing.JRadioButton();
        AnswerCRB = new javax.swing.JRadioButton();
        AnswerBTN = new javax.swing.JButton();
        TitleLBL = new javax.swing.JLabel();
        TimerLBL = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TimerTA = new javax.swing.JTextArea();
        LogoLBL = new javax.swing.JLabel();
        SidePNL = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        DisplayTA1 = new javax.swing.JTextArea();
        QuizSubjectsPNL1 = new javax.swing.JPanel();
        MathematicsRB = new javax.swing.JRadioButton();
        ChemistryRB = new javax.swing.JRadioButton();
        CompSciRB = new javax.swing.JRadioButton();
        MenuBTN1 = new javax.swing.JButton();
        SelectAnsLBL1 = new javax.swing.JLabel();
        AnswerARB1 = new javax.swing.JRadioButton();
        AnswerBRB1 = new javax.swing.JRadioButton();
        AnswerCRB1 = new javax.swing.JRadioButton();
        AnswerBTN1 = new javax.swing.JButton();
        TitleLBL1 = new javax.swing.JLabel();
        TimerLBL1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TimerTA1 = new javax.swing.JTextArea();
        LogoLBL1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        TabsPNL.setBackground(new java.awt.Color(0, 0, 0));
        TabsPNL.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.white, java.awt.Color.black, java.awt.Color.white));
        TabsPNL.setForeground(new java.awt.Color(255, 255, 255));
        TabsPNL.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        CorePNL.setBackground(new java.awt.Color(0, 102, 102));
        CorePNL.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.cyan, java.awt.Color.white, java.awt.Color.black, java.awt.Color.lightGray));
        CorePNL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DisplayTA.setBackground(new java.awt.Color(0, 0, 0));
        DisplayTA.setColumns(20);
        DisplayTA.setForeground(new java.awt.Color(255, 255, 255));
        DisplayTA.setRows(5);
        jScrollPane.setViewportView(DisplayTA);

        CorePNL.add(jScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 328, 133));

        QuizSubjectsPNL.setBackground(new java.awt.Color(0, 0, 0));
        QuizSubjectsPNL.setForeground(new java.awt.Color(255, 255, 255));

        MathsRB.setBackground(new java.awt.Color(0, 0, 0));
        SideBG.add(MathsRB);
        MathsRB.setForeground(new java.awt.Color(255, 255, 255));
        MathsRB.setText("Maths");

        BiologyRB.setBackground(new java.awt.Color(0, 0, 0));
        SideBG.add(BiologyRB);
        BiologyRB.setForeground(new java.awt.Color(255, 255, 255));
        BiologyRB.setText("Biology");

        PhysicsRB.setBackground(new java.awt.Color(0, 0, 0));
        SideBG.add(PhysicsRB);
        PhysicsRB.setForeground(new java.awt.Color(255, 255, 255));
        PhysicsRB.setText("Physics");

        javax.swing.GroupLayout QuizSubjectsPNLLayout = new javax.swing.GroupLayout(QuizSubjectsPNL);
        QuizSubjectsPNL.setLayout(QuizSubjectsPNLLayout);
        QuizSubjectsPNLLayout.setHorizontalGroup(
            QuizSubjectsPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuizSubjectsPNLLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(QuizSubjectsPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PhysicsRB)
                    .addComponent(MathsRB)
                    .addComponent(BiologyRB))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        QuizSubjectsPNLLayout.setVerticalGroup(
            QuizSubjectsPNLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuizSubjectsPNLLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MathsRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BiologyRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PhysicsRB)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        CorePNL.add(QuizSubjectsPNL, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, 90));

        MenuBTN.setBackground(new java.awt.Color(0, 0, 0));
        MenuBTN.setForeground(new java.awt.Color(255, 255, 255));
        MenuBTN.setText("Main Menu");
        MenuBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuBTNActionPerformed(evt);
            }
        });
        CorePNL.add(MenuBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, -1, -1));

        SelectAnsLBL.setForeground(new java.awt.Color(255, 255, 255));
        SelectAnsLBL.setText("Select Answer:");
        CorePNL.add(SelectAnsLBL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        AnswerARB.setBackground(new java.awt.Color(0, 0, 0));
        AnswersBG.add(AnswerARB);
        AnswerARB.setForeground(new java.awt.Color(255, 255, 255));
        AnswerARB.setText("A");
        CorePNL.add(AnswerARB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        AnswerBRB.setBackground(new java.awt.Color(0, 0, 0));
        AnswersBG.add(AnswerBRB);
        AnswerBRB.setForeground(new java.awt.Color(255, 255, 255));
        AnswerBRB.setText("B");
        CorePNL.add(AnswerBRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        AnswerCRB.setBackground(new java.awt.Color(0, 0, 0));
        AnswersBG.add(AnswerCRB);
        AnswerCRB.setForeground(new java.awt.Color(255, 255, 255));
        AnswerCRB.setText("C");
        CorePNL.add(AnswerCRB, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        AnswerBTN.setBackground(new java.awt.Color(0, 0, 0));
        AnswerBTN.setForeground(new java.awt.Color(255, 255, 255));
        AnswerBTN.setText("Answer");
        AnswerBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerBTNActionPerformed(evt);
            }
        });
        CorePNL.add(AnswerBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        TitleLBL.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        TitleLBL.setForeground(new java.awt.Color(255, 255, 255));
        TitleLBL.setText("Quiz");
        CorePNL.add(TitleLBL, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        TimerLBL.setForeground(new java.awt.Color(255, 255, 255));
        TimerLBL.setText("Timer:");
        CorePNL.add(TimerLBL, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, -1, -1));

        TimerTA.setBackground(new java.awt.Color(0, 0, 0));
        TimerTA.setColumns(20);
        TimerTA.setForeground(new java.awt.Color(255, 255, 255));
        TimerTA.setRows(5);
        jScrollPane1.setViewportView(TimerTA);

        CorePNL.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, -1, 110));

        LogoLBL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/quiz logo.png"))); // NOI18N
        CorePNL.add(LogoLBL, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        TabsPNL.addTab("Core ", CorePNL);

        SidePNL.setBackground(new java.awt.Color(0, 102, 102));
        SidePNL.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.cyan, java.awt.Color.white, java.awt.Color.black, java.awt.Color.lightGray));
        SidePNL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DisplayTA1.setBackground(new java.awt.Color(0, 0, 0));
        DisplayTA1.setColumns(20);
        DisplayTA1.setForeground(new java.awt.Color(255, 255, 255));
        DisplayTA1.setRows(5);
        jScrollPane2.setViewportView(DisplayTA1);

        SidePNL.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 328, 133));

        QuizSubjectsPNL1.setBackground(new java.awt.Color(0, 0, 0));
        QuizSubjectsPNL1.setForeground(new java.awt.Color(255, 255, 255));

        MathematicsRB.setBackground(new java.awt.Color(0, 0, 0));
        SideBG.add(MathematicsRB);
        MathematicsRB.setForeground(new java.awt.Color(255, 255, 255));
        MathematicsRB.setText("Mathematics");

        ChemistryRB.setBackground(new java.awt.Color(0, 0, 0));
        SideBG.add(ChemistryRB);
        ChemistryRB.setForeground(new java.awt.Color(255, 255, 255));
        ChemistryRB.setText("Chemistry");

        CompSciRB.setBackground(new java.awt.Color(0, 0, 0));
        SideBG.add(CompSciRB);
        CompSciRB.setForeground(new java.awt.Color(255, 255, 255));
        CompSciRB.setText("Computer Science");

        javax.swing.GroupLayout QuizSubjectsPNL1Layout = new javax.swing.GroupLayout(QuizSubjectsPNL1);
        QuizSubjectsPNL1.setLayout(QuizSubjectsPNL1Layout);
        QuizSubjectsPNL1Layout.setHorizontalGroup(
            QuizSubjectsPNL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuizSubjectsPNL1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(QuizSubjectsPNL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MathematicsRB)
                    .addComponent(CompSciRB)
                    .addComponent(ChemistryRB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        QuizSubjectsPNL1Layout.setVerticalGroup(
            QuizSubjectsPNL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QuizSubjectsPNL1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CompSciRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChemistryRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MathematicsRB)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        SidePNL.add(QuizSubjectsPNL1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, 90));

        MenuBTN1.setBackground(new java.awt.Color(0, 0, 0));
        MenuBTN1.setForeground(new java.awt.Color(255, 255, 255));
        MenuBTN1.setText("Main Menu");
        MenuBTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuBTN1ActionPerformed(evt);
            }
        });
        SidePNL.add(MenuBTN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, -1, -1));

        SelectAnsLBL1.setForeground(new java.awt.Color(255, 255, 255));
        SelectAnsLBL1.setText("Select Answer:");
        SidePNL.add(SelectAnsLBL1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        AnswerARB1.setBackground(new java.awt.Color(0, 0, 0));
        AnswersBG.add(AnswerARB1);
        AnswerARB1.setForeground(new java.awt.Color(255, 255, 255));
        AnswerARB1.setText("A");
        SidePNL.add(AnswerARB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        AnswerBRB1.setBackground(new java.awt.Color(0, 0, 0));
        AnswersBG.add(AnswerBRB1);
        AnswerBRB1.setForeground(new java.awt.Color(255, 255, 255));
        AnswerBRB1.setText("B");
        SidePNL.add(AnswerBRB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        AnswerCRB1.setBackground(new java.awt.Color(0, 0, 0));
        AnswersBG.add(AnswerCRB1);
        AnswerCRB1.setForeground(new java.awt.Color(255, 255, 255));
        AnswerCRB1.setText("C");
        SidePNL.add(AnswerCRB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, -1));

        AnswerBTN1.setBackground(new java.awt.Color(0, 0, 0));
        AnswerBTN1.setForeground(new java.awt.Color(255, 255, 255));
        AnswerBTN1.setText("Answer");
        AnswerBTN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnswerBTN1ActionPerformed(evt);
            }
        });
        SidePNL.add(AnswerBTN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        TitleLBL1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        TitleLBL1.setForeground(new java.awt.Color(255, 255, 255));
        TitleLBL1.setText("Quiz");
        SidePNL.add(TitleLBL1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        TimerLBL1.setForeground(new java.awt.Color(255, 255, 255));
        TimerLBL1.setText("Timer:");
        SidePNL.add(TimerLBL1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, -1, -1));

        TimerTA1.setBackground(new java.awt.Color(0, 0, 0));
        TimerTA1.setColumns(20);
        TimerTA1.setForeground(new java.awt.Color(255, 255, 255));
        TimerTA1.setRows(5);
        jScrollPane3.setViewportView(TimerTA1);

        SidePNL.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 230, 110));

        LogoLBL1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/quiz logo.png"))); // NOI18N
        SidePNL.add(LogoLBL1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        TabsPNL.addTab("Side", SidePNL);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabsPNL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabsPNL, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MenuBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuBTNActionPerformed
        // TODO add your handling code here:
        // If core timer is running, stop it before leaving the quiz
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // Close this window and go back to the Home Page
//        setVisible(false);
//        HomePage HP = new HomePage();
//        HP.setVisible(true);
        System.exit(0);
    }//GEN-LAST:event_MenuBTNActionPerformed

    private void MenuBTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuBTN1ActionPerformed
        // TODO add your handling code here:
        // If side timer is running, stop it before leaving the quiz
        if (sideTimer != null && sideTimer.isRunning()) {
            sideTimer.stop();
        }  

        // Close side quiz window and return to Home Page
//        setVisible(false);
//        HomePage HP = new HomePage();
//        HP.setVisible(true);

        System.exit(0);
    }//GEN-LAST:event_MenuBTN1ActionPerformed

    private void AnswerBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerBTNActionPerformed
        // TODO add your handling code here:
        // If no subject is selected or quiz already finished, do nothing
        if (currentQuestions == null || currentQuestionIndex >= totalQuestions) {
            return;
        }

        char selectedAnswer = 'X';  // Default means nothing selected

        // Determine which answer (A/B/C) the user selected
        if (AnswerARB.isSelected()) {
            selectedAnswer = 'A';
        } else if (AnswerBRB.isSelected()) {
            selectedAnswer = 'B';
        } else if (AnswerCRB.isSelected()) {
            selectedAnswer = 'C';
        } else {
            // User clicked "Answer" without selecting anything
            javax.swing.JOptionPane.showMessageDialog(this, "Please select A, B or C.");
            return;
        }

        // Get the current question object
        QuizData.Question current = currentQuestions[currentQuestionIndex];

        // If user answer matches the correct letter, increase score
        if (selectedAnswer == current.getCorrectAnswer()) {
            score++;
        }

        // Move to next question and refresh display
        currentQuestionIndex++;
        showCurrentQuestion();
    }//GEN-LAST:event_AnswerBTNActionPerformed

    private void AnswerBTN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnswerBTN1ActionPerformed
        // TODO add your handling code here
        // If side subject is not selected or quiz finished, do nothing
        if (sideQuestions == null || sideCurrentQuestionIndex >= sideTotalQuestions) {
            return;
        }

        char selectedAnswer = 'X';  // Default means nothing selected

        // Determine which answer (A/B/C) the user chose for side quiz
        if (AnswerARB1.isSelected()) {
            selectedAnswer = 'A';
        } else if (AnswerBRB1.isSelected()) {
            selectedAnswer = 'B';
        } else if (AnswerCRB1.isSelected()) {
            selectedAnswer = 'C';
        } else {
            // User clicked "Answer" with no answer chosen
            javax.swing.JOptionPane.showMessageDialog(this, "Please select A, B or C.");
            return;
        }

        // Get current side question
        QuizData.Question current = sideQuestions[sideCurrentQuestionIndex];

        // Increase side quiz score if correct
        if (selectedAnswer == current.getCorrectAnswer()) {
            sideScore++;
        }

        // Go to next side question
        sideCurrentQuestionIndex++;
        showCurrentSideQuestion();
    }//GEN-LAST:event_AnswerBTN1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuizGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuizGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuizGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuizGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuizGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton AnswerARB;
    private javax.swing.JRadioButton AnswerARB1;
    private javax.swing.JRadioButton AnswerBRB;
    private javax.swing.JRadioButton AnswerBRB1;
    private javax.swing.JButton AnswerBTN;
    private javax.swing.JButton AnswerBTN1;
    private javax.swing.JRadioButton AnswerCRB;
    private javax.swing.JRadioButton AnswerCRB1;
    private javax.swing.ButtonGroup AnswersBG;
    private javax.swing.JRadioButton BiologyRB;
    private javax.swing.JRadioButton ChemistryRB;
    private javax.swing.JRadioButton CompSciRB;
    private javax.swing.JPanel CorePNL;
    private javax.swing.JTextArea DisplayTA;
    private javax.swing.JTextArea DisplayTA1;
    private javax.swing.JLabel LogoLBL;
    private javax.swing.JLabel LogoLBL1;
    private javax.swing.ButtonGroup MainBG;
    private javax.swing.JRadioButton MathematicsRB;
    private javax.swing.JRadioButton MathsRB;
    private javax.swing.JButton MenuBTN;
    private javax.swing.JButton MenuBTN1;
    private javax.swing.JRadioButton PhysicsRB;
    private javax.swing.JPanel QuizSubjectsPNL;
    private javax.swing.JPanel QuizSubjectsPNL1;
    private javax.swing.JLabel SelectAnsLBL;
    private javax.swing.JLabel SelectAnsLBL1;
    private javax.swing.ButtonGroup SideBG;
    private javax.swing.JPanel SidePNL;
    private javax.swing.JTabbedPane TabsPNL;
    private javax.swing.JLabel TimerLBL;
    private javax.swing.JLabel TimerLBL1;
    private javax.swing.JTextArea TimerTA;
    private javax.swing.JTextArea TimerTA1;
    private javax.swing.JLabel TitleLBL;
    private javax.swing.JLabel TitleLBL1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
