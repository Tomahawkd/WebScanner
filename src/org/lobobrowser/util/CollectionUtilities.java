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
 * Created on Jun 9, 2005
 */
package org.lobobrowser.util;

import org.lobobrowser.html.renderer.Renderable;

import java.util.*;

/**
 * @author J. H. S.
 */
public class CollectionUtilities {
	/**
	 *
	 */
	private CollectionUtilities() {
		super();
	}

	public static Iterator<Renderable> singletonIterator(final Renderable item) {
		return new Iterator<>() {
			private boolean gotItem = false;

			public boolean hasNext() {
				return !this.gotItem;
			}

			public Renderable next() {
				if (this.gotItem) {
					throw new NoSuchElementException();
				}
				this.gotItem = true;
				return item;
			}

			public void remove() {
				if (!this.gotItem) {
					this.gotItem = true;
				} else {
					throw new NoSuchElementException();
				}
			}
		};
	}
}
