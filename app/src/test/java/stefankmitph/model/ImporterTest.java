package stefankmitph.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by KumpitschS on 07.04.2015.
 */
@Config(manifest="./src/main/AndroidManifest.xml", emulateSdk = 19)
@RunWith(RobolectricTestRunner.class)
public class ImporterTest {

    @Test
    public void testRead() throws Exception {
        final String file = "C:/Users/KumpitschS/repo/tools/java/misc/nestle";
        FileInputStream fileInputStream = new FileInputStream(file);
        Importer importer = new Importer();
        importer.read(fileInputStream);
    }
}