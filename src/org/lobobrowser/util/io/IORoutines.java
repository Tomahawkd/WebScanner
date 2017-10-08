/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net
*/
/*
 * Created on Mar 19, 2005
 */
package org.lobobrowser.util.io;

import java.io.*;

/**
 * @author J. H. S.
 */
public class IORoutines {

	public static byte[] load(File file) throws IOException {
		long fileLength = file.length();
		if (fileLength > Integer.MAX_VALUE) {
			throw new IOException("File '" + file.getName() + "' too big");
		}
		try (InputStream in = new FileInputStream(file)) {
			return loadExact(in, (int) fileLength);
		}
	}

	public static byte[] load(InputStream in) throws IOException {
		return load(in, 4096);
	}

	public static byte[] load(InputStream in, int initialBufferSize) throws IOException {
		if (initialBufferSize == 0) {
			initialBufferSize = 1;
		}
		byte[] buffer = new byte[initialBufferSize];
		int offset = 0;
		for (; ; ) {
			int remain = buffer.length - offset;
			if (remain <= 0) {
				int newSize = buffer.length * 2;
				byte[] newBuffer = new byte[newSize];
				System.arraycopy(buffer, 0, newBuffer, 0, offset);
				buffer = newBuffer;
				remain = buffer.length - offset;
			}
			int numRead = in.read(buffer, offset, remain);
			if (numRead == -1) {
				break;
			}
			offset += numRead;
		}
		if (offset < buffer.length) {
			byte[] newBuffer = new byte[offset];
			System.arraycopy(buffer, 0, newBuffer, 0, offset);
			buffer = newBuffer;
		}
		return buffer;
	}

	private static byte[] loadExact(InputStream in, int length) throws IOException {
		byte[] buffer = new byte[length];
		int offset = 0;
		for (; ; ) {
			int remain = length - offset;
			if (remain <= 0) {
				break;
			}
			int numRead = in.read(buffer, offset, remain);
			if (numRead == -1) {
				throw new IOException("Reached EOF, read " + offset + " expecting " + length);
			}
			offset += numRead;
		}
		return buffer;
	}

	public static void save(File file, byte[] content) throws IOException {
		try (FileOutputStream out = new FileOutputStream(file)) {
			out.write(content);
		}
	}

}
