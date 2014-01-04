package baggage.hypertoolkit;


import java.util.function.Function;

public class Route<T extends BaseServices> {
    private final String name;
    private final Function<T, RequestHandler> factory;

    public Route(String name, Function<T, RequestHandler> factory) {

        this.name = name;
        this.factory = factory;
    }

    public String name() {
        return name;
    }

    public Function<T, RequestHandler> factory() {
        return factory;
    }
}
