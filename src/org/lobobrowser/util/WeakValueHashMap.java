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
 * Created on Oct 8, 2005
 */
package org.lobobrowser.util;

import org.w3c.dom.Element;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

public class WeakValueHashMap implements Map<String, Element> {
	private final Map<String, Reference<Element>> map = new HashMap<>();
	private final ReferenceQueue<Element> queue = new ReferenceQueue<>();

	public WeakValueHashMap() {
		super();
	}

	public int size() {
		return this.map.size();
	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public boolean containsKey(Object key) {
		if (key instanceof String) {
			Reference<Element> wf = this.map.get(key);
			return wf != null && wf.get() != null;
		} else return false;
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public Element get(Object key) {
		this.checkQueue();
		Reference<Element> wf = this.map.get(key);
		return wf == null ? null : wf.get();
	}

	public Element put(String key, Element value) {
		this.checkQueue();
		return this.putImpl(key, value);
	}

	private Element putImpl(String key, Element value) {
		if (value == null) {
			throw new IllegalArgumentException("null values not accepted");
		}
		Reference<Element> ref = new LocalWeakReference(key, value, this.queue);
		Reference<Element> oldWf = this.map.put(key, ref);
		return oldWf == null ? null : oldWf.get();
	}

	public Element remove(Object key) {
		this.checkQueue();
		Reference<Element> wf = this.map.remove(key);
		return wf == null ? null : wf.get();
	}

	public void putAll(Map<? extends String, ? extends Element> t) {
		this.checkQueue();
		for (Entry<? extends String, ? extends Element> o : t.entrySet()) {
			this.putImpl(o.getKey(), o.getValue());
		}
	}

	public void clear() {
		this.checkQueue();
		this.map.clear();
	}

	public Set<String> keySet() {
		return this.map.keySet();
	}

	private void checkQueue() {
		ReferenceQueue<Element> queue = this.queue;
		LocalWeakReference ref;
		while ((ref = (LocalWeakReference) queue.poll()) != null) {
			this.map.remove(ref.getKey());
		}
	}

	public Collection<Element> values() {
		return new ArrayList<>();
	}

	public Set<Entry<String, Element>> entrySet() {
		throw new UnsupportedOperationException();
	}

	private static class LocalWeakReference extends WeakReference<Element> {
		private final String key;

		LocalWeakReference(String key, Element target, ReferenceQueue<Element> queue) {
			super(target, queue);
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public boolean equals(Object other) {
			Object target1 = this.get();
			Object target2 = other instanceof LocalWeakReference ? ((LocalWeakReference) other).get() : null;
			return Objects.equals(target1, target2);
		}

		public int hashCode() {
			Object target = this.get();
			return target == null ? 0 : target.hashCode();
		}
	}
}
