package oncotcap.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VariablePlotDescriptor
{
	double getYMin() default 0.0;
	double getYMax() default 10.0;
	double getYStep() default 1.0;
	boolean isYAxisLogScale() default false;
	String getTitle() default "";
}
