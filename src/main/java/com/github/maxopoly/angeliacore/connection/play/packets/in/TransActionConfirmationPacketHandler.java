package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.ItemTransactionManager.State;
import com.github.maxopoly.angeliacore.connection.play.packets.out.ApologizeTransactionPacket;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;
import java.io.IOException;

public class TransActionConfirmationPacketHandler extends AbstractIncomingPacketHandler {

	public TransActionConfirmationPacketHandler(ServerConnection connection) {
		super(connection, 0x11);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			byte windowID = packet.readUnsignedByte();
			short transActionID = packet.readSignedShort();
			boolean accepted = packet.readBoolean();
			connection.getItemTransActionManager().setState(transActionID, accepted ? State.ACCEPTED : State.DENIED);
			if (!accepted) {
				// the client needs to apologize after sending a wrong transaction
				connection.sendPacket(new ApologizeTransactionPacket(windowID, transActionID));
			}
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to handle transaction confirmation packet", e);
		} catch (IOException e) {
			connection.getLogger().error("Failed to reply with apologizing packet for failed transaction", e);
		}
	}
}
