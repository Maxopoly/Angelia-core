package yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Test;

import com.github.maxopoly.angeliacore.libs.yaml.InvalidYamlFormatException;
import com.github.maxopoly.angeliacore.libs.yaml.ConfigSection;
import com.github.maxopoly.angeliacore.libs.yaml.YamlParser;

public class YamlTest {
	
	@Test
	public void loadingTest() {
		List <String> content = loadFile("test.yml");
		try {
			ConfigSection map = YamlParser.loadFromFile(new File("src/test/resources/test.yml"));
			System.out.println(map.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidYamlFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<String> loadFile(String fileName) {
		File f = new File("src/test/resources/" + fileName);
		try {
			return Files.readAllLines(f.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
