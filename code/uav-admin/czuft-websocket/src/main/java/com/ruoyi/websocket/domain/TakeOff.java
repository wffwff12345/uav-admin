package com.ruoyi.websocket.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TakeOff {
    // 无人机fid地址
    public int fid;
    // 起飞高度
    @JsonProperty("Alt")
    public double Alt;

    public TakeOff(int fid) {
        this.fid = fid;
    }
}
