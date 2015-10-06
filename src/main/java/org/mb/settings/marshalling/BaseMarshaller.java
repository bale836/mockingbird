package org.mb.settings.marshalling;

import com.google.common.collect.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 28.06.2015.
 */
public class BaseMarshaller {
    private static final String GET_AS_TYPE_ERROR = "%s is expected";
    private static final String EMPTY_PARAMETER_ERROR = "Not empty %s is expected";
    private static final String GET_AS_STRING_ERROR = "Either string or number is expected";
    private static final String GET_AS_LIST_ERROR = "Either string or list of strings is expected";
    private static final String GET_AS_MAP_ERROR = "Map is expected";

    protected final Object o;

    protected BaseMarshaller(Object o) {
        this.o = o;
    }

    public static BaseMarshaller from(Object o) {
        return new BaseMarshaller(o);
    }

    public <T> T toType(Class<T> type, boolean notNull) throws MarshallingException {
        if (notNull && o == null) {
            throw new MarshallingException(String.format(EMPTY_PARAMETER_ERROR, type.getSimpleName()));
        }
        if (!type.isAssignableFrom(o.getClass())) {
            throw new MarshallingException(String.format(GET_AS_TYPE_ERROR, type.getSimpleName()));
        }
        return type.cast(o);
    }

    public String toStr() throws MarshallingException {
        if (o == null) {
            return "";
        }
        if (o instanceof String) {
            return (String)o;
        }
        else if (o instanceof Number) {
            return String.valueOf(((Number) o).intValue());
        }
        else {
            throw new MarshallingException(GET_AS_STRING_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> toListOfType(Class<T> type) throws MarshallingException {
        if (o == null) {
            return Collections.emptyList();
        }
        if (type.isAssignableFrom(o.getClass())) {
            return Lists.<T>newArrayList((T) o);
        } else if (o instanceof List) {
            for (Object listItem : (List<Object>)o) {
                from(listItem).toType(type, true);
            }
            return (List<T>)o;
        } else {
            throw new MarshallingException(GET_AS_LIST_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> toMapOfType(Class<K> keyType, Class<V> valueType) throws MarshallingException {
        if (o == null) {
            return Collections.emptyMap();
        }
        if (o instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>)o;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                from(entry.getKey()).toType(keyType, true);
                from(entry.getValue()).toType(valueType, true);
            }
            return (Map<K, V>) o;
        } else {
            throw new MarshallingException(GET_AS_MAP_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public <K, V> Multimap<K, V> toMultimapOfType(Class<K> keyType, Class<V> valueType) throws MarshallingException {
        ListMultimap<K, V> multimap = ArrayListMultimap.create();
        if (o == null) {
            return multimap;
        }
        if (o instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>)o;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                multimap.putAll(from(entry.getKey()).toType(keyType, true), from(entry.getValue()).toListOfType(valueType));
            }
            return multimap;
        } else {
            throw new MarshallingException(GET_AS_MAP_ERROR);
        }
    }
}