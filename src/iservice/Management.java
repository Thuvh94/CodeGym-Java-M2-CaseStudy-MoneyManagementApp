package iservice;

import java.util.List;

public interface Management<T> {
        void add (T object);
        List<T> findAll();
}
