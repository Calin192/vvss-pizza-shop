package pizzashop.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    Boolean add(T entity);

}
