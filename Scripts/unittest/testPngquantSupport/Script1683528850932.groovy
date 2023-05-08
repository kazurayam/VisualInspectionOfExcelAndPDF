import com.kazurayam.ks.PngquantSupport
import org.apache.commons.io.FileUtils
import java.nio.file.Files

// prepare the fixture
File sourcePng = new File("src/test/fixtures/unittest/143e657b2aa82bd6044d5af5925868b773a23283.png")
long sourceLength = sourcePng.length()
File targetPng = new File("build/tmp/testPngquantSupport/out.png")
Files.createDirectories(targetPng.toPath().getParent())
FileUtils.copyFile(sourcePng, targetPng)

// compress a PNG file
File compressedPng = new PngquantSupport().compress(targetPng)

long compressedLength = compressedPng.length()
double delta = ((sourceLength - compressedLength) * 100)/sourceLength

println "sourceLength=${sourceLength}, compressedLength=${compressedLength}, delta=${delta}"

