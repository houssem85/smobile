package com.shrikanthravi.collapsiblecalendarview.data;

public class Employee {

    private int mYear;
    private int mMonth;
    private int mDay;
    private int nbRequestAbsence;
    private int nbEmployeePresent;
    private int nbEmployeeAbsent;

    public int getNbRequestAbsence() {
        return nbRequestAbsence;
    }

    public void setNbRequestAbsence(int nbRequestAbsence) {
        this.nbRequestAbsence = nbRequestAbsence;
    }

    public int getNbEmployeePresent() {
        return nbEmployeePresent;
    }

    public void setNbEmployeePresent(int nbEmployeePresent) {
        this.nbEmployeePresent = nbEmployeePresent;
    }

    public int getNbEmployeeAbsent() {
        return nbEmployeeAbsent;
    }

    public void setNbEmployeeAbsent(int nbEmployeeAbsent) {
        this.nbEmployeeAbsent = nbEmployeeAbsent;
    }

    public Employee(int mYear, int mMonth, int mDay) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
    }

    public Employee(int mYear, int mMonth, int mDay, int nbRequestAbsence, int nbEmployeePresent, int nbEmployeeAbsent) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.nbRequestAbsence = nbRequestAbsence;
        this.nbEmployeePresent = nbEmployeePresent;
        this.nbEmployeeAbsent = nbEmployeeAbsent;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int mYear) {
        this.mYear = mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int mDay) {
        this.mDay = mDay;
    }
}
