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
 * Created on Jun 2, 2005
 */
package org.lobobrowser.util;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author J. H. S.
 */
public class Domains {
	private static final Collection<String> gTLDs;

	static {
		gTLDs = new HashSet<>();
		gTLDs.add(".com");
		gTLDs.add(".edu");
		gTLDs.add(".gov");
		gTLDs.add(".int");
		gTLDs.add(".mil");
		gTLDs.add(".net");
		gTLDs.add(".org");
		gTLDs.add(".biz");
		gTLDs.add(".info");
		gTLDs.add(".name");
		gTLDs.add(".pro");
		gTLDs.add(".aero");
		gTLDs.add(".coop");
		gTLDs.add(".museum");
	}

	/**
	 *
	 */
	private Domains() {
		super();
	}

	public static boolean isValidCookieDomain(String domain, String hostName) {
		String plainDomain;
		if (!domain.startsWith(".")) {
			plainDomain = domain;
			domain = "." + domain;
		} else {
			plainDomain = domain.substring(1);
		}
		String plainDomainTL = plainDomain.toLowerCase();
		String hostNameTL = hostName.toLowerCase();
		if (!hostNameTL.endsWith(plainDomainTL)) {
			return false;
		}
		int lastDotIdx = domain.lastIndexOf('.');
		if (lastDotIdx == -1) {
			return false;
		}
		String suffix = domain.substring(lastDotIdx).toLowerCase();
		if (gTLDs.contains(suffix)) {
			return Strings.countChars(domain, '.') >= 2;
		} else {
			return Strings.countChars(domain, '.') >= 3;
		}
	}

}
