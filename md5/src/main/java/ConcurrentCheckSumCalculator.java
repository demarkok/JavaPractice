import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Represents the concurrent version of check sum calculator
 */
public class ConcurrentCheckSumCalculator implements ICheckSumCalculator {

    private final ForkJoinPool fjp = new ForkJoinPool(10);

    /**
     *
     * @param file - the file or folder to calculate md5.
     * @return md5 hash
     */
    @Override
    public byte[] getMD5(File file) {
        return fjp.invoke(new SumCalculatorTask(file));
    }

    private class SumCalculatorTask extends RecursiveTask <byte[]> {

        private File file;

        public SumCalculatorTask(File file) {
            this.file = file;
        }

        @Override
        protected byte[] compute() {

            MessageDigest md = null;

            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if (file.isFile()) {
                try (InputStream is = new FileInputStream(file);
                    DigestInputStream dis = new DigestInputStream(is, md)) {
                    while (dis.available() > 0) {
                        dis.read();
                    }

                } catch (Exception e) {
                }
                return md.digest();
            } else {
                try {
                    md.update(file.getName().getBytes("UTF8"));
                } catch (UnsupportedEncodingException e) {}

                ArrayList<SumCalculatorTask> subtasks = new ArrayList<>();

                for (File subFile : file.listFiles()) {
                    SumCalculatorTask subtask = new SumCalculatorTask(subFile);
                    subtask.fork();
                    subtasks.add(subtask);
                }
                for (SumCalculatorTask subtask: subtasks) {
                    md.update(subtask.compute());
                }
            }
            return md.digest();
        }
    }
}
