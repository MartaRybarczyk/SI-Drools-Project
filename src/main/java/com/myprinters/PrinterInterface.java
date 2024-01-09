package com.myprinters;

import java.util.List;

public interface PrinterInterface {

    int getOption();

    void showQuestion(String question);

    void showOptions(String[] options);

    void showDecision(String decision);

    void setPreviousQuestion(String question);

    String getPreviousQuestion();

    void setPreviousAnswer(String answer);

    String getPreviousAnswer();

    void setFirstAnswer(String answer);

    String getFirstAnswer();

    void log(String message);
}
