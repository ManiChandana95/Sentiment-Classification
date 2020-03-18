/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ieee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Praveen
 */
public class SentimentalCalculation {

    public Map getSentimenatlMap(List commentsList) {
        Map deatilsMap = new HashMap();
        List<String> posWords = new ArrayList<String>();
        List<String> negWords = new ArrayList<String>();
        List<String> negaingWords = new ArrayList<String>();
        int positive = 0;
        int negative = 0;
        int neutral = 0;
        double posPercentage = 0;
        double neuPercentage = 0;
        int star = 0;
        String strPosWords = "";
        String strNegWords = "";
        String strNegatingWords = "";
        try {
            Connection con = DbConnection.getConnections();
            String q = "select * from sentiments";
            PreparedStatement pst = con.prepareStatement(q);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                strPosWords = rs.getString(1);
                strNegWords = rs.getString(2);
                strNegatingWords = rs.getString(3);
            }

            posWords = Arrays.asList(strPosWords.split(","));
            negWords = Arrays.asList(strNegWords.split(","));
            negaingWords = Arrays.asList(strNegatingWords.split(","));

            if (commentsList.size() > 0) {
                for (int i = 0; i < commentsList.size(); i++) {
                    String comment = (String) commentsList.get(i);
                    int score = getSentimentScore(comment, posWords, negWords, negaingWords);
                    if (score == 1) {
                        positive++;
                    } else if (score == -1) {
                        negative++;
                    } else {
                        neutral++;
                    }
                }
                posPercentage = positive * 100 / commentsList.size();

                if (posPercentage < 20) {
                    star = 1;
                } else if (posPercentage > 20 && posPercentage < 40) {
                    star = 2;
                } else if (posPercentage > 40 && posPercentage < 60) {
                    star = 3;
                } else if (posPercentage > 60 && posPercentage < 80) {
                    star = 4;
                } else if (posPercentage > 80 && posPercentage <= 100) {
                    star = 5;
                }
            }

            deatilsMap.put("positiveCount", positive);
            deatilsMap.put("negativeCount", negative);
            deatilsMap.put("neutraleCount", neutral);
            deatilsMap.put("starCount", star);

        } catch (SQLException ex) {
            Logger.getLogger(SentimentalCalculation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return deatilsMap;
    }

    private int getSentimentScore(String input, List<String> posWords, List<String> negWords, List<String> negaingWords) {
// normalize!
        input = input.toLowerCase();
        input = input.trim();
// remove all non alpha-numeric non whitespace chars
        input = input.replaceAll("[^a-zA-Z0-9\\s.]", "");

        int negCounter = 0;
        int posCounter = 0;

// so what we got?
        String[] words = input.split(" ");

// check if the current word appears in our reference lists...
        String previousWord = "";
        for (int i = 0; i < words.length; i++) {
            if (posWords.contains(words[i])) {
                if (negaingWords.contains(previousWord)) {
                    negCounter++;
                } else {
                    posCounter++;
                }
            }
            if (negWords.contains(words[i])) {
                if (negaingWords.contains(previousWord)) {
                    posCounter++;
                } else {
                    negCounter++;
                }
            }
            previousWord = words[i];
        }

// positive matches MINUS negative matches
        int result = (posCounter - negCounter);

// negative?
        if (result < 0) {
            return -1;
// or positive?
        } else if (result > 0) {
            return 1;
        }

// neutral to the rescue!
        return 0;
    }
}
