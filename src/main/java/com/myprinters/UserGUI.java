package com.myprinters;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

public class UserGUI extends JFrame implements PrinterInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGUI.class);

    private JLabel label;
    private JPanel answers;
    private int answerId = -1;

    private String previousQuestion, previousAnswer, firstAnswer;

    public UserGUI() {
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setTitle("MyPrinters");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 24, 24));
        label = new JLabel("Proszę czekać...");
        answers = new JPanel();
        add(label);
        add(answers);
        pack();
        try {
            new DroolsThread(this).start();
        } catch (Throwable t) {
            LOGGER.error("Could not start the Drools thread", t);
        }
    }

    @Override
    public int getOption() {
        return answerId;
    }

    @Override
    public void showQuestion(String question) {
        log("CurrentQuestion(" + question + ")");
        showData(() -> label.setText(question));
    }

    @Override
    public void showOptions(String[] options) {
        log("CurrentAnswers(" + String.join(",", options) + ")");
        showData(() -> {
            if (options.length != 0) {
                answers.setLayout(new GridLayout(options.length, 1));
                for (int i = 0; i < options.length; i++) {
                    final int id = i;
                    final JButton button = new JButton(options[id]);
                    button.addActionListener(e -> answerId = id);
                    answers.add(button);
                }
            }
        });
    }

    @Override
    public void showDecision(String decision) {
        log("FinalDecision(" + decision + ")");
        showData(() -> label.setText(decision));
    }

    private void showData(Runnable action) {
        answerId = -1;
        answers.removeAll();
        answers.setLayout(new FlowLayout());
        action.run();
        pack();
    }

    public void log(String message) {
        LOGGER.info(message);
    }

    private static class DroolsThread extends Thread {

        KieSession kieSession;
        KieServices kieServices;
        KieContainer kContainer;

        public DroolsThread(PrinterInterface printerface) {
            kieServices = KieServices.Factory.get();
            kContainer = kieServices.getKieClasspathContainer();
            kieSession = kContainer.newKieSession("ksession-rules");
            kieSession.setGlobal("printerface", printerface);
        }

        public void run() {
            kieSession.fireAllRules();
        }
    }

    @Override
    public void setPreviousQuestion(String question) {
        previousQuestion = question;
    }

    @Override
    public String getPreviousQuestion() {
        return previousQuestion;
    }

    @Override
    public void setPreviousAnswer(String answer) {
        previousAnswer = answer;
    }

    @Override
    public String getPreviousAnswer() {
        return previousAnswer;
    }

    @Override
    public void setFirstAnswer(String answer) {
        firstAnswer = answer;
    }

    @Override
    public String getFirstAnswer() {
        return firstAnswer;
    }

}