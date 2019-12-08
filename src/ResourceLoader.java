

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

public class ResourceLoader {
	static final ClassLoader cl = ResourceLoader.class.getClassLoader();


	public static List<String> getTable(String path) {
		try {
			InputStream in = cl.getResourceAsStream("tables/" + path);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			return reader.lines().collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Failed to read: " + path);
			e.printStackTrace();
			// represents empty file if there was an error loading.
			return new ArrayList<String>();
		}
	}
}
