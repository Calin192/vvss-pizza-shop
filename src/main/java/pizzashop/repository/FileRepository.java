package pizzashop.repository;

import java.util.ArrayList;
import java.util.List;

public abstract class FileRepository<T> implements Repository<T> {
    protected String filePath;
    protected List<T> entityList;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    protected abstract void readData();

    protected FileRepository(String filePath) {
        this.filePath = filePath;
        entityList = new ArrayList<>();
        readData();
    }

}
