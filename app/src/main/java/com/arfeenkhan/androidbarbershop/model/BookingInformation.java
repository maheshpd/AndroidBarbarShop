package com.arfeenkhan.androidbarbershop.model;

public class BookingInformation {
    private String customeName,customerPhone,time,barberId,barberName,salonId,salonName,saloneAddress;
    private Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String customeName, String customerPhone, String time, String barberId, String barberName, String salonId, String salonName, String saloneAddress, Long slot) {
        this.customeName = customeName;
        this.customerPhone = customerPhone;
        this.time = time;
        this.barberId = barberId;
        this.barberName = barberName;
        this.salonId = salonId;
        this.salonName = salonName;
        this.saloneAddress = saloneAddress;
        this.slot = slot;
    }

    public String getCustomeName() {
        return customeName;
    }

    public void setCustomeName(String customeName) {
        this.customeName = customeName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSaloneAddress() {
        return saloneAddress;
    }

    public void setSaloneAddress(String saloneAddress) {
        this.saloneAddress = saloneAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
