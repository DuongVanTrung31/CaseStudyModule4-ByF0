package cg.casestudy4f0.model.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    PENDING("Chờ xác nhận"),
    SHIPPING("Đang giao hàng"),
    COMPLETED("Hoàn thành"),
    REFUND("Trả hàng"),
    CANCELLED("Huỷ đơn hàng");
    private final String status;
}
