import java.io.File;
import java.security.NoSuchAlgorithmException;

/**
 * Represents interface for Calculator of MD5 hash sum of file
 */
public interface ICheckSumCalculator {

  /**
   *
   * @param file - the file or folder to calculate md5.
   * @return md5 hash
   */
  byte[] getMD5(File file);
}
