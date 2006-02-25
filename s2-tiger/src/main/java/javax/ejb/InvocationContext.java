package javax.ejb;

import java.lang.reflect.Method;
import java.util.Map;

public interface InvocationContext {
    public Object getBean();

    public Method getMethod();

    public Object[] getParameters();

    public void setParameters(Object[] parameters);

    public Map getContextData();

    public Object proceed() throws Exception;
}
