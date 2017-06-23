/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsattacker.core.protocol.parser;

import de.rub.nds.modifiablevariable.util.ArrayConverter;
import de.rub.nds.tlsattacker.core.constants.HandshakeMessageType;
import de.rub.nds.tlsattacker.core.constants.ProtocolVersion;
import de.rub.nds.tlsattacker.core.protocol.message.CertificateRequestMessage;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Robert Merget - robert.merget@rub.de
 */
@RunWith(Parameterized.class)
public class CertificateRequestMessageParserTest {

    @Parameterized.Parameters
    public static Collection<Object[]> generateData() {
        return Arrays
                .asList(new Object[][] { {
                        ArrayConverter
                                .hexStringToByteArray("0d00002603010240001e0601060206030501050205030401040204030301030203030201020202030000"),
                        0,
                        ArrayConverter
                                .hexStringToByteArray("0d00002603010240001e0601060206030501050205030401040204030301030203030201020202030000"),
                        HandshakeMessageType.CERTIFICATE_REQUEST,
                        38,
                        3,
                        ArrayConverter.hexStringToByteArray("010240"),
                        30,
                        ArrayConverter
                                .hexStringToByteArray("060106020603050105020503040104020403030103020303020102020203"),
                        0, null }, });
    }

    private byte[] message;

    private HandshakeMessageType type;
    private int length;

    private int certTypesCount;
    private byte[] certTypes;
    private int sigHashAlgsLength;
    private byte[] sigHashAlgs;
    private int distinguishedNamesLength;
    private byte[] disitinguishedNames;

    public CertificateRequestMessageParserTest(byte[] message, int start, byte[] expectedPart,
            HandshakeMessageType type, int length, int certTypesCount, byte[] certTypes, int sigHashAlgsLength,
            byte[] sigHashAlgs, int distinguishedNamesLength, byte[] disitinguishedNames) {
        this.message = message;
        this.type = type;
        this.length = length;
        this.certTypesCount = certTypesCount;
        this.certTypes = certTypes;
        this.sigHashAlgsLength = sigHashAlgsLength;
        this.sigHashAlgs = sigHashAlgs;
        this.distinguishedNamesLength = distinguishedNamesLength;
        this.disitinguishedNames = disitinguishedNames;

    }

    /**
     * Test of parse method, of class CertificateRequestMessageParser.
     */
    @Test
    public void testParse() {
        CertificateRequestMessageParser parser = new CertificateRequestMessageParser(0, message, ProtocolVersion.TLS12);
        CertificateRequestMessage msg = parser.parse();
        assertArrayEquals(message, msg.getCompleteResultingMessage().getValue());
        assertTrue(msg.getLength().getValue() == length);
        assertTrue(msg.getType().getValue() == type.getValue());
        assertTrue(msg.getClientCertificateTypesCount().getValue() == certTypesCount);
        assertArrayEquals(certTypes, msg.getClientCertificateTypes().getValue());
        assertTrue(msg.getSignatureHashAlgorithmsLength().getValue() == sigHashAlgsLength);
        assertArrayEquals(sigHashAlgs, msg.getSignatureHashAlgorithms().getValue());
        assertTrue(msg.getDistinguishedNamesLength().getValue() == distinguishedNamesLength);
        if (distinguishedNamesLength == 0) {
            assertNull(msg.getDistinguishedNames());
        } else {
            assertArrayEquals(disitinguishedNames, msg.getDistinguishedNames().getValue());
        }
    }

}
