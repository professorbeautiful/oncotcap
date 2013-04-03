package oncotcap.display.browser;

public class Variable extends DependencyNoun {
				public Variable(PreprocessedCodeBundle cb, String varName) {
						super(cb, varName);
				}
				public Variable(String className, String varName) {
						super(className, varName);
				}
				public String getVariableName() {
						return getObjectName();
				}
		}
