package com.safepayu.wallet.model;

public class Results {
    private String rslt_id;
    private String date;

    public String getWinnerResult() {
        return winnerResult;
    }

    public void setWinnerResult(String winnerResult) {
        this.winnerResult = winnerResult;
    }

    private String winnerResult;


    public Results(String rslt_id, String date, String winnerResult){
     this.rslt_id=rslt_id;
     this.date=date;
     this.winnerResult=winnerResult;

 }
    public String getDate() {
        return date;
    }


    public Results(){

    }

    public String getResultMorning() {
        return resultMorning;
    }

    public void setResultMorning(String resultMorning) {
        this.resultMorning = resultMorning;
    }

    private String resultMorning;


    private String resultEvening;
    public Results(String resluttMorning){
    this.resultMorning=resluttMorning;

    }

}
