package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.world.ChunkLoadEvent;
import com.github.maxopoly.angeliacore.libs.nbt.NBTCompound;
import com.github.maxopoly.angeliacore.libs.packetEncoding.BinaryReadOnlyData;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.block.BlockStateFactory;
import com.github.maxopoly.angeliacore.model.block.Chunk;
import com.github.maxopoly.angeliacore.model.block.ChunkSection;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;

public class ChunkDataPacketHandler extends AbstractIncomingPacketHandler {

	private static final int FULL_SIZE_BITS_PER_BLOCK = 13;

	public ChunkDataPacketHandler(ServerConnection connection) {
		super(connection, 0x20);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		if (!connection.getChunkHolder().isHoldingModel()) {
			return;
		}
		try {
			int x = packet.readInt();
			int z = packet.readInt();
			boolean groundUpContinuos = packet.readBoolean();
			int containedSectionBitMask = packet.readVarInt();
			byte[] rawChunkData = packet.readByteArray();
			@SuppressWarnings("unused")
			NBTCompound[] blockEntityData = packet.readNBTArray();
			BinaryReadOnlyData brod = new BinaryReadOnlyData(rawChunkData);
			Chunk chunk = readChunk(x, z, groundUpContinuos, containedSectionBitMask, brod);
			connection.getEventHandler().broadcast(new ChunkLoadEvent(chunk));
			connection.getChunkHolder().putChunk(chunk);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to read chunk data packet", e);
		}

	}

	private Chunk readChunk(int chunkX, int chunkZ, boolean groundUpContinuos, int bitMask, BinaryReadOnlyData data)
			throws EndOfPacketException {
		Chunk chunk = new Chunk(chunkX, chunkZ);
		// This methods logic was mostly taken from the protocol wiki, thanks to whoever
		// wrote it there
		for (int sectionY = 0; sectionY < Chunk.SECTIONS_PER_CHUNK; sectionY++) {
			if (!((bitMask & (1 << sectionY)) != 0)) { // Is the given bit set in the mask?
				continue;
			}
			byte bitsPerBlock = data.readByte();

			// these checks are not needed if the server isn't broken, but let's make sure
			if (bitsPerBlock < 4) {
				bitsPerBlock = 4;
			}
			if (bitsPerBlock > 8) {
				bitsPerBlock = FULL_SIZE_BITS_PER_BLOCK; // 13, currently, but liable to eventually change
			}

			boolean usePalette = bitsPerBlock <= 8;

			int[] palette = null;
			if (usePalette) {
				int numPaletteEntries = data.readVarInt();
				palette = new int[numPaletteEntries];
				for (int i = 0; i < numPaletteEntries; i++) {
					int blockData = data.readVarInt();
					palette[i] = blockData;
				}
			} else {
				// should always be 0
				int buff = data.readVarInt();
				if (buff != 0) {
					connection.getLogger().warn("Weird chunk data found, no palette used, but buffer byte was not 0");
				}
			}

			// A bitmask that contains bitsPerBlock set bits
			int individualValueMask = (1 << bitsPerBlock) - 1;

			int dataArrayLength = data.readVarInt();
			long[] dataArray = new long[dataArrayLength];
			for (int i = 0; i < dataArrayLength; i++) {
				dataArray[i] = data.readLong();
			}
			BlockState[] blocks = new BlockState[Chunk.CHUNK_WIDTH * Chunk.CHUNK_WIDTH * Chunk.SECTION_HEIGHT];
			int read = 0;
			for (int y = 0; y < Chunk.SECTION_HEIGHT; y++) {
				for (int z = 0; z < Chunk.CHUNK_WIDTH; z++) {
					for (int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
						read++;
						int blockNumber = (((y * Chunk.CHUNK_WIDTH) + z) * Chunk.CHUNK_WIDTH) + x;
						int startLong = (blockNumber * bitsPerBlock) / 64;
						int startOffset = (blockNumber * bitsPerBlock) % 64;
						int endLong = ((blockNumber + 1) * bitsPerBlock - 1) / 64;

						int blockData;
						if (startLong == endLong) {
							blockData = (int) (dataArray[startLong] >> startOffset);
						} else {
							int endOffset = 64 - startOffset;
							blockData = (int) (((dataArray[startLong] >> startOffset) & (1 << endOffset) - 1)
									| (dataArray[endLong] << endOffset));
						}
						blockData &= individualValueMask;

						if (usePalette) {
							if (blockData < palette.length) {
								blockData = palette[blockData];
							} else {
								connection.getLogger().info("Data outside of palette; ID: " + blockData + " Length: "
										+ palette.length + " index: " + blockNumber + "  " + read);
							}
						}
						

						BlockState state = BlockStateFactory.getStateByData(blockData);
						blocks[blockNumber] = state;

					}
				}
			}
			

			for (int y = 0; y < Chunk.SECTION_HEIGHT; y++) {
				for (int z = 0; z < Chunk.CHUNK_WIDTH; z++) {
					for (int x = 0; x < Chunk.CHUNK_WIDTH; x += 2) {
						// Note: x += 2 above; we read 2 values along x each time
						@SuppressWarnings("unused")
						byte value = data.readByte();

						// section.SetBlockLight(x, y, z, value & 0xF);
						// section.SetBlockLight(x + 1, y, z, (value >> 4) & 0xF);
					}
				}
			}

			// if (currentDimension.HasSkylight()) { // IE, current dimension is overworld /
			// 0
			for (int y = 0; y < Chunk.SECTION_HEIGHT; y++) {
				for (int z = 0; z < Chunk.CHUNK_WIDTH; z++) {
					for (int x = 0; x < Chunk.CHUNK_WIDTH; x += 2) {
						// Note: x += 2 above; we read 2 values along x each time
						@SuppressWarnings("unused")
						byte value = data.readByte();

						// section.SetSkyLight(x, y, z, value & 0xF);
						// section.SetSkyLight(x + 1, y, z, (value >> 4) & 0xF);
					}
				}
			}
			chunk.setSection(new ChunkSection(blocks), sectionY);
		}

		for (int z = 0; z < Chunk.CHUNK_WIDTH; z++) {
			for (int x = 0; x < Chunk.CHUNK_WIDTH; x++) {
				data.readByte();
				// chunk.SetBiome(x, z, data.readByte());
			}
		}
		return chunk;
	}
}
