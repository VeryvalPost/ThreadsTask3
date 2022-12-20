import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class Main {
    final static int LENGTH_TEXT = 100_000;
    final static int STRING_TEXT = 10_000;

    public static void main(String[] args) {

        BlockingQueue<String> generatedQueueA = new ArrayBlockingQueue(100);
        BlockingQueue<String> generatedQueueB = new ArrayBlockingQueue(100);
        BlockingQueue<String> generatedQueueC = new ArrayBlockingQueue(100);

        String[] texts = new String[STRING_TEXT];

        new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                texts[i] = generateText("abc", LENGTH_TEXT);
                try {
                    generatedQueueA.put(texts[i]);
                    generatedQueueB.put(texts[i]);
                    generatedQueueC.put(texts[i]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            }).start();

        new Thread(() -> {
            try {
                System.out.println("Символов A = "+ countLetter(generatedQueueA,'a'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Символов B = "+ countLetter(generatedQueueB,'b'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();


        new Thread(() -> {
            try {
                System.out.println("Символов C = "+ countLetter(generatedQueueB,'c'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }



    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countLetter (BlockingQueue queue, char letter) throws InterruptedException {
        String text = (String) queue.take();
        char[] stringMass = text.toCharArray();
        int countA = 0;
        int countB = 0;
        int countC = 0;
        for (int i=0; i<STRING_TEXT;i++){
        for (char r : stringMass) {
            if (r == 'a') {
                countA++;
            } else if (r == 'b') {
                countB++;
            } else if (r == 'c') {
                countC++;
            }
        }
        }

        if (letter=='a') return countA;
        else if (letter=='b') return countB;
        else if (letter=='c') return countC;
        else return 0;
    }

}