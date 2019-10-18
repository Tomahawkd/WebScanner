package application.target;

import application.utility.net.Context;

public interface Target {

	void addTarget(Context context);

	DataNode toTree();

	Context getContext(String host, String path);
}
