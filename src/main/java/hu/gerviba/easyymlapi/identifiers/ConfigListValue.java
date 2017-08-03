package hu.gerviba.easyymlapi.identifiers;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import hu.gerviba.easyymlapi.datastructure.CustomListProcessor;
import hu.gerviba.easyymlapi.datastructure.ListType;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ConfigListValue {
	
	class NullProcessor implements CustomListProcessor<String> {
		@Override
		public List<String> process(List<String> raw) {
			throw new RuntimeException("Processor attribute not set");
		}
	}
	
	public ListType type();
	public Class<? extends CustomListProcessor<?>> processor() default NullProcessor.class;
	public String path() default "";
	public String name() default "";
	public String[] defaultValue() default {};
}
