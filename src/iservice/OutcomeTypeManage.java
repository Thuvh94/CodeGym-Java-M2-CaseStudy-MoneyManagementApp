package iservice;

import model.OutcomeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OutcomeTypeManage implements Management<OutcomeType> {
    static List<OutcomeType> outcomeTypeList = new ArrayList();
    static{
        outcomeTypeList.add(new OutcomeType("Ăn uống"));
        outcomeTypeList.add(new OutcomeType("Di chuyển"));
        outcomeTypeList.add(new OutcomeType("Dịp đặc biệt"));
        outcomeTypeList.add(new OutcomeType("Hóa đơn"));
        outcomeTypeList.add(new OutcomeType("Mua sắm"));
        outcomeTypeList.add(new OutcomeType("Giải trí"));
        outcomeTypeList.add(new OutcomeType("Sức khỏe"));
        outcomeTypeList.add(new OutcomeType("Gia đình"));
        outcomeTypeList.add(new OutcomeType("Giáo dục"));
        outcomeTypeList.add(new OutcomeType("Đầu tư"));
        outcomeTypeList.add(new OutcomeType("Bảo hiểm"));
        outcomeTypeList.add(new OutcomeType("Khoản chi khác"));
    }

    @Override
    public void add(OutcomeType object) {
        if (!outcomeTypeList.contains(object))
            outcomeTypeList.add(object);
    }

    @Override
    public List<OutcomeType> findAll() {
            List<OutcomeType> list = new ArrayList<>();
            Iterator<OutcomeType> iterator = outcomeTypeList.iterator();
            while (iterator.hasNext())
                list.add(iterator.next());
            return list;
    }
}
