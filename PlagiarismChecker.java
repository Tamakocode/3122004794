import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class PlagiarismChecker {


    //最长公共子序列
    private static double calculateLongestCommonSubsequence(String originalFile, String plagiarizedFile) throws FileNotFoundException {
        String text1 = readFile(originalFile);
        String text2 = readFile(plagiarizedFile);
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];

        // 动态规划计算最长公共子序列的长度
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 返回归一化的最长公共子序列长度
        return (double) dp[text1.length()][text2.length()] / Math.max(text1.length(), text2.length());
    }




    //编辑距离
    private static double calculateEditDistance(String originalFile, String plagiarizedFile) throws FileNotFoundException {
        String text1 = readFile(originalFile);
        String text2 = readFile(plagiarizedFile);

        int[][] dp = new int[text1.length() + 1][text2.length() + 1];

        // 初始化编辑距离矩阵的第一行和第一列
        for (int i = 0; i <= text1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= text2.length(); j++) {
            dp[0][j] = j;
        }

        // 动态规划计算编辑距离
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
                }
            }
        }

        // 返回归一化的编辑距离
        return 1 - (double) dp[text1.length()][text2.length()] / Math.max(text1.length(), text2.length());
    }




    //将文件的数据读取到字符串
    private static String readFile(String filePath) throws FileNotFoundException {
        StringBuilder text = new StringBuilder();
        Scanner scanner = new Scanner(new File(filePath));

        while (scanner.hasNextLine()) {
            text.append(scanner.nextLine()).append(System.lineSeparator());
        }

        scanner.close();
        return text.toString();
    }




    private static void writeResult(String outputFile, double similarity, String originalFile, String plagiarizedFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile, true))) {
            writer.printf("Original File(原文件): ------------------%s%n", originalFile);
            writer.printf("Plagiarized File(抄袭文件): ------------------%s%n", plagiarizedFile);
            writer.printf("Similarity(相似度): ------------------%.2f%n", similarity);
            writer.printf("Date(检测日期): ------------------%s%n", getCurrentDate());
            writer.println();
            writer.println();
        }
    }

    private static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }


    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("无效参数输入，正确命令为：java 【PlagiarismChecker.java的绝对路径】 original.txt plagiarized.txt output.txt");
            return;
        }

        String originalFile = args[0];
        String plagiarizedFile = args[1];
        String outputFile = args[2];

        try {
            double similarity = (calculateLongestCommonSubsequence(originalFile, plagiarizedFile)+calculateEditDistance(originalFile, plagiarizedFile))/2;
            writeResult(outputFile, similarity, originalFile, plagiarizedFile);
            System.out.println("Similarity(相似度): " + similarity);
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }




}




