/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import constants.ServerConstants;

/**
 * Provides a class for encrypting MapleStory packets with AES OFB encryption.
 */
public class MapleAESOFB {

	private byte iv[];
	private Cipher cipher;
	private final short mapleVersion;

	public static enum EncryptionKey {
		V146((short) 146,
				new SecretKeySpec(new byte[] { (byte) 0x8B, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x24,
						(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8B, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x6D, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xB5, (byte) 0x00, (byte) 0x00,
						(byte) 0x00, (byte) 0xC6, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00,
						(byte) 0x00, (byte) 0x00, (byte) 0xB0, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
				"AES")), V145(
						(short) 145,
						new SecretKeySpec(new byte[] { (byte) 0xF9, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12,
								(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2B, (byte) 0x00, (byte) 0x00,
								(byte) 0x00, (byte) 0x9D, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x46,
								(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x63, (byte) 0x00, (byte) 0x00,
								(byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xAB,
								(byte) 0x00, (byte) 0x00, (byte) 0x00 },
						"AES")), V144((short) 144, new SecretKeySpec(new byte[] { (byte) 0x46, (byte) 0x00, (byte) 0x00,
								(byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xA3,
								(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xB6, (byte) 0x00, (byte) 0x00,
								(byte) 0x00, (byte) 0x2F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xAE,
								(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x57, (byte) 0x00, (byte) 0x00,
								(byte) 0x00, (byte) 0xB7, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
								"AES")), V143((short) 143, new SecretKeySpec(new byte[] { (byte) 0xFA, (byte) 0x00,
										(byte) 0x00, (byte) 0x00, (byte) 0xE0, (byte) 0x00, (byte) 0x00, (byte) 0x00,
										(byte) 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xE8, (byte) 0x00,
										(byte) 0x00, (byte) 0x00, (byte) 0xC9, (byte) 0x00, (byte) 0x00, (byte) 0x00,
										(byte) 0x3F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x72, (byte) 0x00,
										(byte) 0x00, (byte) 0x00, (byte) 0x92, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
										"AES")), V142((short) 142, new SecretKeySpec(new byte[] { 0x6D, 0x00, 0x00,
												0x00, (byte) 0x23, 0x00, 0x00, 0x00, (byte) 0x13, 0x00, 0x00, 0x00,
												(byte) 0xE9, 0x00, 0x00, 0x00, (byte) 0xEE, 0x00, 0x00, 0x00,
												(byte) 0x27, 0x00, 0x00, 0x00, (byte) 0xA8, 0x00, 0x00, 0x00,
												(byte) 0xCF, 0x00, 0x00, 0x00 },
												"AES")), V141((short) 141, new SecretKeySpec(new byte[] { 0x5C, 0x00,
														0x00, 0x00, (byte) 0xC0, 0x00, 0x00, 0x00, (byte) 0xC0, 0x00,
														0x00, 0x00, (byte) 0x86, 0x00, 0x00, 0x00, (byte) 0xEA, 0x00,
														0x00, 0x00, (byte) 0x85, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00,
														0x00, 0x37, 0x00, 0x00, 0x00 },
														"AES")), V140((short) 140, new SecretKeySpec(new byte[] {
																(byte) 0xCD, (byte) 0x00, (byte) 0x00, (byte) 0x00,
																(byte) 0x5C, (byte) 0x00, (byte) 0x00, (byte) 0x00,
																(byte) 0xDC, (byte) 0x00, (byte) 0x00, (byte) 0x00,
																(byte) 0x98, (byte) 0x00, (byte) 0x00, (byte) 0x00,
																(byte) 0xD8, (byte) 0x00, (byte) 0x00, (byte) 0x00,
																(byte) 0x1C, (byte) 0x00, (byte) 0x00, (byte) 0x00,
																(byte) 0x9A, (byte) 0x00, (byte) 0x00, (byte) 0x00,
																(byte) 0x47, (byte) 0x00, (byte) 0x00, (byte) 0x00 },
																"AES")), V139((short) 139, new SecretKeySpec(
																		new byte[] { (byte) 0x54, (byte) 0x00,
																				(byte) 0x00, (byte) 0x00, (byte) 0xCD,
																				(byte) 0x00, (byte) 0x00, (byte) 0x00,
																				(byte) 0xFB, (byte) 0x00, (byte) 0x00,
																				(byte) 0x00, (byte) 0xDC, (byte) 0x00,
																				(byte) 0x00, (byte) 0x00, (byte) 0xE2,
																				(byte) 0x00, (byte) 0x00, (byte) 0x00,
																				(byte) 0x40, (byte) 0x00, (byte) 0x00,
																				(byte) 0x00, (byte) 0x59, (byte) 0x00,
																				(byte) 0x00, (byte) 0x00, (byte) 0x2B,
																				(byte) 0x00, (byte) 0x00, (byte) 0x00 },
																		"AES")), V138(
																				(short) 138,
																				new SecretKeySpec(new byte[] {
																						(byte) 0xA8, 0x1B, 0x1C, 0x14,
																						0x20, 0x35, 0x42, (byte) 0x63,
																						(byte) 0xFC, (byte) 0x8A,
																						(byte) 0x4C, (byte) 0xDC, 0x30,
																						0x37, 0x0A, (byte) 0x9C,
																						(byte) 0xE0, (byte) 0xA3, 0x18,
																						(byte) 0x8E, (byte) 0x3F, 0x0A,
																						(byte) 0xC2, 0x31, (byte) 0xE9,
																						(byte) 0x8F, 0x0A, 0x03,
																						(byte) 0xFB, (byte) 0xBD,
																						(byte) 0x83, 0x29 },
																				"AES")), V130(
																						(short) 130,
																						new SecretKeySpec(new byte[] {
																								0x3F, (byte) 0x9A, 0x75,
																								(byte) 0xBB, 0x05,
																								(byte) 0xCB,
																								(byte) 0xD6, 0x7D,
																								(byte) 0xEB, 0x26, 0x33,
																								(byte) 0xE0, 0x70, 0x20,
																								(byte) 0xCB,
																								(byte) 0xDA, 0x77, 0x38,
																								(byte) 0xBE, 0x0D,
																								(byte) 0xB5,
																								(byte) 0xA9, 0x62,
																								(byte) 0x88, 0x3A, 0x2B,
																								0x1D, 0x2D, (byte) 0xDD,
																								(byte) 0x9E,
																								(byte) 0xD4, 0x73 },
																								"AES")), V129(
																										(short) 129,
																										new SecretKeySpec(
																												new byte[] {
																														0x70,
																														(byte) 0xBE,
																														0x3A,
																														0x73,
																														0x70,
																														0x24,
																														(byte) 0xE0,
																														0x67,
																														0x55,
																														(byte) 0xE5,
																														(byte) 0xFE,
																														0x24,
																														0x42,
																														0x2E,
																														(byte) 0x8E,
																														(byte) 0xF8,
																														0x32,
																														(byte) 0xE6,
																														0x6D,
																														(byte) 0xAF,
																														0x18,
																														(byte) 0xE7,
																														(byte) 0x84,
																														0x0F,
																														0x56,
																														0x42,
																														0x47,
																														0x41,
																														(byte) 0x92,
																														(byte) 0xE2,
																														(byte) 0xA1,
																														(byte) 0x9B },
																												"AES")), V128(
																														(short) 128,
																														new SecretKeySpec(
																																new byte[] {
																																		0x7E,
																																		(byte) 0x9B,
																																		0x50,
																																		0x4F,
																																		0x48,
																																		(byte) 0xC4,
																																		(byte) 0xD2,
																																		(byte) 0x95,
																																		(byte) 0xBD,
																																		(byte) 0x98,
																																		0x15,
																																		(byte) 0xE5,
																																		0x79,
																																		0x40,
																																		0x3F,
																																		0x46,
																																		(byte) 0xA1,
																																		(byte) 0xCC,
																																		(byte) 0x8D,
																																		(byte) 0x88,
																																		(byte) 0xC6,
																																		0x6F,
																																		0x44,
																																		0x0E,
																																		0x08,
																																		0x06,
																																		0x31,
																																		(byte) 0xF0,
																																		(byte) 0xF3,
																																		0x22,
																																		0x58,
																																		0x7B },
																																"AES")), V127(
																																		(short) 127,
																																		new SecretKeySpec(
																																				new byte[] {
																																						0x66,
																																						0x1D,
																																						0x0F,
																																						0x6A,
																																						0x62,
																																						0x20,
																																						0x31,
																																						(byte) 0xD4,
																																						0x21,
																																						(byte) 0xE0,
																																						0x09,
																																						(byte) 0xB9,
																																						0x0E,
																																						(byte) 0xC1,
																																						0x74,
																																						0x28,
																																						0x2B,
																																						0x27,
																																						0x7B,
																																						(byte) 0xBC,
																																						(byte) 0xBC,
																																						0x58,
																																						0x14,
																																						0x08,
																																						0x39,
																																						0x71,
																																						0x2D,
																																						0x0F,
																																						0x7B,
																																						(byte) 0xA6,
																																						0x70,
																																						0x4A },
																																				"AES")), V126(
																																						(short) 126,
																																						new SecretKeySpec(
																																								new byte[] {
																																										(byte) 0x8B,
																																										0x33,
																																										(byte) 0xBF,
																																										0x35,
																																										0x24,
																																										0x71,
																																										0x5E,
																																										0x21,
																																										(byte) 0x8B,
																																										(byte) 0xAA,
																																										(byte) 0x9C,
																																										(byte) 0xB7,
																																										0x6D,
																																										0x57,
																																										0x2E,
																																										0x35,
																																										(byte) 0xB5,
																																										(byte) 0xEF,
																																										0x6C,
																																										0x37,
																																										(byte) 0xC6,
																																										(byte) 0x83,
																																										(byte) 0xFA,
																																										0x4E,
																																										0x08,
																																										0x15,
																																										(byte) 0xF5,
																																										(byte) 0xB8,
																																										(byte) 0xB0,
																																										0x41,
																																										0x2B,
																																										0x01 },
																																								"AES")), V125(
																																										(short) 125,
																																										new SecretKeySpec(
																																												new byte[] {
																																														(byte) 0xF9,
																																														(byte) 0x81,
																																														(byte) 0xF7,
																																														0x75,
																																														0x12,
																																														0x0E,
																																														0x71,
																																														(byte) 0xD7,
																																														0x2B,
																																														(byte) 0xF6,
																																														(byte) 0xF8,
																																														(byte) 0x9A,
																																														(byte) 0x9D,
																																														0x22,
																																														0x55,
																																														0x56,
																																														0x46,
																																														0x7F,
																																														0x01,
																																														(byte) 0x95,
																																														0x63,
																																														(byte) 0xAB,
																																														0x79,
																																														(byte) 0xE1,
																																														(byte) 0x80,
																																														0x59,
																																														0x5D,
																																														0x30,
																																														(byte) 0xAB,
																																														0x29,
																																														(byte) 0x8F,
																																														(byte) 0xDB },
																																												"AES")), V124(
																																														(short) 124,
																																														new SecretKeySpec(
																																																new byte[] {
																																																		0x46,
																																																		0x72,
																																																		0x25,
																																																		(byte) 0xB5,
																																																		0x3C,
																																																		(byte) 0xF2,
																																																		(byte) 0xDC,
																																																		0x46,
																																																		(byte) 0xA3,
																																																		(byte) 0xA4,
																																																		(byte) 0x90,
																																																		(byte) 0xB9,
																																																		(byte) 0xB6,
																																																		0x1C,
																																																		(byte) 0xE7,
																																																		0x70,
																																																		0x2F,
																																																		(byte) 0xFD,
																																																		(byte) 0x81,
																																																		(byte) 0xC8,
																																																		(byte) 0xAE,
																																																		0x65,
																																																		(byte) 0xF2,
																																																		(byte) 0xCB,
																																																		0x57,
																																																		(byte) 0x0B,
																																																		0x46,
																																																		(byte) 0xB2,
																																																		(byte) 0xB7,
																																																		(byte) 0xC8,
																																																		0x18,
																																																		0x5D },
																																																"AES")), V123(
																																																		(short) 123,
																																																		new SecretKeySpec(
																																																				new byte[] {
																																																						(byte) 0xFA,
																																																						0x34,
																																																						0x18,
																																																						(byte) 0xB9,
																																																						(byte) 0xE0,
																																																						(byte) 0xA7,
																																																						(byte) 0xF8,
																																																						(byte) 0xAB,
																																																						0x43,
																																																						0x6D,
																																																						(byte) 0xA9,
																																																						0x3D,
																																																						(byte) 0xE8,
																																																						0x37,
																																																						(byte) 0xC3,
																																																						(byte) 0xAE,
																																																						(byte) 0xC9,
																																																						0x07,
																																																						0x3D,
																																																						(byte) 0x9B,
																																																						0x3F,
																																																						0x2E,
																																																						(byte) 0xDC,
																																																						0x0C,
																																																						0x72,
																																																						0x2D,
																																																						(byte) 0xF0,
																																																						0x30,
																																																						(byte) 0x92,
																																																						(byte) 0xE5,
																																																						0x73,
																																																						0x27 },
																																																				"AES")), V122(
																																																						(short) 122,
																																																						new SecretKeySpec(
																																																								new byte[] {
																																																										0x6D,
																																																										(byte) 0xCC,
																																																										(byte) 0xFD,
																																																										(byte) 0x99,
																																																										0x23,
																																																										0x3E,
																																																										0x30,
																																																										0x43,
																																																										0x13,
																																																										0x47,
																																																										(byte) 0xA4,
																																																										0x1F,
																																																										(byte) 0xE9,
																																																										0x54,
																																																										(byte) 0xAB,
																																																										(byte) 0xBC,
																																																										(byte) 0xEE,
																																																										(byte) 0x9B,
																																																										0x4F,
																																																										(byte) 0xD3,
																																																										0x27,
																																																										0x60,
																																																										0x59,
																																																										(byte) 0xCF,
																																																										(byte) 0xA8,
																																																										(byte) 0xF2,
																																																										(byte) 0xAB,
																																																										0x4B,
																																																										(byte) 0xCF,
																																																										(byte) 0xEB,
																																																										0x00,
																																																										0x31 },
																																																								"AES")), V121(
																																																										(short) 121,
																																																										new SecretKeySpec(
																																																												new byte[] {
																																																														0x5C,
																																																														(byte) 0xFF,
																																																														(byte) 0x9E,
																																																														(byte) 0xAE,
																																																														(byte) 0xC0,
																																																														(byte) 0x94,
																																																														0x18,
																																																														0x38,
																																																														(byte) 0xC0,
																																																														(byte) 0xFC,
																																																														0x37,
																																																														(byte) 0x85,
																																																														(byte) 0x86,
																																																														(byte) 0xDD,
																																																														0x41,
																																																														0x1B,
																																																														(byte) 0xEA,
																																																														0x73,
																																																														(byte) 0xB1,
																																																														(byte) 0xBC,
																																																														(byte) 0x85,
																																																														(byte) 0x8C,
																																																														0x57,
																																																														(byte) 0xAC,
																																																														0x03,
																																																														0x75,
																																																														(byte) 0xC4,
																																																														0x2C,
																																																														0x37,
																																																														(byte) 0x8F,
																																																														0x02,
																																																														0x03 },
																																																												"AES")), V120(
																																																														(short) 120,
																																																														new SecretKeySpec(
																																																																new byte[] {
																																																																		(byte) 0x8D,
																																																																		0x08,
																																																																		(byte) 0xA7,
																																																																		(byte) 0xC6,
																																																																		(byte) 0xD5,
																																																																		(byte) 0xDC,
																																																																		0x41,
																																																																		0x56,
																																																																		(byte) 0xD0,
																																																																		(byte) 0xC4,
																																																																		(byte) 0xE3,
																																																																		0x09,
																																																																		0x2B,
																																																																		(byte) 0xA2,
																																																																		(byte) 0xA6,
																																																																		(byte) 0xAA,
																																																																		(byte) 0xA8,
																																																																		(byte) 0xFA,
																																																																		0x34,
																																																																		0x5F,
																																																																		(byte) 0xFF,
																																																																		(byte) 0xC9,
																																																																		0x40,
																																																																		0x0B,
																																																																		0x74,
																																																																		0x53,
																																																																		0x62,
																																																																		0x09,
																																																																		(byte) 0xFE,
																																																																		(byte) 0xA1,
																																																																		0x0F,
																																																																		(byte) 0xD4 },
																																																																"AES")), V119(
																																																																		(short) 119,
																																																																		new SecretKeySpec(
																																																																				new byte[] {
																																																																						0x5A,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00,
																																																																						0x22,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00,
																																																																						(byte) 0xFB,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00,
																																																																						(byte) 0xD1,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00,
																																																																						(byte) 0x8F,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00,
																																																																						(byte) 0x93,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00,
																																																																						(byte) 0xCD,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00,
																																																																						(byte) 0xE6,
																																																																						0x00,
																																																																						0x00,
																																																																						0x00 },
																																																																				"AES")), V118(
																																																																						(short) 118,
																																																																						new SecretKeySpec(
																																																																								new byte[] {
																																																																										0x5A,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00,
																																																																										0x22,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00,
																																																																										(byte) 0xFB,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00,
																																																																										(byte) 0xD1,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00,
																																																																										(byte) 0x8F,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00,
																																																																										(byte) 0x93,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00,
																																																																										(byte) 0xCD,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00,
																																																																										(byte) 0xE6,
																																																																										0x00,
																																																																										0x00,
																																																																										0x00 },
																																																																								"AES")), V117(
																																																																										(short) 117,
																																																																										new SecretKeySpec(
																																																																												new byte[] {
																																																																														0x13,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00,
																																																																														0x08,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00,
																																																																														0x06,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00,
																																																																														(byte) 0xB4,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00,
																																																																														0x1B,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00,
																																																																														0x0F,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00,
																																																																														0x33,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00,
																																																																														0x52,
																																																																														0x00,
																																																																														0x00,
																																																																														0x00 },
																																																																												"AES"));
		private final SecretKeySpec skey;
		private final short version;

		EncryptionKey(short version, SecretKeySpec skey) {
			this.skey = skey;
			this.version = version;
		}

		public SecretKeySpec getEncryptionKey() {
			return skey;
		}

		public short getVersion() {
			return version;
		}
	}

	private static SecretKeySpec skey;
	// KMS
	// {0x13, 0x00, 0x00, 0x00, 0x52, 0x00, 0x00, 0x00, 0x2A, 0x00, 0x00, 0x00,
	// 0x5B, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00,
	// 0x10, 0x00, 0x00, 0x00, 0x60, 0x00, 0x00, 0x00}
	/* VANA */
	private static final byte[] funnyBytes = new byte[] { (byte) 0xEC, (byte) 0x3F, (byte) 0x77, (byte) 0xA4,
			(byte) 0x45, (byte) 0xD0, (byte) 0x71, (byte) 0xBF, (byte) 0xB7, (byte) 0x98, (byte) 0x20, (byte) 0xFC,
			(byte) 0x4B, (byte) 0xE9, (byte) 0xB3, (byte) 0xE1, (byte) 0x5C, (byte) 0x22, (byte) 0xF7, (byte) 0x0C,
			(byte) 0x44, (byte) 0x1B, (byte) 0x81, (byte) 0xBD, (byte) 0x63, (byte) 0x8D, (byte) 0xD4, (byte) 0xC3,
			(byte) 0xF2, (byte) 0x10, (byte) 0x19, (byte) 0xE0, (byte) 0xFB, (byte) 0xA1, (byte) 0x6E, (byte) 0x66,
			(byte) 0xEA, (byte) 0xAE, (byte) 0xD6, (byte) 0xCE, (byte) 0x06, (byte) 0x18, (byte) 0x4E, (byte) 0xEB,
			(byte) 0x78, (byte) 0x95, (byte) 0xDB, (byte) 0xBA, (byte) 0xB6, (byte) 0x42, (byte) 0x7A, (byte) 0x2A,
			(byte) 0x83, (byte) 0x0B, (byte) 0x54, (byte) 0x67, (byte) 0x6D, (byte) 0xE8, (byte) 0x65, (byte) 0xE7,
			(byte) 0x2F, (byte) 0x07, (byte) 0xF3, (byte) 0xAA, (byte) 0x27, (byte) 0x7B, (byte) 0x85, (byte) 0xB0,
			(byte) 0x26, (byte) 0xFD, (byte) 0x8B, (byte) 0xA9, (byte) 0xFA, (byte) 0xBE, (byte) 0xA8, (byte) 0xD7,
			(byte) 0xCB, (byte) 0xCC, (byte) 0x92, (byte) 0xDA, (byte) 0xF9, (byte) 0x93, (byte) 0x60, (byte) 0x2D,
			(byte) 0xDD, (byte) 0xD2, (byte) 0xA2, (byte) 0x9B, (byte) 0x39, (byte) 0x5F, (byte) 0x82, (byte) 0x21,
			(byte) 0x4C, (byte) 0x69, (byte) 0xF8, (byte) 0x31, (byte) 0x87, (byte) 0xEE, (byte) 0x8E, (byte) 0xAD,
			(byte) 0x8C, (byte) 0x6A, (byte) 0xBC, (byte) 0xB5, (byte) 0x6B, (byte) 0x59, (byte) 0x13, (byte) 0xF1,
			(byte) 0x04, (byte) 0x00, (byte) 0xF6, (byte) 0x5A, (byte) 0x35, (byte) 0x79, (byte) 0x48, (byte) 0x8F,
			(byte) 0x15, (byte) 0xCD, (byte) 0x97, (byte) 0x57, (byte) 0x12, (byte) 0x3E, (byte) 0x37, (byte) 0xFF,
			(byte) 0x9D, (byte) 0x4F, (byte) 0x51, (byte) 0xF5, (byte) 0xA3, (byte) 0x70, (byte) 0xBB, (byte) 0x14,
			(byte) 0x75, (byte) 0xC2, (byte) 0xB8, (byte) 0x72, (byte) 0xC0, (byte) 0xED, (byte) 0x7D, (byte) 0x68,
			(byte) 0xC9, (byte) 0x2E, (byte) 0x0D, (byte) 0x62, (byte) 0x46, (byte) 0x17, (byte) 0x11, (byte) 0x4D,
			(byte) 0x6C, (byte) 0xC4, (byte) 0x7E, (byte) 0x53, (byte) 0xC1, (byte) 0x25, (byte) 0xC7, (byte) 0x9A,
			(byte) 0x1C, (byte) 0x88, (byte) 0x58, (byte) 0x2C, (byte) 0x89, (byte) 0xDC, (byte) 0x02, (byte) 0x64,
			(byte) 0x40, (byte) 0x01, (byte) 0x5D, (byte) 0x38, (byte) 0xA5, (byte) 0xE2, (byte) 0xAF, (byte) 0x55,
			(byte) 0xD5, (byte) 0xEF, (byte) 0x1A, (byte) 0x7C, (byte) 0xA7, (byte) 0x5B, (byte) 0xA6, (byte) 0x6F,
			(byte) 0x86, (byte) 0x9F, (byte) 0x73, (byte) 0xE6, (byte) 0x0A, (byte) 0xDE, (byte) 0x2B, (byte) 0x99,
			(byte) 0x4A, (byte) 0x47, (byte) 0x9C, (byte) 0xDF, (byte) 0x09, (byte) 0x76, (byte) 0x9E, (byte) 0x30,
			(byte) 0x0E, (byte) 0xE4, (byte) 0xB2, (byte) 0x94, (byte) 0xA0, (byte) 0x3B, (byte) 0x34, (byte) 0x1D,
			(byte) 0x28, (byte) 0x0F, (byte) 0x36, (byte) 0xE3, (byte) 0x23, (byte) 0xB4, (byte) 0x03, (byte) 0xD8,
			(byte) 0x90, (byte) 0xC8, (byte) 0x3C, (byte) 0xFE, (byte) 0x5E, (byte) 0x32, (byte) 0x24, (byte) 0x50,
			(byte) 0x1F, (byte) 0x3A, (byte) 0x43, (byte) 0x8A, (byte) 0x96, (byte) 0x41, (byte) 0x74, (byte) 0xAC,
			(byte) 0x52, (byte) 0x33, (byte) 0xF0, (byte) 0xD9, (byte) 0x29, (byte) 0x80, (byte) 0xB1, (byte) 0x16,
			(byte) 0xD3, (byte) 0xAB, (byte) 0x91, (byte) 0xB9, (byte) 0x84, (byte) 0x7F, (byte) 0x61, (byte) 0x1E,
			(byte) 0xCF, (byte) 0xC5, (byte) 0xD1, (byte) 0x56, (byte) 0x3D, (byte) 0xCA, (byte) 0xF4, (byte) 0x05,
			(byte) 0xC6, (byte) 0xE5, (byte) 0x08, (byte) 0x49 };

	/*
	 * ORIGINAL private static final byte[] funnyBytes2 = new byte[] { (byte)
	 * 0xEC, 0x3F, 0x77, (byte) 0xA4, 0x45, (byte) 0xD0, 0x71, (byte) 0xBF,
	 * (byte) 0xB7, (byte) 0x98, 0x20, (byte) 0xFC, 0x4B, (byte) 0xE9, (byte)
	 * 0xB3, (byte) 0xE1, 0x5C, 0x22, (byte) 0xF7, 0x0C, 0x44, 0x1B, (byte)
	 * 0x81, (byte) 0xBD, 0x63, (byte) 0x8D, (byte) 0xD4, (byte) 0xC3, (byte)
	 * 0xF2, 0x10, 0x19, (byte) 0xE0, (byte) 0xFB, (byte) 0xA1, 0x6E, 0x66,
	 * (byte) 0xEA, (byte) 0xAE, (byte) 0xD6, (byte) 0xCE, 0x06, 0x18, 0x4E,
	 * (byte) 0xEB, 0x78, (byte) 0x95, (byte) 0xDB, (byte) 0xBA, (byte) 0xB6,
	 * 0x42, 0x7A, 0x2A, (byte) 0x83, 0x0B, 0x54, 0x67, 0x6D, (byte) 0xE8, 0x65,
	 * (byte) 0xE7, 0x2F, 0x07, (byte) 0xF3, (byte) 0xAA, 0x27, 0x7B, (byte)
	 * 0x85, (byte) 0xB0, 0x26, (byte) 0xFD, (byte) 0x8B, (byte) 0xA9, (byte)
	 * 0xFA, (byte) 0xBE, (byte) 0xA8, (byte) 0xD7, (byte) 0xCB, (byte) 0xCC,
	 * (byte) 0x92, (byte) 0xDA, (byte) 0xF9, (byte) 0x93, 0x60, 0x2D, (byte)
	 * 0xDD, (byte) 0xD2, (byte) 0xA2, (byte) 0x9B, 0x39, 0x5F, (byte) 0x82,
	 * 0x21, 0x4C, 0x69, (byte) 0xF8, 0x31, (byte) 0x87, (byte) 0xEE, (byte)
	 * 0x8E, (byte) 0xAD, (byte) 0x8C, 0x6A, (byte) 0xBC, (byte) 0xB5, 0x6B,
	 * 0x59, 0x13, (byte) 0xF1, 0x04, 0x00, (byte) 0xF6, 0x5A, 0x35, 0x79, 0x48,
	 * (byte) 0x8F, 0x15, (byte) 0xCD, (byte) 0x97, 0x57, 0x12, 0x3E, 0x37,
	 * (byte) 0xFF, (byte) 0x9D, 0x4F, 0x51, (byte) 0xF5, (byte) 0xA3, 0x70,
	 * (byte) 0xBB, 0x14, 0x75, (byte) 0xC2, (byte) 0xB8, 0x72, (byte) 0xC0,
	 * (byte) 0xED, 0x7D, 0x68, (byte) 0xC9, 0x2E, 0x0D, 0x62, 0x46, 0x17, 0x11,
	 * 0x4D, 0x6C, (byte) 0xC4, 0x7E, 0x53, (byte) 0xC1, 0x25, (byte) 0xC7,
	 * (byte) 0x9A, 0x1C, (byte) 0x88, 0x58, 0x2C, (byte) 0x89, (byte) 0xDC,
	 * 0x02, 0x64, 0x40, 0x01, 0x5D, 0x38, (byte) 0xA5, (byte) 0xE2, (byte)
	 * 0xAF, 0x55, (byte) 0xD5, (byte) 0xEF, 0x1A, 0x7C, (byte) 0xA7, 0x5B,
	 * (byte) 0xA6, 0x6F, (byte) 0x86, (byte) 0x9F, 0x73, (byte) 0xE6, 0x0A,
	 * (byte) 0xDE, 0x2B, (byte) 0x99, 0x4A, 0x47, (byte) 0x9C, (byte) 0xDF,
	 * 0x09, 0x76, (byte) 0x9E, 0x30, 0x0E, (byte) 0xE4, (byte) 0xB2, (byte)
	 * 0x94, (byte) 0xA0, 0x3B, 0x34, 0x1D, 0x28, 0x0F, 0x36, (byte) 0xE3, 0x23,
	 * (byte) 0xB4, 0x03, (byte) 0xD8, (byte) 0x90, (byte) 0xC8, 0x3C, (byte)
	 * 0xFE, 0x5E, 0x32, 0x24, 0x50, 0x1F, 0x3A, 0x43, (byte) 0x8A, (byte) 0x96,
	 * 0x41, 0x74, (byte) 0xAC, 0x52, 0x33, (byte) 0xF0, (byte) 0xD9, 0x29,
	 * (byte) 0x80, (byte) 0xB1, 0x16, (byte) 0xD3, (byte) 0xAB, (byte) 0x91,
	 * (byte) 0xB9, (byte) 0x84, 0x7F, 0x61, 0x1E, (byte) 0xCF, (byte) 0xC5,
	 * (byte) 0xD1, 0x56, 0x3D, (byte) 0xCA, (byte) 0xF4, 0x05, (byte) 0xC6,
	 * (byte) 0xE5, 0x08, 0x49, 0x4F, 0x64, 0x69, 0x6E, 0x4D, 0x53, 0x7E, 0x46,
	 * 0x72, 0x7A };
	 */
	private static final byte[] rammyByte = new byte[] { (byte) 0xEC, (byte) 0x3F, (byte) 0x77, (byte) 0xA4,
			(byte) 0x45, (byte) 0xD0, (byte) 0x71, (byte) 0xBF, (byte) 0xB7, (byte) 0x98, (byte) 0x20, (byte) 0xFC,
			(byte) 0x4B, (byte) 0xE9, (byte) 0xB3, (byte) 0xE1, (byte) 0x5C, (byte) 0x22, (byte) 0xF7, (byte) 0x0C,
			(byte) 0x44, (byte) 0x1B, (byte) 0x81, (byte) 0xBD, (byte) 0x63, (byte) 0x8D, (byte) 0xD4, (byte) 0xC3,
			(byte) 0xF2, (byte) 0x10, (byte) 0x19, (byte) 0xE0, (byte) 0xFB, (byte) 0xA1, (byte) 0x6E, (byte) 0x66,
			(byte) 0xEA, (byte) 0xAE, (byte) 0xD6, (byte) 0xCE, (byte) 0x06, (byte) 0x18, (byte) 0x4E, (byte) 0xEB,
			(byte) 0x78, (byte) 0x95, (byte) 0xDB, (byte) 0xBA, (byte) 0xB6, (byte) 0x42, (byte) 0x7A, (byte) 0x2A,
			(byte) 0x83, (byte) 0x0B, (byte) 0x54, (byte) 0x67, (byte) 0x6D, (byte) 0xE8, (byte) 0x65, (byte) 0xE7,
			(byte) 0x2F, (byte) 0x07, (byte) 0xF3, (byte) 0xAA, (byte) 0x27, (byte) 0x7B, (byte) 0x85, (byte) 0xB0,
			(byte) 0x26, (byte) 0xFD, (byte) 0x8B, (byte) 0xA9, (byte) 0xFA, (byte) 0xBE, (byte) 0xA8, (byte) 0xD7,
			(byte) 0xCB, (byte) 0xCC, (byte) 0x92, (byte) 0xDA, (byte) 0xF9, (byte) 0x93, (byte) 0x60, (byte) 0x2D,
			(byte) 0xDD, (byte) 0xD2, (byte) 0xA2, (byte) 0x9B, (byte) 0x39, (byte) 0x5F, (byte) 0x82, (byte) 0x21,
			(byte) 0x4C, (byte) 0x69, (byte) 0xF8, (byte) 0x31, (byte) 0x87, (byte) 0xEE, (byte) 0x8E, (byte) 0xAD,
			(byte) 0x8C, (byte) 0x6A, (byte) 0xBC, (byte) 0xB5, (byte) 0x6B, (byte) 0x59, (byte) 0x13, (byte) 0xF1,
			(byte) 0x04, (byte) 0x00, (byte) 0xF6, (byte) 0x5A, (byte) 0x35, (byte) 0x79, (byte) 0x48, (byte) 0x8F,
			(byte) 0x15, (byte) 0xCD, (byte) 0x97, (byte) 0x57, (byte) 0x12, (byte) 0x3E, (byte) 0x37, (byte) 0xFF,
			(byte) 0x9D, (byte) 0x4F, (byte) 0x51, (byte) 0xF5, (byte) 0xA3, (byte) 0x70, (byte) 0xBB, (byte) 0x14,
			(byte) 0x75, (byte) 0xC2, (byte) 0xB8, (byte) 0x72, (byte) 0xC0, (byte) 0xED, (byte) 0x7D, (byte) 0x68,
			(byte) 0xC9, (byte) 0x2E, (byte) 0x0D, (byte) 0x62, (byte) 0x46, (byte) 0x17, (byte) 0x11, (byte) 0x4D,
			(byte) 0x6C, (byte) 0xC4, (byte) 0x7E, (byte) 0x53, (byte) 0xC1, (byte) 0x25, (byte) 0xC7, (byte) 0x9A,
			(byte) 0x1C, (byte) 0x88, (byte) 0x58, (byte) 0x2C, (byte) 0x89, (byte) 0xDC, (byte) 0x02, (byte) 0x64,
			(byte) 0x40, (byte) 0x01, (byte) 0x5D, (byte) 0x38, (byte) 0xA5, (byte) 0xE2, (byte) 0xAF, (byte) 0x55,
			(byte) 0xD5, (byte) 0xEF, (byte) 0x1A, (byte) 0x7C, (byte) 0xA7, (byte) 0x5B, (byte) 0xA6, (byte) 0x6F,
			(byte) 0x86, (byte) 0x9F, (byte) 0x73, (byte) 0xE6, (byte) 0x0A, (byte) 0xDE, (byte) 0x2B, (byte) 0x99,
			(byte) 0x4A, (byte) 0x47, (byte) 0x9C, (byte) 0xDF, (byte) 0x09, (byte) 0x76, (byte) 0x9E, (byte) 0x30,
			(byte) 0x0E, (byte) 0xE4, (byte) 0xB2, (byte) 0x94, (byte) 0xA0, (byte) 0x3B, (byte) 0x34, (byte) 0x1D,
			(byte) 0x28, (byte) 0x0F, (byte) 0x36, (byte) 0xE3, (byte) 0x23, (byte) 0xB4, (byte) 0x03, (byte) 0xD8,
			(byte) 0x90, (byte) 0xC8, (byte) 0x3C, (byte) 0xFE, (byte) 0x5E, (byte) 0x32, (byte) 0x24, (byte) 0x50,
			(byte) 0x1F, (byte) 0x3A, (byte) 0x43, (byte) 0x8A, (byte) 0x96, (byte) 0x41, (byte) 0x74, (byte) 0xAC,
			(byte) 0x52, (byte) 0x33, (byte) 0xF0, (byte) 0xD9, (byte) 0x29, (byte) 0x80, (byte) 0xB1, (byte) 0x16,
			(byte) 0xD3, (byte) 0xAB, (byte) 0x91, (byte) 0xB9, (byte) 0x84, (byte) 0x7F, (byte) 0x61, (byte) 0x1E,
			(byte) 0xCF, (byte) 0xC5, (byte) 0xD1, (byte) 0x56, (byte) 0x3D, (byte) 0xCA, (byte) 0xF4, (byte) 0x05,
			(byte) 0xC6, (byte) 0xE5, (byte) 0x08, (byte) 0x49 };

	/**
	 * Class constructor - Creates an instance of the MapleStory encryption
	 * cipher.
	 *
	 * @param iv
	 *            The 4-byte IV to use.
	 * @param mapleVersion
	 */
	public MapleAESOFB(byte iv[], short mapleVersion) {
		for (EncryptionKey encryptkey : EncryptionKey.values()) {
			if (("V" + ServerConstants.MAPLE_VERSION).equals(encryptkey.name())) {
				skey = encryptkey.getEncryptionKey();
				break;
			} else {
				// System.out.println("System could not locate encryption for
				// the current version, so it is using " +
				// EncryptionKey.values()[EncryptionKey.values().length].name()
				// + " Encryption.");
				skey = EncryptionKey.values()[ServerConstants.MAPLE_VERSION <= EncryptionKey
						.values()[EncryptionKey.values().length - 1].getVersion() ? EncryptionKey.values().length - 1
								: 0].getEncryptionKey();
				// Always newer/older version encryption, depends on version.
			}
		}
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.err.println("ERROR" + e);
		} catch (InvalidKeyException e) {
			System.err.println(
					"Error initalizing the encryption cipher.  Make sure you're using the Unlimited Strength cryptography jar files.");
		}

		this.setIv(iv);
		this.mapleVersion = (short) (((mapleVersion >> 8) & 0xFF) | ((mapleVersion << 8) & 0xFF00));
	}

	/**
	 * Sets the IV of this instance.
	 *
	 * @param iv
	 *            The new IV.
	 */
	private void setIv(byte[] iv) {
		this.iv = iv;
	}

	/**
	 * For debugging/testing purposes only.
	 *
	 * @return The IV.
	 */
	public byte[] getIv() {
		return this.iv;
	}

	/**
	 * Encrypts <code>data</code> and generates a new IV.
	 *
	 * @param data
	 *            The bytes to encrypt.
	 * @return The encrypted bytes.
	 */
	public byte[] crypt(byte[] data) {
		int remaining = data.length;
		int llength = 0x5B0;
		int start = 0;

		try {
			while (remaining > 0) {
				byte[] myIv = BitTools.multiplyBytes(this.iv, 4, 4);
				if (remaining < llength) {
					llength = remaining;
				}
				for (int x = start; x < (start + llength); x++) {
					if ((x - start) % myIv.length == 0) {
						byte[] newIv = cipher.doFinal(myIv);
						System.arraycopy(newIv, 0, myIv, 0, myIv.length);
						// System.out
						// .println("Iv is now " + HexTool.toString(this.iv));

					}
					data[x] ^= myIv[(x - start) % myIv.length];
				}
				start += llength;
				remaining -= llength;
				llength = 0x5B4;
			}
			updateIv();
		} catch (IllegalBlockSizeException | BadPaddingException e) {
		}
		return data;
	}

	/**
	 * Generates a new IV.
	 */
	private void updateIv() {
		this.iv = getNewIv(this.iv);
	}

	/**
	 * Generates a packet header for a packet that is <code>length</code> long.
	 *
	 * @param length
	 *            How long the packet that this header is for is.
	 * @return The header.
	 */
	public byte[] getPacketHeader(int length) {
		int iiv = (((iv[3]) & 0xFF) | ((iv[2] << 8) & 0xFF00)) ^ mapleVersion;
		int mlength = (((length << 8) & 0xFF00) | (length >>> 8)) ^ iiv;

		return new byte[] { (byte) ((iiv >>> 8) & 0xFF), (byte) (iiv & 0xFF), (byte) ((mlength >>> 8) & 0xFF),
				(byte) (mlength & 0xFF) };
	}

	/**
	 * Gets the packet length from a header.
	 *
	 * @param packetHeader
	 *            The header as an integer.
	 * @return The length of the packet.
	 */
	public static int getPacketLength(int packetHeader) {
		int packetLength = ((packetHeader >>> 16) ^ (packetHeader & 0xFFFF));
		packetLength = ((packetLength << 8) & 0xFF00) | ((packetLength >>> 8) & 0xFF); // fix
																						// endianness
		return packetLength;
	}

	/**
	 * Check the packet to make sure it has a header.
	 *
	 * @param packet
	 *            The packet to check.
	 * @return <code>True</code> if the packet has a correct header,
	 *         <code>false</code> otherwise.
	 */
	public boolean checkPacket(byte[] packet) {
		return ((((packet[0] ^ iv[2]) & 0xFF) == ((mapleVersion >> 8) & 0xFF))
				&& (((packet[1] ^ iv[3]) & 0xFF) == (mapleVersion & 0xFF)));
	}

	/**
	 * Check the header for validity.
	 *
	 * @param packetHeader
	 *            The packet header to check.
	 * @return <code>True</code> if the header is correct, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkPacket(int packetHeader) {
		return checkPacket(new byte[] { (byte) ((packetHeader >> 24) & 0xFF), (byte) ((packetHeader >> 16) & 0xFF) });
	}

	/**
	 * Gets a new IV from <code>oldIv</code>
	 *
	 * @param oldIv
	 *            The old IV to get a new IV from.
	 * @return The new IV.
	 */
	public static byte[] getNewIv(byte oldIv[]) {
		byte[] in = { (byte) 0xf2, 0x53, (byte) 0x50, (byte) 0xc6 }; // magic
		// ;)
		for (int x = 0; x < 4; x++) {
			funnyShit(oldIv[x], in);
			// System.out.println(HexTool.toString(in));
		}
		return in;
	}

	/**
	 * Returns the IV of this instance as a string.
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return "IV: " + HexTool.toString(this.iv);
	}

	/**
	 * Does funny stuff. <code>this.OldIV</code> must not equal <code>in</code>
	 * Modifies <code>in</code> and returns it for convenience.
	 *
	 * @param inputByte
	 *            The byte to apply the funny stuff to.
	 * @param in
	 *            Something needed for all this to occur.
	 */
	public static final void funnyShit(byte inputByte, byte[] in) {
		byte a = in[1];
		byte b = inputByte;
		byte c = funnyBytes[(int) a & 0xFF];
		c -= inputByte;
		in[0] += c;
		c = in[2];
		c ^= funnyBytes[(int) b & 0xFF];
		a -= (int) c & 0xFF;
		in[1] = a;
		a = in[3];
		c = a;
		a -= (int) in[0] & 0xFF;
		c = funnyBytes[(int) c & 0xFF];
		c += inputByte;
		c ^= in[2];
		in[2] = c;
		a += (int) funnyBytes[(int) b & 0xFF] & 0xFF;
		in[3] = a;

		int d = ((int) in[0]) & 0xFF;
		d |= (in[1] << 8) & 0xFF00;
		d |= (in[2] << 16) & 0xFF0000;
		d |= (in[3] << 24) & 0xFF000000;
		int ret_value = d >>> 0x1d;
		d <<= 3;
		ret_value |= d;

		in[0] = (byte) (ret_value & 0xFF);
		in[1] = (byte) ((ret_value >> 8) & 0xFF);
		in[2] = (byte) ((ret_value >> 16) & 0xFF);
		in[3] = (byte) ((ret_value >> 24) & 0xFF);
	}
}