package nbt;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.github.maxopoly.angeliacore.connection.CompressionManager;
import com.github.maxopoly.angeliacore.exceptions.MalformedCompressedDataException;
import com.github.maxopoly.angeliacore.libs.nbt.NBTByte;
import com.github.maxopoly.angeliacore.libs.nbt.NBTCompound;
import com.github.maxopoly.angeliacore.libs.nbt.NBTInt;
import com.github.maxopoly.angeliacore.libs.nbt.NBTList;
import com.github.maxopoly.angeliacore.libs.nbt.NBTLong;
import com.github.maxopoly.angeliacore.libs.nbt.NBTParser;
import com.github.maxopoly.angeliacore.libs.nbt.NBTString;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
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
		for (int i = 0; i < 100; i++) {
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
		// this is intentionally stacking into itself
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
		NBTList<NBTInt> list2 = new NBTList<NBTInt>("list2", NBTInt.ID);
		NBTList<NBTInt> list3 = new NBTList<NBTInt>("list3", NBTInt.ID);
		NBTList<NBTString> list4 = new NBTList<NBTString>("fillList",
				NBTString.ID);
		for (int i = 0; i < 200; i++) {
			list4.add(new NBTString("" + i, "def" + i));
		}
		comp.add(list);
		comp.add(list4);
		comp.add(list2);
		comp.add(list3);
		try {
			assertTrue(doubleConversion(comp));
		} catch (EndOfPacketException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	public void flatFileTests() {
		//all of these should load without exceptions
		new NBTParser(loadFile("hello_world.nbt")).toString();
		new NBTParser(loadGZipDecompressedFile("level.dat")).toString();
		new NBTParser(loadGZipDecompressedFile("bigtest.nbt")).toString();
	}

	private boolean doubleConversion(NBTCompound comp)
			throws EndOfPacketException {
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
	
	private byte[] loadGZipDecompressedFile(String file) {
		try {
			return CompressionManager.decompressGZip(loadFile(file));
		} catch (MalformedCompressedDataException e) {
			e.printStackTrace();
			return null;
		}
	}

	private byte[] loadFile(String name) {
		File file = new File("src/test/resources/" + name);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte fileContent[] = new byte[(int) file.length()];
			int toRead = fileContent.length;
			while (toRead > 0) {
				toRead -= fis.read(fileContent, fileContent.length - toRead, toRead);
			}
			fis.close();
			return fileContent;
		} catch (IOException e) {
			try {
				fis.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}
	}

}
