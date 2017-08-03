package hu.gerviba.easyymlapi.datastructure;

import java.util.List;

@FunctionalInterface
public interface CustomListProcessor<T> {

	public List<T> process(List<String> raw);
	
}
