import java.io.File;
import java.nio.file.Path;

/**
 * Created by demarkok on 30-Mar-17.
 */
public class CLI {

    public static void main(String[] args) {
        ICheckSumCalculator c1 = new NonConcurrentCheckSumCalculator();
        ICheckSumCalculator c2 = new ConcurrentCheckSumCalculator();
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println("No such File");
            return;
        }

        try {
            long start = System.nanoTime();
            c1.getMD5(file);
            System.out.printf("non-concurrent: %d\n", System.nanoTime() - start);

            start = System.nanoTime();
            c2.getMD5(file);
            System.out.printf("concurrent: %d\n", System.nanoTime() - start);

        } catch (Exception e) {
            System.out.println("Unexpected error");
            return;
        }

    }


}
