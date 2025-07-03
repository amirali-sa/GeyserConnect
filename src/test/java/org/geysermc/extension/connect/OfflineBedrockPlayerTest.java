package org.geysermc.extension.connect;

import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.geysermc.geyser.auth.AuthData; // Import for AuthData
import org.cloudburstmc.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.geysermc.extension.connect.config.Config;
import org.geysermc.geyser.api.network.AuthType;
import org.geysermc.geyser.connection.GeyserConnection; // Assuming GeyserSession extends/implements this
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.text.GeyserLocale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfflineBedrockPlayerTest {

    @Mock
    private GeyserConnect geyserConnect;

    @Mock
    private GeyserImpl geyser; // Mock GeyserImpl for GeyserSession constructor

    @Mock
    private GeyserSession session;

    @Mock
    private BedrockPacketHandler originalPacketHandler;

    @Mock
    private Config config;

    @Mock
    private Logger logger;

    private PacketHandler packetHandler;

    @BeforeEach
    void setUp() {
        // Mock methods on GeyserConnect
        when(geyserConnect.config()).thenReturn(config);
        when(geyserConnect.logger()).thenReturn(logger);

        // Mock methods on GeyserSession that might be called by PacketHandler
        // It's important that session.getGeyser() returns our mocked GeyserImpl
        lenient().when(session.getGeyser()).thenReturn(geyser);
        // Used by Utils.displayName(session) which is used in log messages
        lenient().when(session.name()).thenReturn("TestPlayer");


        // Instantiate the class under test
        // We pass 'session' directly, assuming PacketHandler doesn't create it.
        packetHandler = new PacketHandler(geyserConnect, session, originalPacketHandler);
    }

    @Test
    void whenOfflinePlayerAllowed_AndPlayerIsOffline_ThenProceeds() {
        // Arrange
        AuthData mockAuthData = mock(AuthData.class);
        when(mockAuthData.authType()).thenReturn(AuthType.OFFLINE);
        when(session.getAuthData()).thenReturn(mockAuthData);
        when(config.allowOfflineBedrockPlayers()).thenReturn(true);

        SetLocalPlayerAsInitializedPacket packet = new SetLocalPlayerAsInitializedPacket();
        packet.setRuntimeEntityId(1L); // Example value

        // Act
        PacketSignal signal = packetHandler.handle(packet);

        // Assert
        verify(session, never()).disconnect(anyString());
        // We expect the handler to return HANDLED for other reasons (like vhost or UI)
        // So, not disconnecting is the key here.
        // Depending on further logic in PacketHandler, this might need adjustment.
        // For now, we assert that it didn't get kicked for being offline.
        assertEquals(PacketSignal.HANDLED, signal); // Default return if not disconnected early
         verify(logger).debug("Allowing offline Bedrock player TestPlayer as allow-offline-bedrock-players is true.");
    }

    @Test
    void whenOfflinePlayerNotAllowed_AndPlayerIsOffline_ThenDisconnects() {
        // Arrange
        AuthData mockAuthData = mock(AuthData.class);
        when(mockAuthData.authType()).thenReturn(AuthType.OFFLINE);
        when(session.getAuthData()).thenReturn(mockAuthData);
        when(config.allowOfflineBedrockPlayers()).thenReturn(false);
        // Mock session.disconnect to prevent NPE if it tries to use other unmocked parts
        doNothing().when(session).disconnect(anyString());


        SetLocalPlayerAsInitializedPacket packet = new SetLocalPlayerAsInitializedPacket();
        packet.setRuntimeEntityId(1L);

        // Act
        PacketSignal signal = packetHandler.handle(packet);

        // Assert
        verify(session).disconnect("Offline Bedrock players are not permitted by this server.");
        assertEquals(PacketSignal.HANDLED, signal); // Should return HANDLED after disconnecting
-        verify(logger).info("Disconnecting offline Bedrock player TestPlayer as allow-offline-bedrock-players is false.");
    }

    @Test
    void whenOfflinePlayerNotAllowed_AndPlayerIsOnline_ThenProceeds() {
        // Arrange
        AuthData mockAuthData = mock(AuthData.class);
        when(mockAuthData.authType()).thenReturn(AuthType.ONLINE);
        when(session.getAuthData()).thenReturn(mockAuthData);
        when(config.allowOfflineBedrockPlayers()).thenReturn(false); // This config should not affect online players

        SetLocalPlayerAsInitializedPacket packet = new SetLocalPlayerAsInitializedPacket();
        packet.setRuntimeEntityId(1L);

        // Act
        PacketSignal signal = packetHandler.handle(packet);

        // Assert
        verify(session, never()).disconnect(anyString());
        assertEquals(PacketSignal.HANDLED, signal); // Default return
    }

    @Test
    void whenOfflinePlayerAllowed_AndPlayerIsOnline_ThenProceeds() {
        // Arrange
        AuthData mockAuthData = mock(AuthData.class);
        when(mockAuthData.authType()).thenReturn(AuthType.ONLINE);
        when(session.getAuthData()).thenReturn(mockAuthData);
        when(config.allowOfflineBedrockPlayers()).thenReturn(true); // This config should not affect online players

        SetLocalPlayerAsInitializedPacket packet = new SetLocalPlayerAsInitializedPacket();
        packet.setRuntimeEntityId(1L);

        // Act
        PacketSignal signal = packetHandler.handle(packet);

        // Assert
        verify(session, never()).disconnect(anyString());
        assertEquals(PacketSignal.HANDLED, signal); // Default return
    }
}
