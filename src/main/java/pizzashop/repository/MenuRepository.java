package pizzashop.repository;

import pizzashop.model.MenuDataModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MenuRepository extends FileRepository<MenuDataModel> {

    public MenuRepository(String filePath) {
        super(filePath);
    }


    protected void readData() {
        //ClassLoader classLoader = MenuRepository.class.getClassLoader();
        File file = new File(filePath);
        this.entityList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                MenuDataModel menuItem = getMenuItem(line);
                entityList.add(menuItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MenuDataModel getMenuItem(String line) {
        MenuDataModel item;
        if (line == null || line.isEmpty()) return null;
        StringTokenizer st = new StringTokenizer(line, ",");
        String name = st.nextToken();
        double price = Double.parseDouble(st.nextToken());
        item = new MenuDataModel(name, 0, price);
        return item;
    }


    @Override
    public List<MenuDataModel> getAll() {
        return entityList;
    }

    @Override
    public Boolean add(MenuDataModel entity) {
        return false;
    }

}
