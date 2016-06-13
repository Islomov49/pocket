package com.jim.pocketaccounter.report;

import android.content.Context;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
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
    private Context context;
    public CategoryReportDatas(Context context, Calendar begin, Calendar end) {
        this.context = context;
        this.begin = (Calendar) begin.clone();
        this.end = (Calendar) end.clone();
        for (int i=0; i< PocketAccounter.financeManager.getRecords().size(); i++) {
            if (begin.compareTo(PocketAccounter.financeManager.getRecords().get(i).getDate())<=0 &&
                    end.compareTo(PocketAccounter.financeManager.getRecords().get(i).getDate())>=0)
                periodDatas.add(PocketAccounter.financeManager.getRecords().get(i));
        }
    }
    public CategoryReportDatas(Context context) {
        this.context = context;
        for (int i=0; i< PocketAccounter.financeManager.getRecords().size(); i++)
            periodDatas.add(PocketAccounter.financeManager.getRecords().get(i));
    }
    public ArrayList<CategoryDataRow> makeReport() {
        ArrayList<CategoryDataRow> result  = new ArrayList<CategoryDataRow>();
        for (int i=0; i<periodDatas.size(); i++) {
            boolean categoryFound = false;
            int foundCategoryPosition = 0;
            for (int j=0; j<result.size(); j++) {
                if (result.get(j).getCategory().getId().matches(periodDatas.get(i).getCategory().getId())) {
                    categoryFound = true;
                    foundCategoryPosition = j;
                    break;
                }
            }
            if (categoryFound) {
                CategoryDataRow foundCategory = result.get(foundCategoryPosition);
                if (periodDatas.get(i).getSubCategory() == null) {
                    boolean nullSubcatFound = false;
                    int nullSubcatPosition = 0;
                    for (int j = 0; j < foundCategory.getSubCats().size(); j++) {
                        if (foundCategory.getSubCats().get(j).getSubCategory().getId().matches(context.getResources().getString(R.string.no_category))) {
                            nullSubcatPosition = j;
                            nullSubcatFound = true;
                            break;
                        }
                    }
                    if (nullSubcatFound)
                        foundCategory.getSubCats().get(nullSubcatPosition).setAmount(foundCategory.getSubCats().get(nullSubcatPosition).getAmount()+PocketAccounterGeneral.getCost(periodDatas.get(i)));
                    else {
                        SubCategoryWitAmount newSubCategoryWithAmount = new SubCategoryWitAmount();
                        SubCategory noSubCategory = new SubCategory();
                        noSubCategory.setId(context.getResources().getString(R.string.no_category));
                        newSubCategoryWithAmount.setSubCategory(noSubCategory);
                        newSubCategoryWithAmount.setAmount(PocketAccounterGeneral.getCost(periodDatas.get(i)));
                        foundCategory.getSubCats().add(newSubCategoryWithAmount);
                    }
                }
                else {
                    boolean subcatFound = false;
                    int foundSubcatPosition = 0;
                    for (int j=0; j<foundCategory.getSubCats().size(); j++) {
                        if (foundCategory.getSubCats().get(j).getSubCategory().getId().matches(periodDatas.get(i).getSubCategory().getId())) {
                            subcatFound = true;
                            foundSubcatPosition = j;
                            break;
                        }
                    }
                    if (subcatFound) {
                        foundCategory.getSubCats().get(foundSubcatPosition).setAmount(foundCategory.getSubCats().get(foundSubcatPosition).getAmount()+PocketAccounterGeneral.getCost(periodDatas.get(i)));
                    }
                    else {
                        SubCategoryWitAmount newSubCategoryWithAmount = new SubCategoryWitAmount();
                        newSubCategoryWithAmount.setSubCategory(periodDatas.get(i).getSubCategory());
                        newSubCategoryWithAmount.setAmount(PocketAccounterGeneral.getCost(periodDatas.get(i)));
                        foundCategory.getSubCats().add(newSubCategoryWithAmount);
                    }
                }
                double amount = 0.0;
                for (int j=0; j<foundCategory.getSubCats().size(); j++)
                    amount = amount + foundCategory.getSubCats().get(j).getAmount();
                foundCategory.setTotalAmount(amount);
            }
            else {
                CategoryDataRow newCategoryDataRow = new CategoryDataRow();
                newCategoryDataRow.setCategory(periodDatas.get(i).getCategory());
                newCategoryDataRow.setTotalAmount(PocketAccounterGeneral.getCost(periodDatas.get(i)));
                SubCategoryWitAmount newSubCategoryWithAmount = new SubCategoryWitAmount();
                if (periodDatas.get(i).getSubCategory() == null) {
                    SubCategory noSubCategory = new SubCategory();
                    noSubCategory.setId(context.getResources().getString(R.string.no_category));
                    newSubCategoryWithAmount.setSubCategory(noSubCategory);
                    newSubCategoryWithAmount.setAmount(PocketAccounterGeneral.getCost(periodDatas.get(i)));
                }
                else {
                    newSubCategoryWithAmount.setSubCategory(periodDatas.get(i).getSubCategory());
                    newSubCategoryWithAmount.setAmount(PocketAccounterGeneral.getCost(periodDatas.get(i)));
                }
                newCategoryDataRow.getSubCats().add(newSubCategoryWithAmount);
                newCategoryDataRow.setTotalAmount(PocketAccounterGeneral.getCost(periodDatas.get(i)));
                result.add(newCategoryDataRow);
            }
        }
        return result;
    }
}
