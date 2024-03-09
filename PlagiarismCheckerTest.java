import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class PlagiarismCheckerTest {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java PlagiarismChecker [原文文件] [抄袭版论文文件] [答案文件]");
            return;
        }

        String origPath = args[0];
        String plagiarizedPath = args[1];
        String answerPath = args[2];

        try {
            String origText = readFile(origPath);
            String plagiarizedText = readFile(plagiarizedPath);

            // 计算各种相似度度量
            double editDistance = calculateEditDistance(origText, plagiarizedText);
            double jaccardSimilarity = calculateJaccardSimilarity(origText, plagiarizedText);
            double cosineSimilarity = calculateCosineSimilarity(origText, plagiarizedText);
            double longestCommonSubsequence = calculateLongestCommonSubsequence(origText, plagiarizedText);

            double averageSimilarity = (editDistance + jaccardSimilarity + cosineSimilarity + longestCommonSubsequence) / 4.0;

            writeResult(answerPath, String.format("%.2f", averageSimilarity));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String filePath) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        Scanner scanner = new Scanner(new File(filePath));
        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return content.toString();
    }


    //编辑距离
    private static double calculateEditDistance(String text1, String text2) {
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

    //Jaccard 相似度
    private static double calculateJaccardSimilarity(String text1, String text2) {
        String[] text1Array = text1.split("\\s+");
        String[] text2Array = text2.split("\\s+");

        double intersection = 0.0;
        double union = text1Array.length + text2Array.length;

        // 计算交集和并集
        for (String word : text1Array) {
            for (String word2 : text2Array) {
                if (word.equals(word2)) {
                    intersection++;
                    break;
                }
            }
        }

        // 返回 Jaccard 相似度
        return  intersection / (union - intersection);
    }


    //余弦相似度
    private static double calculateCosineSimilarity(String text1, String text2) {
        String[] text1Array = text1.split("\\s+");
        String[] text2Array = text2.split("\\s+");

        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        // 计算向量的点积以及各自的长度平方和
        for (int i = 0; i < Math.max(text1Array.length, text2Array.length); i++) {
            if (i < text1Array.length) {
                magnitude1 += Math.pow(1, 2);
            }
            if (i < text2Array.length) {
                magnitude2 += Math.pow(1, 2);
            }
            if (i < text1Array.length && i < text2Array.length) {
                dotProduct += 1;
            }
        }

        // 返回余弦相似度
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }

    //最长公共子序列
    private static double calculateLongestCommonSubsequence(String text1, String text2) {
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
        return 1 - (double) dp[text1.length()][text2.length()] / Math.max(text1.length(), text2.length());
    }

    private static void writeResult(String filePath, String result) {
        try {
            PrintWriter writer = new PrintWriter(filePath);
            writer.println(result);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


