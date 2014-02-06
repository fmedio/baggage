package baggage.hypertoolkit;

import baggage.hypertoolkit.request.InvalidRequestException;
import baggage.hypertoolkit.views.Resource;
import com.google.common.collect.Multimap;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fmedio on 2/5/14.
 */
public abstract class TypedAction<Request> implements RequestHandler {
    private ParameterFactory parameterFactory;

    protected TypedAction() {
        this.parameterFactory = new ParameterFactory();
    }

    protected abstract Class<Request> requestClass();

    @Override
    public Resource handle(HttpServletRequest servletRequest) throws InvalidRequestException {
        Multimap<String, String> parameters = parameterFactory.getParameters(servletRequest).asMultiMap();
        Request request = new RequestSerializer().deserialize(parameters, requestClass());
        return execute(servletRequest, request);
    }

    @Override
    public boolean log() {
        return true;
    }

    public abstract Resource execute(HttpServletRequest servletRequest, Request request);
}
