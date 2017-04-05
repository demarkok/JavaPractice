import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents the non-concurrent version of check sum calculator
 */
public class NonConcurrentCheckSumCalculator implements ICheckSumCalculator {

  /**
   *
   * @param file - the file or folder to calculate md5.
   * @return md5 hash
   */
  public byte[] getMD5(File file){

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
    } else {
      try {
        md.update(file.getName().getBytes("UTF8"));
      } catch (UnsupportedEncodingException e) {}

      for (File subFile : file.listFiles()) {
        md.update(getMD5(subFile));
      }
    }
    return md.digest();
  }
}
