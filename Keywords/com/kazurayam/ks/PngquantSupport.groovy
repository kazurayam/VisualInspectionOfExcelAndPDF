package com.kazurayam.ks

import java.nio.file.Files
import java.nio.file.Path

import com.kazurayam.subprocessj.CommandLocator
import com.kazurayam.subprocessj.Subprocess

public class PngquantSupport {

	public PngquantSupport() {}

	public File compress(String png) {
		return compress(new File(png))
	}

	public File compress(File png) {
		Path p = compress(png.toPath())
		return p.toFile()
	}

	public Path compress(Path png) {
		Objects.requireNonNull(png)
		assert Files.exists(png)
		CommandLocator.CommandLocatingResult cfr = CommandLocator.find("pngquant")
		if (cfr.returncode() == 0) {
			String commandPath = cfr.command()
			Subprocess.CompletedProcess cp;
			try {
				cp = new Subprocess().run(Arrays.asList(
						commandPath,
						"--ext", ".png", "--force", "--speed", "1",
						png.toString()));
			} catch (IOException e) {
				throw new RuntimeException(e)
			} catch (InterruptedException e) {
				throw new RuntimeException(e)
			}
			cp.stdout().forEach({println(it)})
			cp.stderr().forEach({println(it)})
			assert 0 == cp.returncode()
		}
		return png
	}
}
