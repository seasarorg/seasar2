package javax.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

public interface InvocationContext {
    public Object getTarget();

    public Method getMethod();

    public Object[] getParameters();

    public void setParameters(Object[] parameters);

    public Map<String, Object> getContextData();

    public Object proceed() throws Exception;
}
