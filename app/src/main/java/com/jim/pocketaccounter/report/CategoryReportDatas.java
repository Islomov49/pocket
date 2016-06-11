package com.jim.pocketaccounter.report;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.finance.Category;
import com.jim.pocketaccounter.finance.FinanceRecord;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.finance.SubCategory;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.ArrayList;
import java.util.Calendar;

public class CategoryReportDatas {
    private Calendar begin, end;
    private ArrayList<FinanceRecord> periodDatas;
    private ArrayList<DebtBorrow> debtBorrows;

    public CategoryReportDatas(Calendar begin, Calendar end) {
        this.begin = (Calendar) begin.clone();
        this.end = (Calendar) end.clone();
        for (int i=0; i< PocketAccounter.financeManager.getRecords().size(); i++) {
            if (begin.compareTo(PocketAccounter.financeManager.getRecords().get(i).getDate())<=0 &&
                    end.compareTo(PocketAccounter.financeManager.getRecords().get(i).getDate())>=0)
                periodDatas.add(PocketAccounter.financeManager.getRecords().get(i));
        }
    }
    public CategoryReportDatas() {
        for (int i=0; i< PocketAccounter.financeManager.getRecords().size(); i++)
            periodDatas.add(PocketAccounter.financeManager.getRecords().get(i));
    }
    public ArrayList<CategoryDataRow> makeReport() {
        ArrayList<CategoryDataRow> result  = new ArrayList<CategoryDataRow>();
        for (int i=0; i<periodDatas.size(); i++) {
            boolean categoryFound = false;
            int position = 0;
            for (int j=0; j<result.size(); j++) {
                if (result.get(j).getCategory().getId().matches(periodDatas.get(i).getCategory().getId())) {
                    position = j;
                    categoryFound = true;
                    break;
                }
            }
            if (categoryFound) {
                boolean isNull = (periodDatas.get(i).getSubCategory() == null);
                if (isNull) {
                    boolean nullSubCatIsFound = false;
                    int nullSubCatPosition = 0;
                    for (int j=0; j<result.get(position).getSubCats().size(); j++) {
                        if (result.get(position).getSubCats().get(j).getSubCategory().getName().matches(result.get(position).getCategory().getName())) {
                            nullSubCatIsFound = true;
                            nullSubCatPosition = j;
                            break;
                        }
                    }
                    if (nullSubCatIsFound)
                        result.get(position).getSubCats().get(nullSubCatPosition)
                                .setAmount(result.get(position).getSubCats().get(nullSubCatPosition).getAmount() +
                                        PocketAccounterGeneral.getCost(periodDatas.get(i)));
                }
                else {
                    boolean currentSubCatIsFound = false;
                    int currencySubCatPosition = 0;
                    for (int j=0; j<result.get(position).getSubCats().size(); j++) {
                        if (result.get(position).getSubCats().get(j).getSubCategory().getName().matches(periodDatas.get(i).getSubCategory().getName())) {
                            currentSubCatIsFound = true;
                            currencySubCatPosition = j;
                            break;
                        }
                    }
                    if (currentSubCatIsFound) {
                        result.get(position).getSubCats().get(currencySubCatPosition).setAmount(result.get(position).getSubCats().get(currencySubCatPosition).getAmount()+
                                PocketAccounterGeneral.getCost(periodDatas.get(i)));
                    }
                    else {
                        SubCategory newSubCategory = new SubCategory();
                        newSubCategory.setName(periodDatas.get(i).getSubCategory().getName());
                        newSubCategory.setId(periodDatas.get(i).getSubCategory().getId());
                        newSubCategory.setIcon(periodDatas.get(i).getSubCategory().getIcon());
                        double amount  = PocketAccounterGeneral.getCost(periodDatas.get(i));
                        SubCategoryWitAmount newSubCategoryWithAmount = new SubCategoryWitAmount();
                        newSubCategoryWithAmount.setSubCategory(newSubCategory);
                        newSubCategoryWithAmount.setAmount(amount);
                        result.get(position).getSubCats().add(newSubCategoryWithAmount);
                    }
                }
            }
            else {
                CategoryDataRow newCategoryDataRow = new CategoryDataRow();
                newCategoryDataRow.setCategory(periodDatas.get(i).getCategory());
                SubCategory newSubCategory = new SubCategory();
                if (periodDatas.get(i).getSubCategory() == null)
                    newSubCategory.setName(periodDatas.get(i).getCategory().getName());
                else
                    newSubCategory = periodDatas.get(i).getSubCategory();
                double amount = PocketAccounterGeneral.getCost(periodDatas.get(i));
                SubCategoryWitAmount newSubCatWithAmount = new SubCategoryWitAmount();
                newSubCatWithAmount.setSubCategory(newSubCategory);
                newSubCatWithAmount.setAmount(amount);
                newCategoryDataRow.getSubCats().add(newSubCatWithAmount);
            }
        }
        return result;
    }
}
