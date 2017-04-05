import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Created by demarkok on 30-Mar-17.
 */
public class ICheckSumCalculatorTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void simpleNonConcurrentTest() throws IOException, NoSuchAlgorithmException {
        File file1 = folder.newFile("test1");
        PrintWriter writer = new PrintWriter(file1, "UTF-8");
        writer.println("Hello world");

        File file2 = folder.newFile("test2");
        PrintWriter writer2 = new PrintWriter(file2, "UTF-8");
        writer2.println("Hello world");

        ICheckSumCalculator calculator = new NonConcurrentCheckSumCalculator();
        assertArrayEquals(calculator.getMD5(file1), calculator.getMD5(file2));
    }

    @Test
    public void simpleConcurrentTest() throws IOException, NoSuchAlgorithmException {

        File file1 = folder.newFile("test1");
        PrintWriter writer = new PrintWriter(file1, "UTF-8");
        writer.println("Hello world");

        File file2 = folder.newFile("test2");
        PrintWriter writer2 = new PrintWriter(file2, "UTF-8");
        writer2.println("Hello world");

        ICheckSumCalculator calculator = new ConcurrentCheckSumCalculator();
        assertArrayEquals(calculator.getMD5(file1), calculator.getMD5(file2));
    }

    @Test
    public void sameResultTest() throws IOException, NoSuchAlgorithmException {

        File file = folder.newFile("test1");
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        writer.println("Hello world");

        ICheckSumCalculator calculator1 = new ConcurrentCheckSumCalculator();
        ICheckSumCalculator calculator2 = new NonConcurrentCheckSumCalculator();

        assertArrayEquals(calculator1.getMD5(file), calculator2.getMD5(file));
    }
}