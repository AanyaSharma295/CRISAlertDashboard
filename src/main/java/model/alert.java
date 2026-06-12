package model;

public class Alert {
    private int alertId;
    private String alertTitle;
    private String cardName;
    private String countQuery;
    private String detailQuery;
    private String isActive;
    private String createdBy;
    private int count;

    // Getters and Setters
    public int getAlertId() { return alertId; }
    public void setAlertId(int alertId) { this.alertId = alertId; }

    public String getAlertTitle() { return alertTitle; }
    public void setAlertTitle(String alertTitle) { this.alertTitle = alertTitle; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getCountQuery() { return countQuery; }
    public void setCountQuery(String countQuery) { this.countQuery = countQuery; }

    public String getDetailQuery() { return detailQuery; }
    public void setDetailQuery(String detailQuery) { this.detailQuery = detailQuery; }

    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}