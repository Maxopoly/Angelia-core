package nbt;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.nbt.NBTByte;
import com.github.maxopoly.angeliacore.libs.nbt.NBTCompound;
import com.github.maxopoly.angeliacore.libs.nbt.NBTInt;
import com.github.maxopoly.angeliacore.libs.nbt.NBTList;
import com.github.maxopoly.angeliacore.libs.nbt.NBTLong;
import com.github.maxopoly.angeliacore.libs.nbt.NBTParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import org.junit.Test;

public class NBTTest {

	@Test
  public void simpleNumberTest() {
      NBTCompound comp = getTestingComp();
      try {
				assertTrue(doubleConversion(comp));
			} catch (EndOfPacketException e) {
				fail(e.getMessage());
				e.printStackTrace();
			}
	}

	@Test
	public void listTest() {
		NBTCompound comp = new NBTCompound("");
		NBTList<NBTInt> list = new NBTList<NBTInt>("list", NBTInt.ID);
		for(int i = 0; i < 100; i++) {
			list.add(new NBTInt("" + i, i));
		}
		comp.add(list);
		 try {
				assertTrue(doubleConversion(comp));
			} catch (EndOfPacketException e) {
				fail(e.getMessage());
				e.printStackTrace();
			}
	}

	@Test
	public void nestingTest() {
		NBTCompound comp = getTestingComp();
		NBTCompound comp2 = getTestingComp();
		NBTCompound comp3 = getTestingComp();
		NBTCompound comp4 = getTestingComp();
		//this is intentionally stacking into itself
		comp.add(comp2);
		comp.add(comp3);
		comp2.add(comp3);
		comp3.add(comp4);
		comp2.add(comp4);
		 try {
				assertTrue(doubleConversion(comp));
			} catch (EndOfPacketException e) {
				fail(e.getMessage());
				e.printStackTrace();
			}
	}

	@Test
	public void emptyListTest() {
		NBTCompound comp = getTestingComp();
		NBTList<NBTInt> list = new NBTList<NBTInt>("list", NBTInt.ID);
		comp.add(list);
		 try {
				assertTrue(doubleConversion(comp));
			} catch (EndOfPacketException e) {
				fail(e.getMessage());
				e.printStackTrace();
			}
	}

	private boolean doubleConversion(NBTCompound comp) throws EndOfPacketException {
		return comp.equals(new NBTParser(comp.serialize()).parse());
	}

	private NBTCompound getTestingComp() {
    NBTCompound comp = new NBTCompound("wrapper");
    comp.add(new NBTByte("byte", (byte) 124));
    comp.add(new NBTByte("bytetwo", (byte) 1224));
    comp.add(new NBTInt("integer", 12224));
    comp.add(new NBTLong("long", 23423343434L));
    comp.add(new NBTLong("long2", 23423343434L));
    comp.add(new NBTLong("long3", 23423343434L));
    return comp;
	}

	private byte [] loadFile(String name) {
		URL url = this.getClass().getResource("/" + name);
		File file = new File(url.getFile());
		try {
			FileInputStream fis = new FileInputStream(file);
			byte fileContent[] = new byte[(int)file.length()];
			fis.read(fileContent);
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
