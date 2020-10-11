package iservice;

import model.IncomeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IncomeTypeManage implements Management<IncomeType> {
    static List<IncomeType> incomeTypeList = new ArrayList();

    static {
        incomeTypeList.add(new IncomeType("Thưởng"));
        incomeTypeList.add(new IncomeType("Tiền lãi"));
        incomeTypeList.add(new IncomeType("Lương"));
        incomeTypeList.add(new IncomeType("Được tặng"));
        incomeTypeList.add(new IncomeType("Bán đồ"));
        incomeTypeList.add(new IncomeType("Khoản thu khác"));
    }

    @Override
    public void add(IncomeType object) {
        if (!incomeTypeList.contains(object))
            incomeTypeList.add(object);
    }

    @Override
    public List<IncomeType> findAll() {
        List<IncomeType> list = new ArrayList<>();
        Iterator<IncomeType> iterator = incomeTypeList.iterator();
        while (iterator.hasNext())
            list.add(iterator.next());
        return list;
    }
}
