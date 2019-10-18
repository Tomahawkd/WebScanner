package application.utility.file.cache;

import application.utility.net.Context;

import java.util.ArrayList;

public class ContextList extends ArrayList<Context> {

	ContextList(Context context) {
		super();
		add(context);
	}

	private ContextList(int initialSize) {
		super(initialSize);
	}

	/**
	 * Deep copy of the array.
	 * @return A copy of current array.
	 */
	public ContextList copy() {
		ContextList list = new ContextList(this.size());
		for (Context context : this) {
			list.add(context.copy());
		}
		return list;
	}

	@Override
	public boolean add(Context context) {
		Context currentContext;
		//Initial list
		if (size() == 0) return super.add(context);
		for (int index = 0; index < size(); index++) {
			currentContext = get(index);
			if (currentContext != null) {
				if (currentContext.getURL() != null && context.getURL() != null) {
					if (currentContext.getURL().getPath().equals(context.getURL().getPath())) {
						//Param is different
						if (!currentContext.getParams().equals(context.getParams())) {
							return super.add(context);
						} else {
							//Update data
							remove(index);
							super.add(index, context);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
