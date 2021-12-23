package _01_introAndTrustBoundaries;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathCanonicalization {

	/**
		Noncompliant Path Canonicalization Example
	*/
	public static void bad(String[] args) {
		File f = new File(System.getProperty("user.home") +
		System.getProperty("file.separator") + args[0]);
		String absPath = f.getAbsolutePath();
		if (!isInSecureDir(Paths.get(absPath))) {
			throw new IllegalArgumentException();
		}
		if (!validate(absPath)) {
			// Validation
			throw new IllegalArgumentException();
		}
	}

	/**
		Compliant Path Canonicalization Example
	*/	
	public static void good(String[] args)  throws Exception {
		File f = new File(System.getProperty("user.home") +
		System.getProperty("file.separator") + args[0]);
		String canonPath = f.getCanonicalPath();
		if (!isInSecureDir(Paths.get(canonPath))) {
			throw new IllegalArgumentException();
		}
		if (!validate(canonPath)) {
			// Validation
			throw new IllegalArgumentException();
		}
	}
	
	/* bogus, just to illustrate */
	static boolean isInSecureDir(Path p) {
		return p != null ? true: false;
	}
	/* bogus, just to illustrate */
	static boolean validate(String s) {
		return s != null ? true: false;
	}
	
	public static void main(String[] argv) throws Exception {
		bad(null);
		
		good(null);
	}
}
