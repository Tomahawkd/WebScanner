package application.target;

import application.utility.net.Context;

public interface Target {

	void addTarget(Context context);

	DataNode toTree();
}
