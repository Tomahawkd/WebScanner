package application.utility.file;

import java.io.Serializable;

public interface DataSerializable<T extends Serializable> {

	void setData(T data);

	T getData();
}
