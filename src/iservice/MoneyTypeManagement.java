package iservice;

import model.MoneyType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoneyTypeManagement implements Management<MoneyType> {
    static List<MoneyType> moneyTypeList = new ArrayList();


    static {
        moneyTypeList.add(new MoneyType(1,true,"Thưởng"));
        moneyTypeList.add(new MoneyType(2,true,"Tiền lãi"));
        moneyTypeList.add(new MoneyType(3,true,"Lương"));
        moneyTypeList.add(new MoneyType(4,true,"Được tặng"));
        moneyTypeList.add(new MoneyType(5,true,"Bán đồ"));
        moneyTypeList.add(new MoneyType(6,true,"Khoản thu khác"));
        moneyTypeList.add(new MoneyType(7,false,"Ăn uống"));
        moneyTypeList.add(new MoneyType(8,false,"Di chuyển"));
        moneyTypeList.add(new MoneyType(9,false,"Dịp đặc biệt"));
        moneyTypeList.add(new MoneyType(10,false,"Hóa đơn"));
        moneyTypeList.add(new MoneyType(11,false,"Mua sắm"));
        moneyTypeList.add(new MoneyType(12,false,"Giải trí"));
        moneyTypeList.add(new MoneyType(13,false,"Sức khỏe"));
        moneyTypeList.add(new MoneyType(14,false,"Gia đình"));
        moneyTypeList.add(new MoneyType(15,false,"Giáo dục"));
        moneyTypeList.add(new MoneyType(16,false,"Đầu tư"));
        moneyTypeList.add(new MoneyType(17,false,"Bảo hiểm"));
        moneyTypeList.add(new MoneyType(18,false,"Khoản chi khác"));
    }

    @Override
    public void add(MoneyType object) {
        if (!moneyTypeList.contains(object))
            moneyTypeList.add(object);
    }

    @Override
    public List<MoneyType> findAll() {
        List<MoneyType> list = new ArrayList<>();
        Iterator<MoneyType> iterator = moneyTypeList.iterator();
        while (iterator.hasNext())
            list.add(iterator.next());
        return list;
    }

    public List<MoneyType> findIncomeTypeList(){
        List<MoneyType> incomeTypeList = new ArrayList<>();
        for (int i = 0; i < moneyTypeList.size(); i++) {
            if(moneyTypeList.get(i).isIncomeType())
                incomeTypeList.add(moneyTypeList.get(i));
        }
        return incomeTypeList;
    }

    public List<MoneyType> findOutcomeTypeList(){
        List<MoneyType> outcomeTypeList = new ArrayList<>();
        for (int i = 0; i < moneyTypeList.size(); i++) {
            if(!moneyTypeList.get(i).isIncomeType())
                outcomeTypeList.add(moneyTypeList.get(i));
        }
        return outcomeTypeList;
    }
}
